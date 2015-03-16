/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.model.components;

import de.dfki.asr.compass.model.RGBColor;
import de.dfki.asr.compass.model.SceneNodeComponent;
import java.io.Serializable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public abstract class AbstractLight extends SceneNodeComponent implements Serializable {
	private static final long serialVersionUID = -4790638500536340782L;

	@NotNull
	@XmlElement
	@Embedded
	protected RGBColor color;

	@NotNull
	@XmlElement
	protected float intensity;

	public RGBColor getColor() {
		return color;
	}

	public void setColor(final RGBColor color) {
		this.color = color;
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(final float newIntensity) {
		intensity = newIntensity;
	}
}
