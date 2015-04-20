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
import de.dfki.asr.compass.business.api.ScenarioManager;
import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.rest.exception.UnprocessableEntityException;
import de.dfki.asr.compass.model.Scenario;
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

@Path("/scenarios")
@javax.ejb.Stateless
@Api(value = "/scenarios", description = "Operations about Scenarios")
public class ScenarioRESTService extends AbstractRESTService {

	@Inject
	private ScenarioManager manager;

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
	@ApiOperation("Get a scenario.")
	@ApiResponses({
		@ApiResponse(code = 404, message = "Entity not found.")
	})
	public Scenario handleGetScenarioById(
			@ApiParam(value = "id of the scenario", required = true)
			@PathParam("id") final long entityId)
			throws EntityNotFoundException {
		return manager.findById(entityId);
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(
			value = "Put a scenario",
			notes = "Updates an existing scenario."
	)
	@ApiResponses({
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 404, message = "Entity not found"),
		@ApiResponse(code = 422, message = "Unprocessable Entity")
	})
	public Response handlePutEntity(
			@ApiParam(value = "id of the scenario to be updated", required = true)
			@PathParam("id") final long entityId,
			@ApiParam(value = "the scenario", required = true)
			final Scenario entity)
			throws IllegalArgumentException, EntityNotFoundException, UnprocessableEntityException {
		ensureEntityIntegrityWhenPut(entity, entityId);
		manager.save(entity);
		return Response.noContent().build();
	}

	@Path("/{id}")
	@DELETE
	@ApiOperation("Delete a scenario.")
	@ApiResponses({
		@ApiResponse(code = 404, message = "Entity not found")
	})
	public Response handleDeleteScenario(
			@ApiParam(value = "id of the scenario", required = true)
			@PathParam("id") final long scenarioId) throws EntityNotFoundException {
		manager.removeById(scenarioId);
		return Response.noContent().build();
	}

	@Override
	public void tryGetEntity(final long id) throws EntityNotFoundException {
		manager.findById(id);
	}
}
