/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.model.components;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import de.dfki.asr.compass.model.RGBColor;
import de.dfki.asr.compass.model.components.annotations.CompassComponent;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@CompassComponent(ui = "/WEB-INF/components/directional-light.xhtml", icon = "status-weather-clear")
@JsonSubTypes.Type(DirectionalLight.class)
public class DirectionalLight extends AbstractLight implements Serializable {
	private static final long serialVersionUID = -8970757528356744465L;

	protected boolean castShadow;

	public DirectionalLight() {
		color = new RGBColor(255, 255, 255);
		intensity = 1;
		castShadow = false;
	}

	public boolean isCastShadow() {
		return castShadow;
	}

	public void setCastShadow(final boolean castShadow) {
		this.castShadow = castShadow;
	}
}
