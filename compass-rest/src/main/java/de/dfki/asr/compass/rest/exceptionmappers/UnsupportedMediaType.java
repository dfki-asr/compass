/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.rest.exceptionmappers;

import de.dfki.asr.compass.rest.exception.UnsupportedMediaTypeException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UnsupportedMediaType implements ExceptionMapper<UnsupportedMediaTypeException> {

	@Override
	public Response toResponse(final UnsupportedMediaTypeException exception) {
		return Response.status(Response.Status.BAD_REQUEST)
				.entity(exception.getMessage())
				.build();
	}

}
