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
		assertTrue(projectIsCopy(originalProject, copiedProject));
	}

	@Test
	public void deepCopyProjectClearedIDs() throws IOException, ClassNotFoundException {
		Boolean clearedIDs = true;
		Project originalProject = initializeProject();
		Project copiedProject = (Project) originalProject.deepCopy();
		if (copiedProject.getId() != 0) {
			clearedIDs = false;
		}
		for (Scenario s: copiedProject.getScenarios()) {
			if (s.getId() != 0) {
				clearedIDs = false;
			}
		}
		assertTrue(clearedIDs);
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

	private Boolean projectIsCopy(final Project original, final Project copy ) {
		if (!(original.getName().equals(copy.getName()))) {
			return false;
		}
		if (original.getScenarios().size() != copy.getScenarios().size()) {
			return false;
		}
		for (Scenario s: original.getScenarios()) {
			int scenarioIndex = original.getScenarios().indexOf(s);
			Scenario c = copy.getScenarios().get(scenarioIndex);
			if (!scenarioIsCopy(s, c)) {
				return false;
			}
		}
		return true;
	}

	private Boolean scenarioIsCopy(final Scenario original, final Scenario copy) {
		//Just check whether scenarios have been copied by comparing their name.
		//DeepCopy for sceneNodes working correctly is tested seperately and can
		//be assumed to be true here
		if (!(original.getName().equals(copy.getName()))) {
			return false;
		}
		SceneNode rootA = original.getRoot();
		SceneNode rootB = copy.getRoot();

		if (!(rootA.getName().equals(rootB.getName()))) {
			return false;
		}
		return true;
	}
}
