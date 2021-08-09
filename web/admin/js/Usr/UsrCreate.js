/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var url = "adminController";
var urlRoles="rolesController";
$(document).ready(function () {
    $.post(urlRoles, {evento: "get_roles"}, function (resp) {
        var html = "";
        var arr = $.parseJSON(resp);
        $.each(arr, function (i, obj) {
            html += "<option value='" + obj.ID + "'>" + obj.NOMBRE + "</option>";
        });
        $("#cu_tipo").html(html);
    });
});

function ok_crear() {
    var acepted = true;

    var nombre = $("#cu_nombre").val() || null;
    var apellido_pa = $("#cu_ap_pa").val() || null;
    var apellido_ma = $("#cu_ap_ma").val() || null;
    var tipo = $("#cu_tipo").val() || null;
    var usuario = $("#cu_usuario").val() || null;
    var pass = $("#cu_contrasenha").val() || null;
    var rep_pass = $("#cu_repcontrasenha").val() || null;

    if (nombre == null) {
        $("#cu_nombre").css("background-color", "#f386ab");
        acepted = false;
    } else {
        $("#cu_nombre").css("background-color", "#ffffff");
    }
    if (apellido_pa == null) {
        $("#cu_ap_pa").css("background-color", "#f386ab");
        acepted = false;
    } else {
        $("#cu_ap_pa").css("background-color", "#ffffff");
    }
    if (apellido_ma == null) {
        $("#cu_ap_ma").css("background-color", "#f386ab");
        acepted = false;
    } else {
        $("#cu_ap_ma").css("background-color", "#ffffff");
    }
    if (usuario == null) {
        $("#cu_usuario").css("background-color", "#f386ab");
        acepted = false;
    } else {
        $("#cu_usuario").css("background-color", "#ffffff");
    }
    if (pass == null) {
        $("#cu_contrasenha").css("background-color", "#f386ab");
        acepted = false;
    } else {
        $("#cu_contrasenha").css("background-color", "#ffffff");
    }
    if (rep_pass == null) {
        $("#cu_repcontrasenha").css("background-color", "#f386ab");
        acepted = false;
    } else {
        $("#cu_repcontrasenha").css("background-color", "#ffffff");
    }
    if (rep_pass != pass) {
        $("#cu_repcontrasenha").css("background-color", "#f386ab");
        acepted = false;
    } else {
        $("#cu_repcontrasenha").css("background-color", "#ffffff");
    }
    if (acepted) {
        var passmd5 = md5(pass);
        
        $.post(url, {evento: "registrar_usuario",
            TokenAcceso:"servi12sis3",
            nombre: nombre,
            apellido_pa: apellido_pa,
            apellido_ma: apellido_ma,
            id_rol: tipo,
            usuario: usuario,
            pass: passmd5
        }, function (resp) {
            if(resp=="false"){
                alert("Ocurrio un error al registrar Usuario");
            }else{
               window.history.back();
            }
        });
    }

}