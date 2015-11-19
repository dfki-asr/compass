/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
XML3D.tools.namespace("COMPASS");

(function(){

	"use strict";

	/** The XML3DProducer manages the addition, deletion and modification of XML3D nodes
	 *  to the given XML3DElement
	 */
	COMPASS.XML3DProducer = new XML3D.tools.Class({
		ROOTNODE_ID_ATTRIBUTE: "root_scenenode_id",
		SCENENODE_ID_PREFIX: "sceneNode_",

		initialize: function(xml3dElement) {
			this.xml3d = xml3dElement;
			this.$xml3d = $(this.xml3d);
			this.defs = XML3D.tools.util.getOrCreateDefs(this.xml3d);
			this.$defs = $(this.defs);
		},

		getXML3DElement: function() {
			return this.xml3d;
		},

		getDefsElement: function() {
			return this.defs;
		},

		updateSceneNode: function(sceneNode) {
			this._convertSceneNode(sceneNode);
		},

		constructScene: function() {
			this._createUtilityShaders();
			this._createDefaultCamera();
			this._constructSceneGraph();
		},

		_createUtilityShaders: function() {
			this._createDefaultShader();
			this._createHighlightShader();
		},

		_createDefaultShader: function() {
			var diffuseColor = XML3D.tools.creation.dataSrc("float3", {
				name: "diffuseColor",
				val: "0.2 0.2 0.9"
			});
			var shader = XML3D.tools.creation.element("shader", {
				script: "urn:xml3d:shader:eyelight",
				id: "default_shader",
				children: [diffuseColor]
			});
			this.$defs.append(shader);
		},

		_createHighlightShader: function() {
			var diffuseColor = XML3D.tools.creation.dataSrc("float3", {
				name: "diffuseColor",
				val: "1.0 0.7 0.7"
			});
			var shader = XML3D.tools.creation.element("shader", {
				script: "urn:xml3d:shader:eyelight",
				id: "highlighted_shader",
				children: [diffuseColor]
			});
			this.$defs.append(shader);
		},

		_createDefaultCamera: function() {
			var transform = XML3D.tools.creation.element("transform", {
				id: "camera_transform"
			});
			this.$defs.append(transform);
			var view = XML3D.tools.creation.element("view", {
				id: "default_view"
			});
			var viewGroup = XML3D.tools.creation.element("group", {
				id: "camera_group",
				transform: "#camera_transform",
				children: [view]
			});
			this.$xml3d.append(viewGroup);
			this.xml3d.setAttribute("activeview", "#default_view");
			return viewGroup;
		},

		_constructSceneGraph: function() {
			if(!this.xml3d.hasAttribute(this.ROOTNODE_ID_ATTRIBUTE)) {
				console.warn("XML3DSceneConstructor: given xml3d element doesn't have a root scene node.");
				return;
			}
			var rootNodeId = this.xml3d.getAttribute(this.ROOTNODE_ID_ATTRIBUTE);
			this.convertSceneNodeById(rootNodeId);
		},

		convertSceneNodeById: function(sceneNodeId) {
			COMPASS.RESTClient.sendGETRequest("scenenodes", sceneNodeId, this._convertSceneNode.bind(this));
		},

		_convertSceneNode: function(sceneNode) {
			var parentGroup = this._findParentGroupForSceneNode(sceneNode);
			var group = this.findGroupForSceneNodeId(sceneNode.id);
			if (!group.length) {
				group = XML3D.tools.creation.element("group", {
					id: this.SCENENODE_ID_PREFIX + sceneNode.id,
					// This should be changed once proper shader handling is added
					shader: "#default_shader"
				});
				parentGroup.appendChild(group);
				group = $(group);
			} else {
				this.updateGroupStructureForSceneNode(group, sceneNode);
			}
			group.attr("visible", sceneNode.visible);
			this._convertSceneNodeTransform(sceneNode, group);
			this._convertSceneNodeComponents(sceneNode);
			this._convertSceneNodeChildren(sceneNode, group);
		},

		updateGroupStructureForSceneNode: function($groupNode, sceneNode) {
			if (sceneNode.parent !== null) {
				var parentId = $groupNode.parent().attr("id");
				if (parentId !== this.SCENENODE_ID_PREFIX + sceneNode.parent) {
					var $newParent = $("#" + this.SCENENODE_ID_PREFIX + sceneNode.parent);
					$groupNode.detach();
					$newParent.append($groupNode);
				}
			}
		},

		_findParentGroupForSceneNode: function(sceneNode) {
			if (sceneNode.parent === null) {
				return this.$xml3d[0];
			}
			var $parent = $("#" + this.SCENENODE_ID_PREFIX + sceneNode.parent);
			if (!$parent.length) {
				console.error("Could not find parent group for sceneNode! SceneNode: " + sceneNode.id + ", Parent: " + (this.SCENENODE_ID_PREFIX + sceneNode.parent));
				return this.$xml3d[0];
			}
			return $parent[0];
		},

		_convertSceneNodeTransform: function(sceneNode, $group) {
			var transform = $($group.attr("transform"));
			if (!transform.length) {
				var transformId = "t_" + COMPASS.IDGenerator.newID();
				transform = XML3D.tools.creation.element("transform", {
					id: transformId
				});
				this.$defs.append(transform);
				$group.attr("transform", "#" + transformId);
				transform = $(transform);
			}
			transform.attr("rotation", COMPASS.util.sceneNodeRotationToString(sceneNode.localRotation));
			transform.attr("translation", COMPASS.util.sceneNodeVectorToString(sceneNode.localTranslation));
			transform.attr("scale", COMPASS.util.sceneNodeScalarToVectorString(sceneNode.localScale));
		},

		_convertSceneNodeComponents: function(sceneNode) {
			for (var i = 0; i < sceneNode.components.length; i++) {
				this.convertSceneNodeComponentById(sceneNode.components[i]);
			}
		},

		convertSceneNodeComponentById: function(componentId) {
			COMPASS.RESTClient.sendGETRequest("scenenodecomponents", componentId, this._convertSceneNodeComponent.bind(this));
		},

		_convertSceneNodeComponent: function(component) {
			if (component.type === "de.dfki.asr.compass.model.components.RenderGeometry") {
				this._convertMeshNodeComponent(component);
			} else if (component.type === "de.dfki.asr.compass.model.components.DirectionalLight") {
				this._convertDirectionalLightNodeComponent(component);
			}
		},

		_convertMeshNodeComponent: function(component) {
			var $group = this.findGroupForSceneNodeId(component.owner);
			var meshNode = $group.children("model");
			if (!meshNode.length) {
				meshNode = XML3D.tools.creation.element("model", {
					src: component.meshSource
				});
				$(meshNode).click(this._createMeshClickHandler(component));
				$group.append(meshNode);
				meshNode = $(meshNode);
				meshNode.attr("data-componentid", component.id);
			}
			if (meshNode.attr("src") !== component.meshSource) {
				meshNode.attr("src", component.meshSource);
			}
		},

		_createMeshClickHandler: function(renderGeometryComponent) {
			return function(event) {
				if (event.which === 1) {
					COMPASS.Editor.selectSceneNodeFromXML3D(renderGeometryComponent.owner);
				}
			};
		},

		_convertDirectionalLightNodeComponent: function(component) {
			var lightId = "directionallight_" + component.owner + "_" + component.id;
			var lightShaderId = "ls_" + lightId;
			var light = $("#"+lightId);
			this._convertDirectionalLightShader(lightShaderId, component);
			if (!light.length) {
				light = this._createDirectionalLight(lightId, component);
			}
			light.attr("intensity", component.intensity);
		},

		_createDirectionalLight: function(lightId, component) {
			var $group = this.findGroupForSceneNodeId(component.owner);
			var light = XML3D.tools.creation.element("light", {
				shader: "#ls_" + lightId,
				intensity: component.intensity,
				id: lightId
			});
			$group.append(light);
			light = $(light);
			light.attr("data-componentid", component.id);
			return light;
		},

		_convertDirectionalLightShader: function(lightShaderId, component) {
			var $lightShader = $("#"+lightShaderId);
			if ($lightShader.length === 0) {
				this._createNewLightShaderTag(lightShaderId, component);
			}else{
				this._updateLightShader($lightShader, component);
			}
		},

		_createNewLightShaderTag: function(lightShaderId, component){
			var cns = XML3D.tools.creation;
			var lightShader = cns.element("lightshader", {
				script: "urn:xml3d:lightshader:directional",
				id: lightShaderId,
				children: [
					cns.dataSrc("float3", {
						name: "intensity",
						val: this._getXML3DIntensity(component.color).str()
					}),
					cns.dataSrc("bool", {
						name: "castShadow",
						val: component.castShadow
					})
				]
			});
			this.$defs.append(lightShader);
			lightShader = $(lightShader);
			lightShader.attr("data-componentid", component.id);
		},

		_updateLightShader: function($lightShader, component){
			$lightShader.children("[name='intensity']").text(this._getXML3DIntensity(component.color).str());
			$lightShader.children("[name='castShadow']").text(component.castShadow.toString());
		},

		_getXML3DIntensity: function(color) {
			var colorVec = new XML3DVec3(color.red, color.green, color.blue);
			colorVec = colorVec.scale(1/255);
			return colorVec;
		},

		_convertSceneNodeChildren: function(sceneNode) {
			for (var i = 0; i < sceneNode.children.length; i++) {
				this.convertSceneNodeById(sceneNode.children[i]);
			}
		},

		findGroupForSceneNodeId: function(sceneNodeId) {
			return $("#" + this.SCENENODE_ID_PREFIX + sceneNodeId);
		},

		deleteSceneNodeComponentById: function(componentId) {
			var component = $("[data-componentid='"+componentId+"']");
			component.remove();
			COMPASS.SelectedMeshes = COMPASS.SelectedMeshes.filter(function(item) {
				return this.getAttribute("data-componentid") !== componentId;
			});
		}
	});
}());
