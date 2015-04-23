/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.test.matcher;

import de.dfki.asr.compass.math.Quat4f;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class Quat4fSimilarity extends TypeSafeMatcher<Quat4f> {
	private final Quat4f template;
	private final float epsilon;

	public Quat4fSimilarity(final Quat4f template, final float epsilon) {
		this.template = template;
		this.epsilon = epsilon;
	}

	@Override
	protected boolean matchesSafely(final Quat4f t) {
		return t.epsilonEquals(template, epsilon);
	}

	@Override
	public void describeTo(final Description d) {
		d.appendText("similar value to ");
		d.appendValue(template);
		d.appendText(" (error of ");
		d.appendValue(epsilon);
		d.appendText(")");
	}

	public static Quat4fSimilarity similarTo(final Quat4f template, final float epsilon) {
		return new Quat4fSimilarity(template, epsilon);
	}

	public static Quat4fSimilarity similarTo(final Quat4f template) {
		return new Quat4fSimilarity(template, 0.001f);
	}
}
