/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also avialable at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
XML3D.tools.namespace("COMPASS");

(function() {
"use strict";
	COMPASS.Importer = new XML3D.tools.Singleton({
		assetListTemplate : null,
		urlImportTemplate : null,
		importMode : "url",
		file : null,
		openTransactions : {},
		transactionUpdater : null,
		selectedAssetName : null,

		initialize: function() {
			window.addEventListener("load", this._onWindowLoad.bind(this));
		},

		_onWindowLoad : function() {
			this.resetAtlasURLInputField();
			$("#atlas-new-asset-url-input").val(this._defaultATLASLocation());
			$("#atlas-url-input").on("change", this.getAvailableAssets.bind(this));
			$('#atlas-new-asset-file-upload-form').on('change', this._prepareFileUpload.bind(this));
			$("#atlas-new-asset-name").on('change', this._validateNewAssetInputs.bind(this));
			this._compileHandlebarTemplates();
		},

		resetAtlasURLInputField: function(){
			var $urlInput = $("#atlas-url-input");
			if(this._inputFieldHasDefaultValue($urlInput) || this._inputFieldHasPlaceholderValue($urlInput)){
				$urlInput.val(this._defaultATLASLocation());
			}
		},

		_inputFieldHasDefaultValue: function($urlInput){
			return $urlInput.val() === $urlInput.attr("value");
		},

		_inputFieldHasPlaceholderValue: function($urlInput){
			return $urlInput.val() === $urlInput.attr("placeholder");
		},

		_validateNewAssetInputs : function(evt) {
			if (this.isNewAssetNameValid()) {
				$("#atlas-new-asset-name-fail").addClass("hidden");
			} else {
				$("#atlas-new-asset-name-fail").removeClass("hidden");
			}
		},

		_compileHandlebarTemplates: function() {
			var assetListSource = $("#prefab-asset-list-entry-template").html();
			this.assetListTemplate = Handlebars.compile(assetListSource);
			var transactionListSource = $("#asset-transaction-template").html();
			this.transactionListTemplate = Handlebars.compile(transactionListSource);
			var urlImportSource = $("#rg-asset-list-entry-template").html();
			this.urlImportTemplate = Handlebars.compile(urlImportSource);
		},

		startURLImport : function() {
			this.importMode = "url";
			this.showImportDialog();
		},

		startPrefabImport : function() {
			this.importMode = "prefab";
			this.showImportDialog();
		},

		showImportDialog : function() {
			this.getAvailableAssets();
			this.selectedAssetName = null;
			$("#asset-import-button").attr("disabled", "disabled");
			atlasAssetImportDialog.show();
		},

		showNewAssetDialog : function() {
			this._clearNewAssetDialog();
			atlasNewAssetDialog.show();
		},

		_clearNewAssetDialog : function() {
			$("#atlas-new-asset-name").val("");
			$("#atlas-new-asset-file-upload-form").val("");
			$("#atlas-new-asset-file-fail").addClass("hidden");
			$("#atlas-new-asset-name-fail").addClass("hidden");
			$("#atlas-new-asset-upload-fail").addClass("hidden");
			this.file = null;
		},

		getAvailableAssets : function() {
			$("#asset-list").empty();
			$.ajax(this._atlasURL()  + "rest/asset/", {
				dataType: "json",
				success: this._fillAvailableAssetsFromResponse.bind(this),
				error: this._onError.bind(this)
			});
		},

		_onError : function(e) {
			var errorElem = $('<li style="color:red" class="list-group-item">Could not connect to ATLAS at the given URL.</li>');
			$("#asset-list").append(errorElem);
		},

		_fillAvailableAssetsFromResponse : function(data) {
			data.sort();
			var template = this.importMode === "url" ? this.urlImportTemplate : this.assetListTemplate;
			for (var index in data) {
				var assetUrl = data[index];
				var assetName = assetUrl.substring(assetUrl.lastIndexOf('/') + 1, assetUrl.length);
				var listEntryHTML = template({assetname: assetName});
				$("#asset-list").append($(listEntryHTML));
			}
		},

		_atlasURL: function() {
			return $("#atlas-url-input").val();
		},

		_defaultATLASLocation: function() {
			return window.location.protocol + "//" + window.location.host + "/atlas/";
		},

		importAssetAsURL : function(assetName, textFieldId) {
			var assetURL = this._atlasURL() + "rest/asset/" + assetName + ".xml#" + assetName;
			$("[id*='" + textFieldId + "']").val(assetURL).trigger("change");
			atlasAssetImportDialog.hide();
		},

		importAssetAsPrefab : function() {
			if (!this.selectedAssetName) {
				return;
			}
			var assetURL = this._atlasURL() + "rest/asset/" + this.selectedAssetName + ".xml#" + this.selectedAssetName;
			assetURL = this._forceURLToBeAbsolute(assetURL);
			COMPASS.RemoteCaller.importAssetAsPrefab(assetURL);
			atlasAssetImportDialog.hide();
		},

		_forceURLToBeAbsolute: function(url){
			//@Hack
			var l = document.createElement("a");
			l.href = url;
			return l.href;
		},

		selectAssetToImport: function(assetName) {
			if (this.selectedAssetName) {
				$("#list-item-"+this.selectedAssetName).removeClass("active");
			}
			$("#list-item-"+assetName).addClass("active");
			this.selectedAssetName = assetName;
			$("#asset-import-button").removeAttr("disabled");
		},

		onUploadClicked : function() {
			if (!this.isNewAssetNameValid()) {
				this._validateNewAssetInputs();
				return;
			}
			if (!this._validateFileType(this.file)) {
				$("#atlas-new-asset-file-fail").removeClass("hidden");
				return;
			}
			this._uploadNewAssetToAtlas();
		},

		isNewAssetNameValid : function() {
			return $("#atlas-new-asset-name").val().match(/^[A-Za-z][A-Za-z0-9_:\\.-]*$/);
		},

		_prepareFileUpload: function(evt) {
			this.file = null;
			if (this._validateFileType(evt.target.files[0])) {
				this.file = evt.target.files[0];
				$("#atlas-new-asset-file-fail").addClass("hidden");
			} else {
				$("#atlas-new-asset-file-fail").removeClass("hidden");
			}
		},

		_validateFileType: function(file) {
			return file && file.name.substring(file.name.lastIndexOf('.'), file.name.length) === ".zip";
		},

		_uploadNewAssetToAtlas : function() {
			$("#atlas-new-asset-upload-fail").addClass("hidden");
			$.ajax({
				url: $("#atlas-new-asset-url-input").val() + 'rest/asset/' + $("#atlas-new-asset-name").val(),
				type: 'PUT',
				data: this.file,
				cache: false,
				dataType: "text",
				contentType: "application/zip",
				processData: false,
				success: this._onFileUploadSuccess.bind(this),
				error: this._onFileUploadError.bind(this)
			});
		},

		_onFileUploadSuccess : function(data, status, request) {
			var transactionLocation = request.getResponseHeader("location");
			this._beginTransactionProgressTracking(transactionLocation);
			atlasNewAssetDialog.hide();
			COMPASS.Importer.showImportDialog();
		},

		_onFileUploadError : function(request, status, err) {
			$("#atlas-new-asset-upload-fail").text("Upload failed. Please ensure an ATLAS instance is running at the given URL. " + err).removeClass("hidden");
		},

		_beginTransactionProgressTracking : function(transactionLocation) {
			var transactionId = transactionLocation.substring(transactionLocation.lastIndexOf("/")+1, transactionLocation.length);
			var transaction = {
				id		  : transactionId,
				assetName : $("#atlas-new-asset-name").val(),
				location  : transactionLocation,
				status	  : "UPLOADING",
				statusDescription : "The asset is being uploaded to ATLAS.",
				detail	  : null,
				lastUpdated : new Date().getTime()
			};
			this.openTransactions[transactionId] = transaction;
			if (!this.transactionUpdater) {
				this.transactionUpdater = window.setInterval(this._updateOpenTransactions.bind(this), 1000);
			}
			var listEntryHTML = this.transactionListTemplate(transaction);
			this._growl(listEntryHTML, 'info');
		},

		_growl : function(content, type, delay) {
			$.bootstrapGrowl(content, {
				ele: 'body', // which element to append to
				type: type, // (null, 'info', 'danger', 'success')
				offset: {from: 'top', amount: 20}, // 'top', or 'bottom'
				align: 'right', // ('left', 'right', or 'center')
				width: 250, // (integer, or 'auto')
				delay: delay || 4000, // Time while the message will be displayed.
				allow_dismiss: true, // If true then will display a cross to close the popup.
				stackup_spacing: 10 // spacing between consecutively stacked growls.
			});
		},

		_updateOpenTransactions : function() {
			if ($.isEmptyObject(this.openTransactions)) {
				window.clearInterval(this.transactionUpdater);
				this.transactionUpdater = null;
				return;
			}
			for (var index in this.openTransactions) {
				if (!this.openTransactions.hasOwnProperty(index)) {
					continue;
				}
				var transaction = this.openTransactions[index];
				$.ajax({
					url: transaction.location,
					type: 'GET',
					dataType: "json",
					success: this._onGetTransactionSuccess.bind(this),
					error: this._onGetTransactionError.bind(this)
				});
			}
		},

		_onGetTransactionSuccess : function(data, status, request) {
			var status = data.status;
			var transaction = this.openTransactions[data.id];
			if (status !== transaction.status) {
				transaction.status = status;
				transaction.statusDescription = data.statusDescription;
				transaction.detail = data.detail;
				this._updateAssetFeedbackStatus(transaction);
			}
			transaction.lastUpdated = new Date().getTime();
			if (status !== "RECEIVED" && status !== "PROCESSING") {
				//The asset is either finished or processing failed
				delete this.openTransactions[data.id];
				this.getAvailableAssets();
			}
		},

		_onGetTransactionError : function(request, status, err) {
			console.log(err);
			window.clearInterval(this.transactionUpdater);
			this.transactionUpdater = null;
		},

		_updateAssetFeedbackStatus : function(transaction) {
			var listEntryHTML = this.transactionListTemplate(transaction);
			this._growl(listEntryHTML, this._getGrowlTypeForStatus(transaction.status), transaction.detail ? 120000 : 4000);
		},

		_getGrowlTypeForStatus : function(status) {
			switch(status) {
				case "REJECTED"  : return "danger";
				case "FINISHED"  : return "success";
				case "PROCESSING": //fall through
				default			 : return "info";
			}
		},

		toggleImportAssetHierachy: function(){
			COMPASS.RemoteCaller.toggleImportAssetHierachy();
		}
	});
})();
