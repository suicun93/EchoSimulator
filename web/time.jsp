<%-- 
    Document   : time.jsp
    Created on : Nov 25, 2019, 3:54:32 PM
    Author     : hoang-trung-duc
--%>

<%@page import="java.util.Date"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Set up system time</title>
    </head>
    <body>
        <br>
        <br>        <br>
        Date: <input type="date" id="date" value="" />
        <br>        <br>
        <br>
        Time: <input type="time" id="time" value="" />
        <br>        <br>
        <br>
        <button onclick="sendTime()">Set</button>
    </body>
    <script>
        const INTERVAL_TIME = 15000;
        const TIME_OUT = 1000;
        const STATE_READY = 4;
        const STATUS_OK = 200;
        var dateInput = document.getElementById("date");
        var timeInput = document.getElementById("time");
        window.onload = () => {
            //FF, Opera, Safari, Chrome
            var xmlHttp = new XMLHttpRequest();
            xmlHttp.open('POST', "Time", true);
            xmlHttp.setRequestHeader("Content-Type", "text/html");
            xmlHttp.onload = () => {
                if (xmlHttp.readyState === STATE_READY && xmlHttp.status === STATUS_OK) {
                    var date = xmlHttp.responseText;
                    var p = date.split(' ');
                    var month = p[1] + "";
                    var day = p[0] + "";
                    month = month.length !== 1 ? month : "0" + month;
                    day = day.length !== 1 ? day : "0" + day;
                    dateInput.value =
                            p[2] + '-' + month + '-' + day;
                    timeInput.value =
                            p[3].substr(0, 5);
                } else {
                    window.alert("Connection failed: " + xmlHttp.status);
                }
            };
            xmlHttp.send('');
        };

        function sendTime() {
            date = dateInput.value;
            time = timeInput.value;
            var xmlHttp = new XMLHttpRequest();
            xmlHttp.open("POST", "Time", true);
            xmlHttp.onerror = (e) => {
                window.alert("Can not connect to server");
            };
            xmlHttp.onload = (e) => {
                if (xmlHttp.readyState === STATE_READY && xmlHttp.status === STATUS_OK) {
                    var revdata = xmlHttp.responseText;
                    if (revdata !== "success") {
                        console.log(revdata);
                        window.alert("Set time failed: " + revdata);
                    } else {
                        window.alert("Set time success.");
                    }
                } else {
                    window.alert("Connection failed: " + xmlHttp.status);
                }
            };
            xmlHttp.send(date + " " + time);
        }

    </script>
</html>
