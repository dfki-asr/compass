/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

/* global describe, it */
"use strict";

var SceneNode = require("../../main/webapp/model/scenenode");
var CompassError = require("../../main/webapp/compass-error");
var chai = require("chai");
chai.use(require("chai-things"));
chai.use(require("chai-spies"));
chai.use(require("chai-string"));
var expect = chai.expect;

describe("A SceneNode",function() {
	describe(", when setting the 'visible' property", function() {
		it("should emit the 'change' event.", function() {
			var node = new SceneNode();
			var handler = chai.spy();
			node.on("change", handler);
			node.visible = !node.visible;
			expect(handler).to.have.been.called();
		});
	});
	
	describe(", when parsing a response from the server,", function () {
		it("should transform the children.", function() {
			var serverEntity = {
				children: [2,3,4]
			};
			var node = new SceneNode();
			var parsed = node.parse(serverEntity);
			expect(parsed).to.be.an("object");
			expect(parsed).to.have.property("children")
					.that.is.an("array");
			expect(parsed.children).to.all.have.property("parentNode", node);
			expect(parsed.children).to.contain.an.item.with.property("id", 2);
			expect(parsed.children).to.contain.an.item.with.property("id", 3);
			expect(parsed.children).to.contain.an.item.with.property("id", 4);
		});

		it("shouldn't do anything to an undefined.", function() {
			var node = new SceneNode();
			expect(node.parse(undefined)).to.be.an("undefined");
		});

		it("shouldn't complain about unknown properties from the server", function() {
			expect(function() {
				new SceneNode({someUndefinedProp: "test"}, {parse: true});
			}).to.not.throw();
		});

		it("should keep unknown properties from the server, for plugins", function() {
			var node = new SceneNode({someUndefinedProp: "test"}, {parse: true});
			expect(node.someUndefinedProp).to.equal("test");
		});

		it("shouldn't accept id 0.", function() {
			expect(function() {
				new SceneNode({id: 0}, {parse: true});
			}).to.throw();
		});
	});

	describe("'s URL", function() {
		it("should end in its ID, if it has one", function() {
			var node = new SceneNode({id: 1});
			expect(node.url()).to.be.a("string")
				.and.endWith("scenenodes/1");
		});
		it("should be located below its parent, if it is new", function() {
			var parentNode = new SceneNode({id: 1});
			var node = new SceneNode({parentNode: parentNode});
			expect(node.url.bind(node)).to.not.throw(Error);
			expect(node.url()).to.be.a("string")
				.and.endWith("scenenodes/1/children/");
		});
		it("should error, if neither parent nor id are defined", function() {
			var node = new SceneNode();
			expect(node.url.bind(node)).to.throw(CompassError);
		});
	});
});