/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.rest.exceptionmappers;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class Persistence implements ExceptionMapper<EntityNotFoundException> {

	@Override
	public Response toResponse(final EntityNotFoundException exception) {
		return Response.status(UnprocessableEntity.STATUS_UNPROCESSABLE_ENTITY)
				.entity(exception.getMessage())
				.build();
	}
}
