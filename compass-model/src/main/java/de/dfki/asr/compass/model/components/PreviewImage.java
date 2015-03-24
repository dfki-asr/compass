/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.model.components;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import de.dfki.asr.compass.model.SceneNodeComponent;
import de.dfki.asr.compass.model.components.annotations.CompassComponent;
import de.dfki.asr.compass.model.resource.Image;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@CompassComponent(ui = "/WEB-INF/components/preview-image.xhtml", icon = "mimetypes-image-x-generic")
@JsonSubTypes.Type(PreviewImage.class)
@XmlRootElement
public class PreviewImage extends SceneNodeComponent {

	private static final long serialVersionUID = -4968405049356133948L;

	@OneToOne(cascade = CascadeType.ALL)
	@NotNull
	private Image image;

	public PreviewImage() {
		image = Image.pixel();
	}

	@XmlElement
	public Image getImage() {
		return image;
	}

	public void setImage(final Image image) {
		this.image = image;
	}

	@Override
	public void clearIdsAfterDeepCopy() {
		setId(0);
		image.clearIdsAfterDeepCopy();
	}
}
