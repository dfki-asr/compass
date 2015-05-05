/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.business;

import de.dfki.asr.compass.business.api.ScenarioManager;
import de.dfki.asr.compass.business.exception.EntityConstraintException;
import de.dfki.asr.compass.business.exception.CompassRuntimeException;
import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.business.exception.PersistenceException;
import de.dfki.asr.compass.business.services.CRUDService;
import de.dfki.asr.compass.model.Project;
import de.dfki.asr.compass.model.Project_;
import de.dfki.asr.compass.model.Scenario;
import de.dfki.asr.compass.model.Scenario_;
import de.dfki.asr.compass.model.resource.Image;
import java.io.IOException;
import java.io.Serializable;
import java.util.Set;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

@Named
@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class ScenarioManagerImpl implements Serializable, ScenarioManager {

	private static final long serialVersionUID = 3093598301164333752L;

	@Inject
	private CRUDService crudService;

	@Inject
	private CriteriaBuilder criteriaBuilder;

	@Inject
	private Validator validator;

	@Override
	public void remove(final Scenario scenario) {
		scenario.getProject().removeScenario(scenario);
		crudService.remove(scenario);
	}

	@Override
	public Scenario findById(final long entityId) throws EntityNotFoundException {
		return crudService.findById(Scenario.class, entityId);
	}

	@Override
	public void removeById(final long entityId) throws EntityNotFoundException {
		remove(findById(entityId) );
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void save(final Scenario entity) {
		crudService.save(entity);
	}

	@Override
	public Scenario referenceById(final long id) throws EntityNotFoundException {
		return crudService.referenceById(Scenario.class, id);
	}

	/**
	 * Duplicates a Scenario using a new Name for the duplicate.
	 *
	 * @param scenario scenario to duplicate
	 * @param duplicateScenarioName new name for the duplicate
	 * @throws PersistenceException
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void duplicateScenario(final Scenario scenario, final String duplicateScenarioName) throws PersistenceException {
		Scenario duplicate;
		try {
			duplicate = (Scenario) scenario.deepCopy();
		} catch (IOException | ClassNotFoundException ex) {
			throw new CompassRuntimeException(ex);
		}
		duplicate.setName(duplicateScenarioName);
		ensureUniqueScenarioName(duplicateScenarioName, scenario.getProject());
		try {
			scenario.getProject().addScenario(duplicate);
			ensureScenarioValid(duplicate);
		} catch (EntityConstraintException ex) {
			scenario.getProject().removeScenario(duplicate);
			throw ex;
		}
		crudService.save(duplicate);
	}

	/**
	 * Creates a new Scenario.
	 *
	 * @param name name
	 * @param project parent project
	 * @param preview preview image
	 * @throws PersistenceException
	 * @throws IOException
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void createScenario(final String name, final Project project, final Image preview) throws PersistenceException, IOException {
		ensureUniqueScenarioName(name, project);
		ensureImageReadyForPersist(preview);
		Scenario newScenario = new Scenario();
		newScenario.setName(name);
		newScenario.setPreview(preview);
		try {
			project.addScenario(newScenario);
			ensureScenarioValid(newScenario);
		} catch (EntityConstraintException ex) {
			project.removeScenario(newScenario);
			throw ex;
		}
		crudService.save(newScenario);
	}

	/**
	 * Edits the scenario. Changes name and preview image.
	 *
	 * @param scenario scenario to edit
	 * @param newScenarioName new name
	 * @param newPreviewImage new preview image
	 */
	@Override
	public void editScenario(final Scenario scenario, final String newScenarioName, final Image newPreviewImage) {
		Image oldImage = scenario.getPreview();
		scenario.setPreview(newPreviewImage);
		if (oldImage != null && oldImage.isPersisted()) {
			crudService.remove(oldImage);
		}
		editScenario(scenario, newScenarioName);
	}

	/**
	 * Edits the scenario. Changes name.
	 *
	 * @param scenario scenario to edit
	 * @param newScenarioName new name
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void editScenario(final Scenario scenario, final String newScenarioName) {
		// Keeping the name the same is a valid operation.
		if (!scenario.getName().equals(newScenarioName)) {
			ensureUniqueScenarioName(newScenarioName, scenario.getProject());
			scenario.setName(newScenarioName);
		}
		crudService.save(scenario);
	}

	private void ensureUniqueScenarioName(final String name, final Project project) {
		CriteriaQuery<Long> q = criteriaBuilder.createQuery(Long.class);
		Root<Scenario> s = q.from(Scenario.class);
		q.where(criteriaBuilder.and(
				criteriaBuilder.equal(s.get(Scenario_.name), name),
				criteriaBuilder.equal(s.get(Scenario_.project).get(Project_.id), project.getId())
		));
		q.select(criteriaBuilder.count(s));
		Long count = crudService.createQuery(q).getSingleResult();
		if (count > 0) {
			throw new EntityConstraintException("A scenario of the given name already exists in the project.");
		}
	}

	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	private void ensureImageReadyForPersist(final Image preview) {
		if (preview.isPersisted()) {
			crudService.attach(preview);
		}
	}

	private void ensureScenarioValid(final Scenario newScenario) {
		Set<ConstraintViolation<Scenario>> violations = validator.validate(newScenario);
		if (!violations.isEmpty()) {
			throw new EntityConstraintException(violations.iterator().next().getMessage());
		}
	}
}
