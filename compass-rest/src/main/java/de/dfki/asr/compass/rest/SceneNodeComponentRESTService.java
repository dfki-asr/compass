/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.rest;
import de.dfki.asr.compass.business.api.ComponentManager;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.rest.exception.UnprocessableEntityException;
import de.dfki.asr.compass.model.SceneNodeComponent;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/scenenodecomponents")
@javax.ejb.Stateless
@Api(value = "/scenenodecomponents", description = "Operations about scene node components")
public class SceneNodeComponentRESTService extends AbstractRESTService {

	@Inject
	private ComponentManager manager;

	@OPTIONS
	@Path("/")
	public Response getOptions() {
		// If this wasn't here, a GET to @Path("/") would return a confusing 500
		// (since no functions would match that Path).
		return Response.ok().header("Allow", "OPTIONS").build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation("Get a scene node component")
	@ApiResponses({
		@ApiResponse(code = 404, message = "Entity not found.")
	})
	public SceneNodeComponent getComponentByID(
			@ApiParam(value = "id of the component", required = true)
			@PathParam("id") final long entityId)
			throws EntityNotFoundException {
		return manager.findById(entityId);
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(
			value = "Put scene node component",
			notes = "Updates an existing scene node component"
	)
	@ApiResponses({
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 404, message = "Entity not found"),
		@ApiResponse(code = 422, message = "Unprocessable Entity")
	})
	public Response handlePutEntity(
			@ApiParam(value = "id of the component to be updated", required = true)
			@PathParam("id") final long entityId,
			@ApiParam(value = "the component", required = true)
			final SceneNodeComponent entity)
			throws IllegalArgumentException, EntityNotFoundException, UnprocessableEntityException {
		ensureEntityIntegrityWhenPut(entity, entityId);
		manager.save(entity);
		return Response.noContent().build();
	}

	@DELETE
	@Path("/{id}")
	@ApiOperation(
			value = "Delete a scene node component"
	)
	@ApiResponses({
		@ApiResponse(code = 404, message = "Entity not found.")
	})
	public Response handleDeleteEntity(
			@ApiParam(value = "id of the component", required = true)
			@PathParam("id") final long entityId)
			throws EntityNotFoundException {
		manager.removeById(entityId);
		return Response.noContent().build();
	}

	@Override
	public void tryGetEntity(final long id) throws EntityNotFoundException {
		manager.findById(id);
	}

}
