/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.math;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import org.testng.annotations.Test;

public class OrientationAngleTest {
	private static final float EQUALS_DELTA = 0.0001f;

	@Test
	public void RetainEulerAngles() {
		double[] steps = {0, 90, 180, -90, -180};
		for (double yaw : steps) {
			for (double pitch : steps) {
				for (double roll : steps) {
						sameEulerAngles(yaw, pitch, roll);
				}
			}
		}
	}

	private void sameEulerAngles(final double yaw, final double pitch, final double roll) {
		Orientation o = new Orientation();
		o.setLocalYaw(yaw);
		o.setLocalPitch(pitch);
		o.setLocalRoll(roll);
		assertThat("yaw",   o.getLocalYaw(),   is(closeTo(yaw, EQUALS_DELTA)));
		assertThat("pitch", o.getLocalPitch(), is(closeTo(pitch, EQUALS_DELTA)));
		assertThat("roll",  o.getLocalRoll(),  is(closeTo(roll, EQUALS_DELTA)));
	}

}
