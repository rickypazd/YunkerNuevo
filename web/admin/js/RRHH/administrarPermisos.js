/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


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
            html += "<li class='li_permisos'>" + obj.NOMBRE + "<i class='fas fa-trash-alt btn_fa' onclick='remover_permiso(" + obj.ID + ",this);'></i></li>";
        });
        $("#ul_permisos").html(html);
    });
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
                var html = "<li class='li_permisos'>" + nombre + "<i class='fas fa-trash-alt btn_fa' onclick='remover_permiso(" + resp + ",this);'></i></li>";
                $("#ul_permisos").append(html);
                $("#exampleModalCenter").modal("hide");
            }
        });
    }
}

function remover_permiso(id, element) {
    $.post(url, {evento: "eliminar_permiso", id: id}, function (resp) {
        if (resp == "falso") {
            alert("No se puede eliminar el Permiso por que algun rol lo esta utilizando.");
        } else {
            $(element).parent().remove();
        }
    });
}