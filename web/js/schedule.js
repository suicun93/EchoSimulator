const INTERVAL_TIME = 15000;
const TIME_OUT = 1000;
const STATE_READY = 4;
const STATUS_OK = 200;
var startTimeInput = document.getElementById("startTime");
var startTimeMess = document.getElementById("start-time-invalid-mess");
var endTimeInput = document.getElementById("endTime");
var endTimeMess = document.getElementById("end-time-invalid-mess");
var modeInput = document.getElementById("mode");
var instantaneousInput = document.getElementById("instantaneous");
var instantaneousMess = document.getElementById("instantaneous-value-mess");
var successMsg = document.getElementById("success-msg");
var failedMsg = document.getElementById("failed-msg");

/**
 * @param {String} device 
 */
function schedule(device) {
    var startTime = startTimeInput.value;
    var endTime = endTimeInput.value;
    var mode = modeInput.options[modeInput.selectedIndex].value;
    var instantaneous = instantaneousInput.value;
    
    if (startTime === "") {
        startTimeInput.classList.add("is-danger");
        startTimeMess.style.display = "block";
        return;
    } else {
        startTimeInput.className = "input";
        startTimeMess.style.display = "none";
    }
    if (endTime === "") {
        endTimeInput.classList.add("is-danger");
        endTimeMess.style.display = "block";
        return;
    }else{
        endTimeInput.className = ("input");
        endTimeMess.style.display = "none";
    }

    if (instantaneous === "" || instantaneous <= 0) {
        instantaneousInput.classList.add("is-danger");
        instantaneousMess.style.display = "block";
        return;
    }else{
        instantaneousInput.className = ("input");
        instantaneousMess.style.display = "none";
    }

    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open('POST', "../Schedule", true);
    xmlHttp.setRequestHeader("Content-Type", "text/html");
    xmlHttp.onload = () => {
        if (xmlHttp.readyState === STATE_READY && xmlHttp.status === STATUS_OK) {
            var data = xmlHttp.responseText;
            if (data === "success") {
                successMsg.style.display = "block";
            } else {
                failedMsg.innerHTML = "Schedule set failed: \n "+data;
                failedMsg.style.display = "block";
            }
        } else {
            window.alert("Connection failed: " + xmlHttp.status);
        }
    };
    xmlHttp.send(device + "," + startTime + "," + endTime + "," + mode + "," + instantaneous);
}