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
@XmlType(name = "compassQuat4f")
@XmlAccessorType(XmlAccessType.NONE)
@Embeddable
@Access(AccessType.PROPERTY)
@ApiModel
public class Quat4f extends javax.vecmath.Quat4f {
	private static final long serialVersionUID = -5841376776976285620L;

	public Quat4f() {
		//called by Vector3f
	}

	@SuppressWarnings("PMD.ExcessiveParameterList")
	public Quat4f(final float x, final float y, final float z, final float w) {
		super(x, y, z, w);
	}

	public Quat4f(final Quat4f q1) {
		super(q1);
	}

	public String toDOMString() {
		return x + " " + y + " " + z + " " + w;
	}

	public Quat4f duplicate() {
		return new Quat4f(this);
	}

	@XmlAttribute
	@Basic
	@ApiModelProperty("x")
	@SuppressWarnings("CPD-START") //CPD fails with Vector3f
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

	@XmlAttribute
	@Basic
	@ApiModelProperty("w")
	@SuppressWarnings("CPD-END")
	public float getW() {
		return w;
	}

	public void setX(final float value) {
		x = value;
	}

	public void setY(final float value) {
		y = value;
	}

	public void setZ(final float value) {
		z = value;
	}

	public void setW(final float value) {
		w = value;
	}

}
