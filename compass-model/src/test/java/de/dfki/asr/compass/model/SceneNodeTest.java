/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.model;

import de.dfki.asr.compass.math.Vector3f;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class SceneNodeTest {

	@Test
	public void sceneNodeIsChildShouldReturnFalse () {
		SceneNode parent = new SceneNode();
		SceneNode noChild = new SceneNode();
		assertFalse(noChild.isChildOf(parent));
	}

	@Test
	public void sceneNodeIsChildShouldReturnTrue () {
		SceneNode parent = new SceneNode();
		SceneNode child = new SceneNode();
		child.setParent(parent);
		assertTrue(child.isChildOf(parent));
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void sceneNodeAddComponentShouldThrowIllegalArgument() {
		SceneNode sceneNode = new SceneNode();
		sceneNode.addComponent(null);
	}

	@Test
	public void sceneNodeWorldSpaceOfRootShouldBeLocal() {
		SceneNode sceneNode = new SceneNode();
		sceneNode.setLocalTranslation(new Vector3f(10.0f, 20.0f, 30.0f));
		sceneNode.setLocalRoll(40.0);
		assertEquals(sceneNode.getWorldSpaceTransform(), sceneNode.getLocalTransform());
	}
}
