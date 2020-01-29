/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var url = "autosController";
var JsonResp;
$(document).ready(function () {
    var id = getQueryVariable("id");
    $("#editarPerfil").attr("href", "VehModeloEdit.html?id=" + id);
    $('#text_version_nombre').keypress(function (event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode == '13') {
            ok_agregar();
        }
        //Stop the event from propogation to other handlers
        //If this line will be removed, then keypress event handler attached
        //at document level will also be triggered
        event.stopPropagation();
    });
    mostrar_progress();
    $.post(url, {TokenAcceso: "servi12sis3", evento: "getById_rep_auto_modelo", id: id}, function (resp) {
        cerrar_progress();
        if (resp != null) {
            var objec = $.parseJSON(resp);
            if (objec.estado != "1") {
                alert(objec.mensaje);
            } else {
                var obj = $.parseJSON(objec.resp);
                JsonResp=obj;

                $("#nombre_perfil").html(obj.nombre);
                $("#foto_perfil").attr("src", obj.url_foto);
                $("#dtMarca").html(obj.nombre_marca);
                $("#img_marca").attr("src", obj.url_foto_marca);
                Lista_repuesto();


            }
        }

    });

});

function Lista_repuesto() {

    var id = getQueryVariable("id");
    $.post(url, {evento: "get_rep_auto_version_by_id_rep_auto_modelo", id: id, TokenAcceso: "servi12sis3"}, function (resp) {
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
                    cargar_a_tabla(obj);
                });


            }
        }

    });
}

function cargar_a_tabla(obfinal) {
    var html = "<div data-obj='" + JSON.stringify(obfinal) + "' class='list-group-item list-group-item-action flex-column align-items-start'>";
    html += "               <div class='row iten_repuesto_row'>";
    html += "                     <div class='col-8'>";
    html += "                         <h6>" + obfinal.nombre + "</h6>";
    html += "                     </div>";
    html += "                     <div class='col-4'>";
    html += "                   <div class='col-12 d-flex justify-content-center p-0' style='text-align: center;'>";
    html += "                        <div class='col-5 btnAction btnEditar' onclick='editar(this);'><img src='img/lapiz.svg' alt=''/><p>Editar</p></div>";
    html += "                        <div class='col-5 btnAction btnEliminar' onclick='eliminar(this);'>  <img src='img/bote-de-basura.svg' alt=''/><p>Eliminar</p></div>";
    html += "                    </div>";
    html += "                     </div>";
    html += "                 </div>";
    html += "            </div>";
    $("#list_marcas").append(html);
}
function ok_agregar() {
    var acepted = true;

    var nombre = $("#text_version_nombre").val() || null;


    if (nombre == null) {
        $("#text_version_nombre").css("background-color", "#f386ab");
        acepted = false;
    } else {
        $("#text_version_nombre").css("background-color", "#ffffff");
    }


    var modelo = parseFloat(getQueryVariable("id"));
    if (modelo > 0) {
        $("#id_usr").val(modelo);
    } else {
        exito = false;
        alert("Ocurrio un Error por favor vuelva a intentarlo.");
        window.location.reload();
    }
    if (acepted) {

        mostrar_progress();
        $.post(url, {evento: "registrar_rep_auto_version",
            TokenAcceso: "servi12sis3",
            id: modelo,
            nombre: nombre
        }, function (resp) {
            cerrar_progress();
            if (resp != null) {
                var objec = $.parseJSON(resp);
                if (objec.estado != "1") {
                    alert(objec.mensaje);
                } else {
                    var obfinal = $.parseJSON(objec.resp);
                    cargar_a_tabla(obfinal);
                    $("#text_version_nombre").val("");
                    $("#agg_version").modal("toggle");
                }
            }
        });
    }
}
function ok_editar() {
    var acepted = true;

    var nombre = $("#text_version_nombre_edit").val() || null;


    if (nombre == null) {
        $("#text_version_nombre_edit").css("background-color", "#f386ab");
        acepted = false;
    } else {
        $("#text_version_nombre_edit").css("background-color", "#ffffff");
    }

    var data = $(itnEditando).data("obj");
    var modelo = data.id;
    if (modelo > 0) {
        $("#id_usr").val(modelo);
    } else {
        exito = false;
        alert("Ocurrio un Error por favor vuelva a intentarlo.");
        window.location.reload();
    }
    if (acepted) {

        mostrar_progress();
        $.post(url, {evento: "editar_rep_auto_version",
            TokenAcceso: "servi12sis3",
            id: modelo,
            nombre: nombre
        }, function (resp) {
            cerrar_progress();
            if (resp != null) {
                var objec = $.parseJSON(resp);
                if (objec.estado != "1") {
                    alert(objec.mensaje);
                } else {
                    var obfinal = $.parseJSON(objec.resp);
                      $(itnEditando).css("display", "none");
                    cargar_a_tabla(obfinal);
                    
                    $("#text_version_nombre").val("");
                    $("#editar_version").modal("toggle");
                }
            }
        });
    }
}
function verMarca(){
    window.location.href= "VehMarcaPerfil.html?id="+JsonResp.id_rep_auto_marca;
}
function agregar_modelo() {
    var id = getQueryVariable("id");
    $("#agg_version").modal("show");
    //window.location.href = "VehModeloCreate.html?id="+id;
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


var itnEditando;
function editar(itn) {
    var padre = $(itn).parent().parent().parent().parent();
    var data = $(padre).data("obj");
    itnEditando = padre;
    $("#text_version_nombre_edit").val(data.nombre);
     $("#editar_version").modal("show");
   

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
    $.post(url, {evento: "eliminar_rep_auto_version", id: id, TokenAcceso: "servi12sis3"}, function (resp) {
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