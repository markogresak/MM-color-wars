var port = process.env.PORT || 3000;
var express = require('express');
var app = express();
app.use(express.static(__dirname + '/public'));
require('http').createServer(app)
  .listen(port, function () {
    console.log('Server listening at port %d', port);
  });
