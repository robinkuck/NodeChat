

function Client(socketId, nickname, deviceId) {
    this.socketId = socketId;
    this.nickname = nickname;
    this.deviceId = deviceId;
    this.isOnline = true;
    this.messageQueue = [];
}

Client.prototype.constructor = Client;

Client.prototype.sendMessage = function(message, room) {
    if(this.isOnline) {
        //send message to this clinet in a specific room!
    } else {

    }
}

module.exports = Client;