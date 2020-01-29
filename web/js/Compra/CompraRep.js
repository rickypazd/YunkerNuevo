/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var urlCompra = "comprasController";
$(document).ready(function () {
    var today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
    var yyyy = today.getFullYear();

    var code = Math.floor((Math.random() * 999) + 100) + "-"+Math.floor((Math.random() * 999) + 100)+"-"+Math.floor((Math.random() * 999) + 100);
    $("#inpCodigoCompra").val(code);
    $("#inpFecha").val(dd + '/' + mm + '/' + yyyy);
    $('#inpFecha').datepicker({
       format: 'dd/mm/yyyy',
       startDate: dd + '/' + mm + '/' + yyyy
    });
    
    cargarAlmacenes();
});
function verArticulos() {
    $("#modalArticulos").modal();


}

function AggArticulo(itn) {
    var padre = $(itn).parent().parent().parent().parent();
    var obj = $(padre).data("obj");
    var otn = $("#listArticulos").find(".iditnart" + obj.id);
    if (otn.length > 0) {
        alert("Este repuesto ya fue agregado.")
        return;
    }
    $("#modalArticulos").modal("toggle");
    var html = "<div data-obj='" + JSON.stringify(obj) + "'  class='list-group-item list-group-item-action flex-column align-items-start itnListaArticulo iditnart" + obj.id + "'>";
    html += "               <div class='row'>";
    html += "                     <div class='col-1'>";
    html += "                         <p>" + obj.codigo + "</p>";
    html += "                     </div>";
    html += "                     <div class='col-1'>";
    html += "                         <img src='" + obj.url_foto + "' class='' height='60px' alt='' />";
    html += "                      </div>";
    html += "                     <div class='col-2'>";
    html += "                         <h6>" + obj.nombre + "</h6>";
    html += "                     </div>";
    html += "                     <div class='col-3'>";
    html += "                         <input type='text' class='form-control inputDescripcion' aria-label='Default'/>";
    html += "                     </div>";
    html += "                     <div class='col-1'>";
    html += "                         <input id='inalma"+obj.id+"' type='text' class='form-control inputAlmacen' aria-label='Default' onclick='verAlmacenes(this);'/>";
    html += "                     </div>";
    html += "                     <div class='col-1'>";
    html += "                         <input type='number' class='form-control inputCantidad' onchange='preventNumber(this);calcularTotales();' aria-label='Default' placeholder='0' value='1'/>";
    html += "                     </div>";
    html += "                     <div class='col-1'>";
    html += "                          <input type='number' class='form-control inputPrecio' onchange='preventNumber(this);calcularTotales();' aria-label='Default' placeholder='0' value='" + obj.precio + "'/>";
    html += "                     </div>";
    html += "                     <div class='col-1'>";
    html += "                          <input type='number' class='form-control inputSubTotal' onchange='calcularTotales();' aria-label='Default' placeholder='0' value='" + obj.precio + "' disabled/>";
    html += "                     </div>";
    html += "                     <div class='col-1'>";
    html += "                   <div class='row' style='text-align: center;'>";
//                    html += "                        <div class='col-4 btnAction btnEditar' onclick='editar(this);'><img src='img/lapiz.svg' alt=''/><p>Editar</p></div>";
    html += "                        <div class='col-sm btnAction btnEliminar' onclick='eliminarArticulo(this);'>  <img src='img/bote-de-basura.svg' alt=''/><p>Eliminar</p></div>";
//    html += "                        <div class='col-sm btnAction btnVer' onclick='AgregarAlmacen(this);'>  <img src='img/cheuron.svg' alt=''/><p>Agregar</p></div>";
//    html += "                        <div class='col-sm btnAction btnEliminar' onclick='AggArticulo(this);'>  <img src='img/cheuron.svg' alt=''/><p>Agg</p></div>";
    html += "                    </div>";
    html += "                     </div>";
    html += "                 </div>";
    html += "            </div>";
    $("#listArticulos").append(html);
    $("#inalma"+obj.id).click();
    calcularTotales();
}

function preventNumber(e) {
    if ($(e).val() <= 0) {
        $(e).val(1);
    }

}
function eliminarArticulo(itn) {
    var padre = $(itn).parent().parent().parent().parent();
    //itnSelect = padre;
    var data = $(padre).data("obj");
    $(padre).remove();
    calcularTotales();
    //  alert(data.id);
}
var arrArticulos = [];
function calcularTotales() {

    arrArticulos = [];
    var datos = $("#listArticulos").find(".itnListaArticulo");
    $("#CantArt").html("(" + datos.length + ")");
    var total = 0;
    var subTotal;
    $.each(datos, function (i, obj) {
        subTotal = 0;
        var data = $(obj).data("obj");
        var desc = $(obj).find(".inputDescripcion").val() || "";
        var cantidad = $(obj).find(".inputCantidad").val();
        var id_almacen = $(obj).find(".inputAlmacen").data("obj");
        if(id_almacen==null){
            $(obj).find(".inputAlmacen").css("background-color","#df5b5b");
        }else{
             $(obj).find(".inputAlmacen").css("background-color","#fff");
        }
        var precio = $(obj).find(".inputPrecio").val();
        subTotal = cantidad * precio;
        total += subTotal;
        $(obj).find(".inputSubTotal").val(subTotal);
        arrArticulos.push({
            nombre: data.nombre,
            descripcion: desc,
            id_almacen: id_almacen.id,
            cantidad: cantidad,
            precio: precio,
            subTotal: subTotal,
            id_articulo: data.id
        });
    });
    $("#inTotal").val(total);
}


