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
var state = {};
$(document).ready(function () {
    var id = getQueryVariable("id");


    mostrar_progress();
    $.post(url, {TokenAcceso: "servi12sis3", evento: "get_venta_id", id: id}, function (resp) {
        cerrar_progress();
        if (resp != null) {
            var objec = $.parseJSON(resp);
            if (objec.estado != "1") {
                alert(objec.mensaje);
            } else {
                var obj = $.parseJSON(objec.resp);

                state.obj = obj;
                $("#inpCodigoCompra").val(obj.codigo);
                $("#inpFecha").val(obj.fecha);
                $("#inpNit").val(obj.nit);
                $("#inpProve").val(obj.nombre_cliente);
                $("#inTotal").val(obj.total);
                $("#CantArt").html("(" + obj.detalle.length + ")");

                $("#inpVendedor").val(obj.administrador[0].nombre + " " + obj.administrador[0].apellido_pa + " " + obj.administrador[0].apellido_ma);
                ListaDetalle(obj);
                listaCuotas(obj);


            }
        }

    });



});
var total = 0;
function ListaDetalle(obj) {


    var arr = obj.detalle;
    // $("#resultados").html("(" + arr.length + ")");
    $.each(arr, function (i, obj) {
        total += obj.sub_total;
        var html = "<div data-obj='" + JSON.stringify(obj) + "'  class='list-group-item list-group-item-action flex-column align-items-start itnListaArticulo iditnart" + obj.id + "'>";
        html += "               <div class='row'>";
        html += "                     <div class='col-1'>";
        html += "                         <p>" + obj.id_articulo + "</p>";
        html += "                     </div>";

        html += "                     <div class='col-2'>";
        html += "                         <h6>" + obj.nombre + "</h6>";
        html += "                     </div>";
        html += "                     <div class='col-3'>";

        html += "                         <textarea type='text' class='form-control inputDescripcion' aria-label='Default' rows='3' disabled>";
//         html += "                         <input type='text' class='form-control inputDescripcion' aria-label='Default' value='" + obj.descripcion +  "' disabled/>";
        html += obj.descripcion;
        html += "</textarea>";
        html += "                     </div>";
        html += "                     <div class='col-5 row'>";
        html += "                     <div class='col-4'>";
        html += "                         <input type='number' class='form-control inputCantidad' onkeyup='preventNumber(this);calcularTotales();' onchange='preventNumber(this);calcularTotales();' aria-label='Default' placeholder='0' value='" + obj.cantidad + "' disabled/>";
        html += "                     </div>";
        html += "                     <div class='col-4'>";
        html += "                          <input type='number' class='form-control inputPrecio' onkeyup='preventNumber(this);calcularTotales();' onchange='preventNumber(this);calcularTotales();' aria-label='Default' placeholder='0' value='" + obj.precio + "' disabled/>";
        html += "                     </div>";
        html += "                     <div class='col-4' >";
        html += "                          <input type='number' class='form-control inputPrecio' onkeyup='preventNumber(this);calcularTotales();' onchange='preventNumber(this);calcularTotales();' aria-label='Default' placeholder='0' value='" + obj.descuento + "' disabled/>";
        html += "                     </div>";
        html += "                     <div class='col-4' style='padding:10px;'>";

        html += "                       <button type='button' class='btn btn-primary ' style='height:25px; padding:0;'  onclick='devolverIten(this);'>Devolver</button>";
        html += "                     </div>";
        html += "                     </div>";
        html += "                     <div class='col-1' >";
        html += "                          <input type='number' class='form-control inputSubTotal' onchange='calcularTotales();' aria-label='Default' placeholder='0' value='" + obj.sub_total + "' disabled/>";
        html += "                     </div>";
        html += "                     <div class='col-12' >";
        html += cargarDevoluciones(obj)
        html += "                     </div>";

        html += "                 </div>";
        html += "            </div>";
        $("#listArticulos").append(html);
    });
    $("#inTotal").val(total);
    state.obj.total = total;
}


