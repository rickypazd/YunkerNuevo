/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var url = 'admin/adminController';
var id_usr;

$(document).ready(function (resp) {
    id_usr = getQueryVariable("id");
    mostrar_progress();
    $.post(url, {evento: 'ver_foto', id: id_usr}, function (resp) {
        cerrar_progress();
        var obj = $.parseJSON(resp);
        $("#imagen").attr("src", "data:image/jpeg;base64," + obj.b64);
        $("#nombre_docu").html(obj.nombredocu);
        $("#nombre").html(obj.nombre);
        if (obj.need_fecha)
            $("#fecha").html(obj.fecha_caducidad);

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
