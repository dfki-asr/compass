/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.model.components;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.dfki.asr.compass.model.components.annotations.CompassComponent;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@CompassComponent
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
public class ApplicationComponent implements Serializable {
	private static final long serialVersionUID = -3459288756817221936L;
}
