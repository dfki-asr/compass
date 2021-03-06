/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.rest.mixins.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.dfki.asr.compass.model.Scenario;
import de.dfki.asr.compass.rest.serialization.EntityListToIDListSerializer;
import de.dfki.asr.compass.rest.serialization.ScenarioListDeserializer;
import java.util.List;

@SuppressWarnings("PMD.UnusedPrivateField")
public class ProjectMixin {

	@JsonDeserialize(using = ScenarioListDeserializer.class)
	@JsonSerialize(using = EntityListToIDListSerializer.class)
	private List<Scenario> scenarios;
}
