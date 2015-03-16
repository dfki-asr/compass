/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.model.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.xml.bind.annotation.XmlInlineBinaryData;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Image extends AbstractCompassResource<byte[]> {

	private static final long serialVersionUID = 7898829368880181497L;

	private static final String BASE64PIXELPNG = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABAQMAAAAl21bKAAAAA1BMVEUAAACnej3aAAAAAXRSTlMAQObYZgAAAApJREFUCNdjYAAAAAIAAeIhvDMAAAAASUVORK5CYII=";

	@Lob
	@XmlInlineBinaryData
	@JsonIgnore
	@Basic(fetch = FetchType.LAZY)
	protected byte[] data;

	public Image(final String name, final String mimeType) {
		super(name, mimeType);
	}

	@Deprecated /* only used by JAXB */
	public Image() {
		// Initializing with null to trigger BeanValidation.
		super(null, null);
	}

	@SuppressWarnings("PMD.ArrayIsStoredDirectly")
	public Image(final String name, final String mimeType, final byte[] data) {
		this(name, mimeType);
		this.data = data;
	}

	@Override
	@SuppressWarnings("PMD.MethodReturnsInternalArray")
	public byte[] getData() {
		return data;
	}

	@Override
	@SuppressWarnings("PMD.ArrayIsStoredDirectly")
	public void setData(final byte[] data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Image [id=" + id + ", name=" + name + "]";
	}

	public static Image pixel() {
		return new Image("1x1.png", "image/png", javax.xml.bind.DatatypeConverter.parseBase64Binary(BASE64PIXELPNG));
	}

	@Override
	public void forceEagerFetch() {
		// no need to do anything here.
	}
}
