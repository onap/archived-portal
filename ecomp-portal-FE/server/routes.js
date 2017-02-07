'use strict';
let path = require('path');
let config = require('./config/environment');

module.exports = app => {
    // All undefined asset or api routes should return a 404
    app.route('/:url(api|auth|components|app|bower_components|assets)/*')
        .get(function(req, res){
            res.status(404).send();
        });

    // All other routes should redirect to the index.html
    app.route(config.baseUrl + '/*')
        .get(function (req, res) {
            res.sendFile(path.resolve('client/index.html'));
        });
};
