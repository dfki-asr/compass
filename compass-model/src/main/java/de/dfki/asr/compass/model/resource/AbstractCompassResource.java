/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.model.resource;

import de.dfki.asr.compass.model.AbstractCompassEntity;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractCompassResource<DataType> extends AbstractCompassEntity implements Serializable {

	private static final long serialVersionUID = -956239342880814624L;

	@Id
	@XmlTransient
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected long id;

	@XmlElement
	@NotNull
	protected String name;

	@XmlElement
	@NotNull
	protected String mimeType;

	public AbstractCompassResource(final String name, final String mimeType) {
		this.name = name;
		this.mimeType = mimeType;
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

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(final String mimeType) {
		this.mimeType = mimeType;
	}

	public abstract DataType getData();

	public abstract void setData(final DataType data);

	@Override
	public String toString() {
		return "CompassResource [name=" + name + "]";
	}
}
