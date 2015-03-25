/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.business.exception;

import javax.ejb.ApplicationException;

@ApplicationException(inherited = true, rollback = true)
public class EntityNotFoundException extends Exception {

	private static final long serialVersionUID = -8559277802786237328L;
	private final Class<?> entityClass;
	private final long entityId;

	private static String getExceptionMessage(final Class<?> entityClass, final long entityId) {
		return "Entity (" + entityClass.getSimpleName() + ")not found with ID '" + entityId + "'.";
	}

	public EntityNotFoundException(final Class<?> entityClass, final long entityId) {
		super(getExceptionMessage(entityClass, entityId));
		this.entityClass = entityClass;
		this.entityId = entityId;
	}

	public EntityNotFoundException(final Class<?> entityClass, final long entityId, final Throwable cause) {
		super(getExceptionMessage(entityClass, entityId), cause);
		this.entityClass = entityClass;
		this.entityId = entityId;
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	public long getEntityId() {
		return entityId;
	}
}
