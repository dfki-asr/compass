"use strict";

var gulp = require('gulp');
var plug = require('gulp-load-plugins')({
	rename: {
		'gulp-jscs-with-reporter': 'jscs'
	}
});
var del = require("del");
var assetBrowserify = require('asset-browserifier');

var srcFolder = "./src/main/webapp/";
var javaScriptSrc = srcFolder + "**/*.js";
var gulpFile = "./gulpfile.js";
var entryPoint = srcFolder + "index.html";
var destination = "./target/webapp";
function subFolder(folderName) {
	return destination + "/" + folderName;
}

gulp.task("default", ["build"]);
gulp.task("build", ["bower-vendor", "build-ours"]);
gulp.task("build-ours", ["bundle", "sass"]);

gulp.task("lint", function(){
	return gulp.src([javaScriptSrc, gulpFile])
		.pipe(plug.jshint({
			linter: "jshint",
			lookup: true
		}))
		.pipe(plug.jshint.reporter("jshint-stylish", {verbose: true}))
		.pipe(plug.jshint.reporter("fail"));
});

gulp.task("jscs", ["lint"], function(){
	return gulp.src([javaScriptSrc, gulpFile])
		.pipe(plug.jscs(".jscsrc"))
		.pipe(plug.jscs.reporter("console"))
		.pipe(plug.jscs.reporter("fail"));
});

gulp.task("sass", function(){
	var src = srcFolder + "**/*.scss";
	return gulp.src(src)
		.pipe(plug.sourcemaps.init())
		.pipe(plug.sass().on('error', plug.sass.logError))
		.pipe(plug.sourcemaps.write())
		.pipe(gulp.dest(destination));
});

gulp.task("bower-vendor", function() {
	var vendorFiles = require('main-bower-files');
	var jsFilter = plug.filter(['*.js']);
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

gulp.task("bundle", ["jscs"], function() {
	var filter = new assetBrowserify({
		"browserifyOpts": { "debug": true }
	});
	var assets = plug.useref.assets({}, filter.collect);

	return gulp.src(entryPoint)
		.pipe(assets)
		.pipe(filter.reInject())
		.pipe(plug.sourcemaps.init({loadMaps: true}))
		.pipe(assets.restore())
		.pipe(plug.useref())
		.pipe(plug.sourcemaps.write('./'))
		.pipe(gulp.dest(destination));
});

gulp.task("clean", function(cb) {
	del([destination], cb);
});

gulp.task("build-ours-with-notify", ["build-ours"], function() {
	var notifier = require('node-notifier');
	notifier.notify({
		title: 'Gulp Watch',
		message: 'COMPASS Frontend build done! Yay!',
		sound: true,
		wait: false
	});
});

gulp.task("watch", ["build"], function(){
	// proxy "/resources" to local WildFly for development
	var proxySetup = require('url').parse('http://localhost:8080/compass/resources');
	proxySetup.route = "/resources";
	var proxyMiddleware = require('proxy-middleware')(proxySetup);

	plug.connect.server({
		port: 3030,
		root: "./target/webapp",
		middleware: function() {return [proxyMiddleware];}
	});
	var filesToWatch = srcFolder + "**/*.*";
	gulp.watch(filesToWatch, ["build-ours-with-notify"]);
});
