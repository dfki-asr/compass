/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.cdi.producers;

import de.dfki.asr.compass.cdi.qualifiers.Initialized;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.servlet.ServletContext;

@ApplicationScoped
public class ServletContextProducer {
	private ServletContext context;

	public ServletContextProducer() {
		context = null;
	}

	public void onInitialized(@Observes @Initialized ServletContext ctx) {
		context = ctx;
	}

	@Produces @Initialized
	public ServletContext getServletContext() {
		return context;
	}
}
