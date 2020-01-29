/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var url = "repuestosController";
$(document).ready(function () {
    cargar_categorias();
});
//----------CATEGORIA-------------

function cargar_categorias() {
    mostrar_progress();
    $.post(url, {evento: "getAll_rep_categoriaJSON", TokenAcceso: "servi12sis3"}, function (resp) {
        cerrar_progress();
        if (resp != null) {
            var obj = $.parseJSON(resp);
            if (obj.estado != "1") {
                alert(obj.mensaje);
            } else {
                var arr = $.parseJSON(obj.resp);
                $.each(arr, function (i, object) {
                    cargar_categoria_iten(object);
                });

            }
        }

    });
}
function agregar_categoria() {
    mostrar_progress();
    var exito = true;
    var nombre = $("#nombre_categoria").val() || null;
    if (nombre != null && nombre.length > 0) {
        $("#nombre_categoria").css("background", "#ffffff");
    } else {
        $("#nombre_categoria").css("background", "#df5b5b");
        exito = false;
    }
    if (exito) {
        $.post(url, {evento: "registrar_rep_categoria", TokenAcceso: "servi12sis3", nombre: nombre}, function (resp) {
            cerrar_progress();
            if (resp != null) {
                var obj = $.parseJSON(resp);
                if (obj.estado != "1") {
                    alert(obj.mensaje);
                } else {
                    var object = $.parseJSON(obj.resp);
                    cargar_categoria_iten(object);
                }
            }
            $("#nombre_categoria").val("");
            $("#modal_agregar_categoria").modal("toggle");
        });
    } else {
        cerrar_progress();
    }
}
function cargar_categoria_iten(obj) {
    var html = "<div class='card'>";
    html += "       <div class='card-header item_categoria' id='heading-" + obj.id + "'>";
    html += "           <div class='row'>";
    html += "               <div class='col-2'>";
    html += "                         <img src='" + obj.url_foto + "' class='' height='60px' alt='' />";
    html += "               </div>";
    html += "               <div class='col-7'>";
    html += "                   <h4 >";
    html += "<label class='lng'>es:</label> " + (obj.nombre || "");
    html += "                   </h4>";
    html += "                   <hr>";
    html += "                   <h4 >";
    html += "<label class='lng'>en:</label> " + obj.name;
    html += "                   </h4>";

    html += "               </div>";
    html += "               <div class='col-3'>";
      html += "                   <div class='col-12 d-flex justify-content-center  align-items-center p-0 h-100' style='text-align: center;'>";
    html += "                        <div class='col-4 btnAction btnEditar' data-obj='" + JSON.stringify(obj) + "' onclick='editar(this);'><img src='img/lapiz.svg' alt=''/><p>Editar</p></div>";
    html += "                        <div class='col-4 btnAction btnEliminar' data-obj='" + JSON.stringify(obj) + "' onclick='eliminar(this);'>  <img src='img/bote-de-basura.svg' alt=''/><p>Eliminar</p></div>";
    html += "                        <div class='col-4 btnAction btnVer'  data-toggle='collapse' data-target='#collapse-" + obj.id + "' aria-expanded='false' aria-controls='collapse-" + obj.id + "'  data-subcategorias_cargadas='false' data-obj='" + JSON.stringify(obj) + "' onclick='abrir_categoria(this)'>  <img src='img/cheuron.svg' alt=''/><p>Ver</p></div>";
    html += "                   </div>";
    html += "               </div>";
    html += "           </div>";
    html += "           <div id='collapse-" + obj.id + "' class='collapse' aria-labelledby='heading-" + obj.id + "' data-parent='#accordion'>";
    html += "               <div class='card-body'>";
    html += "                   <button type='button' class='btn btn-primary' data-toggle='modal' data-target='#modal_agregar_sub_categoria' onclick='abrir_agregar_sub_categoria(" + obj.id + ");'>  Agregar Sub-Categoria</button>";
    html += "                       <ul class='list-group lista_sub_categorias' style='margin-top:16px;' id='lista_sub_categoria_" + obj.id + "'>";
    html += "                       </ul>";
    html += "               </div>";
    html += "           </div>";
    html += "       </div>";
    html += "</div>";
    $("#accordion").append(html);
}
function abrir_categoria(iten) {
    var cargado = $(iten).data("subcategorias_cargadas");
    if (!cargado) {
        $(iten).data("subcategorias_cargadas", true);
        var iten_lista = $(iten).parent().parent().parent().parent().find(".lista_sub_categorias");
        //cargar_sub_categoria_iten({},iten_lista);
        var data_obj = $(iten).data("obj");
        var arr = data_obj.sub_categorias;
        $.each(arr, function (i, object) {
            cargar_sub_categoria_iten(object, iten_lista);
        });

    }
}
//----------SUB-CATEGORIA----------
var lista_selected;
function abrir_agregar_sub_categoria(id) {
    $("#btn_agregar_sub_categoria").attr("onclick", "agregar_sub_categoria(" + id + ")");
}
function agregar_sub_categoria(id) {
    mostrar_progress();
    var exito = true;
    var nombre = $("#nombre_sub_categoria").val() || null;
    if (nombre != null && nombre.length > 0) {
        $("#nombre_sub_categoria").css("background", "#ffffff");
    } else {
        $("#nombre_sub_categoria").css("background", "#df5b5b");
        exito = false;
    }
    if (exito) {

        $.post(url, {evento: "registrar_rep_sub_categoria", TokenAcceso: "servi12sis3", nombre: nombre, id_rep_categoria: id}, function (resp) {
            cerrar_progress();
            if (resp != null) {
                var obj = $.parseJSON(resp);
                if (obj.estado != "1") {
                    alert(obj.mensaje);
                } else {
                    var object = $.parseJSON(obj.resp);
                    cargar_sub_categoria_iten(object, $("#lista_sub_categoria_" + id));
                }
            }
            $("#nombre_sub_categoria").val("");
            $("#modal_agregar_sub_categoria").modal("toggle");
        });
    } else {
        cerrar_progress();
    }
}

