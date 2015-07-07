/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

/* global describe, it */
"use strict";

var ProjectCollection = require("../../main/webapp/collection/project-collection");
var chai = require("chai");
chai.use(require("chai-spies"));
var expect = chai.expect;

describe("A ProjectCollection's selection", function () {
	it("should be undefined if the collection is new", function () {
		var collection = new ProjectCollection();
		expect(collection.getSelected()).to.be.an("undefined");
	});

	it("should work by name", function () {
		var collection = new ProjectCollection();
		var node = collection.add({id: 1, name: "Test"});
		collection.selectByName("Test");
		expect(collection.getSelected()).to.be.an("object")
				.and.to.equal(node);
	});

	it("should work by id", function () {
		var collection = new ProjectCollection();
		var node = collection.add({id: 1, name: "Test"});
		collection.selectById(1);
		expect(collection.getSelected()).to.be.an("object")
				.and.to.equal(node);
	});

	it("should trigger an event when selecting by id", function () {
		var collection = new ProjectCollection();
		collection.add({id: 1, name: "Test"});
		var handler = chai.spy();
		collection.on("change:selected", handler);
		collection.selectById(1);
		expect(handler).to.have.been.called();
	});

	it("should trigger an event when selecting by name", function () {
		var collection = new ProjectCollection();
		collection.add({id: 1, name: "Test"});
		var handler = chai.spy();
		collection.on("change:selected", handler);
		collection.selectByName("Test");
		expect(handler).to.have.been.called();
	});

	it("shouldn't trigger an event when selecting by id again", function () {
		var collection = new ProjectCollection();
		collection.add({id: 1, name: "Test"});
		var handler = chai.spy();
		collection.on("change:selected", handler);
		collection.selectById(1);
		collection.selectById(1);
		expect(handler).to.have.been.called.once();
	});

	it("shouldn't trigger an event when selecting by name again", function () {
		var collection = new ProjectCollection();
		collection.add({id: 1, name: "Test"});
		var handler = chai.spy();
		collection.on("change:selected", handler);
		collection.selectByName("Test");
		collection.selectByName("Test");
		expect(handler).to.have.been.called.once();
	});

});
