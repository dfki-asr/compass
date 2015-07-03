/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var SceneNode = require("../../main/webapp/model/scenenode");
var CompassError = require("../../main/webapp/compass-error");
var chai = require("chai");
chai.use(require("chai-things"));
chai.use(require("chai-spies"));
var expect = chai.expect;
var _ = require("lodash");

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
				var node = new SceneNode({someUndefinedProp: "test"}, {parse: true});
			}).to.not.throw();
		});

		it("should keep unknown properties from the server, for plugins", function() {
			var node = new SceneNode({someUndefinedProp: "test"}, {parse: true});
			expect(node.someUndefinedProp).to.equal("test");
		});
	});

	describe("'s URL", function() {
		it("should end in its ID, if it has one", function() {
			expect.fail();
		});
		it("should be located below its parent, if it is new", function() {
			expect.fail();
		});
		it("should error, if neither parent nor id are defined", function() {
			var node = new SceneNode();
			expect(node.url.bind(node)).to.throw(CompassError);
		});
	});
});