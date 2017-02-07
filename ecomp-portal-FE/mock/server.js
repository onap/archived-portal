'use strict';
let express = require('express');
let cors = require('cors');
let app = express();

app.use(cors());

app.use('/ecompportal', require('./ecomp/router'));

let server = app.listen(9001, function () {
  let host = server.address().address;
  let port = server.address().port;

  console.log('Example app listening at http://%s:%s', host, port);
});

