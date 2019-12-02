var closeMsg = document.getElementsByClassName("delete");
for (let i = 0; i < closeMsg.length; i++) {
    let object = closeMsg[i];
    object.onclick = function () {
        object.parentElement.style.display = "none";
    };
}