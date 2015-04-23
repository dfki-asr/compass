/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.model;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ScenarioTest {

	@Test
	public void testStuff() {
		assertTrue(true);
	}

	@Test
	public void scenarioSetProjectShouldSetNewProject() {
		Scenario scenario = new Scenario();
		Project project = new Project();

		scenario.setProject(project);
		assertEquals(scenario.getProject(), project);
	}

	@Test
	public void scenarioSetProjectShouldReplaceOldProject() {
		Scenario scenario = new Scenario();
		Project oldProject = new Project();
		Project newProject = new Project();

		scenario.setProject(oldProject);
		scenario.setProject(newProject);

		assertEquals(scenario.getProject(), newProject);

	}

	@Test
	public void scenarioSetSameProjectShouldNotChangeScenario() {
		Scenario scenario = new Scenario();
		Project project = new Project();

		scenario.setProject(project);

		Scenario scenarioBeforeSet = scenario;
		scenario.setProject(project);

		assertEquals(scenario, scenarioBeforeSet);

	}

	@Test
	public void scenarioSetProjectShouldAddScenario() {
		Scenario scenario = new Scenario();
		Project project = new Project();

		scenario.setProject(project);
		assertTrue(project.getScenarios().contains(scenario));
	}

	@Test
	public void scenarioSetProjectShouldRemoveFromOldProject() {
		Scenario scenario = new Scenario();
		Project oldProject = new Project();
		Project newProject = new Project();

		scenario.setProject(oldProject);
		scenario.setProject(newProject);

		assertFalse(oldProject.getScenarios().contains(scenario));
	}
}
