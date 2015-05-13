"use strict";

var browserify = require('browserify');
var gulp = require('gulp');
var source = require('vinyl-source-stream');
var buffer = require('vinyl-buffer');
var gutil = require('gulp-util');
var sourcemaps = require('gulp-sourcemaps');
var del = require("del");
var pkg = require("./package.json");

var srcFolder = "./src/main/webapp/";
var htmlFiles = [
		srcFolder + "index.html"
	];
var destination = "./target/webapp";

gulp.task("build", function(){
	bundle();
	gulp.src(extraFiles)
	    .pipe(gulp.dest(destination));
});

var browserifyOptions = {
	entries: [srcFolder + "index.js"],
	debug: true
};
var b = browserify(browserifyOptions);

gulp.task("js", bundle); // so you can run `gulp js` to build the file
b.on('log', gutil.log); // output build logs to terminal

function bundle() {
  return b.bundle()
    .on('error', gutil.log.bind(gutil, 'Browserify Error'))
    .pipe(source('bundle.js'))
    .pipe(buffer())
    .pipe(sourcemaps.init({loadMaps: true})) // loads map from browserify file
    .pipe(sourcemaps.write('./')) // writes .map file
    .pipe(gulp.dest(destination));
}

gulp.task("clean", function(cb) {
	del([destination], cb);
});

