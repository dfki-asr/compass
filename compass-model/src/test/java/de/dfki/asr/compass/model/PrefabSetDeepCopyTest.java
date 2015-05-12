/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.model;

import java.io.IOException;
import org.testng.annotations.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PrefabSetDeepCopyTest {

	@Test
	public void deepCopyPrefabSetShouldCreateCopy() throws IOException, ClassNotFoundException {
		PrefabSet originalSet = initializePrefabSet();
		PrefabSet clone = (PrefabSet) originalSet.deepCopy();
		assertThat(clone.getName(), equalTo(originalSet.getName()));
	}

	@Test
	public void deepCopyPrefabSetShouldCopyChildren() throws IOException, ClassNotFoundException {
		PrefabSet originalSet = initializePrefabSet();
		PrefabSet clone = (PrefabSet) originalSet.deepCopy();
		assertThat("same amount of child sets",
				originalSet.getChildren().size() == clone.getChildren().size()
		);
		for (int i = 0; i < originalSet.getChildren().size(); i++) {
			PrefabSet originalChild = originalSet.getChildren().get(i);
			PrefabSet clonedChild = clone.getChildren().get(i);
			assertThat("order retained", clonedChild.getName().equals(originalChild.getName()));
		}
	}

	@Test
	public void deepCopyPrefabSetShouldClearIds() throws IOException, ClassNotFoundException {
		PrefabSet originalSet = initializePrefabSet();
		PrefabSet clone = (PrefabSet) originalSet.deepCopy();
		assertThat("clone id cleared", clone.getId() == 0);
		for (PrefabSet c: clone.getChildren()) {
			assertThat("child PrefabSet id cleared", c.getId() == 0);
		}
		for (SceneNode p: clone.getPrefabs()) {
			assertThat("child prefab id cleared", p.getId() == 0);
		}
	}

	@Test
	public void deepCopyPrefabSetShouldCopyPrefabs() throws IOException, ClassNotFoundException {
		PrefabSet originalSet = initializePrefabSet();
		PrefabSet clone = (PrefabSet) originalSet.deepCopy();
		assertThat("same amount of prefabs",
			originalSet.getPrefabs().size() == clone.getPrefabs().size()
		);
		for (int i = 0; i < originalSet.getPrefabs().size(); i++) {
			SceneNode original = originalSet.getPrefabs().get(i);
			SceneNode copy = clone.getPrefabs().get(i);
			assertThat("order retained", copy.getName().equals(original.getName()));
		}
	}

	private PrefabSet initializePrefabSet() {
		PrefabSet originalSet = new PrefabSet();
		PrefabSet childSetA = new PrefabSet();
		PrefabSet childSetB = new PrefabSet();
		originalSet.setId(1);
		childSetA.setName("childA");
		childSetB.setName("childB");
		childSetA.setId(2);
		childSetB.setId(3);
		childSetA.setParent(originalSet);
		childSetB.setParent(originalSet);
		SceneNode prefabA = new SceneNode();
		SceneNode prefabB = new SceneNode();
		prefabA.setName("TestPrefabA");
		prefabB.setName("TestPrefabB");
		prefabA.setId(4);
		prefabB.setId(5);
		originalSet.addPrefab(prefabA);
		originalSet.addPrefab(prefabB);
		originalSet.setName("PrefabSetTest");
		return originalSet;
	}
}
