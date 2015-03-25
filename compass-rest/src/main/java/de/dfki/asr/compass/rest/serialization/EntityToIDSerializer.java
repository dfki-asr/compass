/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.rest.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import de.dfki.asr.compass.model.AbstractCompassEntity;
import java.io.IOException;

public class EntityToIDSerializer extends JsonSerializer<AbstractCompassEntity> {

	@Override
	public void serialize(final AbstractCompassEntity entity, final JsonGenerator jgen, final SerializerProvider provider) throws IOException, JsonProcessingException {
		jgen.writeNumber(entity.getId());
	}

	@Override
	@SuppressWarnings("PMD.ExcessiveParameterList")
	public void serializeWithType(final AbstractCompassEntity entity, final JsonGenerator jgen,
			final SerializerProvider provider, final TypeSerializer typeSer)
			throws IOException, JsonProcessingException {
		jgen.writeNumber(entity.getId());
	}
}
