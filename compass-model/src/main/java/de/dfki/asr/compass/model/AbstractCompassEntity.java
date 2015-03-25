/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.xml.bind.annotation.XmlTransient;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractCompassEntity {

	@XmlTransient
	public abstract long getId();

	public abstract void setId(long id);

	public abstract void clearIdsAfterDeepCopy();

	@XmlTransient
	@JsonIgnore
	public boolean isPersisted() {
		return this.getId() != 0;
	}

	public AbstractCompassEntity deepCopy() throws IOException, ClassNotFoundException {
		AbstractCompassEntity entity = null;
		forceEagerFetch();
		// Write the object out to a byte array
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(bos);
		out.writeObject(this);
		out.flush();
		out.close();

		// Make an input stream from the byte array and read
		// a copy of the object back in.
		ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
		entity = (AbstractCompassEntity) in.readObject();
		entity.clearIdsAfterDeepCopy();
		return entity;
	}

	public abstract void forceEagerFetch();
}
