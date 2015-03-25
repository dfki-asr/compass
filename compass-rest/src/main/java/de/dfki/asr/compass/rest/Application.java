/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.rest;

import java.util.Set;
import javax.ws.rs.ApplicationPath;
import org.jboss.logging.Logger;

/**
 * JAX-RS Application Entry point.
 * JAX-RS Resources will be available under /[webapp_contex]/[@ApplicationPath]/[@Path].
 */
@ApplicationPath("/resources/restv1")
public class Application extends javax.ws.rs.core.Application {

	@Override
	public Set<Class<?>> getClasses() {
		Logger.getLogger(getClass()).info("Starting COMPASS JAX-RS");
		return super.getClasses();
	}
	// this class doesn't need to do anything but exist.
}
