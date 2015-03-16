/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.rest.util;

import de.dfki.asr.compass.model.AbstractCompassEntity;
import java.net.URI;
import javax.servlet.ServletContext;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

public class LocationBuilder {

	private UriBuilder builder;

	public static LocationBuilder locationOf(final UriInfo info) {
		return new LocationBuilder(info.getAbsolutePathBuilder());
	}

	public static LocationBuilder locationOf(final Class<?> resourceClass) {
		return new LocationBuilder(UriBuilder.fromResource(resourceClass));
	}

	public static LocationBuilder locationOf(final Object resourceObject) {
		return new LocationBuilder(UriBuilder.fromResource(resourceObject.getClass()));
	}

	public static LocationBuilder locationOf(final ServletContext context) {
		return new LocationBuilder(UriBuilder.fromPath(context.getContextPath()));
	}

	public LocationBuilder(final UriBuilder builder) {
		this.builder = builder;
	}

	public LocationBuilder add(final Class<?> clazz) {
		ApplicationPath annotation = clazz.getAnnotation(ApplicationPath.class);
		if (annotation == null) {
			builder = builder.path(clazz);
		} else {
			// Someone wants to add the path of the root application.
			// Unfortunately UriBuilder doesn't handle this case directly.
			builder = builder.path(annotation.value());
		}
		return this;
	}

	public LocationBuilder add(final String relativePath) {
		builder = builder.path(relativePath);
		return this;
	}

	public LocationBuilder add(final AbstractCompassEntity entity) {
		builder = builder.path(Long.toString(entity.getId()));
		return this;
	}

	public LocationBuilder and() {
		return this;
	}

	public URI uri() {
		return builder.build().normalize();
	}
}
