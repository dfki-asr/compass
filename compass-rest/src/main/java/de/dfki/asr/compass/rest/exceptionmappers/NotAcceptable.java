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

@Provider
public class NotAcceptable implements ExceptionMapper<javax.ws.rs.NotAcceptableException> {

	@Override
	public Response toResponse(final javax.ws.rs.NotAcceptableException exception) {
		return Response.status(Response.Status.NOT_ACCEPTABLE)
				.header("Accept", exception.getResponse().getHeaderString("Accept"))
				.entity("Wrong media type for this entity")
				.build();
	}
}
