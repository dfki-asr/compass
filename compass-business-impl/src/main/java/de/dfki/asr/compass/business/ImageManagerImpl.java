/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.business;

import de.dfki.asr.compass.business.api.ImageManager;
import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.business.services.CRUDService;
import de.dfki.asr.compass.model.resource.Image;
import java.io.Serializable;
import java.util.Date;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@Stateless
public class ImageManagerImpl implements Serializable, ImageManager {
	private static final long serialVersionUID = 7545634340536107430L;

	@Inject
	private CRUDService crudService;

	@Override
	public Image findById(final long id) throws EntityNotFoundException {
		return crudService.findById(Image.class, id);
	}

	@Override
	public void remove(final Image image) {
		crudService.remove(image);
	}

	@Override
	public void removeById(final long id) throws EntityNotFoundException {
		remove(findById(id));
	}

	@Override
	public void save(final Image entity) {
		crudService.save(entity);
	}

	@Override
	public Image referenceById(final long id) throws EntityNotFoundException {
		return crudService.referenceById(Image.class, id);
	}

	@Override
	public Image createImage(final String mimeType, final byte[] data) {
		String name = getFreshPreviewImageName();
		return createImage(name, mimeType, data);
	}

	private String getFreshPreviewImageName() {
		return "preview_image_" + (new Date()).getTime();
	}

	@Override
	public Image createImage(final String fileName, final String mimeType, final byte[] data) {
		Image newPreviewImage = new Image(fileName, mimeType, data);
		crudService.save(newPreviewImage);
		return newPreviewImage;
	}
}
