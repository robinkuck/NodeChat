let Room = require('./Room');

function GlobalRoom(id, label) {
    this.id = id;
    this.label = label;
    this.members = [];
}

GlobalRoom.prototype = Object.create(Room.prototype);

GlobalRoom.prototype.constructor = GlobalRoom;

