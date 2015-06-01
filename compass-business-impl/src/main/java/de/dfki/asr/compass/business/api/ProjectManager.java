/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.business.api;

import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.business.exception.PersistenceException;
import de.dfki.asr.compass.model.Project;
import de.dfki.asr.compass.model.Scenario;
import java.util.List;

public interface ProjectManager extends Manager<Project> {

	/**
	 * Add a given scenario to a project specified by its ID. This method changes the database.
	 *
	 * @param scenario scenario to add
	 * @param projectID id of the project the scenario is added to
	 * @throws IllegalArgumentException
	 * @throws EntityNotFoundException
	 */
	void addScenarioToProject(Scenario scenario, long projectID) throws IllegalArgumentException, EntityNotFoundException;

	/**
	 * Add a given scenario to a project. This method changes the database.
	 *
	 * @param scenario scenario to add
	 * @param project project the scenario is added to
	 * @throws IllegalArgumentException
	 */
	void addScenarioToProject(Scenario scenario, Project project) throws IllegalArgumentException;

	/**
	 * Create a copy of a Project, assign the new Name to it.
	 *
	 * @param project project to be copied
	 * @param nameForCopy name for the copy of the project
	 * @throws PersistenceException
	 */
	void copyProject(Project project, String nameForCopy) throws PersistenceException;

	/**
	 * Creates new Project. This method changes the database.
	 *
	 * @param name name of the new project
	 * @throws PersistenceException
	 */
	void createNewProject(String name) throws PersistenceException;

	/**
	 * Set the name of the project.
	 *
	 * @param project project to alter
	 * @param projectName new name
	 * @throws PersistenceException
	 */
	void setProjectName(Project project, String projectName) throws PersistenceException;

	/**
	 * Get a list of all projects.
	 *
	 * @return list of all projects
	 */
	List<Project> getAllProjects();

	/**
	 * Check if a given name is already used.
	 *
	 * @param name name to check
	 * @return boolean
	 */
	boolean isNameUnique(final String name);
}
