<%-- 
    Document   : ev
    Created on : Nov 26, 2019, 4:30:19 PM
    Author     : hoang-trung-duc
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!--<script src="https://kit.fontawesome.com/594a36984d.js" crossorigin="anonymous"></script>-->
        <link rel="stylesheet" href="../css/bulma.min.css">
        <link rel="stylesheet" href="../css/common.css">
        <link rel="stylesheet" href="../css/battery.css">
        <title>EV　Consumption Schedule</title>
    </head>
    <body>
        <div class="bg-color">
            <section class="header">
                <div class="container">
                    <div></div>
                    <h1 class="head-title">EVの消費電力設定</h1>
                </div>
            </section>
            <section class="content">
                <div class="container">
                    <div class="columns">
                        <div class="main-menu column is-8 is-offset-2 msg-wrapper">
                            <div class="menu-form column is-6 is-offset-3">
                                <div class="field">
                                    <label class="label">始まる時間</label>
                                    <div class="control">
                                        <input class="input" id="startTime" type="time" required>
                                    </div>
                                    <p class="help is-danger" id="start-time-invalid-mess" style="display: none">This value is required!</p>
                                </div>
                                <div class="field">
                                    <label class="label">終了時間</label>
                                    <div class="control">
                                        <input class="input" id="endTime" type="time" required>
                                    </div>
                                    <p class="help is-danger" id="end-time-invalid-mess" style="display: none">This value is required!</p>
                                </div>
                                <div class="field">
                                    <label class="label">モード</label>
                                    <div class="select">
                                        <select id="mode"> 
                                            <option value="0x42" selected="selected">充電</option>
                                            <option value="0x41">急速充電</option>
                                            <option value="0x43">放電</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="field">
                                    <label class="label">瞬時値電力量</label>
                                    <div class="control">
                                        <input class="input" type="number"  id="instantaneous" min="1" max="999999999" placeholder="Unit: W" required>
                                    </div>
                                    <p class="help is-danger" id="instantaneous-value-mess" style="display: none">This value is required and must be greater than 0 and smaller than 999999999</p>
                                </div>
                                <div class="field is-grouped">
                                    <div class="control">
                                        <button class="button is-primary is-outlined" onclick="schedule('ev')">設定</button>
                                    </div>
                                    <div class="control">
                                        <button class="button is-danger is-outlined" t onclick="window.location.href = '/Simulator'">戻る</button>
                                    </div>
                                </div>
                                <div class="notification is-primary" id="success-msg" style="display: none">
                                    <button class="delete"></button>
                                    スケジュールを正常に設定しました
                                </div>
                                <div class="notification is-danger" id="failed-msg" style="display: none">
                                    <button class="delete"></button>
                                    スケジュールに失敗しました！
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </body>
    <script src="../js/jquery.js"></script>

    <script src="../js/common.js"></script>
    <script src="../js/schedule.js">
    </script>
</html>
