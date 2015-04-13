/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.math;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class Vector3fDOMStringTest {
	@Test
	public void shouldConvertUnitVector() {
		Vector3f vector = new Vector3f(0, 1, 0);
		assertEquals(vector.toDOMString(), "0.0 1.0 0.0");
	}

	@Test
	public void shouldMaintainOrder() {
		Vector3f vector = new Vector3f(1.0f, 2.0f, 3.0f);
		assertEquals(vector.toDOMString(), "1.0 2.0 3.0");
	}

	@Test
	public void shouldParseZeroVector() {
		Vector3f vector = Vector3f.fromDOMString("0.0 0.0 0.0");
		assertEquals(vector.x, 0.0f);
		assertEquals(vector.y, 0.0f);
		assertEquals(vector.z, 0.0f);
	}

	@Test(expectedExceptions = NumberFormatException.class)
	public void shouldThrowOnParseTooFewEntries() {
		Vector3f vector = Vector3f.fromDOMString("0.0 0.0");
		assertNull(vector);
	}

	@Test(expectedExceptions = NumberFormatException.class)
	public void shouldThrowOnParseTooManyEntries() {
		Vector3f vector = Vector3f.fromDOMString("0.0 0.0 0.0 0.0");
		assertNull(vector);
	}

	@Test(expectedExceptions = NumberFormatException.class)
	public void shouldThrowOnParseNonNumberEntries() {
		Vector3f vector = Vector3f.fromDOMString("foo bar baz");
		assertNull(vector);
	}
}
