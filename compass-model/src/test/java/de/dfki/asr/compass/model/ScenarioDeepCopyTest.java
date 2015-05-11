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
	public void deepCopyScenarioShouldCreateCopy() {
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
	public void deepCopyScenarioShouldCopyPreview() {
		Image originalImage = originalScenario.getPreview();
		Image copiedImage = copiedScenario.getPreview();
		assertEquals(originalImage.getName(), copiedImage.getName(), "Image name");
		assertEquals(originalImage.getMimeType(), copiedImage.getMimeType(), "Image mimeType");
		assertTrue(Arrays.equals(originalImage.getData(), copiedImage.getData()), "Image data");
	}

	@Test
	public void deepCopyScenarioShouldClearIds() {
		assertEquals(copiedScenario.getId(), 0, "Scenario id");
		assertEquals(copiedScenario.getPreview().getId(), 0, "Preview id");
		assertEquals(copiedScenario.getRoot().getId(), 0, "Root node id");
		assertEquals(copiedScenario.getProject().getId(), 0, "Project id");
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
