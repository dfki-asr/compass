/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.rest;

import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.rest.exception.UnprocessableEntityException;
import de.dfki.asr.compass.model.AbstractCompassEntity;

public abstract class AbstractRESTService {

	protected <T extends AbstractCompassEntity> void ensureEntityIntegrityWhenPost(final T entity)
			throws UnprocessableEntityException, IllegalArgumentException {
		if (entity == null) {
			throw new IllegalArgumentException("Trying to post an entity with an empty body.");
		}
		if (entity.isPersisted()) {
			throw new UnprocessableEntityException("The entity to be persisted already has an ID set.");
		}
	}

	protected <T extends AbstractCompassEntity> void ensureEntityIntegrityWhenPut(final T entity, final long id)
			throws EntityNotFoundException, UnprocessableEntityException, IllegalArgumentException {
		ensureEnityNotNull(entity);
		tryGetEntity(id);
		ensureEnityId(entity, id);
	}

	protected abstract void tryGetEntity(final long id) throws EntityNotFoundException;

	private <T extends AbstractCompassEntity> void ensureEnityNotNull(final T entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Trying to put an entity with an empty body.");
		}
	}

	private <T extends AbstractCompassEntity> void ensureEnityId(final T entity, final long id)
			throws UnprocessableEntityException {
		if (entity.getId() == 0) {
			throw new UnprocessableEntityException("No Id specified in the body.");
		}
		if (entity.getId() != id) {
			throw new UnprocessableEntityException("Id given in Path does not match Id of the object.");
		}
	}
}
