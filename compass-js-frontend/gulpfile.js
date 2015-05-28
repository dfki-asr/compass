"use strict";

var browserify = require('browserify');
var gulp = require('gulp');
var plug = require('gulp-load-plugins')();
var source = require('vinyl-source-stream');
var buffer = require('vinyl-buffer');
var sourcemaps = require('gulp-sourcemaps');
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
gulp.task("build", ["bower-vendor", "build-ours"]);
gulp.task("build-ours", ["bundle", "sass", "copyFiles"])

gulp.task("sass", function(){
	var src = srcFolder + "**/*.scss";
	gulp.src(src)
		.pipe(plug.sourcemaps.init())
		.pipe(plug.sass().on('error', plug.sass.logError))
		.pipe(plug.sourcemaps.write())
		.pipe(gulp.dest(destination));
});

gulp.task("bower-vendor", function() {
	var vendorFiles = require('main-bower-files');
	var jsFilter = plug.filter(['*.js', '!*.min.js']);
	var cssFilter = plug.filter('*.css');
	var fontFilter = plug.filter(['*.eot', '*.woff', '*.svg', '*.ttf', '*.woff2']);
	var imageFilter = plug.filter(['*.gif', '*.png', '*.svg', '*.jpg', '*.jpeg']);

	return gulp.src(vendorFiles())
		// JS
		.pipe(jsFilter)
		.pipe(plug.sourcemaps.init())
		.pipe(plug.concat('lib.js'))
		.pipe(gulp.dest(subFolder("vendor/js")))
		.pipe(plug.uglify())
		.pipe(plug.rename({
			suffix: ".min"
		}))
		.pipe(plug.sourcemaps.write("./", {sourceRoot: "/source/bower_components/"}))
		.pipe(gulp.dest(subFolder("vendor/js")))
		.pipe(jsFilter.restore())

		// CSS
		.pipe(cssFilter)
		.pipe(plug.sourcemaps.init())
		.pipe(plug.concat('lib.css'))
		.pipe(gulp.dest(subFolder("vendor/css")))
		.pipe(plug.minifyCss({keepBreaks:true}))
		.pipe(plug.rename({
			suffix: ".min"
		}))
		.pipe(plug.sourcemaps.write("./", {sourceRoot: "/source/bower_components/"}))
		.pipe(gulp.dest(subFolder("vendor/css")))
		.pipe(cssFilter.restore())

		// FONTS
		.pipe(fontFilter)
		.pipe(plug.flatten())
		.pipe(gulp.dest(subFolder("vendor/fonts")))
		.pipe(fontFilter.restore())

		// IMAGES
		.pipe(imageFilter)
		.pipe(plug.flatten())
		.pipe(gulp.dest(subFolder("vendor/images")))
		.pipe(imageFilter.restore());
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
b.on('log', plug.util.log); // output build logs to terminal

gulp.task("bundle", function() {
	return b.bundle()
	        .on('error', plug.util.log.bind(plug.util, 'Browserify Error'))
	        .pipe(source('bundle.js'))
	        .pipe(buffer())
	        .pipe(plug.sourcemaps.init({loadMaps: true})) // loads map from browserify file
	        .pipe(plug.sourcemaps.write('./')) // writes .map file
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
	plug.connect.server({
		port: 3030,
		root: "./target/webapp"
	});
	var filesToWatch = srcFolder + "**/*.*";
	gulp.watch(filesToWatch, ["build-ours"]);
});
