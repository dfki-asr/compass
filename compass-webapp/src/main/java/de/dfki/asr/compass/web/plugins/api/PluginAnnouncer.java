/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.plugins.api;

import de.dfki.asr.compass.web.plugins.PluginViewRegistry;

/**
 * Announce plugin classes.
 * Classes implementing this interface will be instantiated and have the announceComponents() method
 * when the {@see PluginViewRegistry} is looking for plugins. Think of it as a plug-in discovery.
 */
public interface PluginAnnouncer {
	/**
	 * Called to query for new plugins.
	 * When called, the method should invoke {@see PluginViewRegistry.registerPluginView} for each class
	 * the plugin would like to register.
	 * @param registry the registry with which to register component classes.
	 */
	void announcePlugin(PluginViewRegistry registry);
}
