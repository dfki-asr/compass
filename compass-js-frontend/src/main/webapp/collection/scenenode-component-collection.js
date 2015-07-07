/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var CompassCollection = require("./compass-collection");
var ScenenodeComponent = require("../model/scenenode-component");
var Config = require("../config");

var ScenenodeComponentCollection = CompassCollection.extend({
	model: ScenenodeComponent,
	url: Config.getRESTPath("scenenodecomponents/")
});

module.exports = ScenenodeComponentCollection;
