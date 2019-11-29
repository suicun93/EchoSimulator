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
        <link rel="stylesheet" href="css/bulma.min.css"/>
        <link rel="stylesheet" href="css/common.css"/>
        <link rel="stylesheet" href="css/simulator-page.css"/>
        <title>Simulator Page</title>
    </head>
    <body>
        <div class="bg-color">
            <section>
                <div class="container">
                    <!--                Time button-->
                    <button onclick="window.location.href = 'time.jsp'">
                        Time
                    </button>
                    <!--                Time button-->
                    <!--                Schedule EV button-->
                    <!--                Schedule EV button-->
                    <div class="content">
                        <h1>Simulator Controller:</h1>
                        <div class="main-menu-btn">
                            <div class="columns">
                                <div class="device columns column is-4 is-offset-2">
                                    <div class="column is-5">
                                        <img src="img/car.png" alt="Electric Vehicle">
                                    </div>
                                    <div class="device-group-btn column is-7">
                                        <button class ="button is-primary is-rounded" id="startEVBtn" onclick="enable('ev')"
                                                <% if (EchoController.contains("ev")) {
                                                %>disabled<%
                                                    }
                                                %>>Start EV</button>
                                        <button class ="button is-rounded is-danger" type="button" id="stopEVBtn" value="Stop EV" onclick="disable('ev')"
                                                <% if (!EchoController.contains("ev")) {
                                                %>disabled<%
                                                    }
                                                %>
                                                >Stop EV</button>
                                        <button class="button is-info">Configuration</button>
                                    </div>
                                </div>
                                <div class="device column columns is-4 is-offset-2">
                                    <div class="column is-5">
                                        <img src="img/battery.png" alt="Battery">
                                    </div>
                                    <div class="device-group-btn column is-7">
                                        <button class ="button is-primary is-rounded" id="startBatteryBtn" onclick="enable('battery')"
                                                <% if (EchoController.contains("battery")) {
                                                %>disabled<%
                                                    }
                                                %>>Start Battery</button>
                                        <button class ="button is-rounded is-danger " type="button" id="stopBatteryBtn" value="Stop Battery" onclick="disable('battery')"
                                                <% if (!EchoController.contains("battery")) {
                                                %>disabled<%
                                                    }
                                                %>
                                                >Stop Battery</button>
                                        <button class="button is-info" onclick="window.location.href = 'PowerConsumption/battery.jsp'">Configuration</button>
                                    </div>
                                </div>
                            </div>
                            <div class="columns">
                                <div class="device columns column is-4 is-offset-2">
                                    <div class="column is-5">
                                        <img src="img/sun.png" alt="Solar Energy">
                                    </div>
                                    <div class="device-group-btn column is-7">
                                        <button class ="button is-primary  is-rounded" id="startSolarBtn" onclick="enable('solar')"
                                                <% if (EchoController.contains("solar")) {
                                                %>disabled<%
                                                    }
                                                %>>Start Solar</button>
                                        <button class ="button is-rounded is-danger" type="button" id="stopSolarBtn" value="Stop Solar" onclick="disable('solar')"
                                                <% if (!EchoController.contains("solar")) {
                                                %>disabled<%
                                                    }
                                                %>
                                                >Stop Solar</button>
                                        <button class="button is-info">Configuration</button>
                                    </div>
                                </div>
                                <div class="device columns column is-4 is-offset-0 is-offset-2">
                                    <div class="column is-5">
                                        <img src="img/light.png" alt="Light">
                                    </div>
                                    <div class="device-group-btn column is-7">
                                        <button class ="button is-primary  is-rounded" id="startLightBtn" value="Start Light" onclick="enable('light')"
                                                <% if (EchoController.contains("light")) {
                                                %>disabled<%
                                                    }
                                                %>>Start Light</button>
                                        <button class ="button is-rounded is-danger " type="button" id="stopLightBtn" value="Stop Light" onclick="disable('light')"
                                                <% if (!EchoController.contains("light")) {
                                                %>disabled<%
                                                    }
                                                %>
                                                >Stop Light</button>
                                        <button class="button is-info">Configuration</button>
                                    </div>
                                </div>
                            </div>
                            <br>
                            <!--Stop-->
                        </div>
                    </div>
                </div>
            </section>
        </div>

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

        /**
         * Update UI
         * @@param {String} device
         * @@param {Boolean} enable
         */
        // Disable device
        function updateUI(device, enable) {

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
         * Disable 1 device
         * @@param {String} device
         */
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
                        updateUI(device, false);
                    }
                } else {
                    window.alert("Connection failed: " + xmlHttp.status);
                }
            };
            xmlHttp.send(device);
        }

        /**
         * Enable 1 device
         * @@param {String} device
         */
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
                        updateUI(device, true);
                    }
                } else {
                    window.alert("Connection failed: " + xmlHttp.status);
                }
            };
            xmlHttp.send(device);
        }
    </script>
</html>
