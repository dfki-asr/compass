/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var app = require('ampersand-app');
var AmpersandView = require('ampersand-view');
var AmpersandDom = require('ampersand-dom');
var AmpersandViewSwitcher = require('ampersand-view-switcher');
var _ = require('lodash');
var domify = require('domify');
var mainViewTemplate = require('../templates/main-view.html');
var htmlHeaderTemplate = require('../templates/html-header.html');

//This is a main view which is also a "view switcher" for other views.
//Those views will be hosted inside a preordained position inside view-switcher's
//structure. Check its "render" method and also read the into from
//ampersand-view-switcher: https://www.npmjs.com/package/ampersand-view-switcher
var MainView = AmpersandView.extend({
    template: mainViewTemplate,
    initialize: function() {
        this.listenTo(app.router, 'page', this.handleNewPage);
    },
    events: {
        'click a[href]': 'handleLinkClick'
    },
    render: function() {
        document.head.appendChild(domify(htmlHeaderTemplate));
        this.renderWithTemplate();
        //take the element with the hook ('child-page') and make it the root-el for all other views
        this.pageSwitcher = new AmpersandViewSwitcher(this.queryByHook('child-page'), {
            show: function(newView, oldView) {
                document.title = "COMPASS - " + (_.result(newView, 'pageTitle') || "");
                document.scrollTop = 0;
                //when a page gets switched make it visible in the top-menu
                AmpersandDom.addClass(newView.el, 'active');
                app.currentPage = newView;
            }
        });
        return this;
    },
    handleNewPage: function(view) {
        this.pageSwitcher.set(view);
    },
    //for client-side routing
    handleLinkClick: function(e) {
        var aTag = e.target;
        var host = aTag.host;
        if (host === '') {
            host = location.host;
        }
        var local = host === window.location.host;
        if (local && !e.ctrlKey && !e.shiftKey && !e.altKey && !e.metaKey) {
            e.preventDefault();
            app.navigate(aTag.pathname);
        }
    }
});

module.exports = MainView;
