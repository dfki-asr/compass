/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.rest.exceptionmappers;

import de.dfki.asr.compass.rest.exception.UnprocessableEntityException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UnprocessableEntity implements ExceptionMapper<UnprocessableEntityException> {

	// From RFC 4918, WebDAV:
	public static final int STATUS_UNPROCESSABLE_ENTITY = 422;

	@Override
	public Response toResponse(final UnprocessableEntityException exception) {
		return Response.status(STATUS_UNPROCESSABLE_ENTITY)
				.entity(exception.getMessage())
				.build();

	}

}
