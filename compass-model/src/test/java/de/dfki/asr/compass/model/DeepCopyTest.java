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

public class DeepCopyTest {

	@Test
	public void deepCopyPrefabSetShouldCreateCopy() throws IOException, ClassNotFoundException {
		PrefabSet originalSet = new PrefabSet();
		PrefabSet childSetA = new PrefabSet();
		PrefabSet childSetB = new PrefabSet();
		childSetA.setName("childA");
		childSetB.setName("childB");
		childSetA.setParent(originalSet);
		childSetB.setParent(originalSet);
		SceneNode prefabA = new SceneNode();
		SceneNode prefabB = new SceneNode();
		prefabA.setName("TestPrefabA");
		prefabB.setName("TestPrefabB");
		originalSet.addPrefab(prefabA);
		originalSet.addPrefab(prefabB);
		originalSet.setName("PrefabSetTest");
		PrefabSet clone = (PrefabSet) originalSet.deepCopy();
		assertTrue(prefabSetIsCopy(originalSet, clone));
	}

	@Test(enabled = false)
	public void deepCopyProjectShouldCreateCopy() throws IOException, ClassNotFoundException {
		Project originalProject = new Project();
		Project clone = (Project) originalProject.deepCopy();
		assertTrue(clone.equals(originalProject));
	}

	private Boolean prefabSetIsCopy(final PrefabSet original, final PrefabSet copy) {
		if (!original.getName().equals(copy.getName())) {
			return false;
		}
		if (!prefabSetCopiedChildren(original, copy)) {
			return false;
		}
		if (!prefabSetCopiedPrefabs(original, copy)) {
			return false;
		}
		return true;
	}

	private Boolean prefabSetCopiedChildren(final PrefabSet original, final PrefabSet copy) {
		if (original.getChildren().size() != copy.getChildren().size()) {
			return false;
		}
		for (PrefabSet child: original.getChildren()) {
			int childIndex = original.getChildren().indexOf(child);
			if (!(copy.getChildren().get(childIndex).getName().equals(child.getName()))) {
				return false;
			}
		}
		return true;
	}

	private Boolean prefabSetCopiedPrefabs(final PrefabSet original, final PrefabSet copy) {
		if (original.getPrefabs().size() != copy.getPrefabs().size()) {
			return false;
		}
		for (SceneNode prefab: original.getPrefabs()) {
			int prefabIndex = original.getPrefabs().indexOf(prefab);
			if (!(copy.getPrefabs().get(prefabIndex).getName().equals(prefab.getName()))) {
				return false;
			}
		}
		return true;
	}
}
