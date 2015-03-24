/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.components;

import de.dfki.asr.compass.rest.util.CDIInjector;
import de.dfki.asr.compass.web.plugins.PluginViewRegistry;
import java.io.IOException;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.Tag;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagAttributeException;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagException;
import javax.faces.view.facelets.TagHandler;
import javax.inject.Inject;
import org.jboss.logging.Logger;

public class PluginTagHandler extends TagHandler {

	private final TagAttribute slotAttribute;

	@Inject
	private PluginViewRegistry registry;

	@Inject
	private Logger log;

	public PluginTagHandler(TagConfig config) {
		super(config);
		new CDIInjector<PluginTagHandler>().inject(this);
		slotAttribute = getAttribute("slot");
		if (slotAttribute == null) {
			throw new TagException(tag, "Attribute 'slot' is required");
		}
	}

	@Override
	public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
		String slot = slotAttribute.getValue(ctx);
		if (slot == null || slot.length() == 0) {
			return;
		}
		PluginTagProcessor p = new PluginTagProcessor(ctx, parent, tag, slotAttribute);
		p.apply();
		this.nextHandler.apply(ctx, p.getWrapper());
	}

	private class PluginTagProcessor {
		private final FaceletContext context;
		private final UIComponent parent;
		private final TagAttribute slot;
		private final Tag tag;

		private boolean wrapperCreated = false;
		private UINamingContainer wrapper;

		public PluginTagProcessor(FaceletContext ctx, UIComponent parent, Tag tag, TagAttribute slot) {
			this.context = ctx;
			this.parent = parent;
			this.slot = slot;
			this.tag = tag;
		}

		public void apply() {
			ensureWrappingContainer();
			if (wrapperCreated) {
				populateWrapper();
				parent.getChildren().add(wrapper);
			}
		}

		private void ensureWrappingContainer() {
			if (ComponentHandler.isNew(parent)) {
				wrapper = new UINamingContainer();
				wrapper.setId(getWrapperId());
				wrapperCreated = true;
			} else {
				UIComponent wrapperCandidate = parent.findComponent(getWrapperId());
				if (wrapperCandidate instanceof UINamingContainer) {
					wrapper = (UINamingContainer) wrapperCandidate;
				} else {
					wrapper = new UINamingContainer();
					wrapper.setId(getWrapperId());
					wrapperCreated = true;
				}
			}
		}

		private void populateWrapper() {
			String actualSlot = slot.getValue(context);
			List<String> viewsForSlot = registry.getViewIdsForSlot(actualSlot);
			String lastTriedView = "";
			try {
				for (String viewId : viewsForSlot) {
					lastTriedView = viewId;
					context.includeFacelet(wrapper, viewId);
				}
			} catch (IOException e) {
				throw new TagAttributeException(tag, slot, "Invalid path : " + lastTriedView);
			}
		}

		private UIComponent getWrapper() {
			return wrapper;
		}

		private String getWrapperId() {
			return "compass_plugin_wrap_" + slot.getValue(context);
		}
	}
}
