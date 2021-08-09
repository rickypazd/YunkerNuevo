var myApp;
myApp = myApp || (function () {
    var pleaseWaitDiv = $('\
        <div class="modal hide" id="pleaseWaitDialog" data-backdrop="static" data-keyboard="false" style="display: block; z-index: 9999; background-color: #00000099;">\n\
             <div class="lds-hourglass"><h3>Cargando. Por favor espere...</h3>\n\
<h5>Si ocurrio algun error. Favor de recargar la pagina.</h5></div>\n\
        </div>');
    return {
        showPleaseWait: function () {
            pleaseWaitDiv.modal();
        },
        hidePleaseWait: function () {
            pleaseWaitDiv.modal('hide');
        }

    };
})();


function mostrar_progress() {
    myApp.showPleaseWait();
}
function cerrar_progress() {
    myApp.hidePleaseWait();
}


function cargarBuscardor() {

    var buscador = document.getElementById("in_buscar");
    if (buscador != null) {
        var scrollPosition = $(window).scrollTop();
        window.onscroll = function () {
            console.log("TOP: " + buscador.getBoundingClientRect().top);
            console.log("CONSTAN: " + buscador.classList.contains("busfixed"));
            if (buscador.getBoundingClientRect().top < 0 && !buscador.classList.contains("busfixed")) {
                buscador.classList.add("busfixed");
            } else {
                buscador.classList.remove("busfixed");
            }




        };

    }


}



//cargarBuscardor();