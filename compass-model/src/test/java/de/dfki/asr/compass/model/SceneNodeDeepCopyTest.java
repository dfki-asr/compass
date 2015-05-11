/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.model;

import de.dfki.asr.compass.math.Vector3f;
import de.dfki.asr.compass.model.components.PreviewImage;
import de.dfki.asr.compass.model.components.RenderGeometry;
import de.dfki.asr.compass.model.resource.Image;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeSuite;

public class SceneNodeDeepCopyTest {
	private SceneNode originalSceneNode, copiedSceneNode;

	@BeforeSuite
	public void setupCopy() throws IOException, ClassNotFoundException {
		originalSceneNode = initializeSceneNode();
		copiedSceneNode = (SceneNode) originalSceneNode.deepCopy();
	}

	@Test
	public void deepCopySceneNodesShouldClearIds() {
		assertEquals(copiedSceneNode.getId(), 0);
		for (SceneNode c: copiedSceneNode.getChildren()) {
			assertEquals(c.getId(), 0, "SceneNode id");
		}
		for (SceneNodeComponent c: copiedSceneNode.getComponents()) {
			assertEquals(c.getId(), 0, "SceneNodeComponent id");
		}
	}
	@Test
	public void deepCopySceneNodeShouldCopyChildNodes() {
		assertEquals(originalSceneNode.getChildren().size(), copiedSceneNode.getChildren().size());
		assertChildrenAreCopies(originalSceneNode, copiedSceneNode);
	}

	@Test
	public void deepCopySceneNodeShouldCopyTransform() {
		assertEquals(originalSceneNode.getLocalTransform(), copiedSceneNode.getLocalTransform());
	}

	@Test
	public void deepCopySceneNodeShouldCopyComponents() {
		Boolean copiedComponents = true;
		for (SceneNodeComponent c: originalSceneNode.getComponents()) {
			int idx = originalSceneNode.getComponents().indexOf(c);
			if (c.getClass().equals(PreviewImage.class)) {
				Image originalImage = ((PreviewImage) c).getImage();
				Image copyImage = ( (PreviewImage) copiedSceneNode.getComponents().get(idx)).getImage();
				assertCopiedPreview(originalImage, copyImage);
			}
			assertEquals(c.getClass(), copiedSceneNode.getComponents().get(idx).getClass());
		}
		assertTrue(copiedComponents);
	}

	private void assertCopiedPreview(final Image original, final Image copy) {
		assertEquals(original.getName(), copy.getName());
		assertEquals(original.getMimeType(), copy.getMimeType());
		assertTrue(Arrays.equals(original.getData(), copy.getData()));
	}

	private void assertChildrenAreCopies(final SceneNode original, final SceneNode copy) {
		for (SceneNode originalChild: original.getChildren()) {
			int idx = original.getChildren().indexOf(originalChild);
			SceneNode copiedChild = copy.getChildren().get(idx);
			assertChildrenAreCopies(originalChild, copiedChild);
			assertEquals(originalChild.getName(), copiedChild.getName());
		}
	}

	private SceneNode initializeSceneNode() {
		SceneNode sceneNode = new SceneNode("parentSceneNode");
		sceneNode.setId(1);
		sceneNode.setLocalPitch(14.5);
		sceneNode.setLocalYaw(14.5);
		sceneNode.setLocalRoll(14.5);
		sceneNode.setLocalTranslation(new Vector3f(12f, -3.4f, 56.3f));
		RenderGeometry render = new RenderGeometry();
		render.setId(7);
		sceneNode.addComponent(render);
		PreviewImage sceneNodePreview = generatePreview();
		sceneNode.addComponent(sceneNodePreview);
		addNodesTo(sceneNode);
		return sceneNode;
	}

	private PreviewImage generatePreview() {
		Image image = new Image("testImage", "image/jpg");
		byte[] content = new byte[100];
		new Random().nextBytes(content);
		image.setData(content);
		PreviewImage preview = new PreviewImage();
		preview.setImage(image);
		preview.setId(6);
		return preview;
	}

	private SceneNode addNodesTo(final SceneNode sceneNode) {
		SceneNode childA = new SceneNode("childA");
		SceneNode childAB = new SceneNode("childAB");
		SceneNode childB = new SceneNode("childB");
		childA.setId(2);
		childB.setId(3);
		childAB.setId(4);
		childAB.setParent(childA);
		childA.setParent(sceneNode);
		childB.setParent(sceneNode);
		return sceneNode;
	}
}
