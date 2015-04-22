/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.test.integration;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

public class DeploymentDeployTest extends Arquillian {
	public static final String ARTIFACT =
			"de.dfki.asr.compass:compass-deployment:ear:?";

	@Deployment
	public static EnterpriseArchive getCoreCompassDeployment() {
		return Maven.configureResolverViaPlugin()
				.resolve(ARTIFACT)
				.withoutTransitivity()
				.asSingle(EnterpriseArchive.class);
	}

	@Test
	public void itWorked() {
		assertTrue(true);
	}
}
