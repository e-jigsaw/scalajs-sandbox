gulp = require 'gulp'
jade = require 'gulp-jade'
conn = require 'gulp-connect'

paths =
  copy: ['../../target/scala-2.11/scala-js-tutorial-fastopt.js']
  jade: ['index.jade']
  dest: 'build'

gulp.task 'copy', ->
  gulp.src paths.copy
    .pipe gulp.dest(paths.dest)

gulp.task 'jade', ->
  gulp.src paths.jade
    .pipe jade()
    .pipe gulp.dest(paths.dest)

gulp.task 'default', ['copy', 'jade']
gulp.task 'watch', ['default'], ->
  gulp.watch paths.copy, ['copy']
  gulp.watch paths.jade, ['jade']
  conn.server
    root: paths.dest
