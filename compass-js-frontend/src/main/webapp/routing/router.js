/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var app = require('ampersand-app');
var AmpersandRouter = require('ampersand-router');
var StartPage = require("./../ui/page/start");

var CompassRouter = AmpersandRouter.extend({
	initialize: function() {
		//nothing to do here ...
	},
	routes: {
		"" : "start",
		"start" : "start",
		"editor" : "editor",
		"(*path)" : "catchAll"
	},
	start: function(){
		var page = new StartPage();
		this.trigger('page', page);
		page.initUI();
	},
	editor: function(){},
	catchAll: function(){
		this.redirectTo("start");
	}
});

module.exports = CompassRouter;
