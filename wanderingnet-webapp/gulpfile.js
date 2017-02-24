/*
 * Copyright (c) 2015 Stilavia
 */

'use strict';

var VERSION = '1.0.0';

var gulp = require('gulp');
var less = require('gulp-less');
var path = require('path');
var minifyCSS = require('gulp-minify-css');
var watch = require('gulp-watch');
var notify = require("gulp-notify");
var gp_concat = require('gulp-concat');
var gp_rename = require('gulp-rename');

var SOURCE_DIR = path.join(__dirname, 'src/main/resources');

var STATIC_DIR = path.join(SOURCE_DIR, 'static/**/*');
var CSS_DIR = path.join(SOURCE_DIR, 'static/css');
var JS_DIR = path.join(SOURCE_DIR, 'static/js');

var JS_FILES = ["**/*.js", "!script.js"];


var tasks = ['css', 'js'];

gulp.task('css', function () {
    return gulp.src(path.join(CSS_DIR, 'style.less'))
        .pipe(less())
        .pipe(minifyCSS())
        .pipe(notify("Stilavia WebApp CSS done!"))
        .pipe(gulp.dest(CSS_DIR));
});

gulp.task('js', function () {
    return gulp.src(JS_FILES, {cwd: JS_DIR})
        .pipe(gp_concat('script.js'))
        .pipe(gp_rename('script.js'))
        .pipe(notify("Stilavia WebApp JS done!"))
        .pipe(gulp.dest(JS_DIR))
});

gulp.task('watch', tasks, function () {
    gulp.watch([STATIC_DIR, "!" + path.join(CSS_DIR, 'style.css'), "!" + path.join(JS_DIR, 'script.js')], tasks);
});

gulp.task('default', tasks, function () {
});