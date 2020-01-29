/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */




$(document).ready(function () {
    var id = getQueryVariable("id");
   
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
            $("#id_repuesto").val(id_usr);
        } else {
            exito = false;
            alert("Ocurrio un Error por favor vuelva a intentarlo.");
            window.location.reload();
        }
        if (exito) {

            mostrar_progress();
            var formData = new FormData($("#submitform")[0]);
            $.ajax({
                url: 'repuestosController',
                type: 'POST',
                data: formData,
                async: false,
                cache: false,
                contentType: false,
                processData: false,
                success: function (returndata) {
                    cerrar_progress();
                    if (returndata == "exito") {
                        window.location.reload();
                        // alert("exito");
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
  

});