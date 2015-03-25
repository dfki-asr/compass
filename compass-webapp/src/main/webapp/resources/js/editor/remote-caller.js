/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
XML3D.tools.namespace("COMPASS");

(function() {
	"use strict";

	COMPASS.RemoteCaller = new XML3D.tools.Singleton({

		initialize: function() {
			//nothing to do here yet
		},

		selectSceneNodeFromHierachy: function(sceneNodeId) {
			var sceneNodeInformation = [{
					name: 'sceneNodeId',
					value: sceneNodeId
				}];
			remoteSelectSceneNodeFromHierarchy(sceneNodeInformation);
		},

		selectSceneNodeFromXML3D: function(sceneNodeId, groupId) {
			var sceneNodeInformation = [{
					name: 'sceneNodeId',
					value: sceneNodeId
				}];
			remoteSelectSceneNodeFromXML3D(sceneNodeInformation);
		},

		deselectSceneNode: function() {
			remoteDeselectSceneNode();
		},

		selectPrefab: function(id) {
			var sceneNodeInformation = [{
					name: 'sceneNodeId',
					value: id
				}];
			remoteSelectSceneNodeFromPrefab(sceneNodeInformation);
		},

		deselectPrefab: function() {
			remoteDeselectSceneNode();
		},

		createPrefabAtPoint: function(prefabId, hitPointAsDOMString, hitNormalAsDOMString) {
			var dropEventData = [{
				name: "hitPoint",
					value: hitPointAsDOMString
			},
			{
				name: "hitNormal",
					value: hitNormalAsDOMString
			},
			{
				name: "prefabId",
				value: prefabId
			}];
			remoteDropPrefabOnXML3D(dropEventData);
		},

		createPrefabUnderSceneNode: function(prefabId, sceneNodeId) {
			var dropEventData = [{
					name: "prefabId",
					value: prefabId
				},
				{
					name: "sceneNodeId",
					value: sceneNodeId
				}];
			remoteDropPrefabOnSceneHierarchy(dropEventData);
		},

		reparentSceneNode: function(movedSceneNode, newParent) {
			var reparentEventData = [{
				name: "newParentId",
				value: newParent
			},
			{
				name: "movedSceneNodeId",
				value: movedSceneNode
			}];
			remoteReparentSceneNode(reparentEventData);
		},

		replacePrefabWithSceneNode: function(prefabEventData) {
			remoteReplacePrefabWithSceneNode(prefabEventData);
		},

		addPrefabToPrefabSet: function(sceneNodeId, prefabSetId) {
			var prefabEventData = [{
				name: "sceneNodeId",
				value: sceneNodeId
			},
			{
				name: "prefabSetId",
				value: prefabSetId
			}];
			remoteCreatePrefabUnderPrefabSet(prefabEventData);
		},

		movePrefabToPrefabSet: function(sceneNodeId, prefabSetId) {
			var prefabEventData = [{
					name: "sceneNodeId",
					value: sceneNodeId
				},
				{
					name: "prefabSetId",
					value: prefabSetId
				}];
			remoteMovePrefabToPrefabSet(prefabEventData);
		},

		reparentPrefabSet: function(movedPrefabSetId, newParentId) {
			var reparentEventData = [{
				name: "newParentId",
				value: newParentId
			},
			{
				name: "movedNodeId",
				value: movedPrefabSetId
			}];
			remoteReparentPrefabSet(reparentEventData);
		},

		updatePropertyViewTransform: function() {
			var transform = COMPASS.Editor.selectedSceneNodeHolder.transformable.transform;
			var translation = transform.translation;
			var rotation = new COMPASS.EulerRotation().fromQuaternion(transform.rotation);
			var scale = transform.scale.x;

			$("[id$='propertyViewPositionX']").val(translation.x.toFixed(2));
			$("[id$='propertyViewPositionY']").val(translation.y.toFixed(2));
			$("[id$='propertyViewPositionZ']").val(translation.z.toFixed(2));
			$("[id$='propertyViewRotationYaw']").val(rotation.yaw.toFixed(2));
			$("[id$='propertyViewRotationPitch']").val(rotation.pitch.toFixed(2));
			$("[id$='propertyViewRotationRoll']").val(rotation.roll.toFixed(2));
			$("[id$='propertyViewScale']").val(scale.toFixed(2));
		},

		updateSceneNodeTransform: function() {
			var transform = COMPASS.Editor.selectedSceneNodeHolder.transformable.transform;
			var translation = transform.translation;
			var rotation = new COMPASS.EulerRotation().fromQuaternion(transform.rotation);
			var scale = transform.scale.x;

			var transformEventData = [{
				name: "translation",
				value: translation.str()
			},
			{
				name: "yaw",
				value: rotation.yaw
			},
			{
				name: "pitch",
				value: rotation.pitch
			},
			{
				name: "roll",
				value: rotation.roll
			},
			{
				name: "scale",
				value: scale
			}];
			remoteUpdateSelectedSceneNodeTransform(transformEventData);
		},

		refreshSceneTree: function() {
			remoteRefreshSceneTree();
		},

		refreshProperties: function(){
			remoteRefreshProperties();
		},

		refreshPrefabSetHierachy: function(){
			remoteRefreshPrefabSetHierachy();
		},

		refreshPrefabList: function(){
			remoteRefreshPrefabList();
		},

		resetPrefabSetSelection: function(){
			remoteResetPrefabSetSelection();
		}
	});
})();
