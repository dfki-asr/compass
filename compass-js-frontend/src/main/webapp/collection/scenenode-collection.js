/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var CompassCollection = require("./compass-collection");
var Config = require("../config");

var SceneNodeCollection = CompassCollection.extend({
	// we do not specifiy the model (SceneNode) in order to avoid a circular dependency
	url: Config.getRESTPath("scenenodes/"),
	serialize: function () {
		return this.models.map(function (model) {return model.id;});
	}
});

module.exports = SceneNodeCollection;
