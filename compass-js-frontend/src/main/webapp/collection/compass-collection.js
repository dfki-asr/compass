/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var AmpersandRestCollection = require("ampersand-rest-collection");
var syncPromise = require('ampersand-sync-with-promise');
var Promise = require('promise');

var CompassCollection = AmpersandRestCollection.extend({
	sync: syncPromise,
	ajaxConfig: {
		headers: {
			"Accept": "application/json"
		}
	},
	fetchCollectionEntries: function(){
		var promises = [];
		this.each(function(model){
			promises.push(model.fetch());
		});
		return Promise.all(promises);
	}
});

module.exports = CompassCollection;