/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.rest.serialization;

import de.dfki.asr.compass.rest.util.CDIInjector;
import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import java.io.IOException;
import javax.inject.Inject;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import de.dfki.asr.compass.business.api.ComponentManager;
import de.dfki.asr.compass.model.components.RenderGeometry;

public class RenderGeometryDeserializer extends JsonDeserializer<RenderGeometry> {

	@Inject
	private ComponentManager manager;

	public RenderGeometryDeserializer() {
		new CDIInjector<RenderGeometryDeserializer>().inject(this);
	}

	@Override
	public RenderGeometry deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ObjectCodec oc = jp.getCodec();
		Long id = oc.readValue(jp, new TypeReference<Long>() {
		});
		try {
			return (RenderGeometry) manager.referenceById(id);
		} catch (EntityNotFoundException e) {
			throw new IOException("Cannot load from Database", e);
		}
	}

}
