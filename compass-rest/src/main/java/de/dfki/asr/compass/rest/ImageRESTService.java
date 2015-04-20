/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.rest;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import de.dfki.asr.compass.business.api.ImageManager;
import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.model.resource.Image;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/images")
@javax.ejb.Stateless
@Api(value = "/images", description = "Operations about images")
public class ImageRESTService {

	@Inject
	private ImageManager manager;

	@OPTIONS
	@Path("/")
	public Response getOptions() {
		// If this wasn't here, a GET to @Path("/") would return a confusing 500
		// (since no functions would match that Path).
		return Response.ok().header("Allow", "OPTIONS").build();
	}

	@GET
	@Path("/{id}")
	@Produces("image/*")
	@ApiOperation("Get a image specified by its id.")
	@ApiResponses({
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 404, message = "Entity not found.")
	})
	public Response handleGetImageById(
			@ApiParam(value = "id of the requested image", required = true)
			@PathParam("id") final long entityId)
			throws EntityNotFoundException {
		Image image = manager.findById(entityId);
		return Response.ok(image.getData(), image.getMimeType()).build();
	}
}
