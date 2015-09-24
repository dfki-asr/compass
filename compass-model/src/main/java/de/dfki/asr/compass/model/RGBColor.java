/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.model;

import java.io.Serializable;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@Embeddable
@Access(AccessType.PROPERTY)
@XmlType(name = "rgbColor")
@XmlAccessorType(XmlAccessType.NONE)
public class RGBColor implements Serializable {

	private static final long serialVersionUID = -6254694981009000883L;

	protected Integer red;
	protected Integer green;
	protected Integer blue;

	public RGBColor(final Integer r, final Integer g, final Integer b) {
		if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
			throw new IllegalArgumentException("Color r, g, b values must be in [0..255]");
		}
		red = r;
		green = g;
		blue = b;
	}

	public RGBColor() {
		red = 0;
		green = 0;
		blue = 0;
	}

	@Basic
	@Min(value = 0, message = "Red color value must be greater or equal to 0.")
	@Max(value = 255, message = "Red color value must be less than or equal to 200.")
	@XmlAttribute
	public Integer getRed() {
		return red;
	}

	public void setRed(final Integer red) {
		this.red = red;
	}

	@Basic
	@Min(value = 0, message = "Green color value must be greater or equal to 0.")
	@Max(value = 255, message = "Green color value must be less than or equal to 200.")
	@XmlAttribute
	public Integer getGreen() {
		return green;
	}

	public void setGreen(final Integer green) {
		this.green = green;
	}

	@Basic
	@Min(value = 0, message = "Blue color value must be greater or equal to 0.")
	@Max(value = 255, message = "Blue color value must be less than or equal to 200.")
	@XmlAttribute
	public Integer getBlue() {
		return blue;
	}

	public void setBlue(final Integer blue) {
		this.blue = blue;
	}

	@Transient
	public String getHex() {
		return "#" +
				colorToHex(red) +
				colorToHex(green) +
				colorToHex(blue);
	}

	@SuppressWarnings("PMD.AvoidLiteralsInIfCondition")
	public void setHex(final String hexString) {
		String cleanedHexString = hexString;
		if (hexString.charAt(0) == '#') {
			cleanedHexString = hexString.substring(1, hexString.length());
		}
		red = Integer.parseInt(cleanedHexString.substring(0, 2), 16);
		green = Integer.parseInt(cleanedHexString.substring(2, 4), 16);
		blue = Integer.parseInt(cleanedHexString.substring(4, 6), 16);
	}

	private String colorToHex(final int color) {
		String hexColor = Integer.toHexString(color).toUpperCase();
		if (hexColor.length() < 2) {
			return "0".concat(hexColor);
		}
		return Integer.toHexString(color).toUpperCase();
	}
}