function realizarCompra() {
    // mostrar_progress();
    calcularTotales();
    var exito = true;
    var codigoCompra = $("#inpCodigoCompra").val() || null;
    var fechaCompra = $("#inpFecha").val() || null;
    var nombreProveedor = $("#inpProve").val() || null;
    var precioCompra = $("#inTotal").val() || null;
    var TokenAcceso = "servi12sis3";
    var usr_log = $.parseJSON(sessionStorage.getItem("usr_log"));
    if (codigoCompra != null && codigoCompra.length > 0) {
        $("#inpCodigoCompra").css("background", "#ffffff");
    } else {
        $("#inpCodigoCompra").css("background", "#df5b5b");
        exito = false;
    }
    if (fechaCompra != null && fechaCompra.length > 0) {
        $("#inpFecha").css("background", "#ffffff");
    } else {
        $("#inpFecha").css("background", "#df5b5b");
        exito = false;
    }
    if (nombreProveedor != null && nombreProveedor.length > 0) {
        $("#inpProve").css("background", "#ffffff");
    } else {
        $("#inpProve").css("background", "#df5b5b");
        exito = false;
    }


    if (arrArticulos.length <= 0) {
        alert("Deve ingresar articulos.");

    }
    if (exito) {
        mostrar_progress();
        $.post(urlCompra, {
            TokenAcceso: "servi12sis3",
            evento: "realizar_compra",
            codigo: codigoCompra,
            nombre_proveedor: nombreProveedor,
            fecha: fechaCompra,
            total: precioCompra,
            id_admin: usr_log.id,
            arr_detalle: JSON.stringify(arrArticulos)
        }, function (resp) {
            cerrar_progress();
            if (resp != null) {
                var obj = $.parseJSON(resp);
                if (obj.estado != 1) {
                    alert(obj.mensaje);
                } else {
                    alert(obj.mensaje);
                    window.location.reload();
                }
            }

        });
    }
}

var itenTemp;
function verAlmacenes(itn){
    itenTemp= itn;
    $("#modalAlmacenes").modal('show');
}



function cargarAlmacenes(){
    var busqueda = $("#in_buscarAlmacen").val() || "";
    $.post("almacenController", {evento: "getAll_almacen_pagination", busqueda: busqueda, cantidad: 1000, pagina: 0, TokenAcceso: "servi12sis3"}, function (resp) {
        // cerrar_progress();
        enPedido = false;
        $("#img-carga").css("display", "none");
        if (resp != null) {
            var obj = $.parseJSON(resp);
            if (obj.estado != "1") {
                alert(obj.mensaje);
            } else {
                var json = $.parseJSON(obj.resp);
                var arr = json.arrayjson;
                $("#resultadosAlmacen").html("(" + json.count + ")");

                if (json.count == 0) {
                    return;
                }
                $("#resultadosAlmacen").data("cantidad", json.count);
                $.each(arr, function (i, obj) {
                    var html = "<div data-obj='" + JSON.stringify(obj) + "' class='list-group-item list-group-item-action flex-column align-items-start'>";
                    html += "               <div class='row iten_repuesto_row'>";
                    html += "                     <div class='col-2'>";
                    html += "                         <h6>" + obj.nombre + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-3'>";
                    html += "                         <h6>" + obj.direccion + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-2'>";
                    html += "                         <h6>" + obj.lat + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-2'>";
                    html += "                         <h6>" + obj.lng + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-3'>";
                    html += "                   <div class='row' style='text-align: center;'>";
//                    html += "                        <div class='col-4 btnAction btnEditar' onclick='editar(this);'><img src='img/lapiz.svg' alt=''/><p>Editar</p></div>";
//                    html += "                        <div class='col-4 btnAction btnEliminar' onclick='eliminar(this);'>  <img src='img/bote-de-basura.svg' alt=''/><p>Eliminar</p></div>";
                    html += "                        <div class='col-sm btnAction btnVer' onclick='AgregarAlmacen(this);'>  <img src='img/cheuron.svg' alt=''/><p>Agregar</p></div>";
//                    html += "                        <div class='col-4 btnAction btnVer' onclick='ver(this);'>  <img src='img/cheuron.svg' alt=''/><p>Ver</p></div>";
                    html += "                    </div>";
                    html += "                     </div>";
                    html += "                 </div>";
                    html += "            </div>";
                    $("#list").append(html);
                });
                
                

            }
        }

    });
}


function AgregarAlmacen(itn){
    var padre = $(itn).parent().parent().parent().parent();
    //itnSelect = padre;
    var data = $(padre).data("obj");
    $(itenTemp).val(data.nombre);
    $(itenTemp).data("obj",data);
        
     $("#modalAlmacenes").modal('toggle');
     inciarAlmacen(data.id);
     $("#modalAlmacenesdet").modal('show');
}