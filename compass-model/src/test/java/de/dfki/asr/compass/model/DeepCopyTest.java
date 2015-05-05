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
		originalSet.setName("PrefabSetTest");
		PrefabSet clone = (PrefabSet) originalSet.deepCopy();
		assertEquals(originalSet.getName(), clone.getName());
	}

	@Test(enabled = false)
	public void deepCopyProjectShouldCreateCopy() throws IOException, ClassNotFoundException {
		Project originalProject = new Project();
		Project clone = (Project) originalProject.deepCopy();
		assertTrue(clone.equals(originalProject));
	}
}
