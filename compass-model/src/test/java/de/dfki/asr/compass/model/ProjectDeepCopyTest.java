/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.model;

import java.io.IOException;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ProjectDeepCopyTest {

	@Test
	public void deepCopyProjectDeepCopies() throws IOException, ClassNotFoundException {
		Project originalProject = initializeProject();
		Project copiedProject = (Project) originalProject.deepCopy();
		assertProjectIsCopy(originalProject, copiedProject);
	}

	@Test
	public void deepCopyProjectClearedIDs() throws IOException, ClassNotFoundException {
		Project originalProject = initializeProject();
		Project copiedProject = (Project) originalProject.deepCopy();
		assertEquals(copiedProject.getId(), 0);
		for (Scenario copiedScenario: copiedProject.getScenarios()) {
			assertEquals(copiedScenario.getId(), 0);
		}
	}
	private Project initializeProject() {
		Project originalProject = new Project("testProject");
		Scenario scenarioA = new Scenario();
		Scenario scenarioB = new Scenario();
		scenarioA.setName("scenarioA");
		scenarioB.setName("scenarioB");
		scenarioA.getRoot().setName("rootA");
		scenarioB.getRoot().setName("rootB");
		originalProject.addScenario(scenarioA);
		originalProject.addScenario(scenarioB);
		return originalProject;
	}

	private void assertProjectIsCopy(final Project original, final Project copy ) {
		assertEquals(original.getName(),copy.getName());
		assertEquals(original.getScenarios().size(),copy.getScenarios().size());
		for (int i = 0; i < original.getScenarios().size(); i++) {
			Scenario origScenario = original.getScenarios().get(i);
			Scenario copyScenario = copy.getScenarios().get(i);
			assertScenarioIsCopy(origScenario, copyScenario);
		}
	}

	private void assertScenarioIsCopy(final Scenario original, final Scenario copy) {
		//Just check whether scenarios have been copied by comparing their name.
		//DeepCopy for sceneNodes working correctly is tested seperately and can
		//be assumed to be true here
		assertEquals(original.getName(), copy.getName());
		SceneNode origRootSceneNode = original.getRoot();
		SceneNode copyRootSceneNode = copy.getRoot();
		assertEquals(origRootSceneNode.getName(), copyRootSceneNode.getName());
	}
}
