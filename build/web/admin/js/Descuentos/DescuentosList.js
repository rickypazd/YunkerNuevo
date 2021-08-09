/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var url = "adminController";
var cantidad = 10;
var pagina = 0;
$(document).ready(function () {
    Lista_repuesto();
    getDescuentoActual();
});
function buscar() {
    getCantidadMarcas();
}

function getDescuentoActual() {
    $.post("adminController", {evento: "getDescuentoActual", TokenAcceso: "servi12sis3"}, function (resp) {
        if (resp != null) {
            var obj = $.parseJSON(resp);
            if (obj.estado != "1") {
                alert(obj.mensaje);
            } else {
             document.getElementById("descuentoActual").innerHTML=obj.resp+" %";
            }
        }
    });
}
var enPedido = false;
function Lista_repuesto() {

    pagina = $("#list_marcas").data("pagina");

    var limite = $("#resultados").data("cantidad");
    if ((pagina * 10) > limite) {
        return;
    }
    if (enPedido) {
        return;
    }
    var busq = $("#in_buscar").val() || "";
    // mostrar_progress();
    enPedido = true;
    $("#img-carga").css("display", "");
    var busqueda = $("#in_buscar").val() || "";
    $.post(url, {evento: "getAll_pagination_usuarioDescuento", busqueda: busqueda, cantidad: 10, pagina: pagina, TokenAcceso: "servi12sis3"}, function (resp) {
        // cerrar_progress();
        enPedido = false;
        $("#img-carga").css("display", "none");
        if (resp != null) {
            var obj = $.parseJSON(resp);
            if (obj.estado != "1") {
                alert(obj.mensaje);
            } else {
                var arr = $.parseJSON(obj.resp);
                // $("#resultados").html("(" + arr.length + ")");
                $.each(arr, function (i, obj) {
                    var html = "<div data-obj='" + JSON.stringify(obj) + "' class='list-group-item list-group-item-action flex-column align-items-start'>";
                    html += "               <div class='row iten_repuesto_row'>";
                    html += "                     <div class='col-2'>";
                    html += "                         <img src='" + obj.url_foto + "' class='' height='60px' alt='Profile' />";
                    html += "                      </div>";
                    html += "                     <div class='col-2'>";
                    html += "                         <h6>" + obj.nombre + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-2'>";
                    html += "                         <h6>" + obj.apellido_pa + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-2'>";
                    html += "                         <h6>" + obj.apellido_ma + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-1'>";
                    html += "                         <h6>" + obj.usuario + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-2'>";
                    html += "                         <h6>" + obj.descuentos.fecha || "" + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-1'>";
                    html += "                         <h6>" + obj.descuentos.cantidad + " % </h6>";
                    html += "                     </div>";
                    html += "                 </div>";
                    html += "            </div>";
                    $("#list_marcas").append(html);
                });
                pagina++;
                $("#list_marcas").data("pagina", pagina);

                reloadVerify();

            }
        }

    });
}

window.onscroll = function () {
    var scrollHeight = $(document).height();
    var scrollPosition = $(window).height() + $(window).scrollTop();
    if ((scrollHeight - scrollPosition) < 1) {
        Lista_repuesto();
    }

};

function editar(itn) {
    var padre = $(itn).parent().parent().parent().parent();
    var data = $(padre).data("obj");

    window.location.href = "UsrEdit.html?id=" + data.id;

}
function eliminar(itn) {
    var padre = $(itn).parent().parent().parent().parent();
    itnSelect = padre;
    var data = $(padre).data("obj");
    $("#marName").html(data.nombre);
    $("#btn_eliminar").data("id", data.id);
    $("#confirm-delete").modal("show");
    // window.location.href = "UsrPerfil.html?id="+data.id;
    //  alert(data.id);
}
var itnSelect;
function ver(itn) {
    var padre = $(itn).parent().parent().parent().parent();
    var data = $(padre).data("obj");
    window.location.href = "UsrPerfil.html?id=" + data.id;
//    alert(data.id);
}

function ok_eliminar(itn) {
    var id = $(itn).data("id");
    mostrar_progress();
    $.post(url, {evento: "eliminar_usuario", id: id, TokenAcceso: "servi12sis3"}, function (resp) {
        cerrar_progress();
        if (resp != null) {
            var obj = $.parseJSON(resp);
            if (obj.estado != "1") {
                alert(obj.mensaje);
            } else {
                var arr = parseFloat(obj.resp);
                $("#confirm-delete").modal("toggle");
                $(itnSelect).css("display", "none");

                // alert(arr);

            }
        }

    });
}