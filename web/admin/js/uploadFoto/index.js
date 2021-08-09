/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function init() {
    var btnSubir = document.getElementById("btnSubirFoto");
    btnSubir.parentNode.style.position = "relative";
//        btnSubir.style.background="#000";
    btnSubir.style.position = "absolute";
    btnSubir.style.width = "100%";
    btnSubir.style.height = "100%";
    btnSubir.style.top = "0";
    btnSubir.style.left = "0";
    btnSubir.addEventListener('click', function (evt) {
        document.getElementById("imputFoto").click();
    });

    var form = document.createElement("FORM");
    form.name = "fotoForm";
    form.enctype = "multipart/form-data";
    form.method = "POST";
    form.id = "fotoForm";
//        form.action = "";

    var imputEvento = addHiddeImput({
        type: "text",
        name: "evento",
        value: btnSubir.dataset.evento
    })
    form.appendChild(imputEvento);

    form.appendChild(addHiddeImput({
        type: "text",
        name: "id",
        value: btnSubir.dataset.id,
        id: "inputId",
    }));
    var imputToken = addHiddeImput({
        type: "text",
        name: "TokenAcceso",
        value: "servi12sis3"
    })
    form.appendChild(imputToken);

    var imputFoto = addHiddeImput({
        type: "FILE",
        name: "archibo",
        id: "imputFoto"
    })
    imputFoto.addEventListener('change', function (evt) {
        if (evt.target.value) {
            document.getElementById("inputId").value = btnSubir.dataset.id
            var body = new FormData(document.getElementById("fotoForm"));
            var myInit = {
                method: 'POST',
                body: body,
                mode: 'no-cors',
            };
            var myRequest = new Request(btnSubir.dataset.controller, myInit);
            fetch(myRequest)
                    .then(function (response) {
                        window.location.reload();
                    }).catch(error => {
                alert("ERROR")
            })
        }
    })

    form.appendChild(imputFoto);
    btnSubir.appendChild(form);



}


function addHiddeImput(data) {
    var imput = document.createElement("INPUT");
    imput.type = data.type;
    imput.name = data.name;
    imput.id = data.id;
    if (data.value) {
        imput.value = data.value;
    }
    imput.style.display = "none";
    return imput;
}

init();