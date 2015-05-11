var gulp = require("gulp");
var del = require("del");
var pkg = require("./package.json");

var srcFiles = [
		"./src/main/webapp/index.html"
	];

var destination = "./target/webapp";

gulp.task("default", function(){
	gulp.src(srcFiles)
			.pipe(gulp.dest(destination));
});

gulp.task("clean", function(cb) {
	del([destination], cb);
});

