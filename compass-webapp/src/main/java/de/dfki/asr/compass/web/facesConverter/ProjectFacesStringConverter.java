/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.facesConverter;

import de.dfki.asr.compass.business.api.ProjectManager;
import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import de.dfki.asr.compass.model.Project;
import javax.faces.FacesException;
import javax.inject.Inject;

@FacesConverter(forClass = Project.class)
public class ProjectFacesStringConverter implements Converter {

	@Inject
	ProjectManager pm;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		Long projectId = Long.valueOf(value);
		try {
			return pm.findById(projectId);
		} catch (EntityNotFoundException ex) {
			throw new FacesException(ex);
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		Project da = (Project) value;
		return String.valueOf(da.getId());
	}

}
