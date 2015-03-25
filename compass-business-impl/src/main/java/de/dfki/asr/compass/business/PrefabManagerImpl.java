/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.business;

import de.dfki.asr.compass.business.api.PrefabManager;
import de.dfki.asr.compass.business.exception.CompassRuntimeException;
import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.model.PrefabSet;
import de.dfki.asr.compass.model.SceneNode;
import de.dfki.asr.compass.business.services.CRUDService;
import de.dfki.asr.compass.model.PrefabSet_;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Named
@Stateless
public class PrefabManagerImpl implements Serializable, PrefabManager {

	private static final long serialVersionUID = -6552373864799752601L;

	@Inject
	private CRUDService crudService;

	@Inject
	private CriteriaBuilder criteriaBuilder;

	@Override
	public SceneNode createPrefabFromSceneNode(final SceneNode node) {
		SceneNode prefab;
		try {
			prefab = (SceneNode) node.deepCopy();
		} catch (IOException | ClassNotFoundException ex) {
			throw new CompassRuntimeException(ex);
		}
		prefab.setParent(null);
		prefab.updateOrderingIndex();
		crudService.save(prefab);
		return prefab;
	}

	@Override
	public SceneNode createPrefabFromSceneNode(final long id) throws EntityNotFoundException {
		return createPrefabFromSceneNode(crudService.findById(SceneNode.class, id));
	}

	@Override
	public void replacePrefabWithSceneNode(final SceneNode prefab, final SceneNode node) {
		SceneNode instance;
		try {
			instance = (SceneNode) node.deepCopy();
		} catch (IOException | ClassNotFoundException ex) {
			throw new CompassRuntimeException(ex);
		}
		instance.setId(prefab.getId());
		instance.setParent(null);
		crudService.save(instance);
	}

	@Override
	public void replacePrefabWithSceneNode(final long prefabId, final long nodeId) throws EntityNotFoundException {
		replacePrefabWithSceneNode(crudService.findById(SceneNode.class, prefabId), crudService.findById(SceneNode.class, nodeId));
	}

	@Override
	public boolean prefabIsNotReferenced(final SceneNode prefab) {
		CriteriaQuery<Long> q = criteriaBuilder.createQuery(Long.class);
		Root<PrefabSet> s = q.from(PrefabSet.class);
		q.where(criteriaBuilder.isMember(prefab, s.get(PrefabSet_.prefabs)));
		q.select(criteriaBuilder.count(s));
		Long count = crudService.createQuery(q).getSingleResult();
		return count.equals(0L);
	}
}
