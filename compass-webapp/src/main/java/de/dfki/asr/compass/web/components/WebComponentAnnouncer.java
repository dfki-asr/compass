/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.components;

import de.dfki.asr.compass.business.api.ComponentRegistry;
import de.dfki.asr.compass.business.api.ComponentAnnouncer;
import de.dfki.asr.compass.web.plugins.PluginViewRegistry;
import de.dfki.asr.compass.web.plugins.api.PluginAnnouncer;
import javax.ejb.Stateless;

@Stateless
public class WebComponentAnnouncer implements ComponentAnnouncer, PluginAnnouncer {

	@Override
	public void announceComponents(ComponentRegistry componentManager) {
	}

	@Override
	public void announcePlugin(PluginViewRegistry registry) {
		registry.registerPluginView("body", "/plugins/prefab_importer_dialogs.xhtml");
		registry.registerPluginView("prefabButtons", "/plugins/prefab_importer_button.xhtml");
	}
}
