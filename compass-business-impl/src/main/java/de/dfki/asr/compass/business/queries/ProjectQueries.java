/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.business.queries;

import de.dfki.asr.compass.model.Project;
import de.dfki.asr.compass.model.Scenario;
import de.dfki.asr.compass.business.services.CRUDService;
import de.dfki.asr.compass.model.Project_;
import de.dfki.asr.compass.model.Scenario_;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class ProjectQueries {

	@Inject
	private CRUDService crudService;

	@Inject
	private CriteriaBuilder criteriaBuilder;

	public List<Project> getAllSortedAscendingByName() {
		CriteriaQuery<Project> q = criteriaBuilder.createQuery(Project.class);
		Root<Project> p = q.from(Project.class);
		q.orderBy(criteriaBuilder.asc(p.get(Project_.name)));
		return crudService.createQuery(q).getResultList();
	}

	public List<Project> getProjectsByNameAndId(final String projectName, final long id) {
		CriteriaQuery<Project> q = criteriaBuilder.createQuery(Project.class);
		Root<Project> p = q.from(Project.class);
		q.where(criteriaBuilder.and(
				criteriaBuilder.equal(p.get(Project_.name), projectName),
				criteriaBuilder.notEqual(p.get(Project_.id), id)
		));
		return crudService.createQuery(q).getResultList();
	}

	public List<Project> getProjectsWithName(final String projectName) {
		CriteriaQuery<Project> q = criteriaBuilder.createQuery(Project.class);
		Root<Project> p = q.from(Project.class);
		q.where(criteriaBuilder.equal(p.get(Project_.name), projectName));
		return crudService.createQuery(q).getResultList();
	}

	@SuppressWarnings("CPD-START")
	public List<Scenario> getScenariosByName(final long projectId, final String scenarioName) {
		CriteriaQuery<Scenario> q = criteriaBuilder.createQuery(Scenario.class);
		Root<Scenario> s = q.from(Scenario.class);
		q.where(
			criteriaBuilder.and(
				criteriaBuilder.equal(s.get(Scenario_.name), scenarioName),
				criteriaBuilder.equal(
						s.get(Scenario_.project).get(Project_.id),
						projectId
				)
			)
		);
		return crudService.createQuery(q).getResultList();
	}

	@SuppressWarnings("CPD-END")
	public List<Scenario> getScenariosByNameAndDifferentId(final long projectId, final String scenarioName, final long scenarioId) {
		CriteriaQuery<Scenario> q = criteriaBuilder.createQuery(Scenario.class);
		Root<Scenario> s = q.from(Scenario.class);
		q.where(
			criteriaBuilder.and(
				criteriaBuilder.equal(s.get(Scenario_.name), scenarioName),
				criteriaBuilder.equal(
						s.get(Scenario_.project).get(Project_.id),
						projectId
				),
				criteriaBuilder.notEqual(
						s.get(Scenario_.id),
						scenarioId
				)
			)
		);
		return crudService.createQuery(q).getResultList();
	}
}
