/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.rest.exceptionmappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Providers;
import org.jboss.logging.Logger;

/**
 * We want to have full control over what exceptions produce which HTTP response. This class will simply catch all
 * exceptions resulting from REST requests and we can decide how to handle them. The default response is a 500, internal
 * server error.
 */
@Provider
public class CatchAll implements ExceptionMapper<Throwable> {

	@Context
	private Providers providers;

	/**
	 * This will handle all kinds of exceptions. Exceptions can be wrapped into other exceptions with which we might not
	 * be able or don't want to deal. Thus this method will recurse through the "cause"-stack to find the causing
	 * exception we can handle. If there's no such exception an internal server error will be returned.
	 *
	 * @param exception
	 * @return a response matched to the given exception or a 500-response if we can't handle it
	 */
	@Override
	public Response toResponse(final Throwable exception) {
		final ExceptionMapper mapper = providers.getExceptionMapper(exception.getClass());
		if (mapper == null || mapper instanceof CatchAll) {
			if (exception.getCause() != null) {
				return toResponse(exception.getCause());
			}
		} else {
			return mapper.toResponse(exception);
		}

		// Don't know how to handle it properly ---> internal error
		Logger.getLogger(getClass()).error("Unhandled REST exception", exception);
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(exception.getMessage())
				.build();
	}
}
