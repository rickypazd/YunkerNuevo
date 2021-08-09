/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var url = "autosController";
var cantidad = 10;
var pagina = 0;
$(document).ready(function () {
    $("#list_autos").data("pagina", 0);
    pagina = 0;
    $("#list_autos").html("");
    Lista_repuesto();

});
var tiempo = new Date().getTime();
function buscar() {
    var timp = new Date().getTime() - tiempo;
    if (timp > 400) {
        tiempo = new Date().getTime();
        $("#list_autos").data("pagina", 0);
        pagina = 0;
        $("#list_autos").html("");
        Lista_repuesto();
    }
}


var enPedido = false;
function Lista_repuesto() {

    pagina = $("#list_autos").data("pagina");

    var limite = $("#resultados").data("cantidad");
    if ((pagina * 10) >= limite) {
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
    $.post(url, {evento: "getAll_rep_auto_paginationJSON", busqueda: busqueda, cantidad: 10, pagina: pagina, TokenAcceso: "servi12sis3"}, function (resp) {
        // cerrar_progress();
        enPedido = false;
        $("#img-carga").css("display", "none");
        if (resp != null) {
            var obj = $.parseJSON(resp);
            if (obj.estado != "1") {
                alert(obj.mensaje);
            } else {
                var json = $.parseJSON(obj.resp);
                var arr = json.arrayjson;
                $("#resultados").html("(" + json.count + ")");

                if (json.count == 0) {
                    return;
                }
                $("#resultados").data("cantidad", json.count);
                $.each(arr, function (i, obj) {
                    obj = obj.to_json;
                    var html = "<div data-obj='" + JSON.stringify(obj) + "' class='list-group-item list-group-item-action flex-column align-items-start'>";
                    html += "               <div class='row iten_repuesto_row'>";
                    html += "                     <div class='col-1'>";
                    html += "                         <h6>" + obj.clave + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-1'>";
                    html += "                         <img src='" + PARAMS.url_image+obj.url_foto_marca + "' class='' height='60px' alt='' />";
                    html += "                      </div>";
                    html += "                     <div class='col-2'>";
                    html += "                         <h6>" + obj.nombre_marca + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-1'>";
                    html += "                         <img src='" + PARAMS.url_image+obj.url_foto_modelo + "' class='' height='60px' alt='' />";
                    html += "                      </div>";
                    html += "                     <div class='col-2'>";
                    html += "                         <h6>" + obj.nombre_modelo + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-1'>";
                    html += "                         <h6>" + obj.anho + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-2'>";
                    html += "                         <h6>" + obj.nombre_version + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-2'>";
                    html += "                   <div class='col-12 d-flex justify-content-center p-0' style='text-align: center;'>";
//                    html += "                        <div class='col-4 btnAction btnEditar' onclick='editar(this);'><img src='img/lapiz.svg' alt=''/><p>Editar</p></div>";
                    html += "                        <div class='col-4 btnAction btnEliminar' onclick='eliminar(this);'>  <img src='img/bote-de-basura.svg' alt=''/><p>Eliminar</p></div>";
//                    html += "                        <div class='col-4 btnAction btnVer' onclick='ver(this);'>  <img src='img/cheuron.svg' alt=''/><p>Ver</p></div>";
                    html += "                    </div>";
                    html += "                     </div>";
                    html += "                 </div>";
                    html += "            </div>";
                    $("#list_autos").append(html);
                });
                pagina++;
                $("#list_autos").data("pagina", pagina);

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

    window.location.href = "VehMarcaEdit.html?id=" + data.id;

}
function eliminar(itn) {
    var padre = $(itn).parent().parent().parent().parent();
    itnSelect = padre;
    var data = $(padre).data("obj");
    $("#marName").html(data.clave);
    $("#btn_eliminar").data("id", data.id);
    $("#confirm-delete").modal("show");

    //  alert(data.id);
}
var itnSelect;
function ver(itn) {
    var padre = $(itn).parent().parent().parent().parent();
    var data = $(padre).data("obj");
    window.location.href = "VehMarcaPerfil.html?id=" + data.id;

}

function ok_eliminar(itn) {
    var id = $(itn).data("id");
    mostrar_progress();
    $.post(url, {evento: "eliminar_rep_auto_marca", id: id, TokenAcceso: "servi12sis3"}, function (resp) {
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