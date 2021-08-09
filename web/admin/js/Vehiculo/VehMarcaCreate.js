/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var url = "autosController";
$(document).ready(function () {
 
});

function registrar_marca_vehiculo() {
    mostrar_progress();
    var exito = true;
    var nombre = $("#text_marca_nombre").val() || null;
    var foto = $("#file-0d").val() || null;
    var TokenAcceso = "servi12sis3";
    var usr_log = $.parseJSON(sessionStorage.getItem("usr_log"));
        if (nombre != null && nombre.length > 0) {
            $("#text_marca_nombre").css("background", "#ffffff");
        } else {
            $("#text_marca_nombre").css("background", "#df5b5b");
            exito = false;
        }
    if (foto != null && nombre.length > 0) {
        $("#file-0d").css("background", "#ffffff");
    } else {
        $(".file-caption").css("background", "#df5b5b");
        exito = false;
    }
    if (exito) {
            mostrar_progress();
        var formData = new FormData($("#submitform")[0]);
        $.ajax({
            url: url,
            type: 'POST',
            data: formData,
            contentType: false,
            cache: false,
            processData: false,
            success: function (data)
            {
                //despues de cargar
                cerrar_progress();
                if (data != null) {
                    var obj = $.parseJSON(data);
                    if (obj.estado != "1") {
                        alert(obj.mensaje);
                    } else {
                          window.history.back();

                    }
                }
               

            }
        });
    } else {
        cerrar_progress();
    }
}
