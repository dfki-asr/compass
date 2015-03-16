/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.cdi.producers;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import org.jboss.logging.Logger;

public class LoggerProducer {

	@Produces
	public Logger getLogger(final InjectionPoint injectionPoint) {
		return Logger.getLogger(injectionPoint.getMember().getDeclaringClass());
	}

	static public Logger getLogger(final Class<?> targetClass) {
		return Logger.getLogger(targetClass);
	}
}
