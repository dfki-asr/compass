/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.util;

import de.dfki.asr.compass.model.SceneNodeComponent;
import java.util.Comparator;

public class ClassNameComparator implements Comparator<Class<? extends SceneNodeComponent>> {
		@Override
		public int compare(Class<? extends SceneNodeComponent> o1, Class<? extends SceneNodeComponent> o2) {
			return o1.getSimpleName().compareTo(o2.getSimpleName());
		}
	}
