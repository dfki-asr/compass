/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
XML3D.tools.namespace("COMPASS");

(function() {
	"use strict";

	COMPASS.SelectedSceneNodeHolder = new XML3D.tools.Class({
		HIGHLIGHT_SHADER: "#highlighted_shader",
		id: null,
		shaders: null,
		$selectedGroups: [],
		transformable: null,
		isEmptySceneNode: true,

		isNodeSelected: function(group) {
			return this.id === ($(group)).attr("id");
		},

		isSelected: function() {
			return this.id !== null;
		},

		select: function(groupId) {
			this.deselect();
			this.transformable = null;
			this.$selectedGroups = $("#" + groupId);
			this.id = this.$selectedGroups.attr("id");
			this.$selectedGroups = this.$selectedGroups.add(this.$selectedGroups.find("group"));
			this.shaders = this.$selectedGroups.attr("shader");
			this.isEmptySceneNode = ($("#" + groupId).find("model").length < 1);
			this.transformable = XML3D.tools.MotionFactory.createTransformable(
				this.$selectedGroups[0]);
			this.$selectedGroups.attr("shader", this.HIGHLIGHT_SHADER);
			this._setSelectedMeshNodes(groupId);
		},

		deselect: function() {
			if (this.id === null) {
				return;
			}
			this.$selectedGroups.attr("shader", this.shaders);
			this.id = null;
			this.shaders = null;
			COMPASS.SelectedMeshes = [];
		},

		_setSelectedMeshNodes: function(groupId) {
			var meshNodes = $("#" + groupId).find("model");
			COMPASS.SelectedMeshes = meshNodes;
		}
	});
})();
