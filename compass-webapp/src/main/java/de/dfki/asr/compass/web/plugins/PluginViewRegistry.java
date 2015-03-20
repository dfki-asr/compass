/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.plugins;

import de.dfki.asr.compass.web.plugins.api.PluginAnnouncer;
import java.util.ArrayList;
import java.util.HashMap;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import org.jboss.logging.Logger;

@ApplicationScoped
public class PluginViewRegistry {

	@Inject
	private Logger log;

	@Inject
	private Instance<PluginAnnouncer> announcers;

	private final Map<String, Set<String>> viewIds = new HashMap<>();

	@PostConstruct
	protected void collectAnnouncedPlugins() {
		for (PluginAnnouncer announcer : announcers) {
			log.infov("Asking {0} to announce its plugin view.", announcer.getClass().getPackage().getName());
			announcer.announcePlugin(this);
		}
	}

	/**
	 * Called by announcer.announcePlugin.
	 * Analog to ComponentRegistry mechanism.
	 * @param slot slot the id is added to
	 * @param viewId id to add to the slot
	 */
	public void registerPluginView(String slot, String viewId) {
		if (!viewIds.containsKey(slot)) {
			Set<String> set = new HashSet<>();
			set.add(viewId);
			viewIds.put(slot, set);
		} else {
			viewIds.get(slot).add(viewId);
		}
		log.infov("Registered new view id {0} for slot {1}", viewId, slot);
	};

	/**
	 * Get a list of viewIds for a slot. ViewIds are sorted alphabetically, ascending.
	 * @param slot for which the ids are requested
	 * @return sorted list of view ids
	 */
	public List<String> getViewIdsForSlot(String slot) {
		List<String> list = new ArrayList<>();
		if (viewIds.containsKey(slot)) {
			list.addAll(viewIds.get(slot));
			Collections.sort(list);
		}
		return list;
	}

}

