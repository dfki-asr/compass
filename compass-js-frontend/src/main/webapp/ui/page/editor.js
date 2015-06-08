/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var app = require('ampersand-app');
var Scenario = require('../../model/scenario');
var template = require('../templates/editorpage.html');
var BasePage = require('./basepage');
var riot = require("riot");
var $ = global.jQuery;

var HierarchyView = require('../view/editor/hierarchy.js');
var PropertiesView = require('../view/editor/properties.js');
var ThreeDView = require('../view/editor/viewport.js');
var DisabledView = require('../view/editor/disabled.js');
var NavbarView = require('../view/editor/navbar.js');

var EditorPage = BasePage.extend({
	pageTitle: 'Editor',
	template: template,
	scenario: undefined,
	globalLayout: undefined,
	subviews: {
		hierarchy: {
			hook: 'hierarchy',
			constructor: HierarchyView
		},
		viewport: {
			hook: 'viewport',
			constructor: ThreeDView
		},
		properties: {
			hook: 'properties',
			constructor: PropertiesView
		},
		prefabSets: {
			hook: 'prefabSets',
			constructor: DisabledView
		},
		prefabList: {
			hook: 'prefabList',
			constructor: DisabledView
		},
		navbar:{
			hook: 'navbar',
			constructor: NavbarView
		}
	},
	initialize: function (scenarioId, options) {
		//The router gives as a string, but the model wants a number...
		var idAsNumber = parseInt(scenarioId);
		this.scenario = new Scenario({id: idAsNumber});
		this.scenario.fetch({
			success: this.initUI.bind(this)
		});
	},
	render: function () {
		this.renderWithTemplate();
		return this;
	},
	initUI: function () {
		console.log("Editor fetched scenario: " + this.scenario.name);
		this.globalLayout = $(this.query(".layout-container")).layout({
			name:					"outer",
			center__paneSelector:	".outer-center",
			east__paneSelector:		".outer-east",
			west__paneSelector:		".outer-west",
			south__paneSelector:	".outer-south",
			south__size:			150
		});
		$(this.globalLayout.options.south.paneSelector).layout({
			name:					"inner",
			center__paneSelector:	".inner-center",
			west__paneSelector:		".inner-west",
			minSize:				50
		});
		this.navbar.setScenario(this.scenario);
	}
});
module.exports = EditorPage;
