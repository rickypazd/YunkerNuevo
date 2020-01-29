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
    $("#submitform").submit(function (event) {
        event.preventDefault();
        var file = $("#file-0d").val() || null;
        var exito = true;

        if (file == null) {
            $("#file-0d").css("background-color", "#f386ab");
            alert("Es nesesario seleccioar un archibo.");
            exito = false;
        } else {
            $("#file-0d").css("background-color", "#ffffff");
        }
        var id_usr = parseFloat(getQueryVariable("id"));
        if (id_usr > 0) {
            $("#id_usr").val(id_usr);
        } else {
            exito = false;
            alert("Ocurrio un Error por favor vuelva a intentarlo.");
            window.location.reload();
        }
        if (exito) {

            mostrar_progress();
            var formData = new FormData($("#submitform")[0]);
            $.ajax({
                url: 'admin/adminController',
                type: 'POST',
                data: formData,
                async: false,
                cache: false,
                contentType: false,
                processData: false,
                success: function (returndata) {
                    cerrar_progress();
                     if (returndata != null) {
                        var objec = $.parseJSON(returndata);
                        if (objec.estado != "1") {
                            alert(objec.mensaje);
                        } else {
                           window.location.reload();


                        }
                    }


                },
                error: function () {
                    alert("Error al contactarse con el servidor.");
                    cerrar_progress();
                }
            });
            // $(this).unbind('submit').submit();
        }

    });
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
                $("#cu_nombre").val(obj.nombre);
                $("#cu_ap_pa").val(obj.apellido_pa);
                $("#cu_ap_ma").val(obj.apellido_ma);
                // $("#p_usuario").html(obj.usuario);


            }
        }

    });

});

function ok_crear() {
    var acepted = true;

    var nombre = $("#cu_nombre").val() || null;
    var apellido_pa = $("#cu_ap_pa").val() || null;
    var apellido_ma = $("#cu_ap_ma").val() || null;


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

    var id_usr = parseFloat(getQueryVariable("id"));
    if (id_usr > 0) {
        $("#id_usr").val(id_usr);
    } else {
        exito = false;
        alert("Ocurrio un Error por favor vuelva a intentarlo.");
        window.location.reload();
    }
    if (acepted) {

        mostrar_progress();
        $.post(url, {evento: "editar_usuario",
            TokenAcceso: "servi12sis3",
            id: id_usr,
            nombre: nombre,
            apellido_pa: apellido_pa,
            apellido_ma: apellido_ma
        }, function (resp) {
            cerrar_progress();
            if (resp != null) {
                var objec = $.parseJSON(resp);
                if (objec.estado != "1") {
                    alert(objec.mensaje);
                } else {
                  window.history.back();
                }
            }
        });
    }

}
