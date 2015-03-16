/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.rest.mixins;

import com.fasterxml.jackson.databind.module.SimpleModule;
import de.dfki.asr.compass.math.Quat4f;
import de.dfki.asr.compass.model.PrefabSet;
import de.dfki.asr.compass.model.Project;
import de.dfki.asr.compass.model.Scenario;
import de.dfki.asr.compass.model.SceneNode;
import de.dfki.asr.compass.model.SceneNodeComponent;
import de.dfki.asr.compass.rest.mixins.model.PrefabSetMixin;
import de.dfki.asr.compass.rest.mixins.model.ProjectMixin;
import de.dfki.asr.compass.rest.mixins.model.ScenarioMixin;
import de.dfki.asr.compass.rest.mixins.model.SceneNodeComponentMixin;
import de.dfki.asr.compass.rest.mixins.model.SceneNodeMixin;
import de.dfki.asr.compass.rest.mixins.model.math.Quat4fMixin;
import java.io.Serializable;

public class CompassModelMixinModule extends SimpleModule implements Serializable {

	private static final long serialVersionUID = 481278111666551462L;

	public CompassModelMixinModule() {
		super("CompassJacksonMixinModule", PackageVersion.VERSION);
	}

	@Override
	public void setupModule(final SetupContext context) {
		context.setMixInAnnotations(Project.class, ProjectMixin.class);
		context.setMixInAnnotations(PrefabSet.class, PrefabSetMixin.class);
		context.setMixInAnnotations(Scenario.class, ScenarioMixin.class);
		context.setMixInAnnotations(SceneNodeComponent.class, SceneNodeComponentMixin.class);
		context.setMixInAnnotations(SceneNode.class, SceneNodeMixin.class);
		context.setMixInAnnotations(Quat4f.class, Quat4fMixin.class);
	}
}
