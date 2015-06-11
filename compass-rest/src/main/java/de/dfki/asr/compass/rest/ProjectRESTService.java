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
import de.dfki.asr.compass.business.api.ProjectManager;
import de.dfki.asr.compass.business.exception.EntityConstraintException;
import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.model.Project;
import de.dfki.asr.compass.model.Scenario;
import de.dfki.asr.compass.rest.exception.UnprocessableEntityException;
import de.dfki.asr.compass.rest.util.LocationBuilder;
import static de.dfki.asr.compass.rest.util.LocationBuilder.locationOf;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/projects")
@javax.ejb.Stateless
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@Api(value = "/projects", description = "Operations about projects.")
public class ProjectRESTService extends AbstractRESTService {

	@Inject
	private ProjectManager projectManager;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(
			value = "Get a list of all projects",
			response = Project.class,
			responseContainer = "List"
	)
	@ApiResponses({
		@ApiResponse(code = 404, message = "Entity not found")
	})
	public List<Project> handleGetProjectsList() throws EntityNotFoundException {
		return projectManager.getAllProjects();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(
			value = "Get a project",
			notes = "Returns the requested project encoded as json."
	)
	@ApiResponses({
		@ApiResponse(code = 404, message = "Entity not found")
	})
	public Project handleGetProjectById(
			@ApiParam(value = "id of the project", required = true)
			@PathParam("id") final long entityId) throws EntityNotFoundException {
		return projectManager.findById(entityId);
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_XML)
	@ApiOperation(
			value = "Get a project",
			notes = "Returns the requested project encoded as XML"
	)
	@ApiResponses({
		@ApiResponse(code = 404, message = "Entity not found")
	})
	public Project handleGetProjectByIdAsXML(
			@ApiParam(value = "id of the project", required = true)
			@PathParam("id") final long entityId) throws EntityNotFoundException {
		return projectManager.findById(entityId);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(
			value = "Post a project.",
			notes = "Create a new project"
	)
	@ApiResponses({
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 422, message = "Unprocessable Entity")
	})
	public Response handlePostProject(
			@ApiParam(value = "the project", required = true)
			final Project entity) throws IllegalArgumentException, UnprocessableEntityException {
		ensureEntityIntegrityWhenPost(entity);
		checkProjectName(entity.getName());
		projectManager.save(entity);
		LocationBuilder locationPrefix = locationOf(this);
		return Response.created(locationPrefix.add(entity).uri()).build();
	}

	private void checkProjectName(final String name) {
		if (!projectManager.isNameUnique(name)) {
			throw new EntityConstraintException("A project of the given name already exists.");
		}
	}

	private boolean didNameChange(final Project newEntity, final long oldEntityId) throws EntityNotFoundException {
		Project oldEntity = projectManager.findById(oldEntityId);
		String oldName = oldEntity.getName();
		String newName = newEntity.getName();
		return !oldName.equals(newName);
	}

	@POST
	@Path("/{projectId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(
			value = "Post a scenario.",
			notes = "Create a new scenario as child of a project."
	)
	@ApiResponses({
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 404, message = "Entity not found"),
		@ApiResponse(code = 422, message = "Unprocessable Entity")
	})
	public Response handlePostScenarioToProject(
			@ApiParam(value = "the scenario", required = true)
			final Scenario newChild,
			@ApiParam(value = "the project the scenario will be added to", required = true)
			@PathParam("projectId") final Long projectId)
			throws EntityNotFoundException, UnprocessableEntityException, IllegalArgumentException {
		if (newChild.getId() > 0) {
			throw new UnprocessableEntityException("When POSTing new entities the ID field should be set to 0 or left out entirely.");
		}
		projectManager.addScenarioToProject(newChild, projectId);
		return Response.created(locationOf(ScenarioRESTService.class).add(newChild).uri()).build();
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(
			value = "Put a project.",
			notes = "Updates an existing projet."
	)
	@ApiResponses({
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 404, message = "Entity not found"),
		@ApiResponse(code = 422, message = "Unprocessable Entity")
	})
	public Response handlePutEntity(
			@ApiParam(value = "the id of the project to update", required = true)
			@PathParam("id") final long entityId,
			@ApiParam(value = "the project", required = true)
			final Project entity)
			throws IllegalArgumentException, EntityNotFoundException, UnprocessableEntityException {
		ensureEntityIntegrityWhenPut(entity, entityId);
		if (didNameChange(entity, entityId)) {
			checkProjectName(entity.getName());
		}
		projectManager.save(entity);
		return Response.noContent().build();
	}

	@DELETE
	@Path("/{id}")
	@ApiOperation("Delete a project.")
	@ApiResponses({
		@ApiResponse(code = 404, message = "Entity not found")
	})
	public Response handleDeleteEntity(
			@ApiParam(value = "the id of the project to delete", required = true)
			@PathParam("id") final long entityId) throws EntityNotFoundException {
		projectManager.removeById(entityId);
		return Response.noContent().build();
	}

	@Override
	public void tryGetEntity(final long id) throws EntityNotFoundException {
		projectManager.findById(id);
	}
}
