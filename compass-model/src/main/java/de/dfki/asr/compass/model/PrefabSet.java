/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@ApiModel //the mixins change the data references, so that only ids or lists of ids will be delivered
public class PrefabSet extends AbstractCompassEntity implements Hierarchy<PrefabSet>, Serializable {

	private static final long serialVersionUID = -4811249603735759220L;

	@Id
	@XmlTransient
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotNull(message = "Prefab Set must have a name.")
	@XmlElement
	@Column(length = 128)
	@Size(min = 1, max = 128, message = "Maximum Prefab Set name length is ${max} characters.")
	@Pattern(regexp = "^(?!\\s)[-\\w :.()]+(?<!\\s)$",
	         message = "Prefab Set name can only contain letters, dashes, braces, colons and dots. No leading or trailing spaces.")
	@ApiModelProperty("Name")
	private String name;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "parent_id")
	@XmlInverseReference(mappedBy = "children")
	private PrefabSet parent;

	@XmlElement(name = "prefabSet")
	@XmlElementWrapper(name = "children")
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "parent")
	@ApiModelProperty(value = "List containing the ids of child sets", dataType = "Array[long]")
	private final List<PrefabSet> children;

	@XmlElement(name = "node")
	@XmlElementWrapper(name = "prefabs")
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@ApiModelProperty(value = "List of ids of the prefabs in this set", dataType = "Array[long]")
	private final List<SceneNode> prefabs;

	public PrefabSet() {
		name = "";
		children = new CopyOnWriteArrayList<>();
		prefabs = new CopyOnWriteArrayList<>();
	}

	public PrefabSet(final String name) {
		this.name = name;
		this.children = new CopyOnWriteArrayList<>();
		this.prefabs = new CopyOnWriteArrayList<>();
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setId(final long id) {
		this.id = id;
	}

	@Override
	public void forceEagerFetch() {
		for (PrefabSet p : children) {
			p.forceEagerFetch();
		}
		for (SceneNode n : prefabs) {
			n.forceEagerFetch();
		}
	}

	@Override
	public void clearIdsAfterDeepCopy() {
		setId(0);
		for (PrefabSet p : children) {
			p.clearIdsAfterDeepCopy();
		}
		for (SceneNode s : prefabs) {
			s.clearIdsAfterDeepCopy();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public PrefabSet getParent() {
		return parent;
	}

	public void setParent(final PrefabSet newParent) {
		final PrefabSet oldParent = parent;
		if (oldParent != null) {
			if (oldParent.equals(newParent)) {
				return;
			}
			oldParent.removeChild(this);
		}
		if (newParent != null) {
			newParent.addChild(this);
		}
		parent = newParent;
	}

	private void addChild(final PrefabSet child) {
		if (!children.contains(child)) {
			children.add(child);
		}
	}

	private void removeChild(final PrefabSet child) {
		children.remove(child);
	}

	@Override
	public boolean isChildOf(final PrefabSet other) {
		List<PrefabSet> otherChildren = other.getChildren();
		if (otherChildren.contains(this)) {
			return true;
		}
		for (PrefabSet child : otherChildren) {
			if (isChildOf(child)) {
				return true;
			}
		}
		return false;
	}

	public List<PrefabSet> getChildren() {
		return Collections.unmodifiableList(children);
	}


	public List<SceneNode> getPrefabs() {
		return Collections.unmodifiableList(prefabs);
	}

	public void addPrefab(final SceneNode sceneNode) throws IllegalArgumentException {
		if (sceneNode == null) {
			throw new IllegalArgumentException("added null reference prefab ");
		}
		sceneNode.setParent(null);
		sceneNode.updateOrderingIndex();
		prefabs.add(sceneNode);
	}

	public void removePrefab(final SceneNode prefab) {
		prefabs.remove(prefab);
	}

	@Override
	public String toString() {
		return "PrefabSet [id=" + id + "]";
	}
}
