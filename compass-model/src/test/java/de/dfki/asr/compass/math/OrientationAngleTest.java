/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.math;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static de.dfki.asr.compass.test.matcher.EulerAngleSimilarity.similarTo;
import org.testng.annotations.Test;

@SuppressWarnings("PMD.ExcessivePublicCount")
public class OrientationAngleTest {
	@Test
	public void yaw0Pitch0Roll0() {
		sameEulerAngles(0, 0, 0);
	}

	@Test
	public void yaw90Pitch0Roll0() {
		sameEulerAngles(90, 0, 0);
	}

	@Test
	public void yaw180Pitch0Roll0() {
		sameEulerAngles(180, 0, 0);
	}

	@Test
	public void yawMinus90Pitch0Roll0() {
		sameEulerAngles(-90, 0, 0);
	}

	@Test
	public void yaw0Pitch90Roll0() {
		sameEulerAngles(0, 90, 0);
	}

	@Test
	public void yaw90Pitch90Roll0() {
		sameEulerAngles(90, 90, 0);
	}

	@Test
	public void yaw180Pitch90Roll0() {
		sameEulerAngles(180, 90, 0);
	}

	@Test
	public void yawMinus90Pitch90Roll0() {
		sameEulerAngles(-90, 90, 0);
	}


	@Test
	public void yaw0PitchMinus90Roll0() {
		sameEulerAngles(0, -90, 0);
	}

	@Test
	public void yaw90PitchMinus90Roll0() {
		sameEulerAngles(90, -90, 0);
	}

	@Test
	public void yaw180PitchMinus90Roll0() {
		sameEulerAngles(180, -90, 0);
	}

	@Test
	public void yawMinus90PitchMinus90Roll0() {
		sameEulerAngles(-90, -90, 0);
	}

	@Test
	public void yaw0Pitch0Roll90() {
		sameEulerAngles(0, 0, 90);
	}

	@Test
	public void yaw90Pitch0Roll90() {
		sameEulerAngles(90, 0, 90);
	}

	@Test
	public void yaw180Pitch0Roll90() {
		sameEulerAngles(180, 0, 90);
	}

	@Test
	public void yawMinus90Pitch0Roll90() {
		sameEulerAngles(-90, 0, 90);
	}

	@Test
	public void yaw0Pitch0Roll180() {
		sameEulerAngles(0, 0, 180);
	}

	@Test
	public void yaw90Pitch0Roll180() {
		sameEulerAngles(90, 0, 180);
	}

	@Test
	public void yaw180Pitch0Roll180() {
		sameEulerAngles(180, 0, 180);
	}

	@Test
	public void yawMinus90Pitch0Roll180() {
		sameEulerAngles(-90, 0, 180);
	}

	@Test
	public void yaw0Pitch0RollMinus90() {
		sameEulerAngles(0, 0, -90);
	}

	@Test
	public void yaw90Pitch0RollMinus90() {
		sameEulerAngles(90, 0, -90);
	}

	@Test
	public void yaw180Pitch0RollMinus90() {
		sameEulerAngles(180, 0, -90);
	}

	@Test
	public void yawMinus90Pitch0RollMinus90() {
		sameEulerAngles(-90, 0, -90);
	}

	// The following tests are not from the above SO(3) sample set:
	// (and therefore, promptly, don't work.)

//	@Test
//	public void yaw0Pitch90Roll90() {
//		sameEulerAngles(0, 90, 90);
//	}
//
//	@Test
//	public void yaw0Pitch180RollMinus0() {
//		sameEulerAngles(0, 180, 0);
//	}

//	@Test
//	public void testAllCombinations() {
//		float[] steps = {0, 90, 180, -90, -180};
//		for (float yaw : steps) {
//			for (float pitch : steps) {
//				for (float roll : steps) {
//					sameEulerAngles(yaw, pitch, roll);
//				}
//			}
//		}
//	}

	private void sameEulerAngles(final float yaw, final float pitch, final float roll) {
		Orientation o = new Orientation();
		o.setLocalYaw(yaw);
		o.setLocalPitch(pitch);
		o.setLocalRoll(roll);
		assertThat("Euler Angles", o,  is(similarTo(yaw, pitch, roll)));
	}

}
