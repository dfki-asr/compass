/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

window.addEventListener("load", function() {
	var ri = $("xml3d")[0].getRenderInterface();
	var highlightTree = new COMPASS.HighlightRenderTree(ri, COMPASS.Settings.SSAOEnabled);
	ri.setRenderPipeline(highlightTree);
});

(function(webgl) {
	var HighlightRenderTree = function(renderInterface, enableSSAO) {
		webgl.ForwardRenderTree.call(this, renderInterface, enableSSAO);
		this.highlightPass = new COMPASS.HighlightingPass(renderInterface, renderInterface.context.canvasTarget);
	};
	XML3D.createClass(HighlightRenderTree, webgl.ForwardRenderTree);
	XML3D.extend(HighlightRenderTree.prototype, {
		render: function(scene) {
			this.highlightPass.setProcessed(false);
			webgl.ForwardRenderTree.prototype.render.call(this, scene);
			this.highlightPass.renderTree(scene);
		}
	});
	COMPASS.HighlightRenderTree = HighlightRenderTree;


	var HighlightingPass = function(renderInterface, output, opt) {
		webgl.BaseRenderPass.call(this, renderInterface, output, opt);
	};
	XML3D.createClass(HighlightingPass, webgl.BaseRenderPass);
	XML3D.extend(HighlightingPass.prototype, {
		render: (function() {
			var tmpModelViewProjection = XML3D.math.mat4.create();

			return function() {
				if (COMPASS.SelectedMeshes.length < 1) {
					return;
				}
				var program = this.renderInterface.context.programFactory.getProgramByName("highlighting-shader");
				var selectedMeshes = COMPASS.SelectedMeshes;
				var gl = this.renderInterface.context.gl;

				program.bind();
				this.output.bind();
				gl.disable(gl.DEPTH_TEST);
				gl.enable(gl.BLEND);
				for (var i = 0; i < selectedMeshes.length; i++) {
					var element = selectedMeshes[i];
					//@Hack - No official API to get render nodes from the DOM elements
					var renderObjects = element._configured.adapters.webgl_1.renderObjects;
					for (var j = 0, n = renderObjects.length; j < n; j++) {
						var obj = renderObjects[j];
						if (!obj.isVisible()) {
							continue;
						}
						var mesh = obj.mesh;
						var systemUniforms = {};

						obj.getModelViewProjectionMatrix(tmpModelViewProjection);
						systemUniforms["modelViewProjectionMatrix"] = tmpModelViewProjection;
						systemUniforms["uHighlightColor"] = COMPASS.Settings.highlightColor;
						systemUniforms["uTransparency"] = COMPASS.Settings.highlightTransparency;
						program.setSystemUniformVariables(Object.keys(systemUniforms), systemUniforms);

						//@Hack - No way to render meshes using a different primitive type without actually changing their type
						mesh.glType = gl.LINE_LOOP;
						mesh.draw(program);
						mesh.glType = gl.TRIANGLES;
					}
				}
				gl.enable(gl.DEPTH_TEST);
				gl.disable(gl.BLEND);
				this.output.unbind();
				program.unbind();
			}
		})()
	});
	COMPASS.HighlightingPass = HighlightingPass;
})(XML3D.webgl);

XML3D.shaders.register("highlighting-shader", {
	vertex: [
		"attribute vec3 position;",
		"uniform mat4 modelViewProjectionMatrix;",

		"void main(void) {",
		"    gl_Position = modelViewProjectionMatrix * vec4(position, 1.0);",
		"}"
	].join("\n"),
	fragment: [
		"#ifdef GL_ES",
		"precision highp float;",
		"#endif",
		"uniform vec3 uHighlightColor;",
		"uniform float uTransparency;",

		"void main(void) {",
		"  gl_FragColor = vec4(uHighlightColor, uTransparency);",
		"}"
	].join("\n"),
	uniforms: {
		uHighlightColor: [1, 0.6, 0.6],
		uTransparency: 0.8
	},
	samplers: {},
	attributes: {}
});

