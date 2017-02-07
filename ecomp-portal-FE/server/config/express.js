'use strict';

let express = require('express');
let favicon = require('serve-favicon');
let morgan = require('morgan');
let compression = require('compression');
let bodyParser = require('body-parser');
let methodOverride = require('method-override');
let cookieParser = require('cookie-parser');
let errorHandler = require('errorhandler');
let path = require('path');
let config = require('./environment');


module.exports = (app) => {
    var env = app.get('env');

    app.set('views', config.root + '/server/views');
    app.engine('html', require('ejs').renderFile);
    app.set('view engine', 'html');
    app.use(compression());
    app.use(bodyParser.urlencoded({extended: false}));
    app.use(bodyParser.json());
    app.use(methodOverride());
    app.use(cookieParser());

    //if (process.env.NODE_TREE_STRUCTURE === 'unminified') {
        app.use(require('connect-livereload')());
        app.use(config.baseUrl, express.static(path.join(config.root, '.tmp')));
        app.use(config.baseUrl, express.static(path.join(config.root, 'client')));
        app.set('appPath', path.join(config.root, 'client'));
        app.use(morgan('dev'));
        app.use(errorHandler()); // Error handler - has to be last
    //} else {
    //    app.use(favicon(path.join(config.root, 'public', 'favicon.ico')));
    //    app.use(express.static(path.join(config.root, 'public')));
    //    app.set('appPath', path.join(config.root, 'public'));
    //    app.use(morgan('dev'));
    //}
};
