/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var syncPromise = require('ampersand-sync-with-promise');
var AmpersandModel = require("ampersand-model");

var CompassModel = AmpersandModel.extend({
	sync: syncPromise,
	ajaxConfig: {
		headers: {
			"Accept": "application/json"
		}
	},
	extraProperties: 'ignore'
});

module.exports = CompassModel;
