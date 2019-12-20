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
        <link rel="stylesheet" href="css/bootstrap-datetimepicker.min.css">
        <link rel="stylesheet" href="css/common.css"/>
        <link rel="stylesheet" href="css/simulator-page.css"/>
        <title>Simulator Page</title>
    </head>
    <body>
        <div class="bg-color">
            <section>
                <div class="container">
                    <div class="content">
                        <h1>シミュレーターのコントローラー:</h1>
                        <div class="main-menu-btn">
                            <div class="set-time-wrapper">
                                <input size="16" type="text" readonly
                                       class="form_datetime input is-rounded is-primary" id="time">
                                <button class="button is-primary " onclick="setTime()">
                                    設定
                                </button>
                            </div>
                            <div class="msg-wrapper">
                            </div>
                            <div class="columns">
                                <div class="device columns column is-4 is-offset-2">
                                    <div class="column is-5">
                                        <img src="img/car.png" alt="Electric Vehicle">
                                    </div>
                                    <div class="device-group-btn column is-7">
                                        <button class ="button is-success is-rounded" id="startEVBtn" onclick="enable('ev')"
                                                <% if (EchoController.contains("ev")) {
                                                %>disabled<%
                                                    }
                                                %>>EV起動</button>
                                        <button class ="button is-rounded is-warning" type="button" id="stopEVBtn" value="Stop EV" onclick="disable('ev')"
                                                <% if (!EchoController.contains("ev")) {
                                                %>disabled<%
                                                    }
                                                %>
                                                >EV構成</button>
                                        <button class="config-btn button is-info" onclick="window.location.href = 'PowerConsumption/ev.jsp'" id="ev-config-btn"
                                                <% if (!EchoController.contains("ev")) {
                                                %>disabled<%
                                                    }
                                                %>>EV構成</button>
                                    </div>
                                </div>
                                <div class="device column columns is-4 is-offset-2">
                                    <div class="column is-5">
                                        <img src="img/battery.png" alt="Battery">
                                    </div>
                                    <div class="device-group-btn column is-7">
                                        <button class ="button is-success is-rounded" id="startBatteryBtn" onclick="enable('battery')"
                                                <% if (EchoController.contains("battery")) {
                                                %>disabled<%
                                                    }
                                                %>>蓄電池起動</button>
                                        <button class ="button is-rounded is-warning " type="button" id="stopBatteryBtn" value="Stop Battery" onclick="disable('battery')"
                                                <% if (!EchoController.contains("battery")) {
                                                %>disabled<%
                                                    }
                                                %>
                                                >蓄電池消し</button>
                                        <button class="config-btn button is-info" onclick="window.location.href = 'PowerConsumption/battery.jsp'" id="batt-config-btn"
                                                <% if (!EchoController.contains("battery")) {
                                                %>disabled<%
                                                    }
                                                %>>蓄電池構成</button>
                                    </div>
                                </div>
                            </div>
                            <div class="columns">
                                <div class="device columns column is-4 is-offset-2">
                                    <div class="column is-5">
                                        <img src="img/sun.png" alt="Solar Energy">
                                    </div>
                                    <div class="device-group-btn column is-7">
                                        <button class ="button is-success  is-rounded" id="startSolarBtn" onclick="enable('solar')"
                                                <% if (EchoController.contains("solar")) {
                                                %>disabled<%
                                                    }
                                                %>>ソーラー起動</button>
                                        <button class ="button is-rounded is-warning" type="button" id="stopSolarBtn" value="Stop Solar" onclick="disable('solar')"
                                                <% if (!EchoController.contains("solar")) {
                                                %>disabled<%
                                                    }
                                                %>
                                                >ソーラー消し</button>
                                        <button class="config-btn button is-info" onclick="window.location.href = 'PowerConsumption/solar.jsp'" id="solar-config-btn"
                                                <% if (!EchoController.contains("solar")) {
                                                %>disabled<%
                                                    }
                                                %>
                                                >ソーラー構成</button>
                                    </div>
                                </div>
                                <div class="device columns column is-4 is-offset-0 is-offset-2">
                                    <div class="column is-5">
                                        <img src="img/light.png" alt="Light">
                                    </div>
                                    <div class="device-group-btn column is-7">
                                        <button class ="button is-success  is-rounded" id="startLightBtn" value="Start Light" onclick="enable('light')"
                                                <% if (EchoController.contains("light")) {
                                                %>disabled<%
                                                    }
                                                %>>照明起動</button>
                                        <button class ="button is-rounded is-warning " type="button" id="stopLightBtn" value="Stop Light" onclick="disable('light')"
                                                <% if (!EchoController.contains("light")) {
                                                %>disabled<%
                                                    }
                                                %>
                                                >照明消し</button>
                                        <button class="config-btn button is-info is-rounded"  id="light-switch" onclick="switchLight()"
                                                <% if (!EchoController.contains("light")) {
                                                %>disabled<%
                                                    }
                                                %> 
                                                >ON/OFF</button>
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
    <script src="js/jquery.js"></script>
    <script src="js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript">
                                            $(".form_datetime").datetimepicker({format: 'yyyy-mm-dd hh:ii'});
    </script>
    <script src="js/common.js"></script>
    <script src="js/time.js"></script>

    <script>
                                            var EVStart = document.getElementById('startEVBtn');
                                            var EVStop = document.getElementById('stopEVBtn');
                                            var evConfigBtn = document.getElementById('ev-config-btn')
                                            var BatteryStart = document.getElementById('startBatteryBtn');
                                            var BatteryStop = document.getElementById('stopBatteryBtn');
                                            var battConfigBtn = document.getElementById('batt-config-btn')
                                            var SolarStart = document.getElementById('startSolarBtn');
                                            var SolarStop = document.getElementById('stopSolarBtn');
                                            var SolarConfigBtn = document.getElementById("solar-config-btn");
                                            var LightStart = document.getElementById('startLightBtn');
                                            var LightStop = document.getElementById('stopLightBtn');
                                            var LightSwitch = document.getElementById('light-switch');
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
                                                        evConfigBtn.disabled = false;
                                                    } else {
                                                        EVStart.disabled = false;
                                                        EVStop.disabled = true;
                                                        evConfigBtn.disabled = true;
                                                    }
                                                }

                                                // Battery
                                                if (device === "battery") {
                                                    if (enable) {
                                                        BatteryStart.disabled = true;
                                                        BatteryStop.disabled = false;
                                                        battConfigBtn.disabled = false;
                                                    } else {
                                                        BatteryStart.disabled = false;
                                                        BatteryStop.disabled = true;
                                                        battConfigBtn.disabled = true;
                                                    }
                                                }

                                                //            Solar
                                                if (device === "solar") {
                                                    if (enable) {
                                                        SolarStart.disabled = true;
                                                        SolarStop.disabled = false;
                                                        SolarConfigBtn.disabled = false;
                                                    } else {
                                                        SolarStart.disabled = false;
                                                        SolarStop.disabled = true;
                                                        SolarConfigBtn.disabled = true;
                                                    }
                                                }

                                                //            Light
                                                if (device === "light") {
                                                    if (enable) {
                                                        LightStart.disabled = true;
                                                        LightStop.disabled = false;
                                                        LightSwitch.disabled = false;
                                                    } else {
                                                        LightStart.disabled = false;
                                                        LightStop.disabled = true;
                                                        LightSwitch.disabled = true;
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
                                            function switchLight() {
                                                $.ajax({
                                                    type: "POST",
                                                    url: "SwitchLight",
                                                    data: "",
                                                    success: function (response) {
                                                        console.log(response.text);
                                                    },
                                                    error: function (response) {
                                                        console.log(response.text);
                                                    }
                                                });
                                            }
    </script>
</html>
