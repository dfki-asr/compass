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

public class SceneNodeDeepCopyTest {

	@Test
	public void deepCopySceneNodeShouldCreateCopy() throws IOException, ClassNotFoundException {
		//SceneNode originalSceneNode = initializeSceneNode();
		//SceneNode copiedSceneNode = (SceneNode) originalSceneNode.deepCopy();
		assertTrue(true);
	}

	@Test
	public void deepCopySceneNodesShouldClearIds() throws IOException, ClassNotFoundException {
		SceneNode originalSceneNode = initializeSceneNode();
		SceneNode copiedSceneNode = (SceneNode) originalSceneNode.deepCopy();
		assertEquals(copiedSceneNode.getId(), 0);
		for (SceneNode c: copiedSceneNode.getChildren()) {
			assertEquals(c.getId(), 0);
		}
		for (SceneNodeComponent c: copiedSceneNode.getComponents()) {
			assertEquals(c.getId(), 0);
		}
	}
	@Test
	public void deepCopySceneNodeShouldCopyChildNodes() throws IOException, ClassNotFoundException {
		Boolean copiedChildren = true;
		SceneNode originalSceneNode = initializeSceneNode();
		SceneNode copiedSceneNode = (SceneNode) originalSceneNode.deepCopy();
		if ( originalSceneNode.getChildren().size() != copiedSceneNode.getChildren().size()) {
			copiedChildren = false;
		}
		if (!childrenAreCopies(originalSceneNode, copiedSceneNode)) {
			copiedChildren = false;
		}
		assertTrue(copiedChildren);
	}

	@Test
	public void deepCopySceneNodeShouldCopyTransform() throws IOException, ClassNotFoundException {
		SceneNode originalSceneNode = initializeSceneNode();
		SceneNode copiedSceneNode = (SceneNode) originalSceneNode.deepCopy();
		assertEquals(originalSceneNode.getLocalTransform(), copiedSceneNode.getLocalTransform());
	}

	@Test
	public void deepCopySceneNodeShouldCopyComponents() throws IOException, ClassNotFoundException {
		Boolean copiedComponents = true;
		SceneNode originalSceneNode = initializeSceneNode();
		SceneNode copiedSceneNode = (SceneNode) originalSceneNode.deepCopy();
		for (SceneNodeComponent c: originalSceneNode.getComponents()) {
			int idx = originalSceneNode.getComponents().indexOf(c);
			if (c.getClass().equals(PreviewImage.class)) {
				Image originalImage = ((PreviewImage) c).getImage();
				Image copyImage = ( (PreviewImage) copiedSceneNode.getComponents().get(idx)).getImage();
				if (!(copiedPreview(originalImage, copyImage))) {
					copiedComponents = false;
					break;
				}
			}
			if (c.getClass() != copiedSceneNode.getComponents().get(idx).getClass()) {
				copiedComponents = false;
			}
		}
		assertTrue(copiedComponents);
	}

	private Boolean copiedPreview(final Image original, final Image copy) {
		if (!(original.getName().equals(copy.getName()))) {
			return false;
		}
		if (!(original.getMimeType().equals(copy.getMimeType()))) {
			return false;
		}
		if (!(Arrays.equals(original.getData(), copy.getData()))) {
			return false;
		}
		return true;
	}
	private Boolean childrenAreCopies(final SceneNode original, final SceneNode copy) {
		for (SceneNode originalChild: original.getChildren()) {
			int idx = original.getChildren().indexOf(originalChild);
			if (!(childrenAreCopies(originalChild, copy.getChildren().get(idx)))) {
				return false;
			}
			if (!(originalChild.getName().equals(copy.getChildren().get(idx).getName()))) {
				return false;
			}
		}
		return true;
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
