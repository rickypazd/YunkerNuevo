/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var url = "repuestosController";
$(document).ready(function () {
    var idSubCat = getQueryVariable("id");
    agg_detalle();
    if (idSubCat > 0) {



    } else {
         alert("Primero deve seleccionar el repuesto.");
         window.location.href = "RepList.html";
    }
});

function registrar_repuesto_vehiculo() {

    
    
    mostrar_progress();
    var exito = true;
    var nombre = $("#text_nombre").val() || null;
    var serie = $("#text_serie").val() || null;
    var precio = $("#text_precio").val() || null;
    var foto = $("#file-0d").val() || null;
    var id_padre = getQueryVariable("id");
    var TokenAcceso = "servi12sis3";
    var usr_log = $.parseJSON(sessionStorage.getItem("usr_log"));
    if (nombre != null && nombre.length > 0) {
        $("#text_nombre").css("background", "#ffffff");
    } else {
        $("#text_nombre").css("background", "#df5b5b");
        exito = false;
    }
    if (serie != null && serie.length > 0) {
        $("#text_serie").css("background", "#ffffff");
    } else {
        $("#text_serie").css("background", "#df5b5b");
        exito = false;
    }
    if (precio != null && precio.length > 0) {
        $("#text_precio").css("background", "#ffffff");
    } else {
        $("#text_precio").css("background", "#df5b5b");
        exito = false;
    }
    if (foto != null && nombre.length > 0) {
        $("#file-0d").css("background", "#ffffff");
    } else {
        $(".file-caption").css("background", "#df5b5b");
        exito = false;
    }
    var lista = $("#list_marcas").find(".iten_repuesto_row");
    var list = [];
    var inpdetalle, inpvalor;
    var detalle, valor;
    $.each(lista, function (i, obj) {
        inpdetalle = $(lista[i]).find("input[name=detalle]");
        inpvalor = $(lista[i]).find("input[name=valor]");
        detalle = $(inpdetalle).val() || null;
        valor = $(inpvalor).val() || null;
        if (valor != null && valor.length > 0) {
            $(inpvalor).css("background", "#ffffff");
        } else {
            $(inpvalor).css("background", "#df5b5b");
            exito = false;
        }
        if (detalle != null && detalle.length > 0) {
            $(inpdetalle).css("background", "#ffffff");
        } else {
            $(inpdetalle).css("background", "#df5b5b");
            exito = false;
        }
        valor = $(lista[i]).find("input[name=valor]").val();
        list.push({detalle: detalle, valor: valor});
    });
   
    if (id_padre > 0) {
        //exito
        $("input[name=id_padre]").val(id_padre);
    } else {
        alert("Ocurrio algun problema. Disculpe las molestias.");
        window.location.href = "index.html";
    }
    if (exito) {
        mostrar_progress();
        var formData = new FormData($("#submitform")[0]);
        formData.append('list_detalle',JSON.stringify(list));
        $.ajax({
            url: url,
            type: 'POST',
            data: formData,
            contentType: false,
            cache: false,
            processData: false,
            success: function (resp)
            {
                cerrar_progress();
                if (resp != null) {
                    var obj = $.parseJSON(resp);
                    if (obj.estado != 1) {
                        alert(obj.mensaje);
                    } else {
                        alert(obj.mensaje);
                        var obje = $.parseJSON(obj.resp);
                        window.location.href = 'RepPerfil.html?id=' + obje.id;
                    }
                }
            }
        });
    } else {
        cerrar_progress();
    }
}

function agg_detalle() {
    var html = "<div class='list-group-item list-group-item-action flex-column align-items-start'>";
    html += "               <div class='row iten_repuesto_row'>";
    html += "                     <div class='col-5'>";
    html += "                         <input type='text' name='detalle' class='form-control color-input' placeholder='Nombre detalle'>";
    html += "                     </div>";
    html += "                     <div class='col-5'>";
    html += "                         <input type='text' name='valor' class='form-control color-input' placeholder='Valor'>";
    html += "                     </div>";
    html += "                     <div class='col-2'>";
    html += "                   <div class='row' style='text-align: center;'>";
//                    html += "                        <div class='col-4 btnAction btnEditar' onclick='editar(this);'><img src='img/lapiz.svg' alt=''/><p>Editar</p></div>";
    html += "                        <div class='col-8 btnAction btnEliminar' onclick='eliminar(this);'>  <img src='img/bote-de-basura.svg' alt=''/><p>Eliminar</p></div>";
//                    html += "                        <div class='col-4 btnAction btnVer' onclick='ver(this);'>  <img src='img/cheuron.svg' alt=''/><p>Ver</p></div>";
    html += "                    </div>";
    html += "                     </div>";
    html += "                 </div>";
    html += "            </div>";
    $("#list_marcas").append(html);
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

function eliminar(itn) {
    var padre = $(itn).parent().parent().parent().parent();

    $(padre).remove();


    //  alert(data.id);
}