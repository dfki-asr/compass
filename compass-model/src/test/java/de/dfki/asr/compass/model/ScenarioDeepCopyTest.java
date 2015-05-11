/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

package de.dfki.asr.compass.model;

import de.dfki.asr.compass.model.resource.Image;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeTest;

public class ScenarioDeepCopyTest {
	private Scenario originalScenario, copiedScenario;

	@BeforeTest
	public void setUp() throws IOException, ClassNotFoundException {
		originalScenario = initializeScenario();
		copiedScenario = (Scenario) originalScenario.deepCopy();
	}

	@Test
	public void deepCopyScenarioShouldCreateCopy() throws IOException, ClassNotFoundException {
		assertEquals(originalScenario.getName(),
		             copiedScenario.getName(),
		             "Scenario name");
		assertEquals(originalScenario.getProject().getName(),
		             copiedScenario.getProject().getName(),
		             "Project name");
		assertEquals(originalScenario.getRoot().getName(),
		             copiedScenario.getRoot().getName(),
		             "Root SceneNode name");
	}

	@Test
	public void deepCopyScenarioShouldCopyPreview() throws IOException, ClassNotFoundException {
		Boolean imageCopied = true;


		Image originalImage = originalScenario.getPreview();
		Image copiedImage = copiedScenario.getPreview();
		if (!(originalImage.getName().equals(copiedImage.getName()))) {
			imageCopied = false;
		}

		if (!(originalImage.getMimeType().equals(copiedImage.getMimeType()))) {
			imageCopied = false;
		}

		if (!(Arrays.equals(originalImage.getData(), copiedImage.getData()))) {
			imageCopied = false;
		}
		assertTrue(imageCopied);
	}

	@Test
	public void deepCopyScenarioShouldClearIds() throws IOException, ClassNotFoundException {
		Boolean idsCleared = true;
		if (copiedScenario.getId() != 0 ) {
			idsCleared = false;
		}
		if (copiedScenario.getPreview().getId() != 0 ) {
			idsCleared = false;
		}
		if (copiedScenario.getRoot().getId() != 0 ) {
			idsCleared = false;
		}
		if (copiedScenario.getProject().getId() != 0 ) {
			idsCleared = false;
		}
		assertTrue(idsCleared);
	}

	private Scenario initializeScenario() {
		Scenario scenario = new Scenario();
		scenario.setName("testScenario");
		Image previewImage = new Image("testPreview", "image/jpg");
		//some random data simulating image content
		byte[] imageData = new byte[100];
		new Random().nextBytes(imageData);
		previewImage.setData(imageData);
		scenario.setPreview(previewImage);
		scenario.getRoot().setName("testRoot");
		scenario.setProject(new Project("testProject"));
		scenario.setId(1);
		return scenario;
	}
}
