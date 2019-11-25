<%-- 
    Document   : simulator
    Created on : Nov 20, 2019, 8:34:26 AM
    Author     : hoang-trung-duc
--%>

<%@page import="Main.EchoController"%>
<%@page import="com.sonycsl.echo.Echo"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Simulator Page</title>
    </head>
    <body>
        <h1>Simulator Controller:</h1>
        <!--Start-->
        <input type="button" id="startEVBtn" value="Start EV" onclick="enable('ev')"
               <% if (EchoController.contains("ev")) {
               %>disabled<%
                   }
               %>/>
        <input type="button" id="startBatteryBtn" value="Start Battery" onclick="enable('battery')"
               <% if (EchoController.contains("battery")) {
               %>disabled<%
                   }
               %>/>
        <input type="button" id="startSolarBtn" value="Start Solar" onclick="enable('solar')"
               <% if (EchoController.contains("solar")) {
               %>disabled<%
                   }
               %>/>
        <input type="button" id="startLightBtn" value="Start Light" onclick="enable('light')"
               <% if (EchoController.contains("light")) {
               %>disabled<%
                   }
               %>/>

        <br>
        <!--Stop-->
        <input type="button" id="stopEVBtn" value="Stop EV" onclick="disable('ev')"
               <% if (!EchoController.contains("ev")) {
               %>disabled<%
                   }
               %>
               />
        <input type="button" id="stopBatteryBtn" value="Stop Battery" onclick="disable('battery')"
               <% if (!EchoController.contains("battery")) {
               %>disabled<%
                   }
               %>
               />
        <input type="button" id="stopSolarBtn" value="Stop Solar" onclick="disable('solar')"
               <% if (!EchoController.contains("solar")) {
               %>disabled<%
                   }
               %>
               />
        <input type="button" id="stopLightBtn" value="Stop Light" onclick="disable('light')"
               <% if (!EchoController.contains("light")) {
               %>disabled<%
                   }
               %>
               />
    </body>
    <script>
        const INTERVAL_TIME = 15000;
        const TIME_OUT = 1000;
        const STATE_READY = 4;
        const STATUS_OK = 200;
        var EVStart = document.getElementById('startEVBtn');
        var EVStop = document.getElementById('stopEVBtn');
        var BatteryStart = document.getElementById('startBatteryBtn');
        var BatteryStop = document.getElementById('stopBatteryBtn');
        var SolarStart = document.getElementById('startSolarBtn');
        var SolarStop = document.getElementById('stopSolarBtn');
        var LightStart = document.getElementById('startLightBtn');
        var LightStop = document.getElementById('stopLightBtn');

        function update(device, enable) {

            // EV
            if (device === "ev") {
                if (enable) {
                    EVStart.disabled = true;
                    EVStop.disabled = false;
                } else {
                    EVStart.disabled = false;
                    EVStop.disabled = true;
                }
            }

            // Battery
            if (device === "battery") {
                if (enable) {
                    BatteryStart.disabled = true;
                    BatteryStop.disabled = false;
                } else {
                    BatteryStart.disabled = false;
                    BatteryStop.disabled = true;
                }
            }

            //            Solar
            if (device === "solar") {
                if (enable) {
                    SolarStart.disabled = true;
                    SolarStop.disabled = false;
                } else {
                    SolarStart.disabled = false;
                    SolarStop.disabled = true;
                }
            }

            //            Light
            if (device === "light") {
                if (enable) {
                    LightStart.disabled = true;
                    LightStop.disabled = false;
                } else {
                    LightStart.disabled = false;
                    LightStop.disabled = true;
                }
            }
        }

        /**
         * Stop 1 device
         * @@param {String} device
         */
        // Disable device
        function disable(device) {
            var xmlHttp = new XMLHttpRequest();
            xmlHttp.open("POST", "Stop", true);
            xmlHttp.onerror = (e) => {
                window.alert("Can not connect to server");
            };
            xmlHttp.onload = (e) => {
                if (xmlHttp.readyState === STATE_READY && xmlHttp.status === STATUS_OK) {
                    var revdata = xmlHttp.responseText;
                    if (revdata !== "success") {
                        console.log(revdata);
                        window.alert("Stop failed: " + revdata);
                    } else {
                        console.log("Success");
                        update(device, false);
                    }
                } else {
                    window.alert("Connection failed: " + xmlHttp.status);
                }
            };
            xmlHttp.send(device);
        }

        // Enable device
        function enable(device) {
            var xmlHttp = new XMLHttpRequest();
            xmlHttp.open("POST", "Start", true);
            xmlHttp.onerror = (e) => {
                window.alert("Can not connect to server");
            };
            xmlHttp.onload = (e) => {
                if (xmlHttp.readyState === STATE_READY && xmlHttp.status === STATUS_OK) {
                    var revdata = xmlHttp.responseText;
                    if (revdata !== "success") {
                        console.log(revdata);
                        window.alert("Start failed: " + revdata);
                    } else {
                        console.log("Success");
                        update(device, true);
                    }
                } else {
                    window.alert("Connection failed: " + xmlHttp.status);
                }
            };
            xmlHttp.send(device);
        }
    </script>
</html>
