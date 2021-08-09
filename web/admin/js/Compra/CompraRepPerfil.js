/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var url = "comprasController";
$(document).ready(function () {
    var id = getQueryVariable("id");


    mostrar_progress();
    $.post(url, {TokenAcceso: "servi12sis3", evento: "get_compra_id", id: id}, function (resp) {
        cerrar_progress();
        if (resp != null) {
            var objec = $.parseJSON(resp);
            if (objec.estado != "1") {
                alert(objec.mensaje);
            } else {
                var obj = $.parseJSON(objec.resp);


                $("#inpCodigoCompra").val(obj.codigo);
                $("#inpFecha").val(obj.fecha);
                $("#inpNit").val(obj.nit);
                $("#inpProve").val(obj.nombre_proveedor);
                $("#inTotal").val(obj.total);
                $("#CantArt").html("(" + obj.detalle.length + ")");
                $("#inpVendedor").val(obj.administrador[0].nombre + " " + obj.administrador[0].apellido_pa + " " + obj.administrador[0].apellido_ma);
                ListaDetalle(obj);


            }
        }

    });



});

function ListaDetalle(obj) {


    var arr = obj.detalle;
    // $("#resultados").html("(" + arr.length + ")");
    $.each(arr, function (i, obj) {
        var html = "<div data-obj='" + JSON.stringify(obj) + "'  class='list-group-item list-group-item-action flex-column align-items-start itnListaArticulo iditnart" + obj.id + "'>";
        html += "               <div class='row'>";
        html += "                     <div class='col-1'>";
        html += "                         <p>" + obj.id_articulo + "</p>";
        html += "                     </div>";

        html += "                     <div class='col-3'>";
        html += "                         <h6>" + obj.nombre + "</h6>";
        html += "                     </div>";
        html += "                     <div class='col-4'>";
        html += "                         <textarea type='text' class='form-control inputDescripcion' aria-label='Default' rows='3' disabled>";
//         html += "                         <input type='text' class='form-control inputDescripcion' aria-label='Default' value='" + obj.descripcion +  "' disabled/>";
        html += obj.descripcion;
        html += "</textarea>";
        html += "                     </div>";
        html += "                     <div class='col-1'>";
        html += "                         <input type='number' class='form-control inputCantidad' onkeyup='preventNumber(this);calcularTotales();' onchange='preventNumber(this);calcularTotales();' aria-label='Default' placeholder='0'  value='" + obj.cantidad + "' disabled/>";
        html += "                     </div>";
        html += "                     <div class='col-1' style='max-width:12.499999995%;flex-basis:12.499999995%;'>";
        html += "                          <input type='number' class='form-control inputPrecio' onkeyup='preventNumber(this);calcularTotales();' onchange='preventNumber(this);calcularTotales();' aria-label='Default' placeholder='0' value='" + obj.precio + "' disabled/>";
        html += "                     </div>";
        html += "                     <div class='col-1' style='max-width:12.499999995%;flex-basis:12.499999995%;'>";
        html += "                          <input type='number' class='form-control inputSubTotal' onchange='calcularTotales();' aria-label='Default' placeholder='0' value='" + obj.precio + "' disabled/>";
        html += "                     </div>";

        html += "                 </div>";
        html += "            </div>";
        $("#listArticulos").append(html);
    });
}

function verComprovante() {
    var id = getQueryVariable("id");
    window.location.href = "factura.html?id=" + id + "&tipe=compra";
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
