var timeInput = document.getElementById("time");
window.onload = loadTime();
let modal = $('#modal');
function loadTime() {
    //FF, Opera, Safari, Chrome
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open('POST', "Time", true);
    xmlHttp.setRequestHeader("Content-Type", "text/plain");
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
            cleanMsg();
            modal.append(responseMsg(FAIL_STATUS,"<p>Connection failed:</p>" + xmlHttp.status));
            closeMsg();
        }
    };
    xmlHttp.send('');
};

function setTime() {
    time = timeInput.value;
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open("POST", "Time", true);
    xmlHttp.onerror = (e) => {
        cleanMsg();
        modal.append(responseMsg(FAIL_STATUS,"<p>GET TIME:</p>"+"Can not connect to server"));
        closeMsg();
    };
    xmlHttp.onload = (e) => {
        if (xmlHttp.readyState === STATE_READY && xmlHttp.status === STATUS_OK) {
            var revdata = xmlHttp.responseText;
            if (revdata !== "success") {
                cleanMsg();
                modal.append(responseMsg(FAIL_STATUS,"<p>Set time failed:</p>  " + revdata));
                closeMsg();
            } else {
                cleanMsg();
                modal.append(responseMsg(SUCCESS_STATUS,"<p>Set time Successful:</p" + revdata));
                closeMsg();
            }
        } else {
            cleanMsg();
            modal.append(responseMsg(FAIL_STATUS,"<p>Connection failed:</p>" + xmlHttp.status));
            closeMsg();
        }
    };
    xmlHttp.send(time);
}
