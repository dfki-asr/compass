"use strict";

var browserify = require('browserify');
var watch = require('gulp-watch');
var gulp = require('gulp');
var connect = require('gulp-connect');
var source = require('vinyl-source-stream');
var buffer = require('vinyl-buffer');
var gutil = require('gulp-util');
var sass = require('gulp-sass');
var filter = require('gulp-filter');
var rename = require('gulp-rename');
var sourcemaps = require('gulp-sourcemaps');
var concat = require('gulp-concat');
var uglify = require('gulp-uglify');
var minifyCSS = require('gulp-minify-css');
var flatten = require('gulp-flatten');
var del = require("del");
var html = require('html-browserify');
var riotify = require('riotify');

var srcFolder = "./src/main/webapp/";
var extraFiles = [
		srcFolder + "**/*.html"
	];
var destination = "./target/webapp";
function subFolder(folderName) {
	return destination + "/" + folderName;
}

gulp.task("default", ["build"]);

gulp.task("build", ["bundle", "bower-vendor", "sass", "copyFiles"]);

gulp.task("sass", function(){
	var src = srcFolder + "**/*.scss";
	gulp.src(src)
		.pipe(sourcemaps.init())
		.pipe(sass().on('error', sass.logError))
		.pipe(sourcemaps.write())
		.pipe(gulp.dest(destination));
});

gulp.task("bower-vendor", function() {
	var vendorFiles = require('main-bower-files');
	var jsFilter = filter('*.js')
	var cssFilter = filter('*.css')
	var fontFilter = filter(['*.eot', '*.woff', '*.svg', '*.ttf'])
	var imageFilter = filter(['*.gif', '*.png', '*.svg', '*.jpg', '*.jpeg'])

	return gulp.src(vendorFiles())
		// JS
		.pipe(jsFilter)
		.pipe(concat('lib.js'))
		.pipe(gulp.dest(subFolder("vendor/js")))
		.pipe(uglify())
		.pipe(rename({
			suffix: ".min"
		}))
		.pipe(gulp.dest(subFolder("vendor/js")))
		.pipe(jsFilter.restore())

		// CSS
		.pipe(cssFilter)
		.pipe(concat('lib.css'))
		.pipe(gulp.dest(subFolder("vendor/css")))
		.pipe(minifyCSS({keepBreaks:true}))
		.pipe(rename({
			suffix: ".min"
		}))
		.pipe(gulp.dest(subFolder("vendor/css")))
		.pipe(cssFilter.restore())

		// FONTS
		.pipe(fontFilter)
		.pipe(flatten())
		.pipe(gulp.dest(subFolder("vendor/fonts")))
		.pipe(fontFilter.restore())

		// IMAGES
		.pipe(imageFilter)
		.pipe(flatten())
		.pipe(gulp.dest(subFolder("vendor/images")))
		.pipe(imageFilter.restore())
});

var browserifyOptions = {
	entries: [srcFolder + "app.js"],
	debug: true,
	transform: [
		html,
		[riotify]
	]
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
