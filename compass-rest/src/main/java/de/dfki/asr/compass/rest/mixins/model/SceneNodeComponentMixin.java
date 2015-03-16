/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.rest.mixins.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.dfki.asr.compass.model.SceneNode;
import de.dfki.asr.compass.rest.serialization.EntityToIDSerializer;
import de.dfki.asr.compass.rest.serialization.SceneNodeDeserializer;

@SuppressWarnings("PMD.UnusedPrivateField")
public class SceneNodeComponentMixin {
	@JsonSerialize(using = EntityToIDSerializer.class)
	@JsonDeserialize(using = SceneNodeDeserializer.class)
	private SceneNode owner;
}
