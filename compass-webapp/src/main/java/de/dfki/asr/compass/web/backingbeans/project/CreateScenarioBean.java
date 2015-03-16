/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.backingbeans.project;

import de.dfki.asr.compass.business.api.ImageManager;
import de.dfki.asr.compass.business.api.ScenarioManager;
import de.dfki.asr.compass.business.exception.EntityConstraintException;
import de.dfki.asr.compass.business.exception.PersistenceException;
import de.dfki.asr.compass.web.backingbeans.DialogCompassBean;
import de.dfki.asr.compass.model.resource.Image;
import java.io.IOException;
import java.io.InputStream;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import de.dfki.asr.compass.web.util.ResourceLoader;
import java.io.ByteArrayInputStream;
import javax.enterprise.context.SessionScoped;
import javax.servlet.http.HttpSession;
import org.primefaces.model.DefaultStreamedContent;

@Named
@SessionScoped
public class CreateScenarioBean extends DialogCompassBean {

	private static final long serialVersionUID = -3548887760353311367L;

	@Inject
	protected Logger log;

	@Inject
	protected OpenScenarioBean openScenarioBean;

	@Inject
	private ScenarioManager scenarioManager;

	@Inject
	private ImageManager imageManager;

	@Inject
	private ResourceLoader resourceLoader;

	@Inject
	private HttpSession currentSession;

	protected String newScenarioName;
	protected Image newPreviewImage;

	public String getNewScenarioName() {
		return newScenarioName;
	}

	public void setNewScenarioName(String name) {
		newScenarioName = name;
	}

	@Override
	public void begin() {
		super.begin();
		showFacesMessageAtControl("validation.Scenario.name.Prompt", "createScenarioForm:ScenarioNameInput", FacesMessage.SEVERITY_INFO);
		currentSession.removeAttribute("imageData");
		newScenarioName = "";
	}

	private void createTemporaryImage(String fileName, String mimeType, byte[] data) {
		newPreviewImage = new Image(fileName, mimeType, data);
		currentSession.setAttribute("imageData", newPreviewImage);
	}

	public void handleSessionImage(FileUploadEvent event) {
		UploadedFile file = event.getFile();
		createTemporaryImage(file.getFileName(), file.getContentType(), file.getContents());
	}

	public DefaultStreamedContent getStreamedPreviewImage() {
		Image imageFromSession = (Image) currentSession.getAttribute("imageData");
		if (imageFromSession == null) {
			return null;
		} else {
			return new DefaultStreamedContent(new ByteArrayInputStream(newPreviewImage.getData()), "image/png");
		}
	}

	@Override
	public String cancel() {
		super.cancel();
		newScenarioName = "";
		currentSession.removeAttribute("imageData");
		newPreviewImage = null;
		return getRedirectURL("/index.xhtml");
	}

	@Override
	public String confirm() {
		try {
			if (newPreviewImage == null) {
				createDefaultImage();
			}
			scenarioManager.createScenario(newScenarioName, openScenarioBean.getSelectedProject(), newPreviewImage);
		} catch (PersistenceException ex) {
			showFacesMessageAtControl("Something went wrong trying to save the scenario, sorry.", "createScenarioForm:ScenarioNameInput", FacesMessage.SEVERITY_ERROR);
			log.errorv(ex, "Could not save scenario!");
			return null;
		} catch (EntityConstraintException ex) {
			showFacesMessageAtControl(ex.getMessages().iterator().next(), "createScenarioForm:ScenarioNameInput", FacesMessage.SEVERITY_ERROR);
			return null;
		} catch (IOException ex) {
			showFacesMessageAtControl("Internal error. Contact the server admin.", "createScenarioForm:ScenarioNameInput", FacesMessage.SEVERITY_ERROR);
			log.errorv(ex, "Failed to load default image. That really shouldn't happen.");
			return null;
		}
		newScenarioName = "";
		currentSession.removeAttribute("imageData");
		newPreviewImage = null;
		return getRedirectURL("/index.xhtml");
	}

	private void createDefaultImage() throws IOException, PersistenceException {
		InputStream defaultImage = resourceLoader.getResourceAsStream("WEB-INF/classes/default_image.png");
		newPreviewImage = imageManager.createImage("image/png", IOUtils.toByteArray(defaultImage));
	}
}
