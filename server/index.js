var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);

server.listen(8010, function() {
	console.log("Server is now running!");
});

io.on('connection', function(socket){
	console.log("Player connected!");
	
	socket.on('new msg', function(data) {
		console.log('message: ' + data.text);
		socket.broadcast.emit('message', data);
	});
	
	socket.on('disconnect', function() {
		console.log("Player disconnected!");
	});
});

function user(id, name) {
	this.id = id;
	this.name = name;
}