/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

/* global describe, it */
/* jshint -W030 */ // needed for .to.be.empty (which is a hidden function call via getter)
"use strict";

var SceneNodeCollection = require("../../main/webapp/collection/scenenode-collection");
var SceneNode = require("../../main/webapp/model/scenenode");
var chai = require("chai");
chai.use(require("chai-things"));
var expect = chai.expect;

describe("A SceneNodeCollection", function () {
	describe("when serializing", function () {
		it("returns an empty list when it's empty.", function () {
			var collection = new SceneNodeCollection([], {model: SceneNode});
			expect(collection.serialize()).to.be.an("array");
			expect(collection.serialize()).to.be.empty;
		});
		it("returns a list of its member's IDs", function () {
			var collection = new SceneNodeCollection([], {model: SceneNode});
			collection.add(new SceneNode({id: 1, name: "Test"}));
			collection.add({id: 2, name: "Test 2"});
			expect(collection.serialize()).to.be.an("array")
					.and.to.include(1)
					.and.to.include(2);
		});
	});
});
