var stompClient = null;
function hideConnectedUserInfo() {
    $("#lastUserDisconnected").hide();
    $("#connectedUsers").hide();
}

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
        hideConnectedUserInfo();
    }
    $("#messages").html("");
}

function connect() {
    var username = $("#chatUsername").val();
    var socket = new SockJS('/gs-guide-websocket');


    stompClient = Stomp.over(socket);
    stompClient.connect({"username": username}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/userchat.'+username, function (message) {
            console.log('userchat Body: ' + message.body);
            console.log('ParsedBody: ' + JSON.parse(message.body));
            showReceivedMessage(JSON.parse(message.body));
        });
        stompClient.subscribe('/topic/connectedusers', function (message) {
            console.log('userconnected Body: ' + message.body);
            showLastUserConnected(message.body);
        });
        setConnected(true);
    });
}

function disconnect() {
    if (stompClient !== null) {
        var username = $("#chatUsername").val();
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    if (stompClient == null){
        connect();
    }
    var username = $("#destinationUsername").val();
    var message = $("#message").val();
    var jsonString =  JSON.stringify({'message': message, 'chatUser': {'username': username}});
    stompClient.send("/app/userchat/"+username, {}, jsonString);
    appendSentMessage(message)
}

function showReceivedMessage(message) {
    $("#messages").append("<tr><td>User " + message.chatUser.username + " Says: " + message.message + "</td></tr>");
}
function appendSentMessage(message) {
    $("#messages").append("<tr><td>You said: " + message + "</td></tr>");
}

function showLastUserConnected(usernames){
    $("#connectedUsers").show();
    $("#connectedUsers").html("Connected Users: " + usernames);

}

function showLastUserDisonnected(username){
    $("#lastUserDisconnected").show();
    $("#lastUserDisconnected").html("Last Disconnected User: " + username);
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
    setConnected(false);
});

