/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
"use strict";

window.$ = window.jQuery = require("jquery");

var app = require("ampersand-app");

app.extend({
	init: function () {
		console.log('Initializing the global App-Singleton.');
		app.version = "2.0.1";
		app.name = "COMPASS";
		//app.router = new Router();
		app.basePath = "/";
		app.rootDomElement = document.getElementById("app");
		$(document).ready(function () {
			console.log('DOM is ready ... Initialize UI and Routing.');
			app.initUI();
			app.initRouting();
		});
		//helper function for easy navigating between pages (taken from AmpersandJS docs)
		app.navigate = function(page) {
			var url = (page.charAt(0) === '/') ? page.slice(1) : page;
			if (url.indexOf('/') !== -1) {
				url = url.match(/\/(.*?)$/)[1];
			}
			//this.router.history.navigate(url, {trigger: true});
		};
		app.initUI= function(){
			console.log("Initalize UI ... Start with MainView");
			//this.mainView = new MainView({
			//	el: this.rootDomElement
			//});
		};
		app.initRouting = function(){
			console.log("Initalize routing ... navigate to /start");
			app.navigate("/start");
		};
	}
});

module.exports = (function () {
	console.log("Start Initialization ...");
	app.init();
	console.log("Shit is up and running ... COMPASS version " + app.version);
}());

