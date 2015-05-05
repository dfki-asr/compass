/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.business;

import de.dfki.asr.compass.business.api.SceneTreeManager;
import de.dfki.asr.compass.business.services.CRUDService;
import de.dfki.asr.compass.business.exception.CompassRuntimeException;
import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.math.Quat4f;
import de.dfki.asr.compass.math.Vector3f;
import de.dfki.asr.compass.model.SceneNode;
import de.dfki.asr.compass.model.SceneNodeComponent;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.vecmath.Matrix4f;

@Named
@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class SceneTreeManagerImpl implements  Serializable, SceneTreeManager {
	private static final long serialVersionUID = 3073738672780116033L;

	@Inject
	private CRUDService crudService;

	@Override
	public SceneNode findById(final long id) throws EntityNotFoundException {
		return crudService.findById(SceneNode.class, id);
	}

	@Override
	public void remove(final SceneNode node) {
		crudService.remove(node);
	}

	@Override
	public void removeById(final long entityId) throws EntityNotFoundException, IllegalArgumentException {
		SceneNode toBeDeleted = findById(entityId);
		if (toBeDeleted.getParent() == null) {
			throw new IllegalArgumentException("Root SceneNodes for Scenarios may not be deleted.");
		}
		toBeDeleted.setParent(null);
		crudService.remove(toBeDeleted);
	}

	@Override
	public void save(final SceneNode entity) {
		crudService.save(entity);
	}

	@Override
	public SceneNode referenceById(final long id) throws EntityNotFoundException {
		return crudService.referenceById(SceneNode.class, id);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public SceneNode createNode() {
		SceneNode node = new SceneNode("New scene node");
		crudService.save(node);
		return node;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public SceneNode createNewChild(final SceneNode parent) {
		SceneNode node = createNode();
		node.setParent(parent);
		return node;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void reparentNode(final SceneNode node, final SceneNode newParent) {
		SceneNode oldParent = node.getParent();
		node.setParent(newParent);
		adjustTransformsToKeepGlobalTransformation(node, oldParent, newParent);
		crudService.save(node);
	}

	@Override
	public void reparentNode(final long nodeId, final long parentId) throws EntityNotFoundException {
		reparentNode(findById(nodeId), findById(parentId));
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void swapChildren(final SceneNode node, final int childA, final int childB) {
		List<SceneNode> sceneNodes = node.getChildren();
		Collections.swap(sceneNodes, childA, childB);
		sceneNodes.get(childA).updateOrderingIndex();
		sceneNodes.get(childB).updateOrderingIndex();
		crudService.save(node);
	}

	@Override
	public void swapChildren(final long nodeId, final int childA, final int childB) throws EntityNotFoundException {
		swapChildren(findById(nodeId), childA, childB);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public SceneNode duplicateNode(final SceneNode node) {
		SceneNode newNode;
		try {
			newNode = (SceneNode) node.deepCopy();
		} catch (IOException | ClassNotFoundException ex) {
			throw new CompassRuntimeException(ex);
		}
		newNode.setName(newNode.getName() + " - Copy");
		crudService.save(newNode);
		return newNode;
	}

	@Override
	public SceneNode duplicateNode(final long id) throws EntityNotFoundException {
		return duplicateNode(findById(id));
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void addNode(final SceneNode node, final long parentID) throws IllegalArgumentException, EntityNotFoundException {
		addNode(node, findById(parentID));
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.MANDATORY)
	public void addNode(final SceneNode node, final SceneNode parent) throws IllegalArgumentException {
		if (node.getParent() != null) {
			throw new IllegalArgumentException("Tried to add a scene node that already has a parent. Use reparentNode instead.");
		}
		node.setParent(parent);
		crudService.save(node);
	}

	@Override
	public SceneNode addPrefabInstance(final long prefabID, final long parentID) throws EntityNotFoundException {
		return addPrefabInstance(findById(prefabID), findById(parentID));
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public SceneNode addPrefabInstance(final SceneNode prefab, final SceneNode parent) {
		SceneNode node;
		try {
			node = (SceneNode) prefab.deepCopy();
		} catch (IOException | ClassNotFoundException ex) {
			throw new CompassRuntimeException(ex);
		}
		node.setParent(parent);
		crudService.save(node);
		return node;
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveNode(final SceneNode node) {
		crudService.save(node);
	}

	@Override
	public void addComponentToSceneNode(final long parentID, final SceneNodeComponent newComponent) throws EntityNotFoundException, IllegalArgumentException {
		addComponentToSceneNode(findById(parentID), newComponent);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void addComponentToSceneNode(final SceneNode parent, final SceneNodeComponent newComponent) throws IllegalArgumentException {
		newComponent.setOwner(parent);
		parent.addComponent(newComponent);
		crudService.save(newComponent);
	}

	private void adjustTransformsToKeepGlobalTransformation(final SceneNode nodeToAdjust, final SceneNode oldParent, final SceneNode newParent) {
		Matrix4f localTransform = calculateNewLocalTransform(nodeToAdjust.getLocalTransform(), oldParent.getWorldSpaceTransform(), newParent.getWorldSpaceTransform());
		setNewLocalScale(nodeToAdjust, localTransform);
		setNewLocalTranslation(nodeToAdjust, localTransform);
		setNewLocalRotation(nodeToAdjust, localTransform);
	}

	private Matrix4f calculateNewLocalTransform(final Matrix4f oldLocalTransform, final Matrix4f oldTransform, final Matrix4f newTransform) {
		Matrix4f out = new Matrix4f(oldLocalTransform);
		Matrix4f oldToNew = new Matrix4f(newTransform);
		oldToNew.invert();
		oldToNew.mul(oldTransform);
		out.mul(oldToNew, out);
		return out;
	}

	private void setNewLocalScale(final SceneNode nodeToAdjust, final Matrix4f localTransform) {
		nodeToAdjust.setLocalScale(localTransform.getScale());
	}

	private void setNewLocalTranslation(final SceneNode nodeToAdjust, final Matrix4f localTransform) {
		Vector3f translation = new Vector3f();
		localTransform.get(translation);
		nodeToAdjust.setLocalTranslation(translation);
	}

	private void setNewLocalRotation(final SceneNode nodeToAdjust, final Matrix4f localTransform) {
		Quat4f rotation = new Quat4f();
		localTransform.get(rotation);
		nodeToAdjust.setLocalRotation(rotation);
	}

	static public SceneNode getRootNode(final SceneNode node) {
		if (node.getParent() == null) {
			return node;
		}
		return getRootNode(node.getParent());
	}
}
