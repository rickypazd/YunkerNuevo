/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var url = "almacenController";

function registrar() {
   // mostrar_progress();
    var exito = true;
    var nombre = $("#inpNombre").val() || null;
    var direccion = $("#inpDireccion").val() || null;
    var lat = $("#inpLat").val() || null;
    var lng = $("#inpLng").val() || null;
    var TokenAcceso = "servi12sis3";
    var usr_log = $.parseJSON(sessionStorage.getItem("usr_log"));
    if (nombre != null && nombre.length > 0) {
        $("#inpNombre").css("background", "#ffffff");
    } else {
        $("#inpNombre").css("background", "#df5b5b");
        exito = false;
    }
    if (direccion != null && direccion.length > 0) {
        $("#inpDireccion").css("background", "#ffffff");
    } else {
        $("#inpDireccion").css("background", "#df5b5b");
        exito = false;
    }
    if (lat != null && lat.length > 0) {
        $("#inpLat").css("background", "#ffffff");
    } else {
        $("#inpLat").css("background", "#df5b5b");
        exito = false;
    }
    if (lng != null && lng.length > 0) {
        $("#inpLng").css("background", "#ffffff");
    } else {
        $("#inpLng").css("background", "#df5b5b");
        exito = false;
    }

    if (exito) {
        mostrar_progress();
        $.post(url, {TokenAcceso: "servi12sis3", evento: "registrar_almacen", nombre: nombre, direccion: direccion, lat:lat, lng:lng}, function (resp)
        {
            cerrar_progress();
            if (resp != null) {
                var obj = $.parseJSON(resp);
                if (obj.estado != 1) {
                    alert(obj.mensaje);
                } else {
                   // alert(obj.mensaje);
                    var obje = $.parseJSON(obj.resp);
//                    window.location.href = 'RepPerfil.html?id=' + obje.id;
                    window.location.href = 'AlmacenList.html';
                }
            }
        
        });
    } 
}