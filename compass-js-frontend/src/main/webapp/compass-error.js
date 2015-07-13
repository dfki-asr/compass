/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

"use strict";

var CompassError = function (message) {
  this.name = "CompassError";
  this.message = message || "COMPASS frontend Error";
};

CompassError.prototype = Object.create(Error.prototype);
CompassError.prototype.constructor = CompassError;

module.exports = CompassError;
