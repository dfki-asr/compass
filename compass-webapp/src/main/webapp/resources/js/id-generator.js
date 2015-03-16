/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
XML3D.tools.namespace("COMPASS");

(function() {

	"use strict";

	/** Offers a single function, newID(), which creates an ID that
	 *  was not used before in the application. The created ID is a
	 *  number, returned as a string.
	 */
	COMPASS.IDGenerator = new XML3D.tools.Singleton({

		initialize: function() {
			this.callSuper();

			this.id = 0;
		},

		/** @return {string} a new id */
		newID: function() {
			return "" + this.id++;
		}
	});
}());

