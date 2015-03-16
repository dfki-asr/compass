/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
XML3D.tools.namespace("COMPASS");

(function() {
	"use strict";

	/** Note on dragging and communication with the server:
	 *  During dragging the gizmo will update the property view GUI transform fields only
	 *  using COMPASS.RemoteCaller.updateSceneNodeTransform().
	 *  After the drag ended it will notify the server to update and persist the new
	 *  transform of the selected scene node using COMPASS.RemoteCaller.updatePropertyViewTransform()
	 */
	COMPASS.GizmoController = new XML3D.tools.Singleton({
		_gizmo: null,
		_gizmoType: "translate",
		/** The gizmo will use an xml3d overlay. It will create an overlay
		 *  automatically, but it will do this every time the gizmo is attached.
		 *  Thus we will manage the overlay by ourselves.
		 */
		_xml3dOverlay: null,

		createOverlay: function(xml3dElement) {
			this._xml3dOverlay = new XML3D.tools.xml3doverlay.XML3DOverlay(xml3dElement);
			this._xml3dOverlay.attach();
		},

		attachGizmo: function() {
			this.detachGizmo();
			var interactorId = "interactor_" + COMPASS.IDGenerator.newID();

			switch (this._gizmoType) {
				case "translate":
					this._gizmo = this.createTranslateGizmo(interactorId);
					break;
				case "rotate":
					this._gizmo = this.createRotateGizmo(interactorId);
					break;
				default:
					this.detachGizmo();
					throw new Error("Unknown interactor type: "
							+ this._gizmoType + ". Can't create widget.");
			}
			this._gizmo.addListener("dragend", COMPASS.RemoteCaller.updateSceneNodeTransform.bind(COMPASS.RemoteCaller));
			this._gizmo.addListener("drag", COMPASS.RemoteCaller.updatePropertyViewTransform.bind(COMPASS.RemoteCaller));
			this._gizmo.attach();
		},

		createTranslateGizmo: function(interactorId) {
			var options = {
				target: COMPASS.Editor.selectedSceneNodeHolder.transformable,
				xml3dOverlay: this._xml3dOverlay,
				geometry: {
					scale: new XML3DVec3(1, 1, 1)
				},
				isEmptyTarget: COMPASS.Editor.selectedSceneNodeHolder.isEmptySceneNode
			};
			return new XML3D.tools.interaction.widgets.TranslateGizmo(interactorId, options);
		},

		createRotateGizmo: function(interactorId) {
			var options = {
				target: COMPASS.Editor.selectedSceneNodeHolder.transformable,
				xml3dOverlay: this._xml3dOverlay,
				rotateSpeed: 0.02,
				geometry: {
					scale: new XML3DVec3(2, 2, 2)
				},
				isEmptyTarget: COMPASS.Editor.selectedSceneNodeHolder.isEmptySceneNode
			};
			return new XML3D.tools.interaction.widgets.RotateGizmo(interactorId, options);
		},

		detachGizmo: function() {
			if (this._gizmo !== null) {
				this._gizmo.detach();
				this._gizmo = null;
			}
		},

		isAttached: function() {
			return (this._gizmo !== null);
		},

		/** @return {boolean} true if the gizmo is currently being dragged. */
		isActive: function() {
			return this._gizmo.isActive();
		},

		setGizmoButton: function(newType) {
			if(newType === "rotate") {
				$("button[id$='rotate-gizmo-button'").addClass("compass-button-active");
				$("button[id$='translate-gizmo-button'").removeClass("compass-button-active");
			} else {
				$("button[id$='rotate-gizmo-button'").removeClass("compass-button-active");
				$("button[id$='translate-gizmo-button'").addClass("compass-button-active");
			}
		},

		setGizmoType: function(newType) {
			if (newType !== "rotate" && newType !== "translate") {
				console.error("COMPASS.Editor.setInteractorType(): trying to set unknown "
						+ "interactor type '" + newType + "'. Will ignore setting.");
				return;
			}
			this._gizmoType = newType;
			this.setGizmoButton(newType);
			if (this.isAttached())
				this.attachGizmo();
		}
	});
})();