/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.facesConverter;

import de.dfki.asr.compass.math.Vector3f;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Vector3f.class)
public class Vector3fFacesConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Vector3f vec = new Vector3f();
		String[] tmp = value.split(", ");
		try {
			vec.x = Float.valueOf(tmp[0].substring(1, tmp[0].length()));
			vec.y = Float.valueOf(tmp[1]);
			vec.z = Float.valueOf(tmp[2].substring(0, tmp[2].length() - 1));
		} catch (NumberFormatException e) {
			FacesMessage msg = new FacesMessage("Vector conversion error", "Ensure vector input fields are of the form (x, y, z)");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(component.getClientId(), msg);
			throw new ConverterException(msg, e);
		}
		return vec;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		Vector3f vec = (Vector3f) value;
		return vec.toString();
	}

}
