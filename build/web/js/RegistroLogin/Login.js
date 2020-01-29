/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//----------------------------------------------
var url = "loginController";
$(document).ready(function () {

});
$(document).keypress(function (e) {
    if (e.which == 13) {
        ok_login();
    }
});
function ok_login() {
    var usuario = $("#lg_username").val() || null;
    var pass = $("#lg_password").val() || null;

    if (usuario == null) {
        $("#lg_username").parent().append("<label id='lg_username-error' class='form-invalid' for='lg_username'>Nesesita ingresar su usuario.</label>");
        return;
    } else {
        $("#lg_username").parent().children("label.form-invalid").remove();
    }
    if (pass == null) {
        $("#lg_password").parent().append("<label id='lg_password-error' class='form-invalid' for='lg_password'>Contraseña incorrecta.</label>");
        return;
    } else {
        $("#lg_password").parent().children("label.form-invalid").remove();
    }
    var passmd5 = md5(pass);
    mostrar_progress();
    $.post(url, {TokenAcceso:"servi12sis3",evento: "login", usuario: usuario, pass: passmd5}, function (resp) {
        cerrar_progress();
        if (resp != null) {
            var obj = $.parseJSON(resp);
            if (obj.estado != "1") {
                alert(obj.mensaje);
            } else {
                    sessionStorage.setItem("usr_log", obj.resp);
                    window.location.href = "index.html";
            }
        }
    });
}
