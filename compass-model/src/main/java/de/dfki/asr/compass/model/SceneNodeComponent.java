/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
@ApiModel //the mixins change the data references, so that only ids or lists of ids will be delivered
public class SceneNodeComponent extends AbstractCompassEntity implements Serializable {

	private static final long serialVersionUID = -4385588207434414713L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@XmlTransient
	protected long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "owner_id")
	@XmlInverseReference(mappedBy = "components")
	@ApiModelProperty(value = "Id of the owner of the Component", dataType = "long")
	private SceneNode owner;

	public SceneNodeComponent(){
		//called by ChildClasses: Light, PreviewImage and RenderGeometry
	}

	public SceneNodeComponent(final SceneNode owner) {
		this.owner = owner;
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
	public void clearIdsAfterDeepCopy() {
		setId(0);
	}

	public SceneNode getOwner() {
		return owner;
	}

	public void setOwner(final SceneNode owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		return "SceneNodeComponent [id=" + id + "]";
	}

	@Override
	public void forceEagerFetch() {
		// no need to do anything here.
	}
}
