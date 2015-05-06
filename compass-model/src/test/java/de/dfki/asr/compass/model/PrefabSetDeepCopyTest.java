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

public class PrefabSetDeepCopyTest {

	@Test
	public void deepCopyPrefabSetShouldCreateCopy() throws IOException, ClassNotFoundException {
		PrefabSet originalSet = initializePrefabSet();
		PrefabSet clone = (PrefabSet) originalSet.deepCopy();
		assertEquals(originalSet.getName(), clone.getName());
	}

	@Test
	public void deepCopyPrefabSetShouldCopyChildren() throws IOException, ClassNotFoundException {
		Boolean childrenCopied = true;
		PrefabSet originalSet = initializePrefabSet();
		PrefabSet clone = (PrefabSet) originalSet.deepCopy();
		if (originalSet.getChildren().size() != clone.getChildren().size()) {
			childrenCopied = false;
		}
		for (PrefabSet child: originalSet.getChildren()) {
			int childIndex = originalSet.getChildren().indexOf(child);
			if (!(clone.getChildren().get(childIndex).getName().equals(child.getName()))) {
				childrenCopied = false;
			}
		}
		assertTrue(childrenCopied);
	}

	@Test
	public void deepCopyPrefabSetShouldClearIds() throws IOException, ClassNotFoundException {
		Boolean clearedIds = true;
		PrefabSet originalSet = initializePrefabSet();
		PrefabSet clone = (PrefabSet) originalSet.deepCopy();
		if (clone.getId() != 0) {
			clearedIds = false;
		}
		for (PrefabSet c: clone.getChildren()) {
			if (c.getId() != 0) {
				clearedIds = false;
			}
		}
		for (SceneNode p: clone.getPrefabs()) {
			if (p.getId() != 0) {
				clearedIds = false;
			}
		}
		assertTrue(clearedIds);
	}

	@Test
	public void deepCopyPrefabSetShouldCopyPrefabs() throws IOException, ClassNotFoundException {
		Boolean copiedPrefabs = true;
		PrefabSet originalSet = initializePrefabSet();
		PrefabSet clone = (PrefabSet) originalSet.deepCopy();
		if (originalSet.getPrefabs().size() != clone.getPrefabs().size()) {
			copiedPrefabs = false;
		}
		for (SceneNode prefab: originalSet.getPrefabs()) {
			int prefabIndex = originalSet.getPrefabs().indexOf(prefab);
			if (!(clone.getPrefabs().get(prefabIndex).getName().equals(prefab.getName()))) {
				copiedPrefabs =  false;
			}
		}
		assertTrue(copiedPrefabs);
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
