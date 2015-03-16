/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.rest.serialization;

import java.awt.Color;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ColorXmlAdapter extends XmlAdapter<String, Color> {

	@Override
	public Color unmarshal(final String bt) {
		if (bt == null) {
			return null;
		}
		return Color.decode(bt);
	}

	@Override
	public String marshal(final Color vt) {
		if (vt == null) {
			return null;
		}
		return "0x" + Integer.toHexString(vt.getRed()) + Integer.toHexString(vt.getGreen()) + Integer.toHexString(vt.getBlue());
	}
}
