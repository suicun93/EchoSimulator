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
        <link rel="stylesheet" href="../css/bulma.min.css">
        <link rel="stylesheet" href="../css/common.css">
        <link rel="stylesheet" href="../css/battery.css">
    </head>
    <body>
        <div class="bg-color">
            <section class="header">
                <div class="container">
                    <div></div>
                    <h1 class="head-title">Battery Consumption Configuration</h1>
                </div>
            </section>
            <section class="content">
                <div class="container">
                    <div class="columns">
                        <div class="main-menu column is-8 is-offset-2">

                            <form class="menu-form column is-6 is-offset-3" id="form">
                                <div class="field">
                                    <label class="label">Start Time</label>
                                    <div class="control">
                                        <input class="input" id="startTime" type="time" required>
                                    </div>
                                </div>
                                <div class="field">
                                    <label class="label">End Time</label>
                                    <div class="control">
                                        <input class="input" id="endTime" type="time" required>
                                    </div>
                                </div>
                                <div class="field">
                                    <label class="label">Mode</label>
                                    <div class="select">
                                        <select id="mode"> 
                                            <option value="0x42" selected="selected">Charge</option>
                                            <option value="0x41">Rapid Charge</option>
                                            <option value="0x44">Stand By</option>
                                            <option value="0x43">DisCharge</option>
                                            <select >

                                            </select> <br><br>
                                        </select>
                                    </div>
                                </div>
                                <div class="field">
                                    <label class="label">Instantaneous Electric Energy</label>
                                    <div class="control">
                                        <input class="input" type="number"  id="instantaneous" min="1" max="1000" placeholder="Unit: W" required>
                                    </div>
                                </div>
                                <div class="field is-grouped">
                                    <div class="control">
                                        <button class="button is-primary is-outlined" type="submit">Schedule</button>
                                    </div>
                                    <div class="control">
                                        <button class="button is-danger is-outlined" t onclick="window.location.href = '/EchoSimulator'">Cancel</button>
                                    </div>
                                </div>
                            </form>
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
        var startTimeInput = document.getElementById("startTime");
        var endTimeInput = document.getElementById("endTime");
        var modeInput = document.getElementById("mode");
        var instantaneousInput = document.getElementById("instantaneous");
        var form = document.getElementById("form");
        /**
         * @param {String} device 
         */

        form.onsubmit = function(){
            var startTime = startTimeInput.value;
            var endTime = endTimeInput.value;
            var mode = modeInput.options[modeInput.selectedIndex].value;
            var instantaneous = instantaneousInput.value;
            
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
