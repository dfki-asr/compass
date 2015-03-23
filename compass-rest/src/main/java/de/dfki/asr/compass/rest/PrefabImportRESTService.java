/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.rest;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import de.dfki.asr.compass.business.PrefabImporter;
import de.dfki.asr.compass.business.api.PrefabSetManager;
import de.dfki.asr.compass.business.exception.EntityNotFoundException;
import de.dfki.asr.compass.model.PrefabSet;
import de.dfki.asr.compass.model.SceneNode;
import static de.dfki.asr.compass.rest.util.LocationBuilder.locationOf;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

@Path("/prefabsets")
@javax.ejb.Stateless
@Api(value = "/prefabsets", description = "Operations about prefabsets.")
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class PrefabImportRESTService {
	@Inject
	private PrefabImporter prefabImporter;

	@Inject
	private PrefabSetManager prefabSetManager;

	@Context
	private UriInfo uriInfo;

	@SuppressWarnings("CPD-START")
	@POST
	@Path("/{id}/prefabs")
	@ApiOperation("Create a new prefab from an ATLAS URL and add it to the specified prefab set.")
	@ApiResponses({
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 404, message = "Entity not found"),
		@ApiResponse(code = 422, message = "Unprocessable Entity")
	})
	public Response createPrefabFromSceneNode(
			@ApiParam(value = "Id of the parent prefab set", required = true)
			@PathParam("id") final long prefabSetId,
			@ApiParam("Asset URL from which to import prefab")
			@QueryParam("createFromAsset") final String assetURL) throws IllegalArgumentException, EntityNotFoundException, IOException {
		PrefabSet prefabSet = prefabSetManager.findById(prefabSetId);
		return handleCreatePrefabFromAssetURL(prefabSet);
	}
	@SuppressWarnings("CPD-END")

	private Response handleCreatePrefabFromAssetURL(final PrefabSet prefabSet) throws IllegalArgumentException, EntityNotFoundException, IOException {
		MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
		String atlasURL = queryParams.getFirst("createFromAsset");
		if (atlasURL.isEmpty() || !atlasURL.startsWith("http")) {
			return Response.status(Response.Status.BAD_REQUEST).entity("A full URL to the asset is required. eg. http://example.com/atlas/rest/asset/assetname").build();
		}
		boolean includeChildren = false;
		if (queryParams.containsKey("includeChildren")) {
			includeChildren = Boolean.parseBoolean(queryParams.getFirst("includeChildren"));
		}
		SceneNode createdPrefab = createPrefabFromAtlas(atlasURL, includeChildren);
		createdPrefab = prefabSetManager.addSceneNodeToPrefabSet(createdPrefab, prefabSet);
		return Response.created(locationOf(SceneNodeRESTService.class).add(createdPrefab).uri()).build();
	}

	private SceneNode createPrefabFromAtlas(final String url, final boolean includeChildren) throws IOException, IllegalArgumentException {
		UriBuilder assetURI = UriBuilder.fromUri(url);
		String assetName = extractAssetNameFromAssetURL(assetURI.build());
		assetURI.fragment(assetName);
		SceneNode sceneNode;
		if (includeChildren) {
			sceneNode = prefabImporter.createSceneNodeHierachyForAsset(assetURI.toString(), assetName);
		} else {
			sceneNode = prefabImporter.createSceneNodeForAsset(assetURI.toString(), assetName);
		}
		return sceneNode;
	}

	private String extractAssetNameFromAssetURL(final URI uri) {
		java.nio.file.Path path = Paths.get(uri.getPath());
		return path.getName(3).toString();
	}

}
