/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var app = require('ampersand-app');
var template = require('../templates/startpage.html');
var BasePage = require('./basepage');
var riot = require("riot");
require("../tags/dummy.tag");

//this is the BasePage - all other pages extend from it
var StartPage = BasePage.extend({
	pageTitle: 'Start Page',
	template: template,
	initialize: function (options) {
	},
	render: function () {
		this.renderWithTemplate();
		return this;
	},
	initUI: function () {
		this.initRiot();
	},
	initRiot: function () {
		//here we mount a RiotJS tag and send some options to it.
		//The first parameter is the name of the tag like declared in its
		//tag-file. The second parameter is the "opts" object which will
		//later be used to read properties during the execution of the
		//logic inside <script></script> in dummy.tag file
		//But keep in mind that RiotJS doesn't expect you to explicitely
		//write a <script></script>. You can simply write your whole logic
		//after the last HTML-element inside a *.tag-file and RiotJS compiler
		//will automatically recognize it.
		//More info:  https://muut.com/riotjs/guide/#tag-syntax
		riot.mount('dummy', {
			title: 'I want to behave!',
			items: [
				{title: 'Avoid excessive coffeine', text: 'Argh!', done: true},
				{title: 'Be less provocative', text: 'Argh!', done: true},
				{title: 'Be nice to people', text: 'Argh!', done: true}
			]
		});
	}
});
module.exports = StartPage;
