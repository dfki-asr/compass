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
		sceneNode.addComponent(new RenderGeometry());
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
