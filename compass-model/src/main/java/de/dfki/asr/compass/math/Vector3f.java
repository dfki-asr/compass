/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.math;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/** Simple wrapper class to make it parseable for the REST API documentation generation */
@XmlType(name = "compassVector3f")
@XmlAccessorType(XmlAccessType.NONE)
@Embeddable
@Access(AccessType.PROPERTY)
@ApiModel
public class Vector3f extends javax.vecmath.Vector3f {
	private static final long serialVersionUID = -6249507968295690968L;

	public Vector3f() {
		//default ctor
	}

	public Vector3f(final float x, final float y, final float z) {
		super(x, y, z);
	}

	public Vector3f(final Vector3f v1) {
		super(v1);
	}

	public String toDOMString() {
		return x + " " + y + " " + z;
	}

	public void div(final Vector3f b) {
		x /= b.x;
		y /= b.y;
		z /= b.z;
	}

	public void mul(final Vector3f b) {
		x *= b.x;
		y *= b.y;
		z *= b.z;
	}

	@XmlAttribute
	@Basic
	@ApiModelProperty("x")
	public float getX() {
		return x;
	}

	@XmlAttribute
	@Basic
	@ApiModelProperty("y")
	public float getY() {
		return y;
	}

	@XmlAttribute
	@Basic
	@ApiModelProperty("z")
	public float getZ() {
		return z;
	}

	public void setX(final float x) {
		this.x = x;
	}

	public void setY(final float y) {
		this.y = y;
	}

	public void setZ(final float z) {
		this.z = z;
	}

	public Vector3f duplicate() {
		return new Vector3f(x, y, z);
	}
}
