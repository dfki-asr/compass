/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.model;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class RGBColorTest {

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void rgbColorShouldThrowIllegalArgument() {
		RGBColor illegalColor = new RGBColor(300,0,0);
		assertNull(illegalColor);
	}

	@Test
	public void rgbColorShouldAppendZero() {
		RGBColor color = new RGBColor(11,1,0);
		String hexString = color.getHex();
		assertEquals(hexString, "#0B0100");
	}
	@Test
	public void rgbColorShouldReturnHexRed() {
		RGBColor colorRed = new RGBColor(255,0,0);
		String hexStringRed = colorRed.getHex();
		assertEquals(hexStringRed,"#FF0000");
	}

	@Test
	public void rgbColorShouldReturnHexGreen() {
		RGBColor colorRed = new RGBColor(0,255, 0);
		String hexStringRed = colorRed.getHex();
		assertEquals(hexStringRed,"#00FF00");
	}

	@Test
	public void rgbColorShouldReturnHexBlue() {
		RGBColor colorRed = new RGBColor(0,0, 255);
		String hexStringRed = colorRed.getHex();
		assertEquals(hexStringRed,"#0000FF");
	}

	@Test
	public void rgbColorShouldReturnHexGrey() {
		RGBColor colorRed = new RGBColor(211,211,211);
		String hexStringRed = colorRed.getHex();
		assertEquals(hexStringRed,"#D3D3D3");
	}

	@Test
	public void rgbColorShouldSetRed() {
		RGBColor colorRed = new RGBColor();
		colorRed.setHex("#FF0000");
		int red = colorRed.getRed();
		int green = colorRed.getGreen();
		int blue = colorRed.getBlue();
		assertEquals(red, 255);
		assertEquals(green, 0);
		assertEquals(blue, 0);
	}

	@Test
	public void rgbColorShouldSetGreen() {
		RGBColor colorGreen = new RGBColor();
		colorGreen.setHex("#00FF00");
		int red = colorGreen.getRed();
		int green = colorGreen.getGreen();
		int blue = colorGreen.getBlue();
		assertEquals(red, 0);
		assertEquals(green, 255);
		assertEquals(blue, 0);
	}

	@Test
	public void rgbColorShouldSetBlue() {
		RGBColor colorBlue = new RGBColor();
		colorBlue.setHex("#0000FF");
		int red = colorBlue.getRed();
		int green = colorBlue.getGreen();
		int blue = colorBlue.getBlue();
		assertEquals(red, 0);
		assertEquals(green, 0);
		assertEquals(blue, 255);
	}

	@Test
	public void rgbColorShouldSetGrey() {
		RGBColor colorGrey = new RGBColor();
		colorGrey.setHex("#D3D3D3");
		int red = colorGrey.getRed();
		int green = colorGrey.getGreen();
		int blue = colorGrey.getBlue();
		assertEquals(red, 211);
		assertEquals(green, 211);
		assertEquals(blue, 211);
	}
}
