var mysql = require('mysql');

var connection = mysql.createConnection({
    host: "robinkuck.de",
    user: "nodechat_users",
    password: "BOvvSyNn7NGX02bc",
    database: "nodechat"
});

module.exports = {

    connection: connection,

    insertRow: function (nick, deviceid) {
        executeQuery("INSERT INTO users(nick, deviceid) VALUES ('" + nick + "', '" + deviceid + "')");
    },
}

function executeQuery(query) {
    connection.query(query, function (err, result) {
        if (err) throw err;
        console.log("Result: " + result);
    });
}

function returnResult(query) {
    connection.query(query, function (err, result) {
        if (err) throw err;
        console.log("Result: " + JSON.stringify(result));
        return JSON.stringify(result);
    });
}

function resultExists(query) {
    connection.query(query, function (err, result) {
        if (err) throw err;
        if (result.length == 0) {
            return false;
        } else {
            return true;
        }
    });
}
