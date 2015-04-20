/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.model.components;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import de.dfki.asr.compass.model.SceneNodeComponent;
import de.dfki.asr.compass.model.components.annotations.CompassComponent;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@CompassComponent(ui = "/WEB-INF/components/rendergeometry.xhtml", icon = "")
@JsonSubTypes.Type(RenderGeometry.class)
@XmlRootElement
public class RenderGeometry extends SceneNodeComponent implements Serializable {

	private static final long serialVersionUID = -245089801950057463L;

	@XmlElement
	private String meshSource;

	public String getMeshSource() {
		return meshSource;
	}

	public void setMeshSource(final String meshSource) {
		this.meshSource = meshSource;
	}
}
