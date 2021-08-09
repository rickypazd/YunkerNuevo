/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var url = "autosController";
$(document).ready(function () {
    var id = getQueryVariable("id");

    $("#editarPerfil").attr("href", "VehMarcaEdit.html?id=" + id);
    mostrar_progress();
    $.post(url, {TokenAcceso: "servi12sis3", evento: "getById_rep_auto_marca", id: id}, function (resp) {
        cerrar_progress();
        if (resp != null) {
            var objec = $.parseJSON(resp);
            if (objec.estado != "1") {
                alert(objec.mensaje);
            } else {
                var obj = $.parseJSON(objec.resp);


                $("#nombre_perfil").html(obj.nombre);
                $("#foto_perfil").attr("src", PARAMS.url_image+obj.url_foto);
                Lista_repuesto();


            }
        }

    });



});

function Lista_repuesto() {

    var id = getQueryVariable("id");
    $.post(url, {evento: "get_rep_auto_modelo_by_id_rep_auto_marca", id: id, TokenAcceso: "servi12sis3"}, function (resp) {
        // cerrar_progress();
        $("#img-carga").css("display", "none");
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
                    html += "                     <div class='col-3'>";
                    html += "                         <img src='" +PARAMS.url_image+ obj.url_foto + "' class='' height='60px' alt='' />";
                    html += "                      </div>";
                    html += "                     <div class='col-5'>";
                    html += "                         <h6>" + obj.nombre + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-4'>";
                    html += "                   <div class='col-12  d-flex justify-content-center p-0' style='text-align: center;'>";
                    html += "                        <div class='col-4 btnAction btnEditar' onclick='editar(this);'><img src='img/lapiz.svg' alt=''/><p>Editar</p></div>";
                    html += "                        <div class='col-4 btnAction btnEliminar' onclick='eliminar(this);'>  <img src='img/bote-de-basura.svg' alt=''/><p>Eliminar</p></div>";
                    html += "                        <div class='col-4 btnAction btnVer' onclick='ver(this);'>  <img src='img/cheuron.svg' alt=''/><p>Ver</p></div>";
                    html += "                    </div>";
                    html += "                     </div>";
                    html += "                 </div>";
                    html += "            </div>";
                    $("#list_marcas").append(html);
                });


            }
        }

    });
}

function agregar_modelo() {
    var id = getQueryVariable("id");
    window.location.href = "VehModeloCreate.html?id=" + id;
}
function getQueryVariable(varia) {
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (pair[0] == varia) {
            return pair[1];
        }
    }
    return (false);
}

function ver_conductor(id) {
    window.location.href = "VehiculoPerf.html?id=" + id;
}

function editar(itn) {
    var padre = $(itn).parent().parent().parent().parent();
    var data = $(padre).data("obj");

    window.location.href = "VehModeloEdit.html?id=" + data.id;

}
function eliminar(itn) {
    var padre = $(itn).parent().parent().parent().parent();
    itnSelect = padre;
    var data = $(padre).data("obj");
    $("#marName").html(data.nombre);
    $("#btn_eliminar").data("id", data.id);
    $("#confirm-delete").modal("show");
    //  alert(data.id);
}
function ver(itn) {
    var padre = $(itn).parent().parent().parent().parent();
    var data = $(padre).data("obj");
    window.location.href = "VehModeloPerfil.html?id=" + data.id;
}

var itnSelect;
function ok_eliminar(itn) {
    var id = $(itn).data("id");
    mostrar_progress();
    $.post(url, {evento: "eliminar_rep_auto_modelo", id: id, TokenAcceso: "servi12sis3"}, function (resp) {
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