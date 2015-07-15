/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

/* global describe, it */
"use strict";

var SceneNodeComponent = require("../../main/webapp/model/scenenode-component");
var CompassError = require("../../main/webapp/compass-error");
var chai = require("chai");
chai.use(require("chai-things"));
chai.use(require("chai-spies"));
chai.use(require("chai-string"));
var expect = chai.expect;

describe("A SceneNodeComponent", function () {
	it("should retain extra properties", function () {
		var comp = new SceneNodeComponent({foo: "bar"}, {parse: true});
		expect(comp).to.include.property("foo");
	});
	it("should not serialize when not subclassed", function () {
		// since SceneNodeComponent is an abstract base class, which should
		// never be pushed to the server.
		var comp = new SceneNodeComponent();
		expect(function () { comp.serialize(); }).to.throw(CompassError);
	});
});
