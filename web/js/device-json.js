var devices = "";
var ev = null;
var EV_CURRENT_STATUS = "";
var battery = null;
var BATT_CURRENT_STATUS = "";
var solar = null;
var SOLAR_CURRENT_STATUS = "";
var light = null;
var LIGHT_CURRENT_STATUS = "";

getAvailableItem();
if(isDisplay){
    setInterval("getAvailableItem()",5400);
}
function getAvailableItem() {
    $.ajax({
        type: "POST",
        url: "GetAllItems",
        data: "",
        dataType: "json",
        success: function (response) {
            devices = response.devices;
            console.log(response);
            if (!isDisplay) {
                deviceInit();
            } else {
                initDevice();
            }
        },
        error: function (response) {
            console.log(response);
        }

    });
}