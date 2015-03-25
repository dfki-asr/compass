/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.business;

import de.dfki.asr.compass.business.api.PrefabSetManager;
import de.dfki.asr.compass.business.api.PrefabManager;
import de.dfki.asr.compass.business.util.annotations.Hack;
import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.model.PrefabSet;
import de.dfki.asr.compass.model.SceneNode;
import de.dfki.asr.compass.business.services.CRUDService;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@Stateless
public class PrefabSetManagerImpl implements  Serializable, PrefabSetManager {

	private static final long serialVersionUID = -5276223598746613967L;

	@Inject
	private CRUDService crudService;

	@Inject
	private PrefabManager prefabManager;

	@Override
	public PrefabSet findById(final long id) throws EntityNotFoundException {
		return crudService.findById(PrefabSet.class, id);
	}

	@Override
	public void removeById(final long id) throws EntityNotFoundException {
		remove(findById(id));
	}

	@Override
	public void remove(final PrefabSet prefabSet) {
		PrefabSet parent = prefabSet.getParent();
		prefabSet.setParent(null);
		if (parent != null) {
			crudService.save(parent);
		}
		crudService.remove(prefabSet);
	}

	@Override
	public void save(final PrefabSet prefabSet) {
		crudService.save(prefabSet);
	}

	@Override
	public PrefabSet referenceById(final long id) throws EntityNotFoundException {
		return crudService.referenceById(PrefabSet.class, id);
	}

	@Override
	public void addPrefabToSet(final SceneNode prefab, final PrefabSet prefabSet) throws IllegalArgumentException {
		prefabSet.addPrefab(prefab);
		crudService.save(prefabSet);
	}

	@Override
	public void addPrefabToSet(final long prefabId, final long prefabSetId) throws EntityNotFoundException, IllegalArgumentException {
		addPrefabToSet(crudService.findById(SceneNode.class, prefabId), crudService.findById(PrefabSet.class, prefabSetId));
	}

	@Override
	public void addPrefabToSet(final SceneNode prefab, final long prefabSetId) throws EntityNotFoundException, IllegalArgumentException {
		addPrefabToSet(prefab, crudService.findById(PrefabSet.class, prefabSetId));
	}

	@Override
	public SceneNode addSceneNodeToPrefabSet(final SceneNode node, final PrefabSet prefabSet) throws IllegalArgumentException {
		SceneNode prefab = prefabManager.createPrefabFromSceneNode(node);
		addPrefabToSet(prefab, prefabSet);
		return prefab;
	}

	@Override
	public SceneNode addSceneNodeToPrefabSet(final long nodeId, final long prefabSetId) throws EntityNotFoundException, IllegalArgumentException {
		return addSceneNodeToPrefabSet(crudService.findById(SceneNode.class, nodeId), crudService.findById(PrefabSet.class, prefabSetId));
	}

	@Override
	public void movePrefabToPrefabSet(final SceneNode node, final PrefabSet newPrefabSet, final PrefabSet oldPrefabSet) throws IllegalArgumentException {
		deletePrefabFromPrefabSet(node, oldPrefabSet);
		addPrefabToSet(node, newPrefabSet);
	}

	@Override
	public void movePrefabToPrefabSet(final long nodeId, final long newPrefabSetId, final PrefabSet oldPrefabSet) throws EntityNotFoundException, IllegalArgumentException {
		movePrefabToPrefabSet(crudService.findById(SceneNode.class, nodeId), crudService.findById(PrefabSet.class, newPrefabSetId), oldPrefabSet);
	}

	@Override
	public void deletePrefabFromPrefabSet(final SceneNode prefab, final PrefabSet set) {
		set.removePrefab(prefab);
		crudService.save(set);
		if (prefabManager.prefabIsNotReferenced(prefab)) {
			crudService.remove(prefab);
		}
	}

	@Override
	public PrefabSet createNewChild(final PrefabSet parent) {
		PrefabSet newSet = crudService.save(new PrefabSet(findNewPrefabSetName(parent)));
		newSet.setParent(parent);
		crudService.save(parent);
		return newSet;
	}

	private String findNewPrefabSetName(final PrefabSet parent) {
		String desiredPrefix = "New Set";
		Integer index = 0;
		String result = desiredPrefix;
		List<PrefabSet> prefabs = parent.getChildren();
		Iterator<PrefabSet> iterator = prefabs.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().getName().equals(result)) {
				index++;
				result = desiredPrefix + " " + index.toString();
				// restart scanning
				iterator = prefabs.iterator();
			}
		}
		return result;
	}

	@Hack
	@Override
	public void cleanupPrefabs(final PrefabSet set) {
		// Since JPA can't take care of @ManyToMany orphans, they need to be
		// cleaned up here. This works, but is just as horrible as it looks.
		// Sorry.
		LinkedList<SceneNode> orphans = new LinkedList<>();
		LinkedList<SceneNode> toUnlink = new LinkedList<>();
		for (SceneNode prefab : set.getPrefabs()) {
			if (prefabManager.prefabIsNotReferenced(prefab)) {
				orphans.push(prefab);
			} else {
				toUnlink.push(prefab);
			}
		}
		for (SceneNode stillLinked : toUnlink) {
			set.removePrefab(stillLinked);
		}
		for (SceneNode orphan : orphans) {
			set.removePrefab(orphan);
			crudService.remove(orphan);
		}
	}

	@Override
	public List<PrefabSet> getAllSets() {
		return crudService.findAll(PrefabSet.class);
	}

	@Override
	public void appendSetToParent(final long parentID, final PrefabSet newSet) throws EntityNotFoundException {
		appendSetToParent(findById(parentID), newSet);
	}

	@Override
	public void appendSetToParent(final PrefabSet parent, final PrefabSet newSet) {
		newSet.setParent(parent);
		crudService.save(newSet);
	}
}