function cargarDevoluciones(obj) {

    var subTotal = obj.sub_total;
    var arr = obj.devoluciones;

    // $("#resultados").html("(" + arr.length + ")");
    if (!arr[0])
        return "";
    var html = "";
    $.each(arr, function (i, obj) {
        total += obj.sub_total;
        subTotal += obj.sub_total;
        html += "               <div class='row'>";
        html += "                     <div class='col-1'>";
        html += "                     </div>";

        html += "                     <div class='col-2'>";
        html += "                     </div>";
        html += "                     <div class='col-3'>";

//         html += "                         <input type='text' class='form-control inputDescripcion' aria-label='Default' value='" + obj.descripcion +  "' disabled/>";
        html += "Devuelto el: " + obj.descripcion;
        html += "                     </div>";
        html += "                     <div class='col-5 row'>";
        html += "                     <div class='col-4'>";
        html += "                         <input type='number' class='form-control inputCantidad' onkeyup='preventNumber(this);calcularTotales();' onchange='preventNumber(this);calcularTotales();' aria-label='Default' placeholder='0' value='" + obj.cantidad + "' disabled/>";
        html += "                     </div>";
        html += "                     <div class='col-4'>";
        html += "                          <input type='number' class='form-control inputPrecio' onkeyup='preventNumber(this);calcularTotales();' onchange='preventNumber(this);calcularTotales();' aria-label='Default' placeholder='0' value='" + obj.precio + "' disabled/>";
        html += "                     </div>";
        html += "                     <div class='col-4' >";
        html += "                     </div>";
        html += "                     <div class='col-4' style='padding:10px;'>";

        html += "                     </div>";
        html += "                     </div>";
        html += "                     <div class='col-1' >";
        html += "                          <input type='number' class='form-control inputSubTotal' onchange='calcularTotales();' aria-label='Default' placeholder='0' value='" + obj.sub_total + "' disabled/>";
        html += "                     </div>";
        html += "                     <div class='col-12' >";
        html += "                     </div>";

        html += "                 </div>";

    });



    html += "               <div class='col-12 row'>";
    html += "                     <div class='col-8'>";
    html += "                     </div>";
    html += "                     <div class='col-2'>";

    html += "                   Sub-total";
    html += "                     </div>";
    html += "                     <div class='col-2' >";
    html += "                          <input type='number' class='form-control inputSubTotalFinal' onchange='calcularTotales();' aria-label='Default' placeholder='0' value='" + subTotal + "' disabled/>";

    html += "                     </div>";

    html += "                 </div>";
    return html;
}



