/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.rest.serialization;

import de.dfki.asr.compass.rest.util.CDIInjector;
import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import de.dfki.asr.compass.model.AbstractCompassEntity;

/**
 * Base class to deserialize a list of IDs to their model instances.
 *
 * @param <EntityType> the type of model class to deserialize an ID to
 */
public abstract class AbstractIDListDeserializer<EntityType extends AbstractCompassEntity>
		extends JsonDeserializer<List<EntityType>> {

	public AbstractIDListDeserializer() {
		new CDIInjector<AbstractIDListDeserializer>().inject(this);
	}

	@Override
	public List<EntityType> deserialize(final JsonParser jp, final DeserializationContext dc) throws IOException, JsonProcessingException {
		ObjectCodec oc = jp.getCodec();
		List<Long> ids = oc.readValue(jp, new TypeReference<List<Long>>() {
		});
		List<EntityType> refs = new LinkedList<>();
		try {
			for (Long id : ids) {
				refs.add(referenceById(id));
			}
			return refs;
		} catch (EntityNotFoundException e) {
			throw new IOException("Cannot load from Database", e);
		}
	}

	protected abstract EntityType referenceById(long id) throws EntityNotFoundException;
}
