/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.facesConverter;

import de.dfki.asr.compass.math.Quat4f;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Quat4f.class)
public class Quat4fFacesStringConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Quat4f rot = new Quat4f();
		String[] tmp = value.split(", ");
		try {
			rot.x = Float.valueOf(tmp[0].substring(1, tmp[0].length()));
			rot.y = Float.valueOf(tmp[1]);
			rot.z = Float.valueOf(tmp[2]);
			rot.w = Float.valueOf(tmp[3].substring(0, tmp[3].length() - 1));
		} catch (NumberFormatException e) {
			FacesMessage msg = new FacesMessage("Rotation conversion error", "Ensure rotation input fields are of the form (x, y, z, w)");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(component.getClientId(), msg);
			throw new ConverterException(msg, e);
		}
		return rot;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		Quat4f rot = (Quat4f) value;
		return rot.toString();
	}

}
