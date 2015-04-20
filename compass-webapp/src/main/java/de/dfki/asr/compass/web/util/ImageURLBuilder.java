/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.web.util;

import de.dfki.asr.compass.cdi.qualifiers.Initialized;
import de.dfki.asr.compass.model.resource.Image;
import de.dfki.asr.compass.rest.Application;
import de.dfki.asr.compass.rest.ImageRESTService;
import de.dfki.asr.compass.rest.util.LocationBuilder;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;

@Named
@RequestScoped
public class ImageURLBuilder {

	@Inject @Initialized
	private ServletContext context;

	public String build(Image image) {
		return LocationBuilder.locationOf(context)
					.add(Application.class)
					.add(ImageRESTService.class)
					.add(image)
					.uri().toString();
	}
}
