/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

/* global describe, it */
"use strict";

var ScenarioCollection = require("../../main/webapp/collection/scenario-collection");
var Scenario = require("../../main/webapp/model/scenario");
var chai = require("chai");
chai.use(require("chai-spies"));
var expect = chai.expect;

describe("A ScenarioCollection's selection", function () {
	it("should be undefined if the collection is new", function () {
		var collection = new ScenarioCollection();
		expect(collection.getSelected()).to.be.an("undefined");
	});

	it("should work by id", function () {
		var collection = new ScenarioCollection();
		var node = collection.add({id: 1, name: "Test"});
		collection.selectById(1);
		expect(collection.getSelected()).to.be.an("object")
				.and.to.equal(node);
	});

	it("should be undefined when selecting a nonexisting id", function () {
		var collection = new ScenarioCollection();
		collection.add({id: 1, name: "Test"});
		collection.selectById(1);
		collection.selectById(42);
		expect(collection.getSelected()).to.be.an("undefined");
	});

	describe("as indicated by isSelected", function () {
		it("should be true for a selected model", function () {
			var collection = new ScenarioCollection();
			var node = collection.add({id: 1, name: "Test"});
			collection.selectById(1);
			expect(collection.isSelected(node)).to.equal(true);
		});

		it("should be false for an unselected model", function () {
			var collection = new ScenarioCollection();
			var node = collection.add({id: 1, name: "Test"});
			collection.add({id: 2, name: "Test 2"});
			collection.selectById(2);
			expect(collection.isSelected(node)).to.equal(false);
		});

		it("should be false when there is nothing selected", function () {
			var collection = new ScenarioCollection();
			var node = collection.add({id: 1, name: "Test"});
			collection.selectById(-1);
			expect(collection.isSelected(node)).to.equal(false);
		});

		it("should be false when the model is not part of the collection", function () {
			var collection = new ScenarioCollection();
			collection.selectById(-1);
			var node = new Scenario({id: 1, name: "Test"});
			expect(collection.isSelected(node)).to.equal(false);
		});
	});

});
