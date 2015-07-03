/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var Project = require("../../main/webapp/model/project");
var chai = require("chai");
chai.use(require("chai-things"));
var expect = chai.expect;
var _ = require("lodash");

describe("A project",function() {
	describe(", when parsing a server response", function() {
		it("should convert a plain array to scenario id objects", function() {
			var project = new Project();
			project.id = 1;
			var serverEntity = {
				scenarios: [1,2,3]
			};
			var parsed = project.parse(serverEntity);
			expect(parsed).to.have.property("scenarios")
							.that.is.an("array");
			expect(parsed.scenarios[0]).to.be.an("object");
			expect(parsed.scenarios[1]).to.be.an("object");
			expect(parsed.scenarios[2]).to.be.an("object");
			expect(parsed.scenarios).to.contain.an.item.with.property("id", 1);
			expect(parsed.scenarios).to.contain.an.item.with.property("id", 2);
			expect(parsed.scenarios).to.contain.an.item.with.property("id", 3);
		});
	});
});