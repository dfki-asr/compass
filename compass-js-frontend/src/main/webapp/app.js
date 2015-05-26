/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

/* global global */

"use strict";

var jQuery = global.jQuery = require("jquery");
require("bootstrap"); //load Bootstrap jQuery plugins onto the jQuery object

var app = require("ampersand-app");
var CompassRouter = require("./routing/router");
var ProjectCollection = require("./collection/project-collection");
var MainView = require("./ui/view/mainview");

app.extend({
	init: function () {
		app.version = "2.0.1";
		app.name = "COMPASS";
		app.router = new CompassRouter();
		app.basePath = "/";
		app.projects = new ProjectCollection();
		app.projects.fetch();
		jQuery(global.document).ready(function () {
			app.initUI();
			app.initRouting();
		});
		app.initUI = function () {
			this.mainView = new MainView({
				el: global.document.getElementById("app")
			});
			this.mainView.render();
		};
		app.initRouting = function () {
			app.router.on('page', this.mainView.handleNewPage, this.mainView);
			app.router.history.start({pushState: false, root: app.basePath});
		};
	}
});

module.exports = (function () {
	app.init();
	window.app = app; //for debugging only
}());

