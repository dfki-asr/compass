/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.backingbeans.editor;

import de.dfki.asr.compass.business.api.ImageManager;
import de.dfki.asr.compass.business.api.ComponentManager;
import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.business.exception.PersistenceException;
import de.dfki.asr.compass.web.backingbeans.CompassBean;
import de.dfki.asr.compass.model.resource.Image;
import de.dfki.asr.compass.model.components.PreviewImage;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.jboss.logging.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@Named
@RequestScoped
public class PreviewImageUploadBean extends CompassBean {

	@Inject
	private Logger log;

	@Inject
	private ImageManager imageManager;

	@Inject
	private ComponentManager componentManager;

	public void jsfReplacePreviewImage(FileUploadEvent event) {
		try {
			PreviewImage previewComponent = (PreviewImage) event.getComponent().getAttributes().get("previewComponent");
			Image newPreviewImage = savePreviewImageFromEvent(event);
			replacePreviewImageOnComponent(previewComponent, newPreviewImage);
		} catch (ClassCastException e) {
			log.error("No suitable PreviewImage scene node component found! Did you remember to include it in the FileUpload request with an f:attribute tag?");
		} catch (PersistenceException ex) {
			log.error("Could not save Component.");
		}
	}

	private Image savePreviewImageFromEvent(FileUploadEvent event) {
		UploadedFile file = event.getFile();
		return imageManager.createImage(file.getFileName(), file.getContentType(), file.getContents());
	}

	private void replacePreviewImageOnComponent(PreviewImage component, Image newPreviewImage) throws PersistenceException {
		Image oldImage = component.getImage();
		component.setImage(newPreviewImage);
		componentManager.save(component);
		try {
			imageManager.remove(oldImage);
		} catch (EntityNotFoundException ex) {
			log.error("Image already deleted. Possible race condition?");
		}
	}
}
