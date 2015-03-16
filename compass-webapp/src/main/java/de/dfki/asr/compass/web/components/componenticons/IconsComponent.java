/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.components.componenticons;

import de.dfki.asr.compass.cdi.producers.LoggerProducer;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.jboss.logging.Logger;

/**
 * IMPORTANT: this is a JSF-managed object, CDI will not work on it.
 */
@FacesComponent("compass.web.components.componenticons")
public class IconsComponent extends UIComponentBase implements Serializable {

	private static final long serialVersionUID = -5656806814384095309L;

	protected static final String FAMILY = "compass.componentIcons";

	protected Logger log;

	public IconsComponent() {
		log = LoggerProducer.getLogger(IconsComponent.class);
	}

	@Override
	public String getFamily() {
		return FAMILY;
	}

	@Override
	public void encodeBegin(FacesContext context) throws IOException {
		if (context == null) {
			log.error("Context was null, when rendering a componentIcons custom component.");
		}
		writeIcons(context.getResponseWriter());
	}

	private void writeIcons(ResponseWriter writer) throws IOException {
		Object icons = getAttributes().get("icons");
		if (!(icons instanceof List)) {
			log.error("icons is not a list");
			return;
		}
		try {
			List<String> list = (List<String>) icons;
			for (String string : list) {
				String classes = "ui-icon inline ui-icon-" + string;
				writer.startElement("span", this);
				writer.writeAttribute("class", classes, null);
				writer.endElement("span");
			}
		} catch (ClassCastException e) {
			log.error("icons is not a string list", e);
		}
	}

}
