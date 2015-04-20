/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.business.components;

import de.dfki.asr.compass.business.api.ComponentAnnouncer;
import de.dfki.asr.compass.business.api.ComponentRegistry;
import de.dfki.asr.compass.model.components.DirectionalLight;
import de.dfki.asr.compass.model.components.PreviewImage;
import de.dfki.asr.compass.model.components.RenderGeometry;
import javax.ejb.Stateless;

@Stateless
public class BaseComponentAnnouncer implements ComponentAnnouncer {

	@Override
	public void announceComponents(final ComponentRegistry componentManager) {
		componentManager.registerComponent(RenderGeometry.class);
		componentManager.registerComponent(PreviewImage.class);
		componentManager.registerComponent(DirectionalLight.class);
	}
}
