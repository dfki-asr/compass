/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.rest.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import de.dfki.asr.compass.business.api.Manager;
import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.model.AbstractCompassEntity;
import de.dfki.asr.compass.rest.util.CDIInjector;
import java.io.IOException;
import javax.inject.Inject;

public class ReferenceDeserializer<EntityClass extends AbstractCompassEntity> extends StdScalarDeserializer<EntityClass> {
	@Inject
	protected Manager<EntityClass> manager;

	public ReferenceDeserializer(final Class<EntityClass> clazz) {
		super(clazz);
		new CDIInjector<ReferenceDeserializer<EntityClass>>().inject(this);
	}

	@Override
	public EntityClass deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ObjectCodec oc = jp.getCodec();
		Long id = oc.readValue(jp, new TypeReference<Long>() {
		});
		try {
			return manager.referenceById(id);
		} catch (EntityNotFoundException e) {
			throw new IOException("Cannot load from Database", e);
		}
	}
}
