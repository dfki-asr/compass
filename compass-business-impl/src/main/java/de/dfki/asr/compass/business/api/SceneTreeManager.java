/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.business.api;

import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.model.SceneNode;
import de.dfki.asr.compass.model.SceneNodeComponent;

public interface SceneTreeManager extends Manager<SceneNode> {

	/**
	 * Adds a component to a scenenode. This method modifies the database.
	 *
	 * @param parentID id of the node the component is added to
	 * @param newComponent the parent the prefab should be added under
	 * @throws de.dfki.asr.compass.business.exception.EntityNotFoundException
	 * @throws IllegalArgumentException
	 */
	void addComponentToSceneNode(long parentID, SceneNodeComponent newComponent) throws EntityNotFoundException, IllegalArgumentException;

	/**
	 * Adds a component to a scenenode. This method modifies the database.
	 *
	 * @param parent id of the node the component is added to
	 * @param newComponent the parent the prefab should be added under
	 * @throws IllegalArgumentException
	 */
	void addComponentToSceneNode(SceneNode parent, SceneNodeComponent newComponent) throws IllegalArgumentException;

	/**
	 * Add a node to a parent. Parent specified by id.
	 *
	 * @param node node to add
	 * @param parentID id of the parent
	 * @throws IllegalArgumentException
	 * @throws EntityNotFoundException
	 */
	void addNode(SceneNode node, long parentID) throws IllegalArgumentException, EntityNotFoundException;

	/**
	 * Adds the given scene node to the tree under the given parent. This method modifies the database.
	 *
	 * @param node the scene node to be added to the tree
	 * @param parent the parent the scene node should be added under
	 * @throws IllegalArgumentException
	 */
	void addNode(SceneNode node, SceneNode parent) throws IllegalArgumentException;

	/**
	 * Creates and instance of the given prefab and adds it to the tree under the given parent. This method modifies the database.
	 *
	 * @param prefabID the id of the prefab to instantiate and add to the tree
	 * @param parentID the id of the parent the prefab should be added under
	 * @return the newly instanced prefab that was added to the tree
	 * @throws de.dfki.asr.compass.business.exception.EntityNotFoundException
	 */
	SceneNode addPrefabInstance(long prefabID, long parentID) throws EntityNotFoundException;

	/**
	 * Creates and instance of the given prefab and adds it to the tree under the given parent. This method modifies the database.
	 *
	 * @param prefab the prefab to instantiate and add to the tree
	 * @param parent the parent the prefab should be added under
	 * @return the newly instanced prefab that was added to the tree
	 */
	SceneNode addPrefabInstance(SceneNode prefab, SceneNode parent);

	/**
	 * Creates and returns a new scene node underneath the given parent. This method modifies the database.
	 *
	 * @param parent the parent to create the new node under
	 * @return the new scene node
	 */
	SceneNode createNewChild(SceneNode parent);

	/**
	 * Creates and returns a new scene node. This method modifies the database. This method modifies the database.
	 *
	 * @return the new scene node
	 */
	SceneNode createNode();

	/**
	 * Duplicates the given scene node and its subtree. The duplicated node will retain the same parent. This method modifies the database.
	 * This method modifies the database.
	 *
	 * @param node the scene node to duplicate
	 * @return the duplicated scene node
	 */
	SceneNode duplicateNode(SceneNode node);

	/**
	 * Duplicates the given scene node and its subtree. The duplicated node will retain the same parent. This method modifies the database.
	 *
	 * @param id the id of the scene node to be duplicated
	 * @return the duplicated scene node
	 * @throws EntityNotFoundException
	 */
	SceneNode duplicateNode(long id) throws EntityNotFoundException;

	/**
	 * Moves the given scene node into the children of the given parent. This method modifies the database.
	 *
	 * @param node the scene node to be moved
	 * @param newParent the scene node's new parent
	 */
	void reparentNode(SceneNode node, SceneNode newParent);

	/**
	 * Retrieves the given scene node from the database and moves it into the given parent's children. This method modifies the database.
	 *
	 * @param nodeId the id of the scene node to move
	 * @param parentId the id of the scene node's new parent
	 * @throws EntityNotFoundException
	 */
	void reparentNode(long nodeId, long parentId) throws EntityNotFoundException;

	/**
	 * Persists the changes to the given scene node to the database. This method modifies the database.
	 *
	 * @param node the node to save
	 */
	void saveNode(SceneNode node);

	/**
	 * Swaps the two child nodes of the given scene node at the given positions. This method modifies the database.
	 *
	 * @param node the scene node whos children are to be swapped
	 * @param childA the index of the first child node
	 * @param childB the index of the second child node
	 */
	void swapChildren(SceneNode node, int childA, int childB);

	/**
	 * Swaps the two child nodes of the given scene node at the given positions. This method modifies the database.
	 *
	 * @param nodeId the id of the scene node whos children are to be swapped
	 * @param childA the index of the first child node
	 * @param childB the index of the second child node
	 * @throws EntityNotFoundException
	 */
	void swapChildren(long nodeId, int childA, int childB) throws EntityNotFoundException;
}
