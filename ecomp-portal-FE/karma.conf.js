// Karma configuration
// http://karma-runner.github.io/0.10/config/configuration-file.html

module.exports = function (config) {
    config.set({
        // base path, that will be used to resolve files and exclude
        basePath: '',

        // testing framework to use (jasmine/mocha/qunit/...)
        frameworks: ['jasmine'],

        // list of files / patterns to load in the browser
        files: [
            'client/bower_components/jquery/dist/jquery.js',
            'client/bower_components/angular/angular.js',
            'client/bower_components/angular-animate/angular-animate.js',
            'client/bower_components/angular-aria/angular-aria.js',
            'client/bower_components/angular-bootstrap/ui-bootstrap-tpls.js',
            'client/bower_components/angular-cookies/angular-cookies.js',
            // 'client/bower_components/angular-gestures/gestures.js',
            'client/bower_components/angular-material/angular-material.js',
            'client/bower_components/angular-messages/angular-messages.js',
            'client/bower_components/angular-mocks/angular-mocks.js',
            'client/bower_components/angular-resource/angular-resource.js',
            'client/bower_components/angular-route/angular-route.js',
            'client/bower_components/angular-sanitize/angular-sanitize.js',
            'client/bower_components/angular-scenario/angular-scenario.js',
            'client/bower_components/angular-smart-table/dist/smart-table.js',
            'client/bower_components/angular-ui-router/release/angular-ui-router.js',
            'client/bower_components/angular-uuid/uuid.js',
            'client/bower_components/es5-shim/es5-shim.js',
            'client/bower_components/hammerjs/hammer.js',
            'client/bower_components/jqtree/tree.jquery.js',
            'client/bower_components/json3/lib/json3.js',
            'client/bower_components/lodash/lodash.js',
            'client/bower_components/ng-dialog/js/ngDialog.js',
            'client/bower_components/ui-select/dist/select.js',

            'client/utils/test-utils/test-utils.js',

            'client/app/configurations.js',
            'client/app/app.js',
            // 'client/app/**/users*.js',
            // 'client/app/**/users/**/new-user*.js',
            'client/app/**/home/applications-home/applications-home*.js',
            // 'client/app/**/widgets/**/widget-details*.js',
            // 'client/app/**/tabs/**/tabs*.js',

            'client/app/**/*filter*.js',
            'client/app/**/*.coffee',
            'client/app/**/*.jade',
            'client/app/**/*.html',
            'client/bower_components_external/angular.att.abs-2.17.0/att-abs-tpls.js',
            'client/bower_components/hammerjs/hammer.js',
            'client/bower_components/angular-gestures/gestures.js',
            'client/bower_components/ng-dialog/js/ngDialog.js',
            'client/bower_components/angular-cache/dist/angular-cache.js',
            'client/bower_components_external/angular-att-gridster/*.js',
            'client/bower_components_external/angular-att-gridster/*.js'
        ],
        // coverage reporter generates the coverage
        reporters: ['progress', 'coverage'],

        // coverage reporter configuration
        coverageReporter: {
            type : 'html',
            dir : 'coverage/'
        },

        preprocessors: {
            '**/*.jade': 'ng-jade2js',
            '**/*.html': 'html2js',
            'client/app/**/*.js': ['babel'],
            'client/app/**/!(*spec|*mock).js': ['coverage'],
            // '**/*.coffee': 'coffee'
        },

        ngHtml2JsPreprocessor: {
            stripPrefix: 'client/'
        },

        ngJade2JsPreprocessor: {
            stripPrefix: 'client/'
        },


        babelPreprocessor: {
            options: {
                sourceMap: 'inline'
            },
            filename: function (file) {
                return file.originalPath.replace(/\.js$/, '.es5.js');
            },
            sourceFileName: function (file) {
                return file.originalPath;
            }
        },


        // list of files / patterns to exclude
        exclude: [],

        // web server port
        port: 8092,

        // level of logging
        // possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
        logLevel: config.LOG_ERROR,


        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: false,


        // Start these browsers, currently available:
        // - Chrome
        // - ChromeCanary
        // - Firefox
        // - Opera
        // - Safari (only Mac)
        // - PhantomJS
        // - IE (only Windows)
        browsers: ['Firefox'],

        // Continuous Integration mode
        // if true, it capture browsers, run tests and exit
        singleRun: true


    });
};
