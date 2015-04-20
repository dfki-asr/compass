/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.rest.exceptionmappers;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotAllowed implements ExceptionMapper<NotAllowedException> {

	@Context
	private HttpServletRequest request;

	@Override
	public Response toResponse(final NotAllowedException exception) {
		String reason = "You are not allowed to " + request.getMethod() + " here.";
		return Response.status(Response.Status.METHOD_NOT_ALLOWED)
				// copy Allow header, JAX-RS determined allowable methods for us.
				.header("Allow", exception.getResponse().getHeaderString("Allow"))
				.entity(reason)
				.build();
	}

}
