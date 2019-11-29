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
                            <div class="menu-form column is-6 is-offset-3">
                                <div class="field">
                                    <label class="label">Start Time</label>
                                    <div class="control">
                                        <input class="input" id="startTime" type="time" required>
                                    </div>
                                    <p class="help is-danger" id="start-time-invalid-mess" style="display: none">This value is required!</p>
                                </div>
                                <div class="field">
                                    <label class="label">End Time</label>
                                    <div class="control">
                                        <input class="input" id="endTime" type="time" required>
                                    </div>
                                    <p class="help is-danger" id="end-time-invalid-mess" style="display: none">This value is required!</p>
                                </div>
                                <div class="field">
                                    <label class="label">Mode</label>
                                    <div class="select">
                                        <select id="mode"> 
                                            <option value="0x42" selected="selected">Charge</option>
                                            <option value="0x41">Rapid Charge</option>
                                            <option value="0x44">Stand By</option>
                                            <select >
                                            </select> <br><br>
                                        </select>
                                    </div>
                                </div>
                                <div class="field">
                                    <label class="label">Instantaneous Electric Energy</label>
                                    <div class="control">
                                        <input class="input" type="number"  id="instantaneous" min="1" max="999999999" placeholder="Unit: W" required>
                                    </div>
                                    <p class="help is-danger" id="instantaneous-value-mess" style="display: none">This value is required and must be greater than 0 and smaller than 999999999</p>
                                </div>
                                <div class="field is-grouped">
                                    <div class="control">
                                        <button class="button is-primary is-outlined" onclick="schedule('battery')">Schedule</button>
                                    </div>
                                    <div class="control">
                                        <button class="button is-danger is-outlined" t onclick="window.location.href = '/EchoSimulator'">Cancel</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </body>
    <script src="../js/schedule.js">
    </script>
</html>
