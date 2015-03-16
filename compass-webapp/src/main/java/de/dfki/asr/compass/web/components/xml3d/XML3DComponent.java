/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.components.xml3d;

import de.dfki.asr.compass.web.exception.SceneToDomConversionException;
import de.dfki.asr.compass.model.SceneNode;
import de.dfki.asr.compass.cdi.producers.LoggerProducer;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.jboss.logging.Logger;

/**
 * IMPORTANT: this is a JSF-managed object, CDI will not work on it.
 */
@FacesComponent("compass.web.components.xml3d")
public class XML3DComponent extends UIComponentBase implements Serializable {

	private static final long serialVersionUID = -5656806814384095309L;

	protected static final String FAMILY = "compass.xml3d";

	protected Logger log;
	protected ResponseWriter writer;
	protected SceneToDomConverter sceneToDomConverter;

	@Override
	public String getFamily() {
		return FAMILY;
	}

	public XML3DComponent() {
		sceneToDomConverter = new SceneToDomConverter();
		log = LoggerProducer.getLogger(XML3DComponent.class);
	}

	@Override
	public void encodeBegin(FacesContext context) throws IOException {
		if (context == null) {
			log.error("Context was null, when rendering a xml3d custom component.");
			return;
		}
		initializeResponseWriter(context);
		try {
			writeScene();
		} catch (SceneToDomConversionException e) {
			log.error("Failed to write XML3D scene", e);
		} catch (IOException e) {
			log.error("Failed to write XML3D scene", e);
		}
	}

	private void initializeResponseWriter(FacesContext context) {
		writer = context.getResponseWriter();
	}

	private void writeScene() throws IOException, SceneToDomConversionException {
		Object rootSceneNodeAttribute = getAttributes().get("rootSceneNode");
		if (!(rootSceneNodeAttribute instanceof SceneNode)) {
			throw new IOException(
					"xml3d.rootSceneNode property doesn't point to a SceneNode instance.");
		}
		SceneNode rootSceneNode = (SceneNode) rootSceneNodeAttribute;
		String scene = sceneToDomConverter.convertToDomAsString(rootSceneNode, getAttributes());
		writer.write(scene);
	}

}
