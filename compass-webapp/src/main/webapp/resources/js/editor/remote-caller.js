/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
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
			var sceneNodeInformation = this.toJSFParameterList(
				{ 'sceneNodeId': sceneNodeId }
			);
			remoteSelectSceneNodeFromHierarchy(sceneNodeInformation);
		},

		selectSceneNodeFromXML3D: function(sceneNodeId, groupId) {
			var sceneNodeInformation = this.toJSFParameterList(
				{ 'sceneNodeId': sceneNodeId }
			);
			remoteSelectSceneNodeFromXML3D(sceneNodeInformation);
		},

		deselectSceneNode: function() {
			remoteDeselectSceneNode();
		},

		selectPrefab: function(sceneNodeId) {
			var sceneNodeInformation = this.toJSFParameterList(
				{ 'sceneNodeId': sceneNodeId }
			);
			remoteSelectSceneNodeFromPrefab(sceneNodeInformation);
		},

		deselectPrefab: function() {
			remoteDeselectSceneNode();
		},

		createPrefabAtPoint: function(prefabId, hitPointAsDOMString, hitNormalAsDOMString) {
			var dropEventData = this.toJSFParameterList(
				{
					"hitPoint": hitPointAsDOMString,
					"hitNormal": hitNormalAsDOMString,
					"prefabId": prefabId
				});
			remoteDropPrefabOnXML3D(dropEventData);
		},

		createPrefabUnderSceneNode: function(prefabId, sceneNodeId) {
			var dropEventData = this.toJSFParameterList(
				{
					"prefabId": prefabId,
					"sceneNodeId": sceneNodeId
				});
			remoteDropPrefabOnSceneHierarchy(dropEventData);
		},

		reparentSceneNode: function(movedSceneNode, newParent) {
			var reparentEventData = this.toJSFParameterList(
				{
					"newParentId": newParent,
					"movedSceneNodeId": movedSceneNode
				});
			remoteReparentSceneNode(reparentEventData);
		},

		replacePrefabWithSceneNode: function(prefabEventData) {
			remoteReplacePrefabWithSceneNode(prefabEventData);
		},

		addPrefabToPrefabSet: function(sceneNodeId, prefabSetId) {
			var prefabEventData = this.toJSFParameterList(
				{
					"sceneNodeId": sceneNodeId,
					"prefabSetId": prefabSetId,
				});
			remoteCreatePrefabUnderPrefabSet(prefabEventData);
		},

		movePrefabToPrefabSet: function(sceneNodeId, prefabSetId) {
			var prefabEventData = this.toJSFParameterList(
				{
					"sceneNodeId": sceneNodeId,
					"prefabSetId": prefabSetId,
				});
			remoteMovePrefabToPrefabSet(prefabEventData);
		},

		reparentPrefabSet: function(movedPrefabSetId, newParentId) {
			var reparentEventData = this.toJSFParameterList(
				{
					"newParentId": newParentId,
					"movedNodeId": movedPrefabSetId
				});
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

			var transformEventData = this.toJSFParameterList(
				{
					"translation": translation.str(),
					"yaw": rotation.yaw,
					"pitch": rotation.pitch,
					"roll": rotation.roll,
					"scale": scale
				}
			);
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
		},

		toJSFParameterList: function(parameterObject) {
			var jsfParameters = [];
			for (var key in parameterObject) {
				jsfParameters.push({
					name: key,
					value: parameterObject[key]
				});
			}
			return jsfParameters;
		}
	});
})();
