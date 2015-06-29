/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

"use strict";

// POjsO to provide "compile time" app configuration
var Config = function () {
	this.restPath = "./resources/restv1/";
	this.getRESTPath = function (pathSuffix) {
			return this.restPath + pathSuffix;
	};
};

module.exports = new Config();
