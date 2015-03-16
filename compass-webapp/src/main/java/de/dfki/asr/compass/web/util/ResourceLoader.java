/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.util;

import de.dfki.asr.compass.cdi.qualifiers.Initialized;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import org.apache.commons.io.IOUtils;

/**
 * Used to load resources from any of the resource folders in the project.
 */
public class ResourceLoader implements Serializable {

	private static final long serialVersionUID = -2999625711407715213L;

	@Inject @Initialized
	private ServletContext context;

	public String getResourceAsString(String resourceName) throws IOException {
		InputStream resourceStream = getResourceAsStream(resourceName);
		try {
			return IOUtils.toString(resourceStream);
		} catch (IOException e) {
			throw e;
		} finally {
			IOUtils.closeQuietly(resourceStream);
		}
	}

	public InputStream getResourceAsStream(String resourceName) throws IOException {
		InputStream resourceStream = context.getResourceAsStream("/" + resourceName);
		if (resourceStream == null) {
			throw new IOException("Cannot find resource: " + resourceName);
		}
		return resourceStream;
	}
}
