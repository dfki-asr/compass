/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.rest.serialization;

import de.dfki.asr.compass.model.SceneNode;

public class SceneNodeDeserializer extends ReferenceDeserializer<SceneNode> {
	public SceneNodeDeserializer() {
		super(SceneNode.class);
	}
}
