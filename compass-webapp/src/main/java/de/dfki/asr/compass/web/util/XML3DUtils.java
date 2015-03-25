/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.util;

import de.dfki.asr.compass.math.Vector3f;

public final class XML3DUtils {

	private XML3DUtils(){
		//forbidden default ctor
	}

	public static Vector3f createVectorfromXML3DDOMString(String str) {
		String[] parts = str.split(" ");
		float x = Float.parseFloat(parts[0]);
		float y = Float.parseFloat(parts[1]);
		float z = Float.parseFloat(parts[2]);
		return new Vector3f(x, y, z);
	}
}
