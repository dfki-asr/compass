/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

/* global describe, it */
"use strict";

var RenderGeometry = require("../../main/webapp/model/rendergeometry");
var SceneNode = require("../../main/webapp/model/scenenode");
var chai = require("chai");
chai.use(require("chai-things"));
chai.use(require("chai-spies"));
chai.use(require("chai-string"));
var expect = chai.expect;

describe("A RenderGeometry", function () {
	it("should correspond to the server model", function () {
		var node = new SceneNode({id: 42, name: "Test"});
		var component = new RenderGeometry({owner: node});
		var serialized = component.serialize();
		expect(serialized).to.be.an("object");
		expect(serialized).to.have.keys("meshSource", "type", "owner");
		expect(serialized).to.have.property("owner")
				.which.is.a("number")
				.and.equal(42);
		expect(serialized.meshSource).to.be.a("string");
		expect(serialized.type).to.be.a("string")
				.and.to.equal("de.dfki.asr.compass.model.components.RenderGeometry");
	});
});
