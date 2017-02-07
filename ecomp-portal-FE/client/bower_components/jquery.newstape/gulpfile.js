var gulp = require('gulp'),
    uglify = require('gulp-uglify'),
    rename = require('gulp-rename'),
    clean = require('gulp-clean');

gulp.task('minjs', function() {
  return gulp.src(['jquery.newstape.js'])
    .pipe(uglify())
    .pipe(rename({
      suffix: ".min"
    }))
    .pipe(gulp.dest('dist'));
});

gulp.task('clean', function() {
  return gulp.src(['dist/**/.*'], {read: false})
    .pipe(clean());
});

gulp.task('default', ['clean', 'minjs'], function() {

});