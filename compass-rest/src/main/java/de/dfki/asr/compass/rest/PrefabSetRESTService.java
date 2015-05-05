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
import de.dfki.asr.compass.business.api.PrefabSetManager;
import de.dfki.asr.compass.model.PrefabSet;
import de.dfki.asr.compass.model.SceneNode;
import de.dfki.asr.compass.rest.exception.UnprocessableEntityException;
import static de.dfki.asr.compass.rest.util.LocationBuilder.locationOf;
import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.rest.util.LocationBuilder;
import java.io.IOException;
import java.util.List;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/prefabsets")
@javax.ejb.Stateless
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@Api(value = "/prefabsets", description = "Operations about prefabsets.")
@TransactionAttribute(TransactionAttributeType.NEVER)
public class PrefabSetRESTService extends AbstractRESTService {

	@Inject
	private PrefabSetManager prefabSetManager;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(
			value = "Get a list of all prefabsets.",
			response = PrefabSet.class,
			responseContainer = "List"
	)
	@ApiResponses({
		@ApiResponse(code = 404, message = "Entity not found")
	})
	public List<PrefabSet> retrieveAll() throws EntityNotFoundException {
		return prefabSetManager.getAllSets();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation("Get a prefabset.")
	@ApiResponses({
		@ApiResponse(code = 404, message = "Entity not found")
	})
	public PrefabSet retrieveByID(
			@ApiParam(value = "Id of the requested prefab set.", required = true)
			@PathParam("id") final long prefabSetID) throws EntityNotFoundException {
		return prefabSetManager.findById(prefabSetID);
	}

	@POST
	@Path("/{id}/children")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation("Append a prefabset to a specified parent.")
	@ApiResponses({
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 404, message = "Entity not found"),
		@ApiResponse(code = 422, message = "Unprocessable Entity")
	})
	public Response appendToParent(
			@ApiParam(value = "Id of the parent prefab set", required = true)
			@PathParam("id") final long parentID,
			@ApiParam(value = "the set, which will be added", required = true)
			final PrefabSet newSet) throws EntityNotFoundException, UnprocessableEntityException {
		ensureEntityIntegrityWhenPost(newSet);
		prefabSetManager.appendSetToParent(parentID, newSet);
		return Response.created(locationOf(this).add(newSet).uri()).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation("Creates a prefab set.")
	@ApiResponses({
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 422, message = "Unprocessable Entity")
	})
	public Response createPrefabSet(
			@ApiParam(value = "The prefab set to be created", required = true)
			final PrefabSet newSet) throws UnprocessableEntityException, EntityNotFoundException, IllegalArgumentException {
		ensureEntityIntegrityWhenPost(newSet);
		prefabSetManager.save(newSet);
		LocationBuilder locationPrefix = locationOf(this);
		return Response.created(locationPrefix.add(newSet).uri()).build();
	}

	@POST
	@Path("/{id}/prefabs")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(
			value = "Add a prefab to a prefab set.",
			notes = "Creates a new prefab and adds it to the prefab set specified by its id."
	)
	@ApiResponses({
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 404, message = "Entity not found"),
		@ApiResponse(code = 422, message = "Unprocessable Entity")
	})
	public Response appendPrefab(
			@ApiParam(value = "Id of the parent prefab set", required = true)
			@PathParam("id") final long parentID,
			@ApiParam(value = "The prefab to be appended", required = true)
			final SceneNode newPrefab) throws UnprocessableEntityException, EntityNotFoundException, IllegalArgumentException {
		ensureEntityIntegrityWhenPost(newPrefab);
		prefabSetManager.addPrefabToSet(newPrefab, parentID);
		return Response.created(locationOf(SceneNodeRESTService.class).add(newPrefab).uri()).build();
	}

	@POST
	@Path("/{id}/prefabs")
	@ApiOperation("Create a new prefab and add it to the specified prefab set.")
	@ApiResponses({
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 404, message = "Entity not found"),
		@ApiResponse(code = 422, message = "Unprocessable Entity")
	})
	public Response createPrefabFromSceneNode(
			@ApiParam(value = "Id of the parent prefab set", required = true)
			@PathParam("id") final long prefabSetId,
			@QueryParam("createFrom") final long sceneNodeId) throws IllegalArgumentException, EntityNotFoundException, IOException {
		PrefabSet prefabSet = prefabSetManager.findById(prefabSetId);
		SceneNode createdPrefab = prefabSetManager.addSceneNodeToPrefabSet(sceneNodeId, prefabSet.getId());
		return Response.created(locationOf(SceneNodeRESTService.class).add(createdPrefab).uri()).build();
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation("Update the current prefab")
	@ApiResponses({
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 404, message = "Entity not found"),
		@ApiResponse(code = 422, message = "Unprocessable Entity")
	})
	public Response updateFromEntity(
			@ApiParam(value = "Id of the prefab set to be updated", required = true)
			@PathParam("id") final long prefabSetID,
			@ApiParam(value = "the prefab set", required = true)
			final PrefabSet entity) throws IllegalArgumentException, EntityNotFoundException, UnprocessableEntityException {
		ensureEntityIntegrityWhenPut(entity, prefabSetID);
		prefabSetManager.save(entity);
		return Response.noContent().build();
	}

	@DELETE
	@Path("/{id}")
	@ApiOperation("Delete a prefab set.")
	@ApiResponses({
		@ApiResponse(code = 404, message = "Entity not found")
	})
	public Response deletePrefabSetByID(
			@ApiParam(value = "Id of the prefab set", required = true)
			@PathParam("id") final long id) throws EntityNotFoundException {
		prefabSetManager.removeById(id);
		return Response.noContent().build();
	}

	@Override
	public void tryGetEntity(final long id) throws EntityNotFoundException {
		prefabSetManager.findById(id);
	}
}
