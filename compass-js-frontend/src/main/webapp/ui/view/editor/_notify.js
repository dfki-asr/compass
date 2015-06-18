/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var $ = global.jQuery;

var notify = function(type, message) {
    var options = {}
    switch (type) {
        case "danger":
            options.icon = "fa fa-exclamation-triangle";
            break;
        case "success":
            options.icon = "fa fa-check-square-o"
            break;
    }
    options.message = message;
    var settings = {
        type: type,
        placement: {
            from: "top",
            align: "center"
        },
        animate: {
            enter: 'animated fadeInDown',
            exit: 'animated fadeOutUp'
        },
        allow_dismiss: false
    };
    $.notify(options, settings);
};

module.exports = notify;
