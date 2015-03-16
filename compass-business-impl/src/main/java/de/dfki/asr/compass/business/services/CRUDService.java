/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.business.services;

import de.dfki.asr.compass.business.events.EntityChangedEvent;
import de.dfki.asr.compass.business.events.EntityCreatedEvent;
import de.dfki.asr.compass.business.events.EntityDeletedEvent;
import de.dfki.asr.compass.business.exception.EntityConstraintException;
import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.business.exception.PersistenceException;
import de.dfki.asr.compass.model.AbstractCompassEntity;
import java.util.List;
import javax.ejb.Singleton;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;

@Singleton
public class CRUDService {

	@Inject
	private Event<EntityCreatedEvent> entityCreatedEvent;

	@Inject
	private Event<EntityChangedEvent> entityChangedEvent;

	@Inject
	private Event<EntityDeletedEvent> entityDeletedEvent;

	@Inject
	protected EntityManager entityManager;

	public <T extends AbstractCompassEntity> TypedQuery<T> createQuery(final String query, final Class<T> clazz) {
		return entityManager.createQuery(query, clazz);
	}

	public <T> TypedQuery<T> createQuery(final CriteriaQuery<T> query) {
		return entityManager.createQuery(query);
	}

	public <T extends AbstractCompassEntity> TypedQuery<T> createNamedQuery(final String queryName, final Class<T> clazz) {
		return entityManager.createNamedQuery(queryName, clazz);
	}

	public <T extends AbstractCompassEntity> T findById(final Class<T> clazz, final long id) throws EntityNotFoundException {
		T entity = entityManager.find(clazz, id);
		if (entity == null) {
			throw new EntityNotFoundException(clazz, id);
		}
		return entity;
	}

	public <T extends AbstractCompassEntity> T referenceById(final Class<T> clazz, final long id) throws EntityNotFoundException {
		return entityManager.getReference(clazz, id);
	}

	public <T extends AbstractCompassEntity> List<T> findAll(final Class<T> clazz) {
		String query = "select entity from " + clazz.getSimpleName() + " entity";
		TypedQuery<T> entityListQuery = createQuery(query, clazz);
		return entityListQuery.getResultList();
	}

	public <T extends AbstractCompassEntity> List<T> findByIdList(final Class<T> clazz, final List<Long> ids) {
		String query = "select entity from " + clazz.getSimpleName() + " entity WHERE entity.id IN (:ids)";
		TypedQuery<T> entityListQuery = createQuery(query, clazz);
		entityListQuery.setParameter("ids", ids);
		return entityListQuery.getResultList();
	}

	public <T extends AbstractCompassEntity> T attach(final T entity) throws PersistenceException {
		if (entity == null) {
			throw new IllegalStateException("No entity set to be attached.");
		}
		if (!entity.isPersisted()) {
			throw new IllegalStateException("Cannot attach an unpersisted entity.");
		}
		if (entityManager.contains(entity)) {
			// already attached
			return entity;
		} else {
			return entityManager.merge(entity);
		}
	}

	public <T extends AbstractCompassEntity> T save(final T entity) throws PersistenceException {
		if (entity == null) {
			throw new IllegalStateException("No entity set to be persisted.");
		}
		try {
			if (entity.isPersisted()) {
				return merge(entity);
			} else {
				return persist(entity);
			}
		} catch (ConstraintViolationException ex) {
			throw new PersistenceException(ex);
		} catch (HibernateException ex) {
			throw new PersistenceException(ex);
		} catch (javax.validation.ConstraintViolationException ex) {
			throw new EntityConstraintException(ex);
		} catch (javax.persistence.PersistenceException ex) {
			throw new PersistenceException(ex);
		}
	}

	public <T extends AbstractCompassEntity> void remove(final T entity) {
		if (entity == null) {
			throw new IllegalStateException("No entity set to be removed.");
		}
		if (!entity.isPersisted()) {
			// no need to delete entities that have not yet been persisted.
			return;
		}
		if (entityManager.contains(entity)) {
			entityManager.remove(entity);
		} else {
			// entity is unmanaged, so we cannot outright delete it.
			entityManager.remove(entityManager.merge(entity));
		}
		entityManager.flush();
		sendDeletedEvent(entity);
	}

	private <T extends AbstractCompassEntity> T persist(final T entity) {
		entityManager.persist(entity);
		entityManager.flush();
		sendCreatedEvent(entity);
		return entity;
	}

	private <T extends AbstractCompassEntity> T merge(final T entity) {
		T merged = entityManager.merge(entity);
		entityManager.flush();
		sendChangedEvent(entity);
		return merged;
	}

	private <T extends AbstractCompassEntity> void sendCreatedEvent(final T entity) {
		EntityCreatedEvent payload = new EntityCreatedEvent(entity);
		entityCreatedEvent.fire(payload);
	}

	private <T extends AbstractCompassEntity> void sendChangedEvent(final T entity) {
		EntityChangedEvent payload = new EntityChangedEvent(entity);
		entityChangedEvent.fire(payload);
	}

	private <T extends AbstractCompassEntity> void sendDeletedEvent(final T entity) {
		EntityDeletedEvent payload = new EntityDeletedEvent(entity);
		entityDeletedEvent.fire(payload);
	}
}
