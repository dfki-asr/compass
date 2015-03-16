/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.business.api;

/**
 * Announce component classes.
 * Classes implementing this interface will be instantiated and have the announceComponents() method
 * when the {@see ComponentRegistry} is looking for components. Think of it as a plug-in discovery.
 */
public interface ComponentAnnouncer {
	/**
	 * Called to query for new components.
	 * When called, the method should invoke {@see ComponentRegistry.registerComponent} for each class
	 * the plug-in would like to register.
	 * @param registry the registry with which to register component classes.
	 */
	void announceComponents(ComponentRegistry registry);
}
