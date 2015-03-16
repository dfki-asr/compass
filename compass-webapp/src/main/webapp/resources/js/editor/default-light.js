/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
XML3D.tools.namespace("COMPASS");

(function () {
	"use strict";

	COMPASS.DefaultLight = new XML3D.tools.Singleton({
		_enabled: false,

		initialize: function() {
			this._setCSS();
			this._switchDefaultLight();
		},

		toggleState: function(){
			this._enabled = !this._enabled;
			this._setCSS();
			this._switchDefaultLight();
		},

		_setCSS: function(){
			if(this._enabled){
				$("button[id$='defaultLightButton']").addClass("compass-button-active");
			}else{
				$("button[id$='defaultLightButton']").removeClass("compass-button-active");
			}
		},

		_switchDefaultLight: function(){
			$("#defaultLight").attr("visible", this._enabled);
		}
	});

	$.aop.after(
		{target: COMPASS.XML3DProducer, method: '_createDefaultCamera'},
		function (aopCameraGroup) {
			var lightShader = XML3D.tools.creation.element("lightshader", {
				script: "urn:xml3d:lightshader:point",
				id: 'defaultLightShader',
				children: [
					XML3D.tools.creation.dataSrc("float3", {
						name: "intensity",
						val: "1 1 1"
					}),
					XML3D.tools.creation.dataSrc("float3", {
						name: "attenuation",
						val: "1 0 0"
					})
				]
			});
			var light = XML3D.tools.creation.element("light", {
				shader: "#defaultLightShader",
				intensity: 1,
				id: "defaultLight"
			});
			light.setAttribute('visible', $("button[id$='defaultLightButton']").hasClass("compass-button-active"));
			$(COMPASS.Editor.xml3dProducer.getDefsElement()).append(lightShader);
			$(aopCameraGroup).append(light);
			return aopCameraGroup;
		}
	);

	$.aop.after(
		{target: COMPASS.XML3DProducer, method: '_createDirectionalLight'},
		function (light) {
			if ($("button[id$='defaultLightButton']").hasClass("compass-button-active")) {
				$("button[id$='defaultLightButton']").click();
			}
			return light;
		}
	);
})();