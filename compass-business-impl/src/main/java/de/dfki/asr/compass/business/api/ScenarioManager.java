/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.business.api;

import de.dfki.asr.compass.business.exception.PersistenceException;
import de.dfki.asr.compass.model.Project;
import de.dfki.asr.compass.model.Scenario;
import de.dfki.asr.compass.model.resource.Image;
import java.io.IOException;

/**
 *
 * @author wolfgang
 */
public interface ScenarioManager extends Manager<Scenario> {

	/**
	 * Creates a new Scenario.
	 *
	 * @param name name
	 * @param project parent project
	 * @param preview preview image
	 * @throws PersistenceException
	 * @throws IOException
	 */
	void createScenario(String name, Project project, Image preview) throws PersistenceException, IOException;

	/**
	 * Duplicates a Scenario using a new Name for the duplicate.
	 *
	 * @param scenario scenario to duplicate
	 * @param duplicateScenarioName new name for the duplicate
	 * @throws PersistenceException
	 */
	void duplicateScenario(Scenario scenario, String duplicateScenarioName) throws PersistenceException;

	/**
	 * Edits the scenario. Changes name and preview image.
	 *
	 * @param scenario scenario to edit
	 * @param newScenarioName new name
	 * @param newPreviewImage new preview image
	 */
	void editScenario(Scenario scenario, String newScenarioName, Image newPreviewImage);

	/**
	 * Edits the scenario. Changes name.
	 *
	 * @param scenario scenario to edit
	 * @param newScenarioName new name
	 */
	void editScenario(Scenario scenario, String newScenarioName);

}
