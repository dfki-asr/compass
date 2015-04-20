/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.rest.mixins.model.math;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.vecmath.Matrix3f;

@SuppressWarnings("PMD.AbstractNaming")
public abstract class Quat4fMixin {
		@JsonIgnore
		public abstract de.dfki.asr.compass.math.Quat4f setFromMatrix(Matrix3f m);
}
