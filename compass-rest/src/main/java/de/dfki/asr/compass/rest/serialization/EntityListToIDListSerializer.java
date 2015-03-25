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
import de.dfki.asr.compass.model.AbstractCompassEntity;
import java.io.IOException;
import java.util.List;

public class EntityListToIDListSerializer extends JsonSerializer<List<? extends AbstractCompassEntity>> {

	@Override
	public void serialize(final List<? extends AbstractCompassEntity> list, final JsonGenerator jgen, final SerializerProvider provider) throws IOException, JsonProcessingException {
		jgen.writeStartArray();
		for (AbstractCompassEntity entity : list) {
			jgen.writeNumber(entity.getId());
		}
		jgen.writeEndArray();
	}

}
