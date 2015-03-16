/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.backingbeans.editor;

import de.dfki.asr.compass.rest.util.LocationBuilder;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;

@Named
@RequestScoped
public class RestBasePathBean {

	@Inject
	private ServletContext context;

	public String getPath() {
		return LocationBuilder.locationOf(context)
				.add(de.dfki.asr.compass.rest.Application.class)
				.uri().getPath();
	}
}
