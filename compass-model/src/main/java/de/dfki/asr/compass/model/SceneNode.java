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
import de.dfki.asr.compass.math.Orientation;
import de.dfki.asr.compass.math.Quat4f;
import de.dfki.asr.compass.math.Vector3f;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.vecmath.Matrix4f;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@SuppressWarnings( { "PMD.GodClass", "PMD.ExcessivePublicCount", "PMD.ExcessiveImports" } )
@ApiModel //the mixins change the data references, so that only ids or lists of ids will be delivered
public class SceneNode extends AbstractCompassEntity implements Hierarchy<SceneNode>, Serializable {

	private static final long serialVersionUID = 5647862077196653069L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@XmlTransient
	private long id;

	@NotNull
	@XmlAttribute
	@Column(length = 128)
	@Size(min = 1, max = 128, message = "{validation.SceneNode.name.tooLong}")
	@Pattern(regexp = "^(?!\\s)[-\\w :.()]+(?<!\\s)$", message = "{validation.SceneNode.name.Pattern}")
	@ApiModelProperty("Name")
	private String name;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	@XmlInverseReference(mappedBy = "children")
	@ApiModelProperty(value = "Id of the parent SceneNode, 0 if no parent is set", dataType = "long")
	private SceneNode parent;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "owner")
	@XmlElementWrapper(name = "components")
	@XmlElementRef
	@ApiModelProperty("SceneNodeComponents")
	private List<SceneNodeComponent> components;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "parent")
	@OrderBy("indexInParentChildren")
	@XmlElementWrapper(name = "children")
	@XmlElement(name = "node")
	@ApiModelProperty(value = "If of the child SceneNodes", dataType = "Array[long]")
	private List<SceneNode> children;

	@XmlTransient
	@JsonIgnore
	@SuppressWarnings("PMD.UnusedPrivateField")
	// Used implicityly by: this.children @OrderBy("indexInParentChildren").
	// Updated in prepareOrderingIndex, called by JPA.
	private Integer indexInParentChildren = 0;

	@XmlAttribute
	@ApiModelProperty("Wether the 3D model of the SceneNode is selectable in the 3D scene.")
	private boolean selectable3d;

	@XmlAttribute
	@ApiModelProperty("Wether the 3D model of the SceneNode is visibie in the 3D scene.")
	private boolean visible;

	@NotNull
	@Embedded
	// Not an @XmlElement because it's exposed through get/setLocalRotation
	private Orientation localOrientation;

	@NotNull
	@XmlElement
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "x", column = @Column(name = "translate_x")),
		@AttributeOverride(name = "y", column = @Column(name = "translate_y")),
		@AttributeOverride(name = "z", column = @Column(name = "translate_z"))
	})
	@ApiModelProperty("Local translation")
	private Vector3f localTranslation;

	@NotNull
	@XmlElement
	@ApiModelProperty("Local uniform scale")
	private float localScale;

	/// this constructor is needed by Weld as well as SceneNodeFacesStringConverter
	public SceneNode() {
		initializeFields();
	}

	public SceneNode(final String name) {
		initializeFields();
		this.name = name;
	}

	private void initializeFields() {
		name = "";
		components = new CopyOnWriteArrayList<>();
		children = new CopyOnWriteArrayList<>();
		visible = true;
		localOrientation = new Orientation();
		selectable3d = true;
		localTranslation = new Vector3f();
		localScale = 1.f;
		indexInParentChildren = 0;
	}

	@PrePersist
	@PreUpdate
	private void prepareOrderingIndex() {
		if (parent == null) {
			indexInParentChildren = 0;
		} else {
			indexInParentChildren = parent.children.indexOf(this);
		}
	}

	public void updateOrderingIndex() {
		this.prepareOrderingIndex();
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
		for (SceneNode child : children) {
			child.forceEagerFetch();
		}
		for (SceneNodeComponent component : components) {
			component.forceEagerFetch();
		}
	}

	@Override
	public void clearIdsAfterDeepCopy() {
		setId(0);
		for (SceneNode child : children) {
			child.clearIdsAfterDeepCopy();
		}
		for (SceneNodeComponent component : components) {
			component.clearIdsAfterDeepCopy();
		}
	}

	@XmlElement
	@ApiModelProperty("Local Orientation represented as quatarnion.")
	public Quat4f getLocalRotation() {
		return localOrientation.getLocalRotation();
	}

	@JsonIgnore
	public double getLocalYaw() {
		return localOrientation.getLocalYaw();
	}

	@JsonIgnore
	public double getLocalPitch() {
		return localOrientation.getLocalPitch();
	}

	@JsonIgnore
	public double getLocalRoll() {
		return localOrientation.getLocalRoll();
	}

	@JsonIgnore
	public void setLocalYaw(final double yaw) {
		localOrientation.setLocalYaw(yaw);
	}

	@JsonIgnore
	public void setLocalPitch(final double pitch) {
		localOrientation.setLocalPitch(pitch);
	}

	@JsonIgnore
	public void setLocalRoll(final double roll) {
		localOrientation.setLocalRoll(roll);
	}

	public void setLocalRotation(final Quat4f rotation) {
		Quat4f incoming = rotation;
		if (Float.isNaN(rotation.w)) {
			incoming = new Quat4f(0.f, 0.f, 0.f, 1.0f);
		}
		incoming.normalize();
		localOrientation.setLocalRotation(incoming);
	}

	public Vector3f getLocalTranslation() {
		return localTranslation;
	}

	public void setLocalTranslation(final Vector3f translation) {
		localTranslation = translation;
	}

	public float getLocalScale() {
		return localScale;
	}

	public void setLocalScale(final float scale) {
		localScale = scale;
	}

	@JsonIgnore
	public Matrix4f getWorldSpaceTransform() {
		Matrix4f out = getLocalTransform();
		if (parent != null) {
			out.mul(parent.getWorldSpaceTransform(), out);
		}
		return out;
	}

	@JsonIgnore
	public Matrix4f getLocalTransform() {
		Matrix4f out = new Matrix4f();
		out.setIdentity();
		out.setScale(localScale);
		out.setRotation(localOrientation.getLocalRotation());
		out.setTranslation(localTranslation);
		return out;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public SceneNode getParent() {
		return parent;
	}

	@Override
	public void setParent(final SceneNode newParent) {
		final SceneNode oldParent = parent;
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

	private void addChild(final SceneNode child) {
		if (!children.contains(child)) {
			children.add(child);
		}
	}

	private void removeChild(final SceneNode child) {
		children.remove(child);
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(final boolean visible) {
		this.visible = visible;
	}

	public boolean isSelectable3d() {
		return selectable3d;
	}

	public void setSelectable3d(final boolean isSelectable3d) {
		selectable3d = isSelectable3d;
	}

	public void addComponent(final SceneNodeComponent sceneNodeComponent) throws IllegalArgumentException {
		if (sceneNodeComponent == null) {
			throw new IllegalArgumentException("SceneNode-Component to add was passed as null");
		}
		components.add(sceneNodeComponent);
	}

	public boolean removeComponent(final SceneNodeComponent sceneNodeComponent) {
		return components.remove(sceneNodeComponent);
	}

	public List<SceneNodeComponent> getComponents() {
		return Collections.unmodifiableList(components);
	}

	public void setComponents(final List<SceneNodeComponent> components) {
		this.components.clear();
		this.components.addAll(components);
	}

	public <T extends SceneNodeComponent> List<T> getComponentsByType(final Class<T> clazz) {
		List<T> returnList = new CopyOnWriteArrayList<>();
		for (SceneNodeComponent c : this.components) {
			if (clazz.isAssignableFrom(c.getClass())) {
				returnList.add((T) c);
			}
		}
		return returnList;
	}

	public List<SceneNode> getChildren() {
		return children;
	}

	public boolean isChildOf(final SceneNode other) {
		List<SceneNode> otherChildren = other.getChildren();
		if (otherChildren.contains(this)) {
			return true;
		}
		for (SceneNode child : otherChildren) {
			if (isChildOf(child)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "SceneNode [id=" + id + "]";
	}
}
