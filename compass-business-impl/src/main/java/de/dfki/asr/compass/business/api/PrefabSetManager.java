/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.business.api;

import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.business.util.annotations.Hack;
import de.dfki.asr.compass.model.PrefabSet;
import de.dfki.asr.compass.model.SceneNode;
import java.util.List;

public interface PrefabSetManager extends Manager<PrefabSet> {

	/**
	 * Adds the given prefab to the given prefab set. This method modifies the database.
	 *
	 * @param prefab the prefab to add
	 * @param prefabSet the prefab set to add the prefab to
	 * @throws IllegalArgumentException
	 */
	void addPrefabToSet(SceneNode prefab, PrefabSet prefabSet) throws IllegalArgumentException;

	/**
	 * Adds the given prefab to the given prefab set. This method modifies the database.
	 *
	 * @param prefabId the id of the prefab to add
	 * @param prefabSetId the id of the prefab set to add the prefab to
	 * @throws EntityNotFoundException
	 * @throws IllegalArgumentException
	 */
	void addPrefabToSet(long prefabId, long prefabSetId) throws EntityNotFoundException, IllegalArgumentException;

	/**
	 * Adds the given prefab to the given prefab set. This method modifies the database.
	 *
	 * @param prefab the prefab to add
	 * @param prefabSetId the id of the prefab set to add the prefab to
	 * @throws EntityNotFoundException
	 * @throws IllegalArgumentException
	 */
	void addPrefabToSet(SceneNode prefab, long prefabSetId) throws EntityNotFoundException, IllegalArgumentException;

	/**
	 * Creates a new prefab out of the given scene node and adds it to the given set. This method modifies the database.
	 *
	 * @param node the scene node to create the prefab from
	 * @param prefabSet the prefab set to add the new prefab to
	 * @return the created prefab
	 * @throws IllegalArgumentException
	 */
	SceneNode addSceneNodeToPrefabSet(SceneNode node, PrefabSet prefabSet) throws IllegalArgumentException;

	/**
	 * Creates a new prefab out of the given scene node and adds it to the given set. This method modifies the database.
	 *
	 * @param nodeId the id of the scene node to create the prefab from
	 * @param prefabSetId the id of the prefab set to add the new prefab to
	 * @return the created prefab
	 * @throws EntityNotFoundException
	 * @throws IllegalArgumentException
	 */
	SceneNode addSceneNodeToPrefabSet(long nodeId, long prefabSetId) throws EntityNotFoundException, IllegalArgumentException;

	/**
	 * Appends a given set as a child to a parent set. This method modifies the database.
	 *
	 * @param parentID id of the parent set to append to
	 * @param newSet set to append
	 */
	void appendSetToParent(long parentID, PrefabSet newSet) throws EntityNotFoundException;

	/**
	 * Appends a given set as a child to a parent set. This method modifies the database.
	 *
	 * @param parent parent set to append to
	 * @param newSet set to append
	 */
	void appendSetToParent(PrefabSet parent, PrefabSet newSet);

	/**
	 * Remove all prefabs of a set and removes prefabs, which are no longer referenced in other sets, from the database as well. This method
	 * modifies	 * the database.
	 *
	 * @param set the prefabSet to cleanup
	 */
	@Hack
	void cleanupPrefabs(PrefabSet set);

	/**
	 * Creates a new child for a given prefab set and return the new child. This method modifies the database.
	 *
	 * @param parent parent set
	 * @return the new child
	 */
	PrefabSet createNewChild(PrefabSet parent);

	/**
	 * Deletes the given prefab from a given prefabSet
	 *
	 * @param prefab the prefab to be deleted
	 * @param set the prefabSet
	 */
	void deletePrefabFromPrefabSet(SceneNode prefab, PrefabSet set);

	/**
	 * Gets a list of all prefab sets
	 *
	 * @return List containing all prefab sets
	 */
	List<PrefabSet> getAllSets();

	/**
	 * Moves a prefab from a prefab set into another set. This method modifies the database.
	 *
	 * @param nodeId the id of the node to move to the prefabSet
	 * @param newPrefabSetId the id of the prefab set to move the node to
	 * @param oldPrefabSet the prefab set the node will be removed from
	 * @throws EntityNotFoundException
	 * @throws IllegalArgumentException
	 */
	void movePrefabToPrefabSet(long nodeId, long newPrefabSetId, PrefabSet oldPrefabSet) throws EntityNotFoundException, IllegalArgumentException;

	/**
	 * Moves a prefab from a prefab set into another set. This method modifies the database.
	 *
	 * @param node the node to move to the prefabSet
	 * @param newPrefabSet the prefab set to move the node to
	 * @param oldPrefabSet the prefab set the node will be removed from
	 * @throws IllegalArgumentException
	 */
	void movePrefabToPrefabSet(SceneNode node, PrefabSet newPrefabSet, PrefabSet oldPrefabSet) throws IllegalArgumentException;
}
