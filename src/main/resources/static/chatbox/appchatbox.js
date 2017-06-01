var stompClient = null;

var time ="3:00 pm"
	
var autoScroll;
var ringtone;
function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/chatboxapp/chatboxs');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/app/firstJoin', function (response) {
        	var listMessage = JSON.parse(response.body);
        	$.each(listMessage, function( index, message ) {
        		showGreeting(message.message, message.name, message.shortName,message.time);
        	});
        });
        stompClient.subscribe('/topic/chatforward', function (response) {
        	var message = JSON.parse(response.body);
            showGreeting(message.message, message.name, message.shortName,message.time);
        });
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage(message) {
	var date = new Date();
    time = date.getDate() + '/' + (date.getMonth() + 1) + '/' + date.getFullYear() + ' ' + date.getHours() +':' +	date.getMinutes();
    stompClient.send("/app/chat", {}, JSON.stringify({'name': name, "message" : message, "time": time, "shortName": shortName}));
}

function showGreeting(message, name, shortName, time) {
    $("#direct-chat-messages-content").append('<div class="direct-chat-msg doted-border">  <div class="direct-chat-info clearfix"><span class="direct-chat-name pull-left">' + name + '</span>  </div>  <div class="v3-image-name-prospect">' + shortName + '</div> <div class="direct-chat-text">' + message + '</div>  <div class="direct-chat-info clearfix"><span class="direct-chat-timestamp pull-right">' + time + '</span>  </div></div>');
    if(autoScroll.prop('checked'))
    	$('#popup-messages').scrollTo($('#direct-chat-messages'));
    if(ringtone.prop('checked'))
    	$.playSound("ding")
}

jQuery.fn.scrollTo = function(elem, speed) { 
    $(this).animate({
        scrollTop: $(elem).height()
    }, speed == undefined ? 100 : speed); 
    return this; 
};

$(function () {
	connect();
    $("#message").on('keyup', function (e) {
        if (e.keyCode == 13 && $("#message").val() != "") {
        	sendMessage($("#message").val());
        	$("#message").val("");
        }
    });
    
    $('#btnLike').click(function() { 
    	sendMessage('Like mạnh!!!  <i class="glyphicon glyphicon-thumbs-up"></i><i class="glyphicon glyphicon-thumbs-up"></i><i class="glyphicon glyphicon-thumbs-up"></i>');
    });
    
    autoScroll = $('#autoScroll');
    ringtone = $('#ringtone');
    
});

