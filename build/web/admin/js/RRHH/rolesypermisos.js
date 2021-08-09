/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var url = "rolesController";
$(document).ready(function () {
    $("#btn_agg_permiso").css("display", "none");
    get_roles();
});

function get_roles() {
    $.post(url, {evento: "get_roles"}, function (resp) {
        var json =$.parseJSON(resp);
        var html = "";
        $.each(json, function (i, obj) {
            html += "<li class='li_roles' onclick='ver_permisos(" + obj.ID + ",this);'>" + obj.NOMBRE + "</li>";
        });
        $("#list_roles").html(html);
    });
}

function ver_permisos(id, li) {
    $.post(url, {evento: "get_permisos_id_rol", id: id}, function (resp) {
        var json = JSON.parse(resp);
        var html = "";
        $.each(json, function (i, obj) {
            html += "<li class='li_permisos_rol'><span>" + obj.NOMBRE + "</span><i class='fas fa-trash-alt btn_fa' onclick='remover_permiso("+obj.ID_PERMISO+","+obj.ID_ROL+",this);'></i></li>";
        });
        $("#btn_agg_permiso").css("display", "");
        $("#list_roles").find("li").css("background-color", "#FFFFFF");
        $(li).css("background-color", "#4f4f4f");
        $("#list_permisos_de_rol").html(html);
        $("#rol_a_modificar").html($(li).text());
        $("#nombre_rol_select").html($(li).text());
        $("#rol_a_modificar").data("id_rol", id);
    });
}

function ver_agg_permiso() {
    var id = $("#rol_a_modificar").data("id_rol");
    $.post(url, {evento: "get_permisos_dis_rol", id: id}, function (resp) {
        var arr = $.parseJSON(resp);
        var html = "";
        $.each(arr, function (i, obj) {
            html += "<li><label><input type='checkbox' value='" + obj.ID + "'>" + obj.NOMBRE + "</label></li>";
        });
        $("#list_permisos_disponibles").html(html);
        $("#exampleModalCenter").modal("show");
    });

}

function agregar_permiso() {
    var id = $("#rol_a_modificar").data("id_rol");
    var arr = $("#list_permisos_disponibles").find("input:checked");
    var json = '[';
    $.each(arr, function (i, obj) {
        json += '{"id":' + $(obj).val() + '},';
    });
    json=json.substring(0, json.length-1);
    json += ']';
    
    $.post(url,{evento:"agg_permiso_a_rol",id:id,jsonarr:json},function(resp){
        if(resp=="exito"){
            window.location.reload();    
        }else{
            alert("Ocurrio Algun Error al agregar permiso");
        }
        
    });
}
function remover_permiso(id_permiso,id_rol,elemento){
       
    $.post(url,{evento:"remover_permiso",id_rol:id_rol,id_permiso:id_permiso},function(resp){
        if(resp=="exito"){
            $(elemento).parent().remove();
        }else{
            alert("Ocurrio un error al borrar no se efectuaron cambios");
        }
    });
}