/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function initDevice() {
    var solarElectricAmount = 0;
    var evElectricAmount = 0;
    var batteryElectricAmount = 0;

    if (devices.hasOwnProperty("ev")) {
        let ev = devices.ev;
        if (ev.status == ON_STATUS && (ev.mode == CHARGE_MODE || ev.mode == DISCHARGE_MODE || ev.mode == RAPID_CHARGE_MODE)) {
            $(".device-ev").html(deviceElem("ev", ev.name, "car", ev.d3 + " W", ev.e4, ev.e2 + " Wh", ev.mode));
            if (ev.mode == CHARGE_MODE || ev.mode == RAPID_CHARGE_MODE) {
                evElectricAmount = 0 - ev.d3;
            } else if (ev.mode == DISCHARGE_MODE) {
                evElectricAmount = ev.d3;
            }
        } else {
            $(".device-ev").html(deviceElem("ev", ev.name, "car", 0 + " W", ev.e4, ev.e2 + " Wh", ""));
        }
    } else {
        $(".device-ev").html("");
    }
    if (devices.hasOwnProperty("battery")) {
        let batt = devices.battery;
        if (batt.status == ON_STATUS && (batt.mode == CHARGE_MODE || batt.mode == DISCHARGE_MODE || batt.mode == RAPID_CHARGE_MODE)) {
            $(".device-batt").html(deviceElem("battery", batt.name, "battery", batt.d3 + " W", batt.e4, batt.e2 + " Wh", batt.mode));
            if (batt.mode == CHARGE_MODE || batt.mode == RAPID_CHARGE_MODE) {
                batteryElectricAmount = 0 - batt.d3;
            } else if (batt.mode == DISCHARGE_MODE) {
                batteryElectricAmount = batt.d3;
            }
        } else {
            $(".device-batt").html(deviceElem("battery", batt.name, "battery", 0 + " W", batt.e4, batt.e2 + " Wh", ""));
        }
    } else {
        $(".device-batt").html("");
    }
    if (devices.hasOwnProperty("solar")) {
        let solar = devices.solar;
        if (solar.status == ON_STATUS) {
            solarElectricAmount = solar.e0;
            $(".device-solar").html(deviceElem("solar", solar.name, "sun", solar.e0 + " W", 0, solar.e1 + " Wh", CHARGE_MODE));
        } else {
            $(".device-solar").html(deviceElem("solar", solar.name, "sun", 0 + " W", 0, solar.e1 + " Wh", ""));
        }
    } else {
        $(".device-solar").html("");
    }
    if (devices.hasOwnProperty("light")) {
        let light = devices.light;
        $(".device-light").html(deviceElem("light", light.name, "light", light.status, 0, 0 + " Wh", ""));
    } else {
        $(".device-light").html("");
    }
    $("#sum-up").html(sumUp() + " W");

    function deviceElem(device, name, img, instValue, remain, mesureValue, mode) {
        let strMode = "";
        switch (mode) {
            case CHARGE_MODE:
            case RAPID_CHARGE_MODE:
                strMode = "charging";
                break;
            case DISCHARGE_MODE:
                strMode = "discharging";
                break;
            default:
                strMode = "";
        }

        if (device == "solar") {
            return "<div class='" + device + "-" + strMode + "'>" +
                    "<div class='delay1'></div>" +
                    "<div class='delay2'></div>" +
                    "<div class='delay3'></div>" +
                    "<div class='delay4'></div>" +
                    "</div>" +
                    "<div class='device'>" +
                    "<div class='device-img-wrapper'>" +
                    "<img src='img/" + img + ".png' alt='" + name + "'>" +
                    "</div>" +
                    "<div class='device-info-wrapper'>" +
                    "<h4>" + name + "</h4>"
                    +
                    `<div>
                        <label for="instant-value">瞬時値の電力量:</label>
                        <h5>${instValue}</h5>
                    </div>
                    <div>
                        <label for="instant-value">現在の電力量:</label>
                        <h5>${mesureValue}</h5>
                    </div>`
                    +
                    "</div>" +
                    "</div>";
        } else if (device == "light") {
            return   "<div class='" + device + "-" + strMode + "'>" +
                    "<div class='delay1'></div>" +
                    "<div class='delay2'></div>" +
                    "<div class='delay3'></div>" +
                    "<div class='delay4'></div>" +
                    "</div>" +
                    "<div class='device'>" +
                    "<div class='device-img-wrapper'>" +
                    "<img src='img/" + img + ".png' alt='" + name + "'>" +
                    "</div>" +
                    "<div class='device-info-wrapper'>" +
                    "<h4>" + name + "</h4>"
                    +
                    `<h2>${instValue}</h2>`
                    +
                    "</div>" +
                    "</div>";

        } else {
            return "<div class='device'>" +
                    "<div class='device-img-wrapper'>" +
                    "<img src='img/" + img + ".png' alt='" + name + "'>" +
                    "<span class='remain-energy'>" + remain + "%</span>" +
                    "</div>" +
                    "<div class='device-info-wrapper'>" +
                    "<h4>" + name + "</h4>" +
                    `<div>
                    <label for="instant-value">瞬時値の電力量:</label>
                    <h5>${instValue}</h5>
                </div>
                <div>
                    <label for="instant-value">現在の電力量:</label>
                    <h5>${mesureValue}</h5>
                </div>` +
                    "</div>" +
                    "</div>" +
                    "<div class='" + device + "-" + strMode + "'>" +
                    "<div class='delay1'></div>" +
                    "<div class='delay2'></div>" +
                    "<div class='delay3'></div>" +
                    "<div class='delay4'></div>" +
                    "</div>";
        }
    }
    function sumUp() {
        return parseInt(solarElectricAmount) + parseInt(evElectricAmount) + parseInt(batteryElectricAmount);
    }
}
