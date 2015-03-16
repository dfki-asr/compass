/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.business.api;

import de.dfki.asr.compass.model.resource.Image;

public interface ImageManager extends Manager<Image> {

	/**
	 * Creates a new Image and generates a name for this image.
	 *
	 * @param mimeType mime type (e.g. file type)
	 * @param data image data
	 * @return created Image
	 */
	Image createImage(String mimeType, byte[] data);

	/**
	 * Creates a new Image.
	 * The created image is not persisted, you need to call save() on it again.
	 *
	 * @param fileName name
	 * @param mimeType mime type (e.g. file type)
	 * @param data image data
	 * @return created image
	 */
	Image createImage(String fileName, String mimeType, byte[] data);

}
