<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="include/taglib.jsp"%>
<html>
<head>
        <meta charset="utf-8">
        <meta http-equiv="x-ua-compatible" content="ie=edge">
        <title>MJ TCS Client</title>
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <script src="/plugin/socket/sockjs-1.0.3.js"></script>
        <script src="/plugin/stomp/stomp.js"></script>
        <script type="text/javascript">
            var wsUrl = "http://localhost:8080/tcs-web/stomp";
            var stompClient = null;

            function setConnected(connected) {
                document.getElementById('connect').disabled = connected;
                document.getElementById('disconnect').disabled = !connected;
            }

            function connect() {
                var socket = new SockJS(wsUrl);
                stompClient = Stomp.over(socket);
                stompClient.connect({}, function(frame) {
                    setConnected(true);
                    console.log('Connected: ' + frame);
//                stompClient.subscribe('/topic/greetings', function(greeting){
//                    showMessage(JSON.parse(greeting.body).content);
//                });
                });
            }

            function disconnect() {
                if (stompClient != null) {
                    stompClient.disconnect();
                }
                setConnected(false);
                console.log("Disconnected");
            }

            function uploadFile() {
                var file = document.getElementById('filename').files[0];
                stompClient.send("/app/tcs_model", JSON.stringify({ 'filename': file.name }));
                var reader = new FileReader();
                //alert(file.name);

                reader.loadend = function() {
                }

                reader.onload = function(e) {
                    var content = e.target.result;
                    content.split('\n').forEach(function(v) {
                        var line = v;
                    });
                    stompClient.send("/app/tcs_model", content);
                    alert("the File has been transferred.")
                    stompClient.send("/app/tcs_model", 'end');
                }

                reader.readAsText(file);
            }

            function sendHello() {
                var name = document.getElementById('name').value;
                stompClient.send("/app/hello", {}, JSON.stringify({ 'name': name }));
            }

            function showMessage(message) {
                var response = document.getElementById('response');
                var p = document.createElement('p');
                p.style.wordWrap = 'break-word';
                p.appendChild(document.createTextNode(message));
                response.appendChild(p);
            }
        </script>
    </head>
    <body>
        <div>
            <div>
                <button id="connect" onclick="connect();">Connect</button>
                <button id="disconnect" disabled="disabled" onclick="disconnect();">Disconnect</button>
            </div>
            <div>
                <input type="file" id="filename" />
                <br>
                <input type="button" value="Upload" onclick="uploadFile()" />
            </div>
        </div>

    </body>
</html>
