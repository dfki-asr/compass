/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.rest.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * Customized {@code ContextResolver} implementation that does not use any annotations to produce/resolve JSON field names.
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class JacksonContextResolver implements ContextResolver<ObjectMapper> {

	private final ObjectMapper objectMapper;

	/**
	 * Creates a new instance.
	 */
	public JacksonContextResolver() {
		this.objectMapper = new ObjectMapper();
		this.objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
		this.objectMapper.findAndRegisterModules();
	}

	/**
	 * @param objectType
	 * @return objectMmapper
	 * @see javax.ws.rs.ext.ContextResolver#getContext(java.lang.Class)
	 */
	@Override
	public ObjectMapper getContext(final Class<?> objectType) {
		return objectMapper;
	}
}
