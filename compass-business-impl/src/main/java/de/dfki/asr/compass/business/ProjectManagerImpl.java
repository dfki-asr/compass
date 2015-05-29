/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.business;

import de.dfki.asr.compass.business.api.ProjectManager;
import de.dfki.asr.compass.business.exception.CompassRuntimeException;
import de.dfki.asr.compass.business.exception.EntityConstraintException;
import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.business.exception.PersistenceException;
import de.dfki.asr.compass.model.Project;
import de.dfki.asr.compass.model.Project_;
import de.dfki.asr.compass.model.Scenario;
import de.dfki.asr.compass.business.services.CRUDService;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Named
@Stateless
public class ProjectManagerImpl implements Serializable, ProjectManager {

	private static final long serialVersionUID = 2261821344802823306L;

	@Inject
	private CRUDService crudService;

	@Inject
	private CriteriaBuilder criteriaBuilder;

	@Override
	public Project findById(final long id) throws EntityNotFoundException {
		return crudService.findById(Project.class, id);
	}

	@Override
	public void removeById(final long entityId) throws EntityNotFoundException {
		remove(findById(entityId));
	}

	@Override
	public void save(final Project entity) {
		ensureUniqueProjectName(entity);
		crudService.save(entity);
	}

	@Override
	public Project referenceById(final long id) throws EntityNotFoundException {
		return crudService.referenceById(Project.class, id);
	}

	@Override
	public void remove(final Project entity) {
		crudService.remove(entity);
	}

	@Override
	public List<Project> getAllProjects() {
		return crudService.findAll(Project.class);
	}

	@Override
	public void addScenarioToProject(final Scenario scenario, final long projectID) throws IllegalArgumentException, EntityNotFoundException {
		addScenarioToProject(scenario, findById(projectID));
	}

	@Override
	public void addScenarioToProject(final Scenario scenario, final Project project) throws IllegalArgumentException {
		project.addScenario(scenario);
		crudService.save(scenario);
	}

	@Override
	public void createNewProject(final String name) throws PersistenceException {
		ensureUniqueProjectName(name);
		Project newProject = new Project(name);
		crudService.save(newProject);
	}

	@Override
	public void copyProject(final Project project, final String nameForCopy) throws PersistenceException {
		ensureUniqueProjectName(nameForCopy);
		Project attached = crudService.attach(project);
		Project newProject;
		try {
			newProject = (Project) attached.deepCopy();
		} catch (IOException | ClassNotFoundException ex) {
			throw new CompassRuntimeException(ex);
		}
		newProject.setName(nameForCopy);
		crudService.save(newProject);
	}

	@Override
	public void setProjectName(final Project project, final String projectName) throws PersistenceException {
		if (!project.getName().equals(projectName)) {
			ensureUniqueProjectName(projectName);
			project.setName(projectName);
		}
		crudService.save(project);
	}

	private void ensureUniqueProjectName(final String name) {
		CriteriaQuery<Long> q = criteriaBuilder.createQuery(Long.class);
		Root<Project> p = q.from(Project.class);
		q.where(criteriaBuilder.equal(p.get(Project_.name), name));
		q.select(criteriaBuilder.count(p));
		Long count = crudService.createQuery(q).getSingleResult();
		if (count > 0) {
			throw new EntityConstraintException("A project of the given name already exists.");
		}
	}

	private void ensureUniqueProjectName(final Project project) {
		ensureUniqueProjectName(project.getName());
	}
}
