/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var url = "admin/adminController";
$(document).ready(function () {

    buscar_conductor();
});

function buscar_conductor() {
    var busq=$("#in_buscar").val() || "";
    
    $.post(url, {evento: "buscar_conductor",busqueda:busq}, function (resp) {
        var json = JSON.parse(resp);
        var html = "";
        $("#resultados").html("("+json.length+")");
        $.each(json, function (i, obj) {
            html += "<a href='javaScript:void(0);' onclick='ver_conductor("+obj.id+")' class='list-group-item list-group-item-action flex-column align-items-start'>";
             html += "<div class='d-flex w-100 justify-content-between'>";
                  html += "<h5 class='mb-1' id='co_nombre'>"+obj.nombre+" "+obj.apellido_pa+" "+obj.apellido_ma+"</h5>";
                  html += "<small>ACTIVO</small>";
              html += "</div>";
                  html += "<small><b>CI:</b> "+obj.ci+"&nbsp; &nbsp;<b>Creditos:</b> "+obj.creditos+"</small>";
                  html += "</br><small><b>SEXO:</b> "+obj.sexo+"</small>";
            html += "</a>";
            
   
        });
        $("#list_conductores").html(html);
    });
}



function ver_conductor(id){
    window.location.href="verPerfil.html?id="+id;
}