/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
XML3D.tools.namespace("COMPASS");

(function() {
	"use strict";

	COMPASS.CADExamineController = new XML3D.tools.Class(XML3D.tools.MouseExamineController, {
		_createControls: function(options) {
			return {
				rotate: options.controls.rotate || XML3D.tools.MOUSEBUTTON_MIDDLE,
				dolly: options.controls.dolly || XML3D.tools.MOUSEBUTTON_RIGHT
			};
		},

		_toggleAttached: function(doAttach) {
			this._ensureWheelCallback();
			var regFn;
			if (doAttach) {
				regFn =  this._eventDispatcher.on.bind(this._eventDispatcher);
				document.addEventListener("wheel", this._wheelCallback, true);
			} else {
				regFn = this._eventDispatcher.off.bind(this._eventDispatcher);
				document.removeEventListener("wheel", this._wheelCallback, true);
			}
			regFn(this._targetXml3d, "mousedown", this.callback("_onXML3DMouseDown"));
			regFn(document, "mousemove", this.callback("_onDocumentMouseMove"));
			regFn(document, "mouseup", this.callback("_onDocumentMouseUp"));
		},

		_ensureWheelCallback: function() {
			if (!this._wheelCallback) {
				this._wheelCallback = this._onXML3DScroll.bind(this);
			}
		},

		_createMouseEventDispatcher: function() {
			var disp = new XML3D.tools.util.EventDispatcher();
			return disp;
		},

		_onXML3DScroll: function(evt){
			if(evt.target.tagName !== "CANVAS" || this._currentAction !== this.NONE){
				return;
			}
			var deltaSign = evt.deltaY / Math.abs(evt.deltaY);
			var delta = deltaSign * this._wheelFovFactor;
			this.fov(delta);
			evt.stopImmediatePropagation();
		},

		onDragStart: function(action) {
			switch (action.evt.buttons) {
				case 4: /* middle only */
					if (action.evt.shiftKey && !action.evt.ctrlKey) {
						this._currentAction = this.PAN;
					} else if (!action.evt.shiftKey && action.evt.ctrlKey) {
						this._currentAction = this.DOLLY;
					} else {
						this._currentAction = this.ROTATE;
					}
					break;
				case 5: /* left + middle */
					this._currentAction = this.DOLLY;
					break;
				case 6: /* right + middle */
					this._currentAction = this.PAN;
					break;
				default:
					this._currentAction = this.NONE;
			}
		},

		onDrag: function(action) {
			switch (this._currentAction) {
				case this.ROTATE:
					this.behavior.rotateByAngles(
					        -action.delta.y * this._rotateFactor,
					        -action.delta.x * this._rotateFactor);
					break;
				case this.PAN:
					this.pan(action.delta.x * this._panFactor,
					         action.delta.y * this._panFactor);
					break;
				case this.DOLLY:
					var delta = action.delta.y - action.delta.x;
					this.fov(delta * this._fovFactor);
					break;
				default:
					this._currentAction = this.NONE;
			}
		},

		onDragEnd: function() {
			this._currentAction = this.NONE;
		},

		pan: function(deltaRight, deltaUp) {
			var cameraUp = this.upDirection();
			var cameraIn = this.behavior.getLookDirection();
			var cameraRight = cameraUp.cross(cameraIn);
			var movement = cameraUp.scale(deltaUp).add(cameraRight.scale(deltaRight));
			var originalDistance = this.behavior._getDistanceToExamineOrigin();
			var newExamine = this.behavior.getExamineOrigin().add(movement);
			var newPosition = this.behavior.target.getPosition().add(movement);
			this.behavior._examineOrigin.set(newExamine);
			this.behavior._setTargetPosition(newPosition);
			var newDistance = this.behavior._getDistanceToExamineOrigin();
			if (Math.abs(originalDistance - newDistance) > 0.01) {
				console.warn("examine distance changed while panning!");
			}
		},

		upDirection: function() {
			var curRot = this.behavior.target.getOrientation();
			var defaultUp = new window.XML3DVec3(0, 1, 0);
			var upDirection = curRot.rotateVec3(defaultUp).normalize();
			return upDirection;
		},

		fov: function(deltaFoV) {
			var view = this.target.object.querySelector("view");
			if (view.fieldOfView) {
				// needs to be clamped to avoid mirroring the view.
				var lowClamp = Math.max(view.fieldOfView + deltaFoV, 1e-11);
				var newFov = Math.min(Math.PI-(1e-11), lowClamp);
				view.fieldOfView = newFov;
			}
		},

		setMoveSpeed: function(moveFactor) {
			// magic values determined by trial study
			this._panFactor = 5 * Math.pow(2, moveFactor / 6);
			this._fovFactor = 2 * Math.pow(2, moveFactor / 14);
			this._wheelFovFactor = 0.01 * Math.pow(2, moveFactor / 9);
			this._rotateFactor = 0.5 * Math.pow(2, moveFactor / 12);
		},

		PAN: "bogusStringIndicatingPanMode"
	});

	COMPASS.CameraController = new XML3D.tools.Singleton({
		activeController: null,
		viewGroup: null,
		cameraSensitivity: 1,

		initialize: function() {
			return;
		},

		disableCamera: function() {
			this.activeController.detach();
		},

		enableCamera: function() {
			this.activeController.attach();
		},

		switchToFlyCamera: function() {
			this.activeController.detach();
			this.activeController = this._createFlyController();
			this.activeController.attach();
			this._setCameraSensivityOfController();
		},

		switchToOrbitCamera: function() {
			this.activeController.detach();
			this.activeController = this._createOrbitController();
			this.activeController.attach();
			this._setCameraSensivityOfController();
		},

		resetCameraPosition: function() {
			var xml3d = COMPASS.Editor.xml3dProducer.getXML3DElement();
			var sceneBoundingBox = xml3d.getBoundingBox();
			this._setCameraToEncloseBoundingBox(sceneBoundingBox);
		},

		_getDimensionsOfBoundingBox: function(scenebbox) {
			var dims = {};
			dims.x = Math.abs(scenebbox.max.x - scenebbox.min.x);
			dims.y = Math.abs(scenebbox.max.y - scenebbox.min.y);
			dims.z = Math.abs(scenebbox.max.z - scenebbox.min.z);
			return dims;
		},

		_setCameraToEncloseBoundingBox: function(sceneBBox) {
			var sceneDimensions = this._getDimensionsOfBoundingBox(sceneBBox);
			var $viewGroupTransform = $(this.viewGroup.getAttribute("transform"));
			// Create a cube out of the largest scene dimension and position the camera at the top looking down at a 45 degree angle
			var longestSide = Math.max(Math.max(sceneDimensions.x, sceneDimensions.y), sceneDimensions.z);
			var center = sceneBBox.center();
			var x = center.x;
			var y = center.y + longestSide;
			var z = center.z + longestSide;
			if (isNaN(x) || isNaN(y) || isNaN(z)) {
				// Scene dimensions are invalid, set a default camera position
				$viewGroupTransform.attr("translation", "0 0 10");
				$viewGroupTransform.attr("rotation", "1 0 0 0");
			} else {
				$viewGroupTransform.attr("translation", x + " "+ y + " " + z);
				$viewGroupTransform.attr("rotation", "1 0 0 -0.785");
			}
			if (this.activeController.behavior._setExamineOrigin) {
				this.activeController.behavior._setExamineOrigin(center);
				this.activeController.behavior._setDistanceToExamineOrigin(Math.sqrt(longestSide*longestSide + longestSide*longestSide));
			}
			// also reset FoV
			var view = this.viewGroup.querySelector("view");
			view.fieldOfView = 45/180 * Math.PI;
		},

		createInitialCameraController: function(aopCameraGroup) {
			this.viewGroup = aopCameraGroup;
			this.activeController = this._createFlyController();
			this.activeController.attach();
			return aopCameraGroup;
		},

		_createOrbitController: function() {
			return new COMPASS.CADExamineController(this.viewGroup, {
				rotateSpeed: 5
			});
		},

		_createFlyController: function() {
			return new XML3D.tools.MouseKeyboardFlyController(this.viewGroup, {
				rotateSpeed: 5,
				controls: {
					rotationActivator: XML3D.tools.MOUSEBUTTON_RIGHT
				}
			});
		},

		_setCameraSensivityOfController: function(){
			if (this.activeController.PAN) {
				this.activeController.setMoveSpeed(this.cameraSensitivity);
			} else {
				var speed = Math.pow(2, this.cameraSensitivity / 3);
				this.activeController.setMoveSpeed(speed);
			}
		},

		onCameraSensitivityChanged: function() {
			this.cameraSensitivity = $("input[id$='camera-sensitivity-input']").val();
			this._setCameraSensivityOfController();
			$("input[id$='camera-sensitivity-label']").val(this.cameraSensitivity);
		},

		moveCameraToCurrentlySelectedSceneNode: function(){
			var boundingBox = this._createBoundingBoxOfListOfGroups(COMPASS.Editor.selectedSceneNodeHolder.$selectedGroups);
			if(!boundingBox || boundingBox.isEmpty()){
				return;
			}
			this._setCameraToEncloseBoundingBox(boundingBox);
		},

		_createBoundingBoxOfListOfGroups: function(selectedGroups){
			var index = 0;
			if(!selectedGroups || !selectedGroups[index] || !selectedGroups[index].getBoundingBox){
				return undefined;
			}
			var overallBoundingBox = selectedGroups[index].getBoundingBox();
			for(index = 1; index < selectedGroups.length; index++){
				if(!selectedGroups[index] || !selectedGroups[index].getBoundingBox){
					continue;
				}
				var groupBoundingBox = selectedGroups[index].getBoundingBox();
				overallBoundingBox.extend(groupBoundingBox);
			}
			return overallBoundingBox;
		}

	});

	$.aop.after( {target: COMPASS.XML3DProducer, method: "_createDefaultCamera"}, COMPASS.CameraController.createInitialCameraController.bind(COMPASS.CameraController) );
	$.aop.after( {target: XML3D.tools.MouseKeyboardFlyController, method: "detach"},
		function() {
			$("button[id$='fly-camera-button']").removeClass("compass-button-active");
		}
	);
	$.aop.after({target: XML3D.tools.MouseKeyboardFlyController, method: "attach"},
		function() {
			$("button[id$='fly-camera-button']").addClass("compass-button-active");
		}
	);
	$.aop.after({target: XML3D.tools.MouseExamineController, method: "detach"},
		function() {
			$("button[id$='orbit-camera-button']").removeClass("compass-button-active");
		}
	);
	$.aop.after({target: XML3D.tools.MouseExamineController, method: "attach"},
		function() {
			$("button[id$='orbit-camera-button']").addClass("compass-button-active");
		}
	);
})();
