var stompClient = null;
var maximumReconnectAttempts=30;
var reconnectAttempts=0;
function hideConnectedUserInfo() {
    $("#connectedUsers").hide();
    $("#connectedUsersCount").hide();
}

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#chatUsername").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#onlineStatus").css({'color': 'green'}).html("<i><b>online</b></i>");
        $("#conversation").show();
    }
    else {
        $("#onlineStatus").css({'color': 'red'}).html("<i><b>offline</b></i>");
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
        reconnectAttempts=0;
        stompClient.subscribe('/topic/messages.'+username, function (message) {
                    console.log('userchat Body: ' + message.body);
                    var chatMessage = JSON.parse(message.body);
                    console.log('ParsedBody: ' + chatMessage);
                    if (username == chatMessage.fromChatUser.username){
                        console.log("You sent this message");
                        showSentMessage(chatMessage);
                    } else {
                        console.log("You are receiving this message");
                        showReceivedMessage(chatMessage);
                    }
                });
        stompClient.subscribe('/topic/connectedusers', function (message) {
            console.log('userconnected Message: ' + message);
            console.log('userconnected Body: ' + message.body);
            var connectedChatUsers = JSON.parse(message.body);
            updatedConnectedUsers(connectedChatUsers);
        });
        stompClient.send("/app/whoisconnected/", {'username': username}, {});
        setConnected(true);
    }, function (frame) {
        console.log("Error Frame: " + frame);
        if (frame.indexOf("Whoops! Lost connection") >= 0){
            console.log("Handling system disconnect");
            setConnected(false);
            if (reconnectAttempts <= maximumReconnectAttempts){
                reconnectAttempts++;
                setTimeout(function(){ connect() }, 15000);
            }
        }
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
        reconnectAttempts=0;
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    if (stompClient == null){
        connect();
    }
    var username = $("#destinationUsername").val();
    var senderUsername = $("#chatUsername").val();
    var message = $("#message").val();
    var jsonString =  JSON.stringify({'message': message, 'chatUser': {'username': username}, 'fromChatUser': {'username': senderUsername}});
    stompClient.send("/app/userchat/"+username, {'username': senderUsername}, jsonString);
}

function showReceivedMessage(message) {
    $("#messages").append("<tr><td>" + message.sentDateTime + " - <b>" + message.fromChatUser.username + "</b> Says: " + message.message + "</td></tr>");
}
function showSentMessage(message) {
    $("#messages").append("<tr><td>" + message.sentDateTime+ " - <i>You said: " + message.message+ "</i></td></tr>");
}

function updatedConnectedUsers(connectedChatUsers){
    var connectedUsername = $("#chatUsername").val();

    if (connectedChatUsers.chatUsers.length > 0) {
        var connectedUserCount = 0;
        var usernames = "";
        for (var i=0;i<connectedChatUsers.chatUsers.length;i++){
            var username = connectedChatUsers.chatUsers[i].username;
            if (username == connectedUsername){
                continue;
            }
            usernames += username;
            connectedUserCount++;
            if (i<connectedChatUsers.chatUsers.length-1){
                usernames += ", ";
            }
        }
        $("#connectedUsersCount").html(connectedUserCount + " Other Users online.");
        $("#connectedUsersCount").show();
        $("#connectedUsers").html("Users you can message: <br><b>" + usernames + "</b><br>");
        if (connectedUserCount > 0){
            $("#connectedUsers").show();
        } else {
            $("#connectedUsers").hide();

        }
    }
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

