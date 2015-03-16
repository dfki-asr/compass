/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.rest.exceptionmappers;

import javax.ws.rs.NotSupportedException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotSupported implements ExceptionMapper<NotSupportedException> {

	@Override
	public Response toResponse(final NotSupportedException exception) {
		return Response.status(Response.Status.UNSUPPORTED_MEDIA_TYPE)
				.entity(exception.getMessage())
				.header("Vary", "content-type")
				.build();
	}

}
