/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.math;

import static de.dfki.asr.compass.test.matcher.Quat4fSimilarity.similarTo;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.IsCloseTo.closeTo;
import org.testng.annotations.Test;

public class OrientationUpdateTest {

	private static final float EQUALS_DELTA = 0.0001f;

	@Test
	public void yaw0Pitch0Roll0() {
		sameQuaternion(new Quat4f(0, 0, 0, 1), yawPitchRoll(0, 0, 0));
	}

	@Test
	public void yaw90Pitch0Roll0() {
		sameQuaternion(new Quat4f(0, 0.7071f, 0, 0.7071f), yawPitchRoll(90, 0, 0));
	}

	@Test
	public void yaw180Pitch0Roll0() {
		sameQuaternion(new Quat4f(0, 1, 0, 0), yawPitchRoll(180, 0, 0));
	}

	@Test
	public void yawMinus90Pitch0Roll0() {
		sameQuaternion(new Quat4f(0, -0.7071f, 0, 0.7071f), yawPitchRoll(-90, 0, 0));
	}

	@Test
	public void yaw0Pitch90Roll0() {
		sameQuaternion(new Quat4f(0, 0, 0.7071f, 0.7071f), yawPitchRoll(0, 90, 0));
	}

	@Test
	public void yaw90Pitch90Roll0() {
		sameQuaternion(new Quat4f(0.5f, 0.5f, 0.5f, 0.5f), yawPitchRoll(90, 90, 0));
	}

	@Test
	public void yaw180Pitch90Roll0() {
		sameQuaternion(new Quat4f(0.7071f, 0.7071f, 0, 0), yawPitchRoll(180, 90, 0));
	}

	@Test
	public void yawMinus90Pitch90Roll0() {
		sameQuaternion(new Quat4f(-0.5f, -0.5f, 0.5f, 0.5f), yawPitchRoll(-90, 90, 0));
	}


	@Test
	public void yaw0PitchMinus90Roll0() {
		sameQuaternion(new Quat4f(0, 0, -0.7071f, 0.7071f), yawPitchRoll(0, -90, 0));
	}

	@Test
	public void yaw90PitchMinus90Roll0() {
		sameQuaternion(new Quat4f(-0.5f, 0.5f, -0.5f, 0.5f), yawPitchRoll(90, -90, 0));
	}

	@Test
	public void yaw180PitchMinus90Roll0() {
		sameQuaternion(new Quat4f(-0.7071f, 0.7071f, 0, 0), yawPitchRoll(180, -90, 0));
	}

	@Test
	public void yawMinus90PitchMinus90Roll0() {
		sameQuaternion(new Quat4f(0.5f, -0.5f, -0.5f, 0.5f), yawPitchRoll(-90, -90, 0));
	}

	@Test
	public void yaw0Pitch0Roll90() {
		sameQuaternion(new Quat4f(0.7071f, 0, 0, 0.7071f), yawPitchRoll(0, 0, 90));
	}

	@Test
	public void yaw90Pitch0Roll90() {
		sameQuaternion(new Quat4f(0.5f, 0.5f, -0.5f, 0.5f), yawPitchRoll(90, 0, 90));
	}

	@Test
	public void yaw180Pitch0Roll90() {
		sameQuaternion(new Quat4f(0, 0.7071f, -0.7071f, 0), yawPitchRoll(180, 0, 90));
	}

	@Test
	public void yawMinus90Pitch0Roll90() {
		sameQuaternion(new Quat4f(0.5f, -0.5f, 0.5f, 0.5f), yawPitchRoll(-90, 0, 90));
	}

	@Test
	public void yaw0Pitch0Roll180() {
		sameQuaternion(new Quat4f(1, 0, 0, 0), yawPitchRoll(0, 0, 180));
	}

	@Test
	public void yaw90Pitch0Roll180() {
		sameQuaternion(new Quat4f(0.7071f, 0, -0.7071f, 0), yawPitchRoll(90, 0, 180));
	}

	@Test
	public void yaw180Pitch0Roll180() {
		sameQuaternion(new Quat4f(0, 0, 1, 0), yawPitchRoll(180, 0, 180));
	}

	@Test
	public void yawMinus90Pitch0Roll180() {
		sameQuaternion(new Quat4f(0.7071f, 0, 0.7071f, 0), yawPitchRoll(-90, 0, 180));
	}

	@Test
	public void yaw0Pitch0RollMinus90() {
		sameQuaternion(new Quat4f(-0.7071f, 0, 0, 0.7071f), yawPitchRoll(0, 0, -90));
	}

	@Test
	public void yaw90Pitch0RollMinus90() {
		sameQuaternion(new Quat4f(-0.5f, 0.5f, 0.5f, 0.5f), yawPitchRoll(90, 0, -90));
	}

	@Test
	public void yaw180Pitch0RollMinus90() {
		sameQuaternion(new Quat4f(0, 0.7071f, 0.7071f, 0), yawPitchRoll(180, 0, -90));
	}

	@Test
	public void yawMinus90Pitch0RollMinus90() {
		sameQuaternion(new Quat4f(-0.5f, -0.5f, -0.5f, 0.5f), yawPitchRoll(-90, 0, -90));
	}

	@SuppressWarnings("PMD.ExcessiveParameterList")
	private void checkConversions(final Quat4f quat, final double yaw, final double pitch, final double roll) {
		Orientation o = yawPitchRoll(yaw, pitch, roll);
		assertThat(o.getLocalRotation(), is(similarTo(quat, EQUALS_DELTA)));
		assertThat(o.getLocalYaw(),   is(closeTo(yaw, EQUALS_DELTA)));
		assertThat(o.getLocalPitch(), is(closeTo(pitch, EQUALS_DELTA)));
		assertThat(o.getLocalRoll(),  is(closeTo(roll, EQUALS_DELTA)));
	}

	private Orientation yawPitchRoll(final double yaw, final double pitch, final double roll) {
		Orientation o = new Orientation();
		o.setLocalYaw(yaw);
		o.setLocalPitch(pitch);
		o.setLocalRoll(roll);
		return o;
	}

	private void sameQuaternion(final Quat4f quaternion, final Orientation orientation) {
		assertThat("Quaternion", orientation.getLocalRotation(), similarTo(quaternion, EQUALS_DELTA));
	}

	private void sameEulerAngles(final double yaw, final double pitch, final double roll) {
		Orientation o = yawPitchRoll(yaw, pitch, roll);
		assertThat("yaw",   o.getLocalYaw(),   is(closeTo(yaw, EQUALS_DELTA)));
		assertThat("pitch", o.getLocalPitch(), is(closeTo(pitch, EQUALS_DELTA)));
		assertThat("roll",  o.getLocalRoll(),  is(closeTo(roll, EQUALS_DELTA)));
	}

}
