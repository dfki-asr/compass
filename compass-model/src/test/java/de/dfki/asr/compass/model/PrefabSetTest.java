/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.model;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class PrefabSetTest {

	@Test
	public void prefabSetSetParentShouldSetParent() {
		PrefabSet childPrefab = new PrefabSet("original");
		PrefabSet newParent = new PrefabSet("newParent");
		childPrefab.setParent(newParent);
		assertEquals(childPrefab.getParent(),newParent);
	}

	@Test
	public void prefabSetSetParentShouldReparent() {
		PrefabSet childPrefab = new PrefabSet("original");
		PrefabSet newParent = new PrefabSet("newParent");
		PrefabSet oldParent = new PrefabSet("oldParent");
		childPrefab.setParent(oldParent);
		childPrefab.setParent(newParent);
		assertEquals(childPrefab.getParent(), newParent);
		assertNotEquals(childPrefab.getParent(), oldParent);
	}

	@Test
	public void prefabSetSetParentShouldAppendNewChild() {
		PrefabSet childPrefab = new PrefabSet("original");
		PrefabSet newParent = new PrefabSet("newParent");
		PrefabSet oldParent = new PrefabSet("oldParent");
		childPrefab.setParent(oldParent);
		childPrefab.setParent(newParent);
		assertTrue(childPrefab.isChildOf(newParent));
	}

	@Test
	public void prefabSetSetParentShouldRemoveOldChild() {
		PrefabSet childPrefab = new PrefabSet("original");
		PrefabSet newParent = new PrefabSet("newParent");
		PrefabSet oldParent = new PrefabSet("oldParent");
		childPrefab.setParent(oldParent);
		childPrefab.setParent(newParent);
		assertFalse(childPrefab.isChildOf(oldParent));
	}

	@Test
	public void prefabSetSetSameParentShouldDoNothing() {
		PrefabSet childPrefab = new PrefabSet("original");
		PrefabSet newParent = new PrefabSet("newParent");
		childPrefab.setParent(newParent);
		PrefabSet beforeSet = childPrefab;
		childPrefab.setParent(newParent);
		assertEquals(childPrefab,beforeSet);
	}

	@Test
	public void grandchildIsChildOfGrandparent() {
		PrefabSet grandparent = new PrefabSet("grandparent");
		PrefabSet parent = new PrefabSet("parent");
		PrefabSet child = new PrefabSet("child");
		parent.setParent(grandparent);
		child.setParent(parent);
		assertTrue(child.isChildOf(grandparent));
	}
}
