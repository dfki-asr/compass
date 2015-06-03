/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var AmpersandRestCollection = require("ampersand-rest-collection");
var Config = require("../config");

var SceneNodeCollection = AmpersandRestCollection.extend({
	url: Config.getRESTPath("scenenodes/"),

	}
});
module.exports = SceneNodeCollection;
var SceneNode = require("../model/scenenode");
SceneNodeCollection.prototype.model = SceneNode;