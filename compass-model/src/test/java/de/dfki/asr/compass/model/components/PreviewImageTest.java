/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.model.components;

import de.dfki.asr.compass.model.resource.Image;
import java.io.IOException;
import java.util.Random;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class PreviewImageTest {

	@Test
	public void previewImageDeepCopyShouldCopyData() throws IOException, ClassNotFoundException {
		PreviewImage originalImage = new PreviewImage();
		originalImage.setImage(initializeImage());
		originalImage.setId(1);
		PreviewImage copiedImage = (PreviewImage) originalImage.deepCopy();
		assertEquals(copiedImage.getImage().getData(), originalImage.getImage().getData());
	}

	private Image initializeImage() {
		Image image = new Image("testImage", "image/jpg");
		byte[] imageData = new byte[100];
		new Random().nextBytes(imageData);
		image.setData(imageData);
		return image;
	}
}
