"use strict"

const regPath = document.querySelector(".registerbutton");
const logPath = document.querySelector(".loginbutton");
const form = document.querySelector(".formtocustom");
const inputUser = document.querySelector("#username-a30d");
const alert = document.querySelector(".alert");

inputUser.onchange = function () {
    const username = inputUser.value;
    fetch('/getUsers')
        .then((response) => {
            response.json().then((data) => {
                if (data.indexOf(username) > -1 && inputUser.value !== ""){
                    alert.classList.remove("hidden");
                    regPath.classList.add("disabled");
                }
                else {
                    alert.classList.add("hidden");
                    regPath.classList.remove("disabled");
                    logPath.classList.remove("disabled");
                }
            });
        })
};

regPath.addEventListener('click', (e) => {
    e.preventDefault();
    document.form.action = regPath.attributes.href.value
})
logPath.addEventListener('click', () => {
    document.form.action = logPath.attributes.href.value
})




