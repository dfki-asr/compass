/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

/* global describe, it */
"use strict";

var Project = require("../../main/webapp/model/project");
var chai = require("chai");
chai.use(require("chai-things"));
var expect = chai.expect;

describe("A project", function () {
	describe(", when parsing a server response", function () {
		it("should convert a plain array to scenario id objects", function () {
			var project = new Project();
			project.id = 1;
			var serverEntity = {
				scenarios: [1, 2, 3]
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

