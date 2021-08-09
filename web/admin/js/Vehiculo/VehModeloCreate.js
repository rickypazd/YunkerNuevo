/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var url = "autosController";
var id_usr = 0;
$(document).ready(function () {
    cargar_marcas();
    id_usr = parseFloat(getQueryVariable("id"));

});




function registrar_modelo_vehiculo() {
    mostrar_progress();
    var exito = true;
    var nombre = $("#text_modelo_nombre").val() || null;
    var foto = $("#file-0d").val() || null;
    var id_rep_auto_marca = $("#id_rep_auto_marca").val() || null;
    var TokenAcceso = "servi12sis3";
    var usr_log = $.parseJSON(sessionStorage.getItem("usr_log"));
    if (nombre != null && nombre.length > 0) {
        $("#text_modelo_nombre").css("background", "#ffffff");
    } else {
        $("#text_modelo_nombre").css("background", "#df5b5b");
        exito = false;
    }
    if (id_rep_auto_marca != null && id_rep_auto_marca.length > 0) {
        $("#rep_auto_marca").css("background", "#ffffff");
    } else {
        $("#rep_auto_marca").css("background", "#df5b5b");
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



function cargar_marcas() {
    mostrar_progress();
    $.post(url, {
        evento: "getAll_rep_auto_marca",
        TokenAcceso: "servi12sis3"
    }, function (resp) {
          cerrar_progress();
        $("#img-carga").css("display", "none");
        if (resp != null) {
            var obj = $.parseJSON(resp);
            if (obj.estado != 1) {
                //error
                alert(obj.mensaje);
            } else {
                //exito
                //alert(resp);
                $("#lista_marca").html("");
                cargar_lista_marca($.parseJSON(obj.resp));

            }
        }
    });
}
function cargar_lista_marca(arr) {
    $.each(arr, function (i, obj) {
        cargar_iten_marca(obj);
    });
}
function cargar_iten_marca(obj) {
    var html = "<li onclick='seleccionar_marca(" + JSON.stringify(obj) + ")' data-id='" + obj.id + "'>";
    html += "   <img src='" + obj.url_foto + "' height='50' width='80' alt=''/>";
    html += "   <span>" + obj.nombre + "</span>";
    html += "</li>";
    $("#lista_marca").append(html);
    if (id_usr > 0) {
        if (id_usr == obj.id) {
            seleccionar_marca(obj);
        }
    }

}

function seleccionar_marca(obj) {
    $("#id_rep_auto_marca").val(obj.id);
    $("#rep_auto_marca").val(obj.nombre);
    $("#img_marca").attr("src", obj.url_foto);
    $(".bd-marca").modal("hide");

}