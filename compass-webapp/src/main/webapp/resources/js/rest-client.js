/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
XML3D.tools.namespace("COMPASS");

(function() {

	"use strict";

	COMPASS.RESTClient = new XML3D.tools.Singleton({
		sendGETRequest: function(resourceName, id, successCallback, errorCallback) {
			var request = {
				contentType: "application/json",
				url: this.buildURL(resourceName, id),
				accepts: {
					json: "application/json"
				},
				error: errorCallback || this._defaultErrorCallback.bind(this, "GET", resourceName, id),
				success: successCallback || this._defaultSuccessCallback.bind(this, "GET", resourceName, id)
			}
			$.ajax(request);
		},

		sendPUTRequest: function(resourceName, obj, successCallback, errorCallback) {
			var request = {
				type: "PUT",
				contentType: "application/json",
				dataType: "application/json",
				url: this.buildURL(resourceName, obj.id),
				accepts: {
					json: "application/json"
				},
				data: JSON.stringify(obj),
				error: errorCallback || this._defaultErrorCallback.bind(this, "GET", resourceName, obj.id),
				success: successCallback || this._defaultSuccessCallback.bind(this, "GET", resourceName, obj.id)
			};
			$.ajax(request);
		},

		sendPOSTRequest: function(resourceName, obj, successCallback, errorCallback) {
			var request = {
				type: "POST",
				contentType: "application/json",
				url: this.buildURL(resourceName),
				data: JSON.stringify(obj),
				error: errorCallback || this._defaultErrorCallback.bind(this, "POST", resourceName, undefined),
				success: successCallback || this._defaultSuccessCallback.bind(this, "POST", resourceName, undefined)
			};
			$.ajax(request);
		},

		sendDELETERequest: function(resourceName, id, successCallback, errorCallback) {
			var request = {
				type: "DELETE",
				contentType: "application/json",
				url: this.buildURL(resourceName, id),
				error: errorCallback || this._defaultErrorCallback.bind(this, "DELETE", resourceName, id),
				success: successCallback || this._defaultSuccessCallback.bind(this, "DELETE", resourceName, id)
			};
			$.ajax(request);
		},

		buildURL: function(resourceName, resourceId) {
			var url = window.location.origin;
			url += COMPASS.ServerProperties.restBasePath;
			url += "/" + resourceName;
			if(resourceId !== undefined)
				url += "/" + resourceId;
			return url;
		},

		sendRequest: function(requestType, resourceUrl, successCallback, errorCallback) {
			var request = {
				type: requestType,
				contentType: "application/json",
				url: this.buildURL(resourceUrl),
				accepts: {
					json: "application/json"
				},
				error: errorCallback || this._defaultErrorCallback.bind(this, requestType, resourceUrl),
				success: successCallback || this._defaultSuccessCallback.bind(this, requestType, resourceUrl)
			}
			$.ajax(request);
		},

		_defaultErrorCallback: function(requestType, resourceName, id, data, textStatus, jqXHR) {
			var errorString = "Failed to "+requestType+" resource: " + resourceName;
			if(id !== undefined)
				errorString += " with id " + id;
			console.error(errorString);
		},

		_defaultSuccessCallback: function(requestType, resourceName, id, data, textStatus, jqXHR) {
			var infoString = requestType+" of resource "+resourceName;
			if(id !== undefined)
				infoString += " with id "+id;
			infoString += " was successful";
			console.info(infoString);
		}

	});
})();
