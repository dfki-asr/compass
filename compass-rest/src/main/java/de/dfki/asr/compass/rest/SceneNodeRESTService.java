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
import de.dfki.asr.compass.business.api.SceneTreeManager;
import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.rest.exception.UnprocessableEntityException;
import de.dfki.asr.compass.model.SceneNode;
import de.dfki.asr.compass.model.SceneNodeComponent;
import static de.dfki.asr.compass.rest.util.LocationBuilder.locationOf;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/scenenodes")
@javax.ejb.Stateless
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@Api(value = "/scenenodes", description = "Operations about scene nodes.")
public class SceneNodeRESTService extends AbstractRESTService {

	@Inject
	private SceneTreeManager sceneTreeManager;

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
	@ApiOperation("Get a scene node.")
	@ApiResponses({
		@ApiResponse(code = 404, message = "Entity not found.")
	})
	public SceneNode getNodeByID(
			@ApiParam(value = "id of the requested scene node", required = true)
			@PathParam("id") final long entityId) throws EntityNotFoundException {
		return sceneTreeManager.findById(entityId);
	}

	@GET
	@Path("/{parentId}/children")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(
			value = "Retrieve a scene node's children.",
			notes = "Create a new scene node, as a child of the specified parent"
	)
	@ApiResponses({
		@ApiResponse(code = 404, message = "Entity not found")
	})
	public List<SceneNode> getChildrenForParent(
			@ApiParam(value = "Parent scene node id", required = true)
			@PathParam("parentId") final long parentId
			) throws EntityNotFoundException {
		SceneNode parent = sceneTreeManager.findById(parentId);
		return parent.getChildren();
	}

	@GET
	@Path("/{parentId}/children/{childId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(
			value = "Retrieve a specific child of a node.",
			notes = "Convenience method, equivalent to /{childId}"
	)
	@ApiResponses({
		@ApiResponse(code = 404, message = "Entity not found")
	})
	public SceneNode getChildForParent(
			@ApiParam(value = "Parent scene node id", required = true)
			@PathParam("childId") final long childId
			) throws EntityNotFoundException {
		return getNodeByID(childId);
	}

	@POST
	@Path("/{parentId}/children")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(
			value = "Post a scene node.",
			notes = "Create a new scene node, as a child of the specified parent"
	)
	@ApiResponses({
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 404, message = "Entity not found"),
		@ApiResponse(code = 422, message = "Unprocessable Entity")
	})
	public Response postNewChildForParent(
			@ApiParam(value = "Parent scene node id", required = true)
			@PathParam("parentId") final long parentId,
			@ApiParam(value = "Scene node", required = true)
			final SceneNode newChild)
			throws EntityNotFoundException, IllegalArgumentException, UnprocessableEntityException {
		ensureEntityIntegrityWhenPost(newChild);
		sceneTreeManager.addNode(newChild, parentId);
		return Response.created(locationOf(SceneNodeRESTService.class).add(newChild).uri())
				.entity(newChild).build();
	}

	@POST
	@Path("/{parentId}/children")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation("Create a scene node based on a prefab. Add it to the specified parent")
	@ApiResponses({
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 404, message = "Entity not found"),
		@ApiResponse(code = 422, message = "Unprocessable Entity")
	})
	public Response instantiatePrefab(
			@ApiParam(value = "Parent scene node id", required = true)
			@PathParam("parentId") final long parentID,
			@ApiParam(value = "prefab id", required = true)
			@QueryParam("instantiate") final long prefabID)
			throws EntityNotFoundException {
		SceneNode instantiated = sceneTreeManager.addPrefabInstance(prefabID, parentID);
		return Response.created(locationOf(this).add(instantiated).uri())
				.entity(instantiated).build();
	}

	@POST
	@Path("/{id}/components")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(
			value = "Post a scene node component",
			notes = "Add a scene node component to the specified scene node."
	)
	@ApiResponses({
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 404, message = "Entity not found"),
		@ApiResponse(code = 422, message = "Unprocessable Entity")
	})
	public Response handlePostComponent(
			@ApiParam(value = "Parent scene node id", required = true)
			@PathParam("id") final Long parentId,
			@ApiParam(value = "Scene node component", required = true)
			final SceneNodeComponent newComponent)
			throws IllegalArgumentException, UnprocessableEntityException, EntityNotFoundException {
		ensureEntityIntegrityWhenPost(newComponent);
		sceneTreeManager.addComponentToSceneNode(parentId, newComponent);
		return Response.created(locationOf(SceneNodeComponentRESTService.class).add(newComponent).uri()).build();
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(
			value = "Put a scene node.",
			notes = "Updates an existing scene node."
	)
	@ApiResponses({
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 404, message = "Entity not found"),
		@ApiResponse(code = 422, message = "Unprocessable Entity")
	})
	public Response handlePutEntity(
			@ApiParam(value = "Scene node id", required = true)
			@PathParam("id") final  long entityId,
			@ApiParam(value = "Scene node", required = true)
			final SceneNode entity)
			throws IllegalArgumentException, EntityNotFoundException, UnprocessableEntityException {
		ensureEntityIntegrityWhenPut(entity, entityId);
		sceneTreeManager.saveNode(entity);
		return Response.noContent().build();
	}

	@DELETE
	@Path("/{id}")
	@ApiOperation("Delete a scene node.")
	public Response handleDeleteEntity(
			@ApiParam(value = "Scene node id", required = true)
			@PathParam("id") final long entityId) throws EntityNotFoundException {
		try {
			sceneTreeManager.removeById(entityId);
		} catch (IllegalArgumentException ex) {
			return Response.status(Status.CONFLICT).entity(ex.getMessage()).build();
		}
		return Response.noContent().build();
	}

	@Override
	public void tryGetEntity(final long id) throws EntityNotFoundException {
		sceneTreeManager.findById(id);
	}
}
