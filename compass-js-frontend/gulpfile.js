"use strict";

var browserify = require('browserify');
var watch = require('gulp-watch');
var gulp = require('gulp');
var connect = require('gulp-connect');
var source = require('vinyl-source-stream');
var buffer = require('vinyl-buffer');
var gutil = require('gulp-util');
var sass = require('gulp-sass');
var sourcemaps = require('gulp-sourcemaps');
var del = require("del");

var srcFolder = "./src/main/webapp/";
var extraFiles = [
		srcFolder + "**/*.html"
	];
var destination = "./target/webapp";

gulp.task("build", ["bundle", "vendorcss","sass", "copyFiles"]);

gulp.task("sass", function(){
	var src = srcFolder + "**/*.scss";
	gulp.src(src)
		.pipe(sourcemaps.init())
		.pipe(sass().on('error', sass.logError))
		.pipe(sourcemaps.write())
		.pipe(gulp.dest(destination));
});

gulp.task("vendorcss", function(){
	var src = [
		"./node_modules/bootstrap/dist/css/bootstrap.css",
		"./node_modules/bootstrap/dist/css/bootstrap.css.map"
	];
	return gulp.src(src)
			.pipe(gulp.dest(destination + "/css"));
});

var browserifyOptions = {
	entries: [srcFolder + "app.js"],
	debug: true
};
var b = browserify(browserifyOptions);
b.on('log', gutil.log); // output build logs to terminal

gulp.task("bundle", function() {
	return b.bundle()
	        .on('error', gutil.log.bind(gutil, 'Browserify Error'))
	        .pipe(source('bundle.js'))
	        .pipe(buffer())
	        .pipe(sourcemaps.init({loadMaps: true})) // loads map from browserify file
	        .pipe(sourcemaps.write('./')) // writes .map file
	        .pipe(gulp.dest(destination));
});

gulp.task("copyFiles", function (){
	return gulp.src(extraFiles)
	           .pipe(gulp.dest(destination));
});

gulp.task("clean", function(cb) {
	del([destination], cb);
});

gulp.task("watch", ["build"], function(){
	connect.server({
		port: 3030,
		root: "./target/webapp"
	});
	var filesToWatch = srcFolder + "**/*.*";
	gulp.watch(filesToWatch, ["build"]);
});
