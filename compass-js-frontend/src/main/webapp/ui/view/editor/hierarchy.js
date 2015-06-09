/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var $ = global.jQuery;
var app = require('ampersand-app');
var AmpersandView = require('ampersand-view');
var template = require('../../templates/editor/hierarchy.html');

var HierarchyView = AmpersandView.extend({
    template: template,
    initialize: function (options) {
    },
    render: function() {
        this.renderWithTemplate();
        this.tree = $(this.query(".tree")).fancytree({
            extensions: ["glyph"],
            source: [
                {title: "Node 1", key: "1"},
                {title: "Folder 2", key: "2", folder: true, children: [
                    {title: "Node 2.1", key: "3"},
                    {title: "Node 2.2", key: "4"}
                ]}
            ],
            glyph: {
                map: {
                    doc: "fa fa-file-o",
                    docOpen: "fa fa-file",
                    checkbox: "glyphicon glyphicon-unchecked",
                    checkboxSelected: "glyphicon glyphicon-check",
                    checkboxUnknown: "glyphicon glyphicon-share",
                    error: "glyphicon glyphicon-warning-sign",
                    expanderClosed: "fa fa-caret-right",
                    expanderLazy: "fa fa-caret-right",
                    expanderOpen: "fa fa-caret-down",
                    folder: "fa fa-folder-o",
                    folderOpen: "fa fa-folder-open-o",
                    loading: "fa fa-spinner fa-pulse"
                }
            }
        });
        return this;
    }
});

module.exports = HierarchyView;