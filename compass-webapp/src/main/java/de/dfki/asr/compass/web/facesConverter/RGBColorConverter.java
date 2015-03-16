/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.facesConverter;

import de.dfki.asr.compass.model.RGBColor;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = RGBColor.class)
public class RGBColorConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		try {
			RGBColor color = new RGBColor();
			color.setHex(value);
			return color;
		} catch (NumberFormatException e) {
			FacesMessage msg = new FacesMessage("Color conversion error", "Ensure color is a hex string of the form '#RRGGBB'");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(component.getClientId(), msg);
			throw new ConverterException(msg, e);
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		RGBColor color = (RGBColor) value;
		return color.getHex().substring(1)/* without "#"*/.toUpperCase();
	}
}
