/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
XML3D.tools.namespace("COMPASS");

(function() {
	"use strict";

	COMPASS.Editor = new XML3D.tools.Singleton({
		xml3dElement: null,
		xml3dProducer: null,
		selectedSceneNodeHolder: new COMPASS.SelectedSceneNodeHolder(),
		_xml3dClickEventProvider: null,
		_stompClient: null,
		selectedPrefabID: null,
		sceneHierachyVerticalScroll: 0,
		prefabSetHierachyVerticalScroll: 0,

		initialize: function() {
			window.addEventListener("load", this._onWindowLoad.bind(this));
		},

		isPrefabSetWithIdSelected: function(id){
			var selectedSet = this._getSelectedPrefabSetAsJQuery();
			if (!selectedSet.length) {
				return false;
			}
			var idSpan = selectedSet.find(".prefab-set-id-field");
			return id === idSpan.html();
		},

		_getSelectedPrefabSetAsJQuery: function(){
			return this._getPrefabSetHierachyListElementAsJQuery().find(".ui-state-highlight");
		},

		saveSceneHierachyScrollBarPosition: function() {
			this.sceneHierachyVerticalScroll = this._getSceneHierachyListElementAsJQuery().scrollTop();
		},

		fixScrollBarAfterSelectingASceneNode: function() {
			this._getSceneHierachyListElementAsJQuery().scrollTop(this.sceneHierachyVerticalScroll);
		},

		_getSceneHierachyListElementAsJQuery: function() {
			return $($("#sceneTreeViewForm\\:sceneTreeView").children("ul"));
		},

		scrollToSelectedSceneNode: function() {
			var selectedSceneNodeTreeNode = this._getSceneHierachyListElementAsJQuery().find(".ui-state-highlight");
			if (!selectedSceneNodeTreeNode.length) {
				//avoid exceptions on load
				return;
			}
			this.sceneHierachyVerticalScroll = selectedSceneNodeTreeNode.position().top;
			this.fixScrollBarAfterSelectingASceneNode();
		},

		savePrefabSetHierachyScrollbarPosition: function() {
			this.prefabSetHierachyVerticalScroll = this._getPrefabSetHierachyListElementAsJQuery().scrollTop();
		},

		_getPrefabSetHierachyListElementAsJQuery: function() {
			return $($("#prefabSetViewForm\\:prefabSetView").children("ul"));
		},

		fixScrollBarAfterSelectingAPrefabSet: function() {
			this._getPrefabSetHierachyListElementAsJQuery().scrollTop(this.prefabSetHierachyVerticalScroll);
		},

		scrollToSelectedPrefabSet: function() {
			var selectedPrefabSet = this._getSelectedPrefabSetAsJQuery();
			if (!selectedPrefabSet.length) {
				return;
			}
			this.prefabSetHierachyVerticalScroll = selectedPrefabSet.position().top;
			this.fixScrollBarAfterSelectingASceneNode();
		},

		selectSceneNode: function(sceneNodeId) {
			this.saveSceneHierachyScrollBarPosition();
			this.deselectPrefab();
			var groupId = COMPASS.Editor.xml3dProducer.SCENENODE_ID_PREFIX + sceneNodeId;
			this.selectedSceneNodeHolder.select(groupId);
			COMPASS.GizmoController.attachGizmo();
		},

		selectSceneNodeFromXML3D: function(sceneNodeId) {
			this.saveSceneHierachyScrollBarPosition();
			this._isSceneNodeSelectable3d(sceneNodeId, function(isSelectable){
				if (!isSelectable) {
					this.deselectSceneNode();
					return;
				}
				this.selectSceneNode(sceneNodeId);
				COMPASS.RemoteCaller.selectSceneNodeFromXML3D(sceneNodeId);
			}.bind(this));
		},

		/** Find the first node in the path to the root that has the selectable3d
		 *  flag set to false and invoke the given callback with the result.
		 *
		 * @param {type} sceneNodeId
		 * @param {function(boolean)} resultCallback, first argument is whether the node is selectable
		 */
		_isSceneNodeSelectable3d: function(sceneNodeId, resultCallback) {
			COMPASS.RESTClient.sendGETRequest("scenenodes", sceneNodeId, function(sceneNode) {
				this._checkSceneNodeIfSelectable3d(sceneNode, resultCallback);
			}.bind(this));
		},

		_checkSceneNodeIfSelectable3d: function(sceneNode, resultCallback) {
			if(sceneNode.selectable3d === false) {
				resultCallback(false);
			} else if(sceneNode.parent === undefined || sceneNode.parent === null) {
				resultCallback(true);
			} else {
				this._isSceneNodeSelectable3d(sceneNode.parent, resultCallback);
			}
		},

		deselectSceneNode: function() {
			this.saveSceneHierachyScrollBarPosition();
			if (this.selectedSceneNodeHolder.isSelected()) {
				this.selectedSceneNodeHolder.deselect();
				COMPASS.GizmoController.detachGizmo();
			}
			COMPASS.RemoteCaller.deselectSceneNode();
		},

		togglePrefabSelection: function(element, id) {
			if (this.selectedPrefabID !== null && this.selectedPrefabID !== id) {
				this.deselectPrefab();
				this._selectPrefab(element, id);
			} else if (id === this.selectedPrefabID) {
				this.deselectPrefab(element);
			} else {
				this._selectPrefab(element, id);
			}
		},

		_selectPrefab: function(element, id) {
			this.deselectSceneNode();
			this.selectedPrefabID = id;
			element.parents(".prefabSetGridElement").addClass("prefabSetGridElementSelected");
			COMPASS.RemoteCaller.selectPrefab(id);
		},

		deselectPrefab: function() {
			if (this.selectedPrefabID === null) {
				return;
			}
			this.selectedPrefabID = null;
			$(".prefabSetGridElementSelected").removeClass("prefabSetGridElementSelected");
			COMPASS.RemoteCaller.deselectPrefab();
		},

		deletePrefab: function() {
			this.selectedPrefabID = null;
		},

		_onWindowLoad: function() {
			this._createXML3DScene();
			this._addXML3DEventListeners();
			this._stompClient = new COMPASS.StompClient();
			COMPASS.SelectedMeshes = [];
			this._setXML3DOptions();
		},
		_setXML3DOptions: function() {
			XML3D.options.setValue("renderer-ssao", COMPASS.Settings.SSAOEnabled);
			XML3D.options.setValue("renderer-ssao-intensity", COMPASS.Settings.SSAOIntensity);
			XML3D.options.setValue("renderer-mousemove-picking", COMPASS.Settings.mouseMovePickingEnabled);
			XML3D.options.setValue("renderer-faceculling", COMPASS.Settings.FaceCulling);
		},

		_createXML3DScene: function() {
			var $xml3d = $("xml3d#editor_scene");
			this._createXML3DSceneByXml3dElement($xml3d.get(0));
		},

		_createXML3DSceneByXml3dElement: function(xml3dElement) {
			this.xml3dElement = xml3dElement;
			this.xml3dProducer = new COMPASS.XML3DProducer(xml3dElement);
			this.xml3dProducer.constructScene();
			COMPASS.GizmoController.createOverlay(xml3dElement);
		},
		
		_addXML3DEventListeners: function() {
			this._xml3dClickEventProvider = new XML3D.tools.util.MouseClickEventProvider(
					this.xml3dElement, COMPASS.EventHandler.onXML3DElementClick.bind(COMPASS.EventHandler));
		},

		restorePrefabSelectionAfterUpdate: function() {
			if (this.selectedPrefabID === null) {
				return;
			}
			var element = $(".prefab-id-field:contains(" + this.selectedPrefabID + ")");
			element.parents(".prefabSetGridElement").addClass("prefabSetGridElementSelected");
		},

	});
})();
