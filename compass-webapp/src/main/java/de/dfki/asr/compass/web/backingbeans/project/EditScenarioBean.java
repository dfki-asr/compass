/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.backingbeans.project;

import de.dfki.asr.compass.business.api.ScenarioManager;
import de.dfki.asr.compass.business.exception.EntityConstraintException;
import de.dfki.asr.compass.business.exception.PersistenceException;
import de.dfki.asr.compass.web.backingbeans.DialogCompassBean;
import de.dfki.asr.compass.model.resource.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.UploadedFile;

@Named
@SessionScoped
public class EditScenarioBean extends DialogCompassBean {

	private static final long serialVersionUID = 3863014565317587844L;

	@Inject
	protected OpenScenarioBean openScenarioBean;

	@Inject
	private ScenarioManager scenarioManager;

	private Image newPreviewImage;
	private String newScenarioName;
	private boolean imageAdded;

	@Inject
	private HttpSession currentSession;

	public String getNewScenarioName() {
		if (newScenarioName == null) {
			return openScenarioBean.getSelectedScenario().getName();
		}
		return newScenarioName;
	}

	public void setNewScenarioName(String name) {
		newScenarioName = name;
	}

	public int getImageHeight() {
		BufferedImage bufferedPreview;
		float scalingFactor;
		try {
			if (imageAdded) {
				bufferedPreview = ImageIO.read(new ByteArrayInputStream(newPreviewImage.getData()));
				scalingFactor = Math.min(1.0f, 400.0f / bufferedPreview.getWidth());
				return (int) (scalingFactor * bufferedPreview.getHeight());
			} else if (openScenarioBean.getSelectedScenario().getPreview() != null) {
				bufferedPreview = ImageIO.read(new ByteArrayInputStream(openScenarioBean.getSelectedScenario().getPreview().getData()));
				scalingFactor = Math.min(1.0f, 400.0f / bufferedPreview.getWidth());
				return (int) (scalingFactor * bufferedPreview.getHeight());
			}
		} catch (IOException ioEx) {
			return 300;
		}
		return 300;
	}

	public DefaultStreamedContent getStreamedPreviewImage() {
		if (imageAdded) {
			Image imageFromSession = (Image) currentSession.getAttribute("imageData");
			if (imageFromSession == null) {
				return null;
			} else {
				return new DefaultStreamedContent(new ByteArrayInputStream(newPreviewImage.getData()), "image/png");
			}
		}
		if (openScenarioBean.getSelectedScenario().getPreview() != null) {
			return new DefaultStreamedContent(new ByteArrayInputStream(openScenarioBean.getSelectedScenario().getPreview().getData()), "image/png");
		}
		return null;
	}

	@Override
	@PostConstruct
	public void begin() {
		super.begin();
		currentSession.removeAttribute("imageData");
		imageAdded = false;
	}

	private void createTemporaryImage(String fileName, String mimeType, byte[] data) {
		newPreviewImage = new Image(fileName, mimeType, data);
		currentSession.setAttribute("imageData", newPreviewImage);
	}

	public void handleSessionImage(FileUploadEvent event) {
		imageAdded = true;
		UploadedFile file = event.getFile();
		createTemporaryImage(file.getFileName(), file.getContentType(), file.getContents());
	}

	@Override
	public String cancel() {
		if (imageAdded) {
			currentSession.removeAttribute("imageData");
		}
		newScenarioName = null;
		imageAdded = false;
		return getRedirectURL("/index.xhtml");
	}

	@Override
	public String confirm() {
		try {
			if (imageAdded) {
				currentSession.removeAttribute("imageData");
				scenarioManager.editScenario(openScenarioBean.getSelectedScenario(), newScenarioName, newPreviewImage);
			} else {
				scenarioManager.editScenario(openScenarioBean.getSelectedScenario(), newScenarioName);
			}
			newScenarioName = null;
			imageAdded = false;
		} catch (PersistenceException ex) {
			// The underlying exceptions aren't really specific enough (without resorting to parsing vendor-specific messages).
			// Even though other things may go wrong, I figure in 90% of the cases this will be the problem:
			showFacesMessageAtControl("A scenario of that name already exists", "editScenarioForm:NameInput", FacesMessage.SEVERITY_ERROR);
			return null;
		} catch (EntityConstraintException ex) {
			showFacesMessageAtControl(ex.getMessages().iterator().next(), "editScenarioForm:NameInput", FacesMessage.SEVERITY_ERROR);
			return null;
		}
		return getRedirectURL("/index.xhtml");
	}
}