function verComprovante() {
    var id = getQueryVariable("id");
    window.location.href = "factura.html?id=" + id + "&tipe=venta";
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

var itemDevuelto;
var cantidad = 0;
var sub_total = 0;
function devolverIten(itn) {
    $("#modalDevolucion").modal("show");
    var padre = $(itn).parent().parent().parent().parent();
    //itnSelect = padre;
    itemDevuelto = $(padre).data("obj");

    var arr = itemDevuelto.devoluciones;
    cantidad = itemDevuelto.cantidad;
    sub_total = itemDevuelto.sub_total;
    if (arr[0]) {
        $.each(arr, function (i, obj) {
            cantidad -= obj.cantidad;
            sub_total += obj.sub_total;
        });
    }
    
        var maximo = (itemDevuelto.sub_total / itemDevuelto.cantidad)
    var cuotas = state.obj.cuotas;
    if (cuotas) {
        var tcu = 0;
        for (var i = 0; i < cuotas.length; i++) {
            if (cuotas[i].fecha_on) {
                tcu += cuotas[i].monto;
            }
        }
        var porcent = ((tcu) / state.obj.total);
       var  maximoNew = (maximo * porcent).toFixed(2);
    }

//    var maximo = (itemDevuelto.sub_total / itemDevuelto.cantidad)
    $("#can_dev").val(1);
    $("#mon_dev").val(maximo);
    calcularDev();

}
function calcularDev() {

    var maximo = (itemDevuelto.sub_total / itemDevuelto.cantidad)
    var cuotas = state.obj.cuotas;
     var   maximoNew;
    if (cuotas) {
        var tcu = 0;
        for (var i = 0; i < cuotas.length; i++) {
            if (cuotas[i].fecha_on) {
                tcu += cuotas[i].monto;
            }
        }
        var porcent = ((tcu) / state.obj.total);
      maximoNew= (maximo * porcent).toFixed(2);
    }



    if ($("#can_dev").val() < 0) {
        $("#can_dev").val(0);
    } else if ($("#can_dev").val() > cantidad) {
        $("#can_dev").val(cantidad);
    }
    if ($("#mon_dev").val() < 0) {
        $("#mon_dev").val(0);
    } else if ($("#mon_dev").val() > maximo) {
        $("#mon_dev").val(maximo);
    }
    if(maximoNew){
         $("#total_mon_dev").val((($("#can_dev").val() * $("#mon_dev").val())*porcent).toFixed(2));
    }else{
         $("#total_mon_dev").val(($("#can_dev").val() * $("#mon_dev").val()));
    }
   
}
function realizarDevolucion() {
    var exito = true;
    if ($("#can_dev").val() == 0) {
        $("#can_dev").css("border-color", "#f00");
        exito = false;
    } else {
        $("#can_dev").css("border-color", "#aaa");
    }

    if (exito) {
        itemDevuelto.cantidad = $("#can_dev").val();
        itemDevuelto.precio = $("#mon_dev").val();
        itemDevuelto.sub_total = $("#total_mon_dev").val();
        mostrar_progress();
        var usr_log = $.parseJSON(sessionStorage.getItem("usr_log"));
        $.post(url, {TokenAcceso: "servi12sis3", evento: "devolverArticulo", obj: JSON.stringify(itemDevuelto), id_admin: usr_log.id}, function (resp) {
            cerrar_progress();
            if (resp != null) {
                var objec = $.parseJSON(resp);
                if (objec.estado != "1") {
                    alert(objec.mensaje);
                } else {
                    verComprovante();
//                    window.location.reload();
                }
            }
        });
    }
}



function listaCuotas(obj) {
    var elm = document.getElementById("inCuotas");

    var data = obj.cuotas;
    elm.value = data.length || 1;

    if (elm.value <= 0) {
        elm.value = 1;
        $("#planDePagos").css("display", "none");
    } else if (elm.value > 1) {
        $("#planDePagos").css("display", "");
        var html = "";




        for (var i = 0; i < elm.value; i++) {
            html += "<div class='row justify-content-start inCuotaDetalle'>";
            html += "                            <div class='col-2'>                                                                                                                ";
            html += "                               #";
            html += "                               <input type='number' class='form-control inNumero'  aria-label='Default' placeholder='0' value='" + data[i].numero + "'  disabled/>";
            html += "                           </div>";
            html += "                           <div class='col-4'>                                                                                                                ";
            html += "                            Fecha: <input type='text' class='form-control inFecha' aria-label='Default' placeholder='0' value='" + data[i].fecha + "' disabled/> ";
            html += "                           </div>";
            html += "                           <div class='col-3'>                                                                                                                ";
            html += "                               Monto: <input type='number' class='form-control montoCuota' aria-label='Default' placeholder='0' value='" + data[i].monto.toFixed(3) + "' disabled onchange='calculatePlan(this);'/>";
            html += "                           </div>";
            html += "                           <div class='col-3'>                                                                                                                ";
            if (!data[i].fecha_on) {
                html += "                               Estado:<input type='text' class='form-control' aria-label='Default' style='background:#1b7bff; color:#fff' placeholder='0' value='Pagar' onClick='pagarCuota(this);' data-obj='" + JSON.stringify(data[i]) + "'/>";
            } else {
                html += "                               Estado:<input type='text' class='form-control' aria-label='Default' placeholder='0' value='" + data[i].fecha_on.substring(0, 10) + "' disabled />";

            }

            html += "                           </div>";

            html += "                       </div>";
        }
        $("#planDetalle").html(html);


    } else {
        $("#planDePagos").css("display", "none");
    }

}

var itenSelect;
function pagarCuota(elm) {
    var obj = JSON.parse(elm.dataset.obj);
    var html = "";

    html += "<div class='row justify-content-center inCuotaDetalle'>";
    html += "                            <div class='col-2'>                                                                                                                ";
    html += "                               #";
    html += "                               <input type='number' class='form-control inNumero'  aria-label='Default' placeholder='0' value='" + obj.numero + "'  disabled/>";
    html += "                           </div>";
    html += "                           <div class='col-4'>                                                                                                                ";
    html += "                            Fecha: <input type='text' class='form-control inFecha' aria-label='Default' placeholder='0' value='" + obj.fecha + "' disabled/> ";
    html += "                           </div>";
    html += "                           <div class='col-3'>                                                                                                                ";
    html += "                               Monto: <input type='number' class='form-control montoCuota' aria-label='Default' placeholder='0' value='" + obj.monto.toFixed(3) + "' disabled onchange='calculatePlan(this);'/>";
    html += "                           </div>";


    html += "                       </div>";


    $("#detalleCuota").html(html);
    $("#modalConfirmarPago").modal();
    itenSelect = obj;


}

function okPagarCuota() {

    $.post(url, {TokenAcceso: "servi12sis3", evento: "pagar_cuota", id: itenSelect.id}, function (resp) {
        window.location.reload();
    })


}