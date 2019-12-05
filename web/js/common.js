var hasMsg = false;
function responseMsg(type, content) {
    cleanMsg();
    hasMsg = true;
    return "<div class=\"notification is-" + (type == SUCCESS_STATUS ? "primary" : "danger") + "\" id=\"response-msg\" ><button class=\"delete\"></button>" + content + "</div>";
}
function cleanMsg(e) {
    $("#response-msg").remove();
    hasMsg = false;
}
function closeMsg() {
    $(".delete").click(cleanMsg);
}

const INTERVAL_TIME = 15000;
const TIME_OUT = 1000;
const STATE_READY = 4;
const STATUS_OK = 200;
const SUCCESS_STATUS = "success";
const FAIL_STATUS = "failed";
const ON_STATUS = "ON";
const OFF_STATUS = "OFF";
