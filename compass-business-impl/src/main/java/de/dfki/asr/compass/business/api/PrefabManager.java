/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.business.api;

import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.model.SceneNode;

public interface PrefabManager {

	/**
	 * Creates a new prefab from the given scene node but does not add it to a prefab set. This method modifies the database.
	 *
	 * @param node the scene node that should be used to create the prefab
	 * @return the new prefab
	 */
	SceneNode createPrefabFromSceneNode(SceneNode node);

	/**
	 * Creates a new prefab from the given scene node but does not add it to a prefab set. This method modifies the database.
	 *
	 * @param id id of the scene node that should be used to create the prefab
	 * @return the new prefab
	 * @throws EntityNotFoundException
	 */
	SceneNode createPrefabFromSceneNode(long id) throws EntityNotFoundException;

	/**
	 * Replaces the given prefab with a copy of the given scene node. This method modifies the database.
	 *
	 * @param prefab the prefab to be replaced
	 * @param node the scene node to replace it with
	 */
	void replacePrefabWithSceneNode(SceneNode prefab, SceneNode node);

	/**
	 * Replaces the given prefab with a copy of the given scene node. This method modifies the database.
	 *
	 * @param prefabId the id of the prefab to replace
	 * @param nodeId the id of the scene node to replace it with
	 * @throws EntityNotFoundException
	 */
	void replacePrefabWithSceneNode(long prefabId, long nodeId) throws EntityNotFoundException;

	/**
	 * Check whether a prefab is still referenced by a prefab set.
	 *
	 * @param prefab prefab to check
	 * @return result
	 */
	boolean prefabIsNotReferenced(SceneNode prefab);
}
