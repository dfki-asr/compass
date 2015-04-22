/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.math;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class OrientationUpdateTest {

	private static final double EQUALS_DELTA = 0.0001;

	@Test
	public void yaw0Pitch0Roll0() {
		checkConversions(new Quat4f(0, 0, 0, 1), 0, 0, 0);
	}

	@Test
	public void yaw90Pitch0Roll0() {
		checkConversions(new Quat4f(0, 0.7071f, 0, 0.7071f), 90, 0, 0);
	}

	@Test
	public void yaw180Pitch0Roll0() {
		checkConversions(new Quat4f(0, 1, 0, 0), 180, 0, 0);
	}

	@Test
	public void yawMinus90Pitch0Roll0() {
		checkConversions(new Quat4f(0, -0.7071f, 0, 0.7071f), -90, 0, 0);
	}

	@Test
	public void yaw0Pitch90Roll0() {
		checkConversions(new Quat4f(0, 0, 0.7071f, 0.7071f), 0, 90, 0);
	}

	@Test
	public void yaw90Pitch90Roll0() {
		checkConversions(new Quat4f(0.5f, 0.5f, 0.5f, 0.5f), 90, 90, 0);
	}

	@Test
	public void yaw180Pitch90Roll0() {
		checkConversions(new Quat4f(0.7071f, 0.7071f, 0, 0), 180, 90, 0);
	}

	@Test
	public void yawMinus90Pitch90Roll0() {
		checkConversions(new Quat4f(-0.5f, -0.5f, 0.5f, 0.5f), -90, 90, 0);
	}


	@Test
	public void yaw0PitchMinus90Roll0() {
		checkConversions(new Quat4f(0, 0, -0.7071f, 0.7071f), 0, -90, 0);
	}

	@Test
	public void yaw90PitchMinus90Roll0() {
		checkConversions(new Quat4f(-0.5f, 0.5f, -0.5f, 0.5f), 90, -90, 0);
	}

	@Test
	public void yaw180PitchMinus90Roll0() {
		checkConversions(new Quat4f(-0.7071f, 0.7071f, 0, 0), 180, -90, 0);
	}

	@Test
	public void yawMinus90PitchMinus90Roll0() {
		checkConversions(new Quat4f(0.5f, -0.5f, -0.5f, 0.5f), -90, -90, 0);
	}

	@Test
	public void yaw0Pitch0Roll90() {
		checkConversions(new Quat4f(0.7071f, 0, 0, 0.7071f), 0, 0, 90);
	}

	@Test
	public void yaw90Pitch0Roll90() {
		checkConversions(new Quat4f(0.5f, 0.5f, -0.5f, 0.5f), 90, 0, 90);
	}

	@Test
	public void yaw180Pitch0Roll90() {
		checkConversions(new Quat4f(0, 0.7071f, -0.7071f, 0), 180, 0, 90);
	}

	@Test
	public void yawMinus90Pitch0Roll90() {
		checkConversions(new Quat4f(0.5f, -0.5f, 0.5f, 0.5f), -90, 0, 90);
	}

	@Test
	public void yaw0Pitch0Roll180() {
		checkConversions(new Quat4f(1, 0, 0, 0), 0, 0, 180);
	}

	@Test
	public void yaw90Pitch0Roll180() {
		checkConversions(new Quat4f(0.7071f, 0, -0.7071f, 0), 90, 0, 180);
	}

	@Test
	public void yaw180Pitch0Roll180() {
		checkConversions(new Quat4f(0, 0, 1, 0), 180, 0, 180);
	}

	@Test
	public void yawMinus90Pitch0Roll180() {
		checkConversions(new Quat4f(0.7071f, 0, 0.7071f, 0), -90, 0, 180);
	}

	@Test
	public void yaw0Pitch0RollMinus90() {
		checkConversions(new Quat4f(-0.7071f, 0, 0, 0.7071f), 0, 0, -90);
	}

	@Test
	public void yaw90Pitch0RollMinus90() {
		checkConversions(new Quat4f(-0.5f, 0.5f, 0.5f, 0.5f), 90, 0, -90);
	}

	@Test
	public void yaw180Pitch0RollMinus90() {
		checkConversions(new Quat4f(0, 0.7071f, 0.7071f, 0), 180, 0, -90);
	}

	@Test
	public void yawMinus90Pitch0RollMinus90() {
		checkConversions(new Quat4f(-0.5f, -0.5f, -0.5f, 0.5f), -90, 0, -90);
	}

	@SuppressWarnings("PMD.ExcessiveParameterList")
	private void checkConversions(final Quat4f quat, final double yaw, final double pitch, final double roll) {
		Orientation o = new Orientation();
		o.setLocalYaw(yaw);
		o.setLocalPitch(pitch);
		o.setLocalRoll(roll);
		checkEulerAngles(o, yaw, pitch, roll);
		checkQuaternion(o, quat);
	}

	@SuppressWarnings("PMD.ExcessiveParameterList")
	private void checkEulerAngles(final Orientation o, final double yaw, final double pitch, final double roll) {
		assertEquals(o.getLocalYaw(), yaw, EQUALS_DELTA, "Yaw");
		assertEquals(o.getLocalPitch(), pitch, EQUALS_DELTA, "Pitch");
		assertEquals(o.getLocalRoll(), roll, EQUALS_DELTA, "Roll");
	}

	private void checkQuaternion(final Orientation o, final Quat4f quat) {
		Quat4f actualRotation = o.getLocalRotation();
		//assertEquals(actualRotation, quat, "Quat4f");
		assertEquals(actualRotation.x, quat.x, EQUALS_DELTA, "X");
		assertEquals(actualRotation.y, quat.y, EQUALS_DELTA, "Y");
		assertEquals(actualRotation.z, quat.z, EQUALS_DELTA, "Z");
		assertEquals(actualRotation.w, quat.w, EQUALS_DELTA, "W");
	}

}
