/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function () {

    reloadVerify();
    if (document.getElementById("in_buscar")) {
        document.getElementById("in_buscar").focus();
    }
});
function reloadVerify() {
    if (!sessionStorage.getItem("usr_log")) {
        window.location.href = "/admin/Login.html";
    } else {
        var arr = $("body").find("[data-permiso]");
        var usr_log = $.parseJSON(sessionStorage.getItem("usr_log"));
        var permisos = usr_log.permisos;
        var dataurl = $("body").data("permisourl");
        if (permisos != null) {
            if (dataurl != null) {
                if (contiene(permisos, dataurl) === false) {
                    alert("Usted no cuenta con permisos para ingresar a esta pagina, favor de contactarse con el administrador");
                    window.location.href = "index.html";
                }

            }

            $.each(arr, function (i, obj) {
                var permisodata = $(obj).data("permiso");
                if (contiene(permisos, permisodata) === false) {
                    $(obj).remove();
                }
            });
        }

        cargar_usr(usr_log);
    }
}
function cargar_usr(usr_log) {
//    $(".usr_foto_perfil").attr("src", usr_log.foto_perfil);
    $(".usr_nombre").html(usr_log.nombre);
    $(".usr_nombre_rol").html(usr_log.nombrerol);
}
function contiene(arr, val) {
    var bol = false;
    $.each(arr, function (i, obj) {
        if (val == obj.NOMBRE) {
            bol = true;
        }
    });
    return bol;
}
function cerrarSesion() {
    if (sessionStorage.getItem("usr_log")) {
        sessionStorage.removeItem("usr_log");
        window.location.href = "Login.html";
    }
}

function ver_pagina(pag) {
    window.location.href = pag;
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
function getUrl() {
    var query = window.location.pathname.split("/");
    return query[query.length - 1];
}



