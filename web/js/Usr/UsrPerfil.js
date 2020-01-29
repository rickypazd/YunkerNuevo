/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var url = "admin/adminController";
$(document).ready(function () {
    var id = getQueryVariable("id");
    $("#editarPerfil").attr("href", "UsrEdit.html?id=" + id);
    mostrar_progress();
    $.post(url, {TokenAcceso: "servi12sis3", evento: "getById_usuario", id: id}, function (resp) {
        cerrar_progress();
        if (resp != null) {
            var objec = $.parseJSON(resp);
            if (objec.estado != "1") {
                alert(objec.mensaje);
            } else {
                var obj = $.parseJSON(objec.resp);


                $("#nombre_perfil").html(obj.nombre + " " + obj.apellido_pa + " " + obj.apellido_ma);
                $("#foto_perfil").attr("src", obj.url_foto);
                
                $("#p_nombre").html(obj.nombre);
                $("#p_apellidopa").html(obj.apellido_pa);
                $("#p_apellidoma").html(obj.apellido_ma);
                $("#p_usuario").html(obj.usuario);


            }
        }

    });

});

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