/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
XML3D.tools.namespace("COMPASS");

(function() {
	"use strict";

	COMPASS.EventHandler = new XML3D.tools.Singleton({

		selectSceneNodeFromHierarchy: function(evt) {
			COMPASS.Editor.saveSceneHierachyScrollBarPosition();
			var id = $(evt.target.parentElement).find(".scene-node-id-field").text();
			COMPASS.Editor.selectSceneNode(id);
			COMPASS.RemoteCaller.selectSceneNodeFromHierachy(id);
		},

		prefabSelectionEventListener: function(event) {
			var element = $(event.target);
			var id = element.parent().find(".prefab-id-field").text();
			if (event.button === XML3D.tools.MOUSEBUTTON_RIGHT && COMPASS.Editor.selectedPrefabID === id) {
				return;
			}
			COMPASS.Editor.togglePrefabSelection(element, id);
		},

		disableCameraControlsOnFocus: function(evt) {
			COMPASS.CameraController.disableCamera();
		},

		enableCameraControlsOnBlur: function(evt) {
			COMPASS.CameraController.enableCamera();
		},

		onObjectDroppedOntoXML3DElement: function(evt, ui) {
			var prefabId = $(evt.toElement.parentElement).find(".prefab-id-field").text();
			if (!prefabId) {
				console.error("Dropped prefab did not have an id!");
				return;
			}
			var hit = this._getMouseHitInformation(evt.clientX, evt.clientY);
			COMPASS.RemoteCaller.createPrefabAtPoint(prefabId, hit.point.str(), hit.normal.str());
		},

		_getMouseHitInformation: function(clientX, clientY) {
			var hitPoint = new XML3DVec3();
			var hitNormal = new XML3DVec3();
			var object = COMPASS.Editor.xml3dElement.getElementByPoint(clientX, clientY, hitPoint, hitNormal);
			if (object === null) {
				hitPoint = new XML3DVec3([0, 0, 0]);
				hitNormal = new XML3DVec3([0, 1, 0]);
			}
			return { point: hitPoint, normal: hitNormal };
		},

		onObjectDroppedOntoSceneHierarchy: function(evt, ui) {
			var prefabId = $(ui.draggable[0]).find(".prefab-id-field").text();
			var draggedSceneNodeId = $(ui.draggable[0].parentElement).find(".scene-node-id-field").text();
			var targetSceneNodeId = $(evt.target).siblings().filter(".scene-node-id-field").text() || -1; //-1 will reparent to root

			if (draggedSceneNodeId !== "") {
				COMPASS.RemoteCaller.reparentSceneNode(draggedSceneNodeId, targetSceneNodeId);
			} else {
				COMPASS.RemoteCaller.createPrefabUnderSceneNode(prefabId, targetSceneNodeId);
			}
		},

		onObjectDroppedOntoPrefab: function(evt, ui) {
			var prefabEventData = [{
					name: "sceneNodeId",
					value: $(ui.draggable[0].parentElement).find(".scene-node-id-field").text()
				},
				{
					name: "prefabId",
					value: $(evt.target.parentElement).find(".prefab-id-field").text()
				}];
			replaceConfirmDialog.onConfirmData = prefabEventData;
			replaceConfirmDialog.show();
		},

		onObjectDroppedOntoPrefabsPanel: function(evt, ui) {
			var draggedSceneNodeId = $(ui.draggable[0].parentElement).find(".scene-node-id-field").text();
			var prefabEventData = [{name: "sceneNodeId", value: draggedSceneNodeId}];
			remoteCreatePrefabUnderSelectedPrefabSet(prefabEventData);
		},

		onObjectDroppedOntoPrefabSet: function(evt, ui) {
			var draggedPrefabSetId = $(ui.draggable[0].parentElement).find(".prefab-set-id-field").text();
			var draggedSceneNodeId = $(ui.draggable[0].parentElement).find(".scene-node-id-field").text();
			var draggedPrefabId = $(ui.draggable[0].parentElement).find(".prefab-id-field").text();
			var targetPrefabSetId = $(evt.target).siblings().filter(".prefab-set-id-field").text() || -1; //-1 will add to root
			if (draggedSceneNodeId) {
				COMPASS.RemoteCaller.addPrefabToPrefabSet(draggedSceneNodeId, targetPrefabSetId);
			} else if (draggedPrefabSetId) {
				COMPASS.RemoteCaller.reparentPrefabSet(draggedPrefabSetId, targetPrefabSetId);
			} else if (draggedPrefabId) {
				COMPASS.RemoteCaller.movePrefabToPrefabSet(draggedPrefabId, targetPrefabSetId);
			}
		},

		onXML3DElementClick: function(evt) {
			// only deselect it when we clicked on an empty area
			if (evt.target === COMPASS.Editor.xml3dElement) {
				if (COMPASS.Editor.selectedSceneNodeHolder.isSelected()) {
					COMPASS.Editor.deselectSceneNode();
				}
			}
		},

		onSceneHierarchyClick: function(evt) {
			// only deselect the node if we click on an empty area
			// i.e. the node this event was raised on
			if ($(evt.target).hasClass("ui-tree-container")) {
				if (COMPASS.Editor.selectedSceneNodeHolder.isSelected()) {
					COMPASS.Editor.deselectSceneNode();
				}
			}
		},

		onPrefabGridClick: function(event) {
			if ($(event.target).hasClass("ui-datagrid-content")) {
				if (COMPASS.Editor.selectedPrefabID) {
					COMPASS.Editor.deselectPrefab();
				}
			}
		},

		onSceneNodeCreatedEvent: function(sceneNodeId) {
			COMPASS.Editor.xml3dProducer.convertSceneNodeById(sceneNodeId);
			COMPASS.RemoteCaller.refreshSceneTree();
		},

		onSceneNodeDeletedEvent: function(sceneNodeId) {
			if (COMPASS.Editor.selectedPrefabID === sceneNodeId) {
				COMPASS.Editor.deselectPrefab();
				COMPASS.RemoteCaller.refreshProperties();
			}
			var groupNode = document.getElementById(COMPASS.Editor.xml3dProducer.SCENENODE_ID_PREFIX + sceneNodeId);
			if (!groupNode) {
				console.error("Could not find group node for deleted scene node with ID " + sceneNodeId);
				return;
			}
			if ( COMPASS.Editor.selectedSceneNodeHolder.isNodeSelected(groupNode) ||
				this._parentSceneNodeOfTheCurrentlySelectedSceneNodeWasDeleted(groupNode) ) {
				COMPASS.Editor.deselectSceneNode();
				COMPASS.RemoteCaller.refreshProperties();
				deleteSceneNodeConfirmDialog.hide();
			}
			this._deleteGroupNode($(groupNode));
			COMPASS.RemoteCaller.refreshSceneTree();
		},

		_parentSceneNodeOfTheCurrentlySelectedSceneNodeWasDeleted: function(groupNode){
			//we know that structure of the xml3d groupNodes (should) ressemble the structure of the sceneNodes ...
			return $(groupNode).find("group[id='"+ COMPASS.Editor.selectedSceneNodeHolder.id +"']").size() > 0;
		},

		onSceneNodeComponentCreatedEvent: function(componentId) {
			COMPASS.Editor.xml3dProducer.convertSceneNodeComponentById(componentId);
			this._refreshSceneNodeOfComponent(componentId);
		},

		onSceneNodeComponentDeletedEvent: function(componentId) {
			COMPASS.Editor.xml3dProducer.deleteSceneNodeComponentById(componentId);
			this._refreshSceneNodeOfComponent(componentId);
		},

		onSceneNodeComponentChangedEvent: function(componentId) {
			COMPASS.Editor.xml3dProducer.convertSceneNodeComponentById(componentId);
			this._refreshSceneNodeOfComponent(componentId);
		},

		_refreshSceneNodeOfComponent: function(componentId){
			var that = this;
			COMPASS.RESTClient.sendGETRequest("scenenodecomponents", componentId, function(retrievedComponent){
				that.onSceneNodeChangedEvent(retrievedComponent.owner);
			});
		},

		_deleteGroupNode: function($group) {
			$($group.attr("transform")).remove();
			var children = $group.children();
			for (var i = 0; i < children.length; i++) {
				if (children[i].tagName === "light") {
					$(children[i].getAttribute("shader")).remove();
				} else if (children[i].tagName === "group") {
					this._deleteGroupNode($(children[i]));
				}
			}
			$group.remove();
		},

		onSceneNodeChangedEvent: function(sceneNodeId) {
			COMPASS.RESTClient.sendGETRequest("scenenodes", sceneNodeId, this._updateSceneNodeFromRemote.bind(this));
			if(this._prefabIsCurrentlyDisplayedInTheGrid(sceneNodeId)){
				COMPASS.RemoteCaller.refreshPrefabList();
			}
			if(COMPASS.Editor.selectedPrefabID === sceneNodeId){
				COMPASS.RemoteCaller.refreshProperties();
			}else{
				COMPASS.RemoteCaller.refreshSceneTree();
				var groupNode = document.getElementById(COMPASS.Editor.xml3dProducer.SCENENODE_ID_PREFIX + sceneNodeId);
				if(COMPASS.Editor.selectedSceneNodeHolder.isNodeSelected(groupNode)){
					COMPASS.RemoteCaller.refreshProperties();
				}
			}
		},

		_prefabIsCurrentlyDisplayedInTheGrid: function(sceneNodeId){
			return $("span.prefab-id-field:contains('" + sceneNodeId + "')").size() > 0;
		},

		_updateSceneNodeFromRemote: function(sceneNode) {
			if (sceneNode.parent !== null) {
				COMPASS.Editor.xml3dProducer.updateSceneNode(sceneNode);
				COMPASS.GizmoController.detachGizmo();
				COMPASS.GizmoController.attachGizmo();
			}
		},

		onConfirmReplacePrefab: function() {
			COMPASS.RemoteCaller.replacePrefabWithSceneNode(replaceConfirmDialog.onConfirmData);
			replaceConfirmDialog.onConfirmData = [];
			replaceConfirmDialog.hide();
		},

		blurElementOnEnter: function(evt) {
			if (evt.keyCode === XML3D.tools.KEY_ENTER) {
				evt.target.blur();
			}
		},

		onPrefabSetDeleted: function(setId){
			COMPASS.RemoteCaller.resetPrefabSetSelection();
			COMPASS.RemoteCaller.refreshPrefabList();
			COMPASS.Editor.deselectPrefab();
			COMPASS.RemoteCaller.refreshProperties();
			COMPASS.RemoteCaller.refreshPrefabSetHierachy();
		},

		onPrefabSetCreated: function(setId){
			COMPASS.RemoteCaller.refreshPrefabSetHierachy();
		},

		onPrefabSetChanged: function(setId){
			if(COMPASS.Editor.isPrefabSetWithIdSelected(setId)){
				COMPASS.RemoteCaller.refreshPrefabList();
			}
			COMPASS.RemoteCaller.refreshPrefabSetHierachy();
		}
	});
})();
