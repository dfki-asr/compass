/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.facesConverter;

import de.dfki.asr.compass.business.api.SceneTreeManager;
import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.model.SceneNode;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@FacesConverter(forClass = SceneNode.class)
public class SceneNodeFacesStringConverter implements Converter {

	@Inject
	private SceneTreeManager manager;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		long sceneNodeId = Long.valueOf(value);
		if (sceneNodeId < 1) // permit "null" pointers for cases where an entity optionally refers
		{
			return null;	// to another one and we want to remove that connection
		}
		try {
			return manager.findById(sceneNodeId);
		} catch (EntityNotFoundException e) {
			SceneNode sceneNode = new SceneNode();
			sceneNode.setId(Long.valueOf(value));
			return sceneNode;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		SceneNode sceneNode = (SceneNode) value;
		return String.valueOf(sceneNode.getId());
	}

}
