var startTimeInput = document.getElementById("startTime");
var startTimeMess = document.getElementById("start-time-invalid-mess");
var endTimeInput = document.getElementById("endTime");
var endTimeMess = document.getElementById("end-time-invalid-mess");
var modeInput = document.getElementById("mode");
var instantaneousInput = document.getElementById("instantaneous");
var instantaneousMess = document.getElementById("instantaneous-value-mess");
var successMsg = document.getElementById("success-msg");
var failedMsg = document.getElementById("failed-msg");
let modal = $('.msg-wrapper');
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
    } else {
        endTimeInput.className = ("input");
        endTimeMess.style.display = "none";
    }

    if (instantaneous === "" || instantaneous <= 0) {
        instantaneousInput.classList.add("is-danger");
        instantaneousMess.style.display = "block";
        return;
    } else {
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
                modal.append(responseMsg(SUCCESS_STATUS, "<p> Set time Success</p>  " ));
                closeMsg();
            } else {
                modal.append(responseMsg(FAIL_STATUS, "<p> Set time failed:</p>  " + data));
                closeMsg();
            }
        } else {
            modal.append(responseMsg(FAIL_STATUS, "<p> Connection failed:</p>  " + xmlHttp.status));
            closeMsg();
        }
    };
    xmlHttp.send(device + "," + startTime + "," + endTime + "," + mode + "," + instantaneous);
}