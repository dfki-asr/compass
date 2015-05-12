/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.model;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ProjectTest {

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void projectShouldThrowIllegalArgument() {
		Project project = new Project();
		project.addScenario(null);
	}

	@Test
	public void projectAddScenarioShouldAddScenario() {
		Project project = new Project ();
		Scenario scenario = new Scenario();
		project.addScenario(scenario);
		assertTrue(project.getScenarios().contains(scenario));
	}

	@Test
	public void projectAddScenarioShouldSetProject() {
		Project project = new Project ();
		Scenario scenario = new Scenario();
		project.addScenario(scenario);
		assertEquals(scenario.getProject(), project);
	}

	@Test
	public void projectRemoveScenarioShouldDetachProject() {
		Project project = new Project ();
		Scenario scenario = new Scenario();
		project.addScenario(scenario);
		project.removeScenario(scenario);
		assertNull(scenario.getProject());
	}

	@Test
	public void projectRemoveScenarioShouldRemoveScenario() {
		Project project = new Project ();
		Scenario scenario = new Scenario();
		project.addScenario(scenario);
		project.removeScenario(scenario);
		assertFalse(project.getScenarios().contains(scenario));
	}
}
