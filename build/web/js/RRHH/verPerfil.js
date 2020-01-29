/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var url = "admin/adminController";
$(document).ready(function () {
    var id = getQueryVariable("id");
    $("#asignar_vehiculo").attr("href", "asignarVehiculo.html?idCn=" + id);
    $("#documentos").attr("href", "Documentos.html?id=" + id);
    $("#agg_creditos").attr("href", "aggCreditos.html?id=" + id);
    $("#perfilcon").attr("href", "editarConductor.html?id=" + id);
    $("#cambiarpass").attr("href", "cambiarContra.html?id=" + id);
    mostrar_progress();
    $.post(url, {evento:"get_usuario_id_sp", id: id}, function (resp) {
        cerrar_progress();
        if (resp.length > 0) {
            var obj = JSON.parse(resp);
            $("#nombre_perfil").html(obj.nombre + " " + obj.apellido_pa + " " + obj.apellido_ma);
            $("#ci_perfil").html("CI: " + obj.ci);
            var fotp=obj.foto_perfil || "";
            if (fotp.length > 0) {
                $("#foto_perfil").attr("src", obj.foto_perfil);
            }
            $("#p_usuario").html( obj.usuario);
            $("#p_creditos").html("CREDITOS: " + obj.creditos);
            $("#p_sexo").html(obj.sexo);
            $("#p_fecha_nac").html( obj.fecha_nac);
            $("#p_ciudad").html(obj.ciudad);
            $("#p_telefono").html(obj.telefono);
            $("#p_correo").html(obj.correo);
            $("#p_n_licencia").html(obj.numero_licencia);
            $("#p_cat_licencia").html(obj.categoria_licencia);
            get_vehiculos();
        }

    });

});
function get_vehiculos() {
    var id = getQueryVariable("id");
    id_conductor = id;
    mostrar_progress();
    $.post(url, {evento: "get_vehiculos_asignados_cond", id: id}, function (resp) {
        cerrar_progress();
        if (resp == "falso") {
            alert("Error al cargar datos");

        } else {
            var json = JSON.parse(resp);
            var html = "";
            $.each(json, function (i, obj) {
                html += "<a href='javaScript:void(0);' onclick='ver_conductor(" + obj.id_vehiculo + ");' class='list-group-item list-group-item-action flex-column align-items-start'>";
                html += "<div class='d-flex w-100 justify-content-between'>";
                html += "<h5 class='mb-1'>Placa: " + obj.placa + "</h5>";
                if (obj.estado == '0') {
                    html += "<small>HABILITADO</small>";
                } else if (obj.estado == '1') {
                    html += "<small>DESABILITADO</small>";
                }

                html += "</div>";

                html += "<p class='mb-1'><b>Marca:</b> <span >" + obj.marca + "</span>&nbsp; &nbsp;<b>Modelo:</b> <span >" + obj.modelo + "</span></p>";
                html += "<small><b>Año:</b> " + obj.ano + "&nbsp; &nbsp;<b>Color:</b> " + obj.color + "</small>";
                html += "</a>";
            });
            $("#lista_asignados").html(html);
         
        }
    });
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