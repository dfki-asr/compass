/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.cdi.producers;

import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaBuilder;

/**
 * Producer for entity managers to we have a single place where to configure them. We are using a container-managed
 * EntityManager, means we don't have to fuddle around with transactions manually. Per default a persistence context of
 * a manager is transaction-scoped, meaning it will discard all changes when the transaction is closed. But many of the
 * entities have lazy loading, which are accessed after a transaction, thus we need to use the context
 * {@link PersistenceContextType.EXTENDED}.
 */
@Stateful
@ApplicationScoped
public class EntityManagerProducer {

	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	@Produces
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Produces
	public CriteriaBuilder getCriteriaBuilder() {
		return entityManager.getCriteriaBuilder();
	}
}