function cargar_sub_categoria_iten(obj, iten) {
    var html = " <li class='list-group-item row'>";
    html += "<div class='col-8'>";
    html += "<label class='lng lng-es'>es:</label> " + (obj.nombre || "");
html += "<hr>";
    html += "<label class='lng lng-en'>en:</label> " + (obj.name || "");
    html += "</div>";


    html += "                     <div class='col-4'>";
      html += "                   <div class='col-12 d-flex justify-content-center  align-items-center p-0 h-100' style='text-align: center;'>";
    html += "                        <div class='col-4 btnAction btnEditar' data-obj='" + JSON.stringify(obj) + "' onclick='editarSub(this);'><img src='img/lapiz.svg' alt=''/><p>Editar</p></div>";
    html += "                        <div class='col-4 btnAction btnEliminar'  data-obj='" + JSON.stringify(obj) + "' onclick='eliminarSub(this);'>  <img src='img/bote-de-basura.svg' alt=''/><p>Eliminar</p></div>";
    html += "                        <div class='col-4 btnAction btnAgg' data-obj='" + JSON.stringify(obj) + "' onclick='agg_repuesto(this)'>  <img src='img/aggicon.svg' alt=''/><p>Agregar Repuesto</p></div>";
    html += "                    </div>";

    html += "                                </li>";
    $(iten).append(html);
}


function editar(itn) {

    var data = $(itn).data("obj");

    // window.location.href = "art_vehiculo_marcaEdit.html?id=" + data.id;

}
function eliminar(itn) {

    itnSelect = itn;
    var data = $(itn).data("obj");
    $("#marName").html(data.nombre);
    $("#btn_eliminar").data("id", data.id);
    $("#confirm-delete").modal("show");
    //  alert(data.id);
}
function ver(itn) {


}

function agg_repuesto(elmn) {
    var data = $(elmn).data("obj");
    window.location.href = "RepCreate.html?idSubCat=" + data.id;
}
var itnSelect;
function ok_eliminar(itn) {
    var id = $(itn).data("id");
    mostrar_progress();
    $.post(url, {evento: "eliminar_rep_categoria", id: id, TokenAcceso: "servi12sis3"}, function (resp) {
        cerrar_progress();
        if (resp != null) {
            var obj = $.parseJSON(resp);
            if (obj.estado != "1") {
                alert(obj.mensaje);
            } else {
                var arr = parseFloat(obj.resp);
                $("#confirm-delete").modal("toggle");
                $(itnSelect).parent().parent().parent().parent().css("display", "none");

                // alert(arr);

            }
        }

    });
}


function eliminarSub(itn) {

    itnSelectSub = itn;
    var data = $(itn).data("obj");
    $("#marNameSub").html(data.nombre);
    $("#btn_eliminarSub").data("id", data.id);
    $("#confirm-deleteSub").modal("show");
    //  alert(data.id);
}

var itnSelectSub;
function ok_eliminarSub(itn) {
    var id = $(itn).data("id");
    mostrar_progress();
    $.post(url, {evento: "eliminar_rep_sub_categoria", id: id, TokenAcceso: "servi12sis3"}, function (resp) {
        cerrar_progress();
        if (resp != null) {
            var obj = $.parseJSON(resp);
            if (obj.estado != "1") {
                alert(obj.mensaje);
            } else {
                //var arr = parseFloat(obj.resp);

                $(itnSelectSub).parent().parent().parent().remove();
                $("#confirm-deleteSub").modal("toggle");
                // alert(arr);

            }
        }

    });
}

