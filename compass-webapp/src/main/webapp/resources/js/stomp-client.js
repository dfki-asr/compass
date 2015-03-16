/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
XML3D.tools.namespace("COMPASS");

(function() {
	"use strict";

	COMPASS.StompClient = new XML3D.tools.Class({
		_username: "stomp-user",
		_password: "stomp-user-313",
		_client: null,
		_projectsSubscription: null,
		_scenariosSubscription: null,
		_sceneNodesSubscription: null,
		_connectionActive: false,

		initialize: function() {
			var serverURL = "ws://"+document.location.hostname+":61614/stomp";
			this._client = Stomp.client(serverURL);
			this._client.debug = function(){return undefined;};
			this._client.connect(this._username,this._password,
				this._onConnectSuccess.bind(this), this._onConnectError.bind(this));
		},

		_onConnectSuccess: function() {
			console.log("[stomp] connected.");
			this._connectionActive = true;
			this._projectsSubscription = this._client.subscribe(
				"jms.topic.compass.projects", this._onProjectsMessage.bind(this));
			this._scenariosSubscription = this._client.subscribe(
				"jms.topic.compass.scenarios", this._onScenariosMessage.bind(this));
			this._sceneNodesSubscription = this._client.subscribe(
				"jms.topic.compass.sceneNodes", this._onSceneNodesMessage.bind(this));
			this._sceneNodeComponentsSubscription = this._client.subscribe(
				"jms.topic.compass.sceneNodeComponents", this._onSceneNodeComponentsMessage.bind(this));
			this._prefabSetsSubscription = this._client.subscribe(
				"jms.topic.compass.prefabSets", this._onPrefabSetMessage.bind(this));
		},

		_onConnectError: function(errorFrame) {
			if (this._connectionActive) {
				console.log("[stomp] connection dropped along the way. Sorry, but you'll have to reload.");
				$("#stompErrorModal").modal({keyboard: false, backdrop: "static"});
			} else {
				console.log("[stomp] failed to connect: " + errorFrame.body);
			}
		},

		_onProjectsMessage: function(messageFrame) {
			console.log("[stomp][projects] received message: " + messageFrame.body);
		},

		_onScenariosMessage: function(messageFrame) {
			console.log("[stomp][scenarios] received message: " + messageFrame.body);
		},

		_onSceneNodesMessage: function(messageFrame) {
			console.log("[stomp][sceneNodes] received message: " + messageFrame.body);
			var eventBody = messageFrame.body.split(":");
			var eventType = eventBody[1];
			var eventSubject = eventBody[2];
			if (eventType === "created") {
				COMPASS.EventHandler.onSceneNodeCreatedEvent(eventSubject);
			} else if (eventType === "deleted") {
				COMPASS.EventHandler.onSceneNodeDeletedEvent(eventSubject);
			} else if (eventType === "changed") {
				COMPASS.EventHandler.onSceneNodeChangedEvent(eventSubject);
			}
		},

		_onSceneNodeComponentsMessage: function(messageFrame) {
			console.log("[stomp][sceneNodeComponents] received message: " + messageFrame.body);
			var eventBody = messageFrame.body.split(":");
			var eventType = eventBody[1];
			var eventSubject = eventBody[2];
			if (eventType === "created") {
				COMPASS.EventHandler.onSceneNodeComponentCreatedEvent(eventSubject);
			} else if (eventType === "deleted") {
				COMPASS.EventHandler.onSceneNodeComponentDeletedEvent(eventSubject);
			} else if (eventType === "changed") {
				COMPASS.EventHandler.onSceneNodeComponentChangedEvent(eventSubject);
			}
		},

		_onPrefabSetMessage: function(messageFrame){
			console.log("[stomp][PrefabSet] received message: " + messageFrame.body);
			var eventBody = messageFrame.body.split(":");
			var eventType = eventBody[1];
			var eventSubject = eventBody[2];
			if (eventType === "created") {
				COMPASS.EventHandler.onPrefabSetCreated(eventSubject);
			} else if (eventType === "deleted") {
				COMPASS.EventHandler.onPrefabSetDeleted(eventSubject);
			} else if (eventType === "changed") {
				COMPASS.EventHandler.onPrefabSetChanged(eventSubject);
			}
		}
	});
})();