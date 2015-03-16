/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.facesConverter;

import java.awt.Color;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Color.class)
public class ColorFacesConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value.charAt(0) == '#') {
			value = value.substring(1, value.length());
		}
		try {
			int r = Integer.parseInt(value.substring(0, 2), 16);
			int g = Integer.parseInt(value.substring(2, 4), 16);
			int b = Integer.parseInt(value.substring(4, 6), 16);
			return new Color(r, g, b);
		} catch (NumberFormatException e) {
			FacesMessage msg = new FacesMessage("Color conversion error", "Ensure color is a hex string of the form '#RRGGBB'");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(component.getClientId(), msg);
			throw new ConverterException(msg, e);
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		Color color = (Color) value;
		String hex = "";
		hex += color.getRed() > 0 ? Integer.toHexString(color.getRed()) : "00";
		hex += color.getGreen() > 0 ? Integer.toHexString(color.getGreen()) : "00";
		hex += color.getBlue() > 0 ? Integer.toHexString(color.getBlue()) : "00";
		return hex.toUpperCase();
	}
}
