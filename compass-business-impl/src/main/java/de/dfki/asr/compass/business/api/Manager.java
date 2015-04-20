/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.business.api;

import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.model.AbstractCompassEntity;

public interface Manager<EntityClass extends AbstractCompassEntity> {

	/**
	 * Get an entity specified by the Id.
	 *
	 * @param id of the project to get
	 * @return the entity
	 * @throws EntityNotFoundException
	 */
	EntityClass findById(long id) throws EntityNotFoundException;

	/**
	 * Removes an entity specified by the Id.
	 *
	 * @param id of the entity to be removed
	 * @throws EntityNotFoundException
	 */
	void removeById(long id) throws EntityNotFoundException;

	/**
	 * Removes an entity
	 *
	 * @param entity entity to be removed
	 * @throws EntityNotFoundException
	 */
	void remove(EntityClass entity) throws EntityNotFoundException;

	/**
	 * Persists the entity
	 *
	 * @param entity to persist
	 */
	void save(EntityClass entity);

	/**
	 * Returns a JPA reference to the entity specified by the Id.
	 *
	 * @param id of the requested entity
	 * @return reference to the entity
	 * @throws EntityNotFoundException
	 */
	EntityClass referenceById(long id) throws EntityNotFoundException;
}
