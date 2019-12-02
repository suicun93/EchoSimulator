const INTERVAL_TIME = 15000;
const TIME_OUT = 1000;
const STATE_READY = 4;
const STATUS_OK = 200;
var timeInput = document.getElementById("time");
var successMsg = document.getElementById("success-msg");
var failedMsg = document.getElementById("failed-msg");
window.onload = loadTime();

function loadTime() {
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
            timeInput.value =
                    p[2] + '-' + month + '-' + day + " " + p[3].substr(0, 5);
        } else {
            window.alert("Connection failed: " + xmlHttp.status);
        }
    };
    xmlHttp.send('');
}
;
function setTime() {
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
                failedMsg.innerHTML = "Set time failed: \n "+revdata;
                failedMsg.style.display = "block";
            } else {
                successMsg.style.display = "block";
            }
        } else {
            window.alert("Connection failed: " + xmlHttp.status);
        }
    };
    xmlHttp.send(time);
}
