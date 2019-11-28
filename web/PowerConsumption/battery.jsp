<%-- 
    Document   : battery
    Created on : Nov 26, 2019, 4:30:19 PM
    Author     : hoang-trung-duc
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Battery Consumption Schedule</title>
    </head>
    <body>
        <h1>Battery Consumption Schedule</h1><br>

        Start: <br>
        <input type="time" id="startTime" value="" /><br><br>

        End: <br>
        <input type="time" id="endTime" value="" /><br><br>

        Mode: <br>
        <select id="mode">
            <option value="0x42" selected="selected">Charge</option>
            <option value="0x41">Rapid Charge</option>
            <option value="0x44">Stand By</option>
        </select> <br><br>

        Instantaneous Electric Energy: <br>
        <input type="number"  id="instantaneous" min="1" max="1000" />W<br><br>

        <input type="submit" value="Schedule" onclick="schedule('battery')"/>
    </body>
    <script>
        const INTERVAL_TIME = 15000;
        const TIME_OUT = 1000;
        const STATE_READY = 4;
        const STATUS_OK = 200;
        var startTimeInput = document.getElementById("startTime");
        var endTimeInput = document.getElementById("endTime");
        var modeInput = document.getElementById("mode");
        var instantaneousInput = document.getElementById("instantaneous");

        /**
         * @param {String} device 
         */
        function schedule(device) {
            var startTime = startTimeInput.value;
            var endTime = endTimeInput.value;
            var mode = modeInput.options[modeInput.selectedIndex].value;
            var instantaneous = instantaneousInput.value;
            if (startTime === "") {
                window.alert("Enter Start Time");
                return;
            }
            if (endTime === "") {
                window.alert("Enter End Time");
                return;
            }

            if (instantaneous === "") {
                window.alert("Enter Instantaneous Electric Energy");
                return;
            }

            if (parseInt(instantaneous) <= 0) {
                window.alert("Enter Instantaneous Electric Energy > 0");
                return;
            }

            var xmlHttp = new XMLHttpRequest();
            xmlHttp.open('POST', "../Schedule", true);
            xmlHttp.setRequestHeader("Content-Type", "text/html");
            xmlHttp.onload = () => {
                if (xmlHttp.readyState === STATE_READY && xmlHttp.status === STATUS_OK) {
                    var data = xmlHttp.responseText;
                    if (data === "success") {
                        window.alert("success");
                    } else {
                        window.alert("Failed: " + data);
                    }
                } else {
                    window.alert("Connection failed: " + xmlHttp.status);
                }
            };
            xmlHttp.send(device + "," + startTime + "," + endTime + "," + mode + "," + instantaneous);
        }
    </script>
</html>
