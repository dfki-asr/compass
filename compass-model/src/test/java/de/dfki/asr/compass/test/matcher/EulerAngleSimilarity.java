/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.test.matcher;

import de.dfki.asr.compass.math.Orientation;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class EulerAngleSimilarity extends TypeSafeMatcher<Orientation> {
	private final float targetYaw;
	private final float targetPitch;
	private final float targetRoll;
	private final float epsilon;

	@SuppressWarnings("PMD.ExcessiveParameterList")
	public EulerAngleSimilarity(final float yaw, final float pitch, final float roll, final float epsilon) {
		this.targetYaw = yaw;
		this.targetPitch = pitch;
		this.targetRoll = roll;
		this.epsilon = epsilon;
	}

	@Override
	protected boolean matchesSafely(final Orientation t) {
		return Math.abs(t.getLocalYaw() - targetYaw) < epsilon &&
		       Math.abs(t.getLocalPitch() - targetPitch) < epsilon &&
		       Math.abs(t.getLocalRoll() - targetRoll) < epsilon;
	}

	@Override
	public void describeTo(final Description d) {
		d.appendText("Euler angles similar to ");
		d.appendValueList("(",", ",")",targetYaw, targetPitch, targetRoll);
	}

	@Override
	protected void describeMismatchSafely(final Orientation item, final Description mismatchDescription) {
		mismatchDescription.appendText("was Euler angles ");
		mismatchDescription.appendValueList("(",", ",")", item.getLocalYaw(), item.getLocalPitch(), item.getLocalRoll());
	}

	@SuppressWarnings("PMD.ExcessiveParameterList")
	public static EulerAngleSimilarity similarTo(final float yaw, final float pitch, final float roll, final float epsilon) {
		return new EulerAngleSimilarity(yaw, pitch, roll, epsilon);
	}

	public static EulerAngleSimilarity similarTo(final float yaw, final float pitch, final float roll) {
		return new EulerAngleSimilarity(yaw, pitch, roll, 0.001f);
	}
}
