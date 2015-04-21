/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.rest.serialization;

import de.dfki.asr.compass.business.api.ImageManager;
import de.dfki.asr.compass.model.resource.Image;
import javax.inject.Inject;

public class ImageDeserializer extends ReferenceDeserializer<Image> {
	@Inject
	protected ImageManager mgr;

	public ImageDeserializer() {
		super(Image.class);
		setManager(mgr);
	}
}
