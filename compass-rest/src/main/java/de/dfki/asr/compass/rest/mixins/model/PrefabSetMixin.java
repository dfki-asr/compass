/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.rest.mixins.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.dfki.asr.compass.model.PrefabSet;
import de.dfki.asr.compass.model.SceneNode;
import de.dfki.asr.compass.rest.serialization.EntityListToIDListSerializer;
import de.dfki.asr.compass.rest.serialization.PrefabSetListDeserializer;
import de.dfki.asr.compass.rest.serialization.SceneNodeListDeserializer;
import java.util.List;

@SuppressWarnings("PMD.UnusedPrivateField")
public class PrefabSetMixin {

	@JsonSerialize(using = EntityListToIDListSerializer.class)
	@JsonDeserialize(using = PrefabSetListDeserializer.class)
	private List<PrefabSet> children;

	@JsonSerialize(using = EntityListToIDListSerializer.class)
	@JsonDeserialize(using = SceneNodeListDeserializer.class)
	private List<SceneNode> prefabs;

}
