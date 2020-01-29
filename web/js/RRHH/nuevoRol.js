/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



var url = "rolesController";
$(document).ready(function () {
    get_permisos();
});

function get_permisos() {
    $.post(url, {evento: "get_permisos"}, function (resp) {
        var json = JSON.parse(resp);
        var html = "";
        $.each(json, function (i, obj) {
            html += "<li class='li_permisos' onclick='agg_permiso(" + obj.ID + ",this);'>" + obj.NOMBRE + "</li>";
        });
        $("#ul_permisos").html(html);
    });
}

function agg_permiso(id, li) {
    var html = "<li class='li_permisos li_permisos_x_agg' data-nombre=" + $(li).text() + " data-id=" + id + " onclick='quitar_permiso(" + id + ",this);'>" + $(li).text() + "</li>";
    $("#ul_permisos_agg").append(html);
    $(li).remove();
}

function quitar_permiso(id, li) {
    var html = "<li class='li_permisos' onclick='agg_permiso(" + id + ",this);'>" + $(li).text() + "</li>";
    $("#ul_permisos").append(html);
    $(li).remove();
}

function nuevo_permiso() {
    var correcto = true;
    var nombre = $("#nuevo_permiso_nombre").val();
    $("#nuevo_permiso_nombre").val("");
    if (nombre == null) {
        correcto = false;
        $("#nuevo_permiso_nombre").css("background-color", "#bf484e");
    }


    if (correcto) {
        $.post(url, {evento: "nuevo_permiso", nombre: nombre}, function (resp) {
            if (resp == "falso") {
                $("#exampleModalCenter").modal("hide");
                alert("EL PERMISO YA EXISTE");
            } else {
                var html = "<li class='li_permisos' onclick='agg_permiso(" + resp + ",this);'>" + nombre + "</li>";
                $("#ul_permisos").append(html);
                $("#exampleModalCenter").modal("hide");
            }
        });
    }
}

function ok_agg_rol() {

    var correcto = true;
    var nombre = $("#in_nombre_rol").val();
    if (nombre == null) {
        correcto = false;
        $("#in_nombre_rol").css("background-color", "#bf484e");
    }


    if (correcto) {
        $.post(url, {evento: "nuevo_rol", nombre: nombre}, function (resp) {
            if (resp == "falso") {
                alert("EL ROL YA EXISTE");
            } else {

                var arr = $("#ul_permisos_agg").find("li");
                if (arr.length <= 0) {
                     alert("SE AGREGO CORRECTAMENTE");
                     window.location.href = "rolesypermisos.html";
                } else {
                    var json = '[';
                    $.each(arr, function (i, obj) {
                        json += '{"id":' + $(obj).data("id") + ',"nombre":"' + $(obj).data("nombre") + '"},';
                    });
                    json = json.substring(0, json.length - 1);
                    json += ']';
                    $.post(url, {evento: "agg_permiso_a_rol", jsonarr: json, id: resp}, function (respa) {
                        if (respa == "exito") {
                            alert("SE AGREGO CORRECTAMENTE");
                            window.location.href = "rolesypermisos.html";
                        }
                    });
                }

            }
        });
    }
}