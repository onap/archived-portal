'use strict';
//let serverCommon = require('ssp_server_common');
let express = require('express');
let config = require('./config/environment');

//let logger = serverCommon.getLogger(config);
// Setup server
let app = express();
let server = require('http').createServer(app);
require('./config/express')(app);
require('./routes')(app);

// Start server
server.listen(config.port, config.ip, ()=> {
    console.log('Express server listening on %d port, in %s mode', config.port, config.ip);
});

// Expose app
module.exports = app;
