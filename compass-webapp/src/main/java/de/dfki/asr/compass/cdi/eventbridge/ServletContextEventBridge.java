/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.cdi.eventbridge;

import de.dfki.asr.compass.cdi.qualifiers.Destroyed;
import de.dfki.asr.compass.cdi.qualifiers.Initialized;
import javax.enterprise.event.Event;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

// modeled after: https://gist.github.com/mojavelinux/637959
@WebListener
public class ServletContextEventBridge implements ServletContextListener {

	@Inject
	private Event<ServletContext> servletContextEvent;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		servletContextEvent
				.select(new AnnotationLiteral<Initialized>() {
				})
				.fire(sce.getServletContext());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		servletContextEvent
				.select(new AnnotationLiteral<Destroyed>() {
				})
				.fire(sce.getServletContext());
	}
}
