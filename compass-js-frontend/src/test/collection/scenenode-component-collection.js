/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

/* global describe, it */
"use strict";

var SceneNodeComponentCollection = require("../../main/webapp/collection/scenenode-component-collection");
var RenderGeometry = require("../../main/webapp/model/rendergeometry");
var SceneNodeComponent = require("../../main/webapp/model/scenenode-component");
var chai = require("chai");
chai.use(require("chai-things"));
chai.use(require("chai-spies"));
chai.use(require("chai-string"));
var expect = chai.expect;

describe("A SceneNodeComponent collection", function () {
	describe("parsing a list of SceneNodeComponents", function () {
		it("should correctly parse a RenderGeometry", function () {
			var list = [
				{
					id: 1,
					type: "de.dfki.asr.compass.model.components.RenderGeometry",
					owner: 42,
					meshSource: "http://example.com/"
				}
			];
			var collection = new SceneNodeComponentCollection(list);
			expect(collection).to.have.length(1);
			expect(collection.at(0)).to.be.an.instanceof(RenderGeometry);
		});
		it("should default parse an unknown component", function () {
			var list = [
				{
					id: 1,
					type: "org.example.SomeComponent",
					extra: "one",
					somethingElse: 31337
				}
			];
			var collection = new SceneNodeComponentCollection(list);
			expect(collection).to.have.length(1);
			var component = collection.at(0);
			expect(component).to.be.an.instanceof(SceneNodeComponent);
			expect(component).to.include.property("extra", "one");
			expect(component).to.include.property("somethingElse", 31337);
		});
	});
});
