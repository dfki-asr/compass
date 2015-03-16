/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.business;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.dfki.asr.compass.business.exception.CompassRuntimeException;
import de.dfki.asr.compass.model.ListingFolder;
import de.dfki.asr.compass.model.SceneNode;
import de.dfki.asr.compass.model.components.RenderGeometry;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

@Named
public class PrefabImporter implements Serializable {

	private static final long serialVersionUID = -7150310931394475439L;

	private static final int MAX_NAME_LENGTH = 128;

	private String assetName;

	public SceneNode createSceneNodeForAsset(final String assetURL, final String assetName) throws IllegalArgumentException {
		return createSceneNode(assetURL, assetName);
	}

	private SceneNode createSceneNode(final String meshURL, final String name) throws IllegalArgumentException {
		SceneNode node = new SceneNode();
		node.setName(makeNameValid(name));
		node.updateOrderingIndex();
		addRenderGeometryToSceneNode(node, meshURL);
		return node;
	}

	private String makeNameValid(final String name) {
		//@Hack we do not get the information from the SceneNode class annotaions
		String validName = name.trim();
		validName = makeNameLengthValid(validName);
		validName = makeNameContentValid(validName);
		return validName;
	}

	private String makeNameLengthValid(final String name) {
		if (name.length() > MAX_NAME_LENGTH) {
			return name.substring(0, MAX_NAME_LENGTH);
		}
		if (name.isEmpty()) {
			return "(unnamed)";
		}
		return name;
	}

	private String makeNameContentValid(final String name) {
		return name.replaceAll("[^-\\w :.()]", "_");
	}

	private void addRenderGeometryToSceneNode(final SceneNode node, final String meshURL) throws IllegalArgumentException {
		RenderGeometry mesh = new RenderGeometry();
		mesh.setMeshSource(meshURL);
		mesh.setOwner(node);
		node.addComponent(mesh);
	}

	public SceneNode createSceneNodeHierachyForAsset(final String assetURL, final String assetName) throws IOException, IllegalArgumentException {
		this.assetName = assetName;
		String json = retrieveHierachyAsJSON(assetURL);
		if (json.isEmpty()) {
			return null;
		}
		ListingFolder rootFolder = parseJSONToFolderHierachy(json);
		SceneNode rootNode = createSceneNodeHierachyFromFolderHierachy(rootFolder, null);
		rootNode.setName(assetName);
		return rootNode;
	}

	private String retrieveHierachyAsJSON(final String assetURL) {
		URI url;
		try {
			url = new URI(assetURL);
		} catch (URISyntaxException ex) {
			throw new CompassRuntimeException(ex);
		}
		if (url.getHost() == null) {
			return "";
		}
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(url);
		Response response = target.request().accept(MediaType.APPLICATION_JSON).get();
		String json = response.readEntity(String.class);
		response.close();
		return json;
	}

	private ListingFolder parseJSONToFolderHierachy(final String json) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, ListingFolder.class);
	}

	private SceneNode createSceneNodeHierachyFromFolderHierachy(final ListingFolder folder, final SceneNode parent) throws IllegalArgumentException {
		UriBuilder builder = UriBuilder.fromUri(folder.getUrl());
		builder.fragment(assetName).queryParam("includeChildren", "false");
		SceneNode node = createSceneNode(builder.build().toString(), folder.getName());
		if (parent != null) {
			node.setParent(parent);
		}
		for (ListingFolder child : folder.getChildren()) {
			createSceneNodeHierachyFromFolderHierachy(child, node);
		}
		return node;
	}

}
