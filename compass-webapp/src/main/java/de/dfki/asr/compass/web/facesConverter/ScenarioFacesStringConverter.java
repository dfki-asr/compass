/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.facesConverter;

import de.dfki.asr.compass.business.api.ScenarioManager;
import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import de.dfki.asr.compass.model.Scenario;
import javax.faces.FacesException;
import javax.inject.Inject;

@FacesConverter(forClass = Scenario.class)
public class ScenarioFacesStringConverter implements Converter {

	@Inject
	private ScenarioManager manager;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
		Long scenarioId = Long.valueOf(value);
		try {
			return manager.findById(scenarioId);
		} catch (EntityNotFoundException ex) {
			throw new FacesException(ex);
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		Scenario scenario = (Scenario) value;
		return String.valueOf(scenario.getId());
	}

}
