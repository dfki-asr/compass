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
	public void checkSetParentOfChild()	{
		PrefabSet originalPrefab = new PrefabSet("original");
		PrefabSet newParent = new PrefabSet("newParent");
		PrefabSet oldParent = new PrefabSet("oldParent");
		originalPrefab.setParent(oldParent);
		originalPrefab.setParent(newParent);

		assertEquals(originalPrefab.getParent(), newParent);
		assertNotEquals(originalPrefab.getParent(), oldParent);
		assertTrue(originalPrefab.isChildOf(newParent));
	}

	@Test
	public void setParentRemovesOldChild() {
		PrefabSet originalPrefab = new PrefabSet("original");
		PrefabSet newParent = new PrefabSet("newParent");
		PrefabSet oldParent = new PrefabSet("oldParent");
		originalPrefab.setParent(oldParent);
		originalPrefab.setParent(newParent);

		assertFalse(originalPrefab.isChildOf(oldParent));

	}
	@Test
	public void checkSetParent() {
		PrefabSet originalPrefab = new PrefabSet("original");
		PrefabSet newParent = new PrefabSet("newParent");

		originalPrefab.setParent(newParent);
		assertEquals(originalPrefab.getParent(),newParent);
		assertTrue(originalPrefab.isChildOf(newParent));
	}

	@Test
	public void checkSetSameParent() {
		PrefabSet originalPrefab = new PrefabSet("original");
		PrefabSet newParent = new PrefabSet("newParent");

		originalPrefab.setParent(newParent);
		assertEquals(originalPrefab.getParent(),newParent);
		assertTrue(originalPrefab.isChildOf(newParent));
	}

}
