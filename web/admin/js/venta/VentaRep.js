/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var urlCompra = "comprasController";
var descuentoActual = 0;
$(document).ready(function () {
    var today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
    var yyyy = today.getFullYear();
    var code = Math.floor((Math.random() * 999) + 100) + "-" + Math.floor((Math.random() * 999) + 100) + "-" + Math.floor((Math.random() * 999) + 100);
    $("#inpCodigoCompra").val(code);
    $("#inpFecha").val(dd + '/' + mm + '/' + yyyy);
    $('#inpFecha').datepicker({
        format: 'dd/mm/yyyy',
        startDate: dd + '/' + mm + '/' + yyyy
    });
    getDescuentoActual();
    var usr_log = $.parseJSON(sessionStorage.getItem("usr_log"));
    $("#inpVendedor").val(usr_log.nombre + " " + usr_log.apellido_pa + " " + usr_log.apellido_ma);


//    alert(JSON.stringify(usr_log));
//    crear_factura();
});

function print() {
    const filename = 'ThisIsYourPDFFilename.pdf';

    html2canvas(document.querySelector('#pdfId'),
            {scale: 1}
    ).then(canvas => {
        let pdf = new jsPDF('l', 'px', [canvas.width, canvas.height]);

        pdf.addImage(canvas.toDataURL('image/png'), 'PNG', 0, 0, canvas.width / 1.4, canvas.height / 1.4);
        pdf.save(filename);
    });
}

function getDescuentoActual() {
    $.post("adminController", {evento: "getDescuentoActual", TokenAcceso: "servi12sis3"}, function (resp) {
        if (resp != null) {
            var obj = $.parseJSON(resp);
            if (obj.estado != "1") {
                alert(obj.mensaje);
            } else {
                descuentoActual = parseInt(obj.resp);
                //document.getElementById("descuentoActual").innerHTML = obj.resp + " %";
            }
        }
    });
}
function verArticulos() {
    $("#modalArticulos").modal();


}

function AggArticulo(itn) {
    var padre = $(itn).parent().parent().parent().parent();
    var object = $(padre).data("obj");
    var obj = object.repuesto;
    var otn = $("#listArticulos").find(".iditnart" + obj.id);

    if (otn.length > 0) {
        alert("Este repuesto ya fue agregado.");
        return;
    }
    if (obj.cantidad > 0) {

    } else {
        alert("Este repuesto esta agotado.");
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
    html += "                     <div class='col-2'>";
    html += "                         <textarea type='text' class='form-control inputDescripcion' aria-label='Default' rows='3'>";
    html += "Cat.: " + (object.categorias[0].nombre || "") + ". \n ";
//        html += "" + (object.categorias[0].name || "") + " ";
    if (object.categorias.length > 1) {
        html += "SubCat.: " + (object.categorias[1].nombre || "") + ". ";
//        html += "" + (object.categorias[1].name || "") + " ";
    }
    if (object.detalle != null) {
        for (var i = 0; i < object.detalle.length; i++) {
            if (object.detalle[i].valor.length > 0) {

                html += "**" + object.detalle[i].detalle + "";
                html += "*" + object.detalle[i].valor + "**";


            }

        }
    }

    html += "</textarea>";
    html += "                     </div>";
    html += "                     <div class='col-1'>";
    html += "                         <p>" + object.almacen + "</p>";
    console.log(object);
    html += "                     </div>";
    html += "                     <div class='col-1'>";
    html += "                         <input type='number' class='form-control inputCantidad' onkeyup='preventNumber(this);calcularTotales();' onchange='preventNumber(this);calcularTotales();' aria-label='Default' placeholder='0' value='1'/>";
    html += "                     </div>";
    html += "                     <div class='col-1'>";
    html += "                          <input type='number' class='form-control inputPrecio' onkeyup='preventNumber(this);calcularTotales();' onchange='preventNumber(this);calcularTotales();' aria-label='Default'  placeholder='0' value='" + obj.precio + "'/>";
    html += "                     </div>";
    html += "                     <div class='col-1'>";
    html += "                          <input type='number' class='form-control inputDescuento' onchange='calcularTotales();' aria-label='Default' placeholder='0' value='0'/>";
    html += "                     </div>";
    html += "                     <div class='col-1'>";
    html += "                          <input type='number' class='form-control inputSubTotal' onchange='calcularTotales();' aria-label='Default' placeholder='0' value='" + obj.precio + "' disabled/>";
    html += "                     </div>";
    html += "                     <div class='col-1'>";
    html += "                   <div class='' style='text-align: center;'>";
//                    html += "                        <div class='col-4 btnAction btnEditar' onclick='editar(this);'><img src='img/lapiz.svg' alt=''/><p>Editar</p></div>";
    html += "                        <div class='col-12 btnAction btnEliminar' onclick='eliminarArticulo(this);'>  <img src='img/bote-de-basura.svg' alt=''/><p>Eliminar</p></div>";
//    html += "                        <div class='col-sm btnAction btnVer' onclick='ver(this);'>  <img src='img/cheuron.svg' alt=''/><p>Ver</p></div>";
//    html += "                        <div class='col-sm btnAction btnEliminar' onclick='AggArticulo(this);'>  <img src='img/cheuron.svg' alt=''/><p>Agg</p></div>";
    html += "                    </div>";
    html += "                     </div>";
    html += "                 </div>";
    html += "            </div>";
    $("#listArticulos").append(html);
    $(".iditnart" + obj.id + "").find(".inputCantidad").focus(function () {
        $(this).select();
    });
    $(".iditnart" + obj.id + "").find(".inputCantidad").focus();
    calcularTotales();
}

function preventNumber(e) {
    var padre = $(e).parent().parent().parent();
    //itnSelect = padre;
    var data = $(padre).data("obj");
    var cantidad = data.cantidad || 0;

    if ($(e).val() <= 0) {
        $(e).val(1);
    }
    if ($(e).is(".inputCantidad") && $(e).val() > cantidad) {
        $(e).val(data.cantidad);
        alert("No contamos con mas articulos de este tipo.");
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
        var precio = $(obj).find(".inputPrecio").val();
        var descuento = $(obj).find(".inputDescuento").val();
        if (descuento < 0) {
            $(obj).find(".inputDescuento").val(0);
            var descuento = 0;
        } else
        if (descuento > descuentoActual) {
//            alert(descuentoActual);
            $(obj).find(".inputDescuento").val(descuentoActual);
            var descuento = descuentoActual;
        }
        subTotal = cantidad * precio;
        subTotal = subTotal - (subTotal * (descuento / 100))
        total += subTotal;
        $(obj).find(".inputSubTotal").val(subTotal);
        arrArticulos.push({
            nombre: data.nombre,
            descripcion: desc,
            cantidad: cantidad,
            precio: precio,
            subTotal: subTotal,
            id_articulo: data.id,
            descuento: descuento
        });
    });
    $("#inTotal").val(total);
    handleKeyCuotas();

}
var dataCuotas = [];

function realizarCompra() {
    
    // mostrar_progress();
//    print();

getPlanDetalle();
    
    var exito = true;
    var codigoCompra = $("#inpCodigoCompra").val() || null;
    var fechaCompra = $("#inpFecha").val() || null;
    var nombreProveedor = $("#inpProve").val() || "";
    var nit = $("#inpNit").val() || "";
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
        var cuotas = document.getElementById("inCuotas").value;
        $.post(urlCompra, {
            TokenAcceso: "servi12sis3",
            evento: "realizar_venta",
            codigo: codigoCompra,
            nombre_cliente: nombreProveedor,
            nit: nit,
            cuotas: cuotas,
            plandetalle: JSON.stringify(dataCuotas),
            fecha: fechaCompra,
            total: precioCompra,
            id_admin: usr_log.id,
            id_almacen: 1,
            arr_detalle: JSON.stringify(arrArticulos)
        }, function (resp) {
            cerrar_progress();
            if (resp != null) {
                var obj = $.parseJSON(resp);
                if (obj.estado != 1) {
                    alert(obj.mensaje);
                } else {

//                    print();
//                    alert(obj);
                window.location.href = "VentaRepPerfil.html?id=" + obj.resp ;
//                    window.location.href = "factura.html?id=" + obj.resp + "&tipe=venta";

                }
            }

        });
    }
}

function crear_factura() {
    var x = 0;
    var y = 0;

    var doc = new jsPDF('p', 'px', [200, 400]);
    doc.setFontSize(10);
    doc.setFont('courier')
    doc.setFontType('normal')
    doc.text(8, 8, 'COMPROVANTE DE VENTA');
    doc.setLineWidth(0.5);

    doc.line(5, 13, 145, 13); // horizontal line
    doc.setFontSize(8);
    doc.text(8, 30, 'FECHA:');
    doc.text(8, 20, 'CODIGO:');
    doc.text(8, 40, 'CLIENTE:');
    doc.text(8, 50, 'NIT:');
    doc.line(5, 54, 145, 54); // horizontal line
    doc.text(8, 60, 'PRODUCTOS');
    doc.line(5, 62, 145, 62);

    doc.save("First.pdf");
}


function handleKeyCuotas() {
    var elm = document.getElementById("inCuotas");
    dataCuotas=[];
    var fecha = $("#inpFecha").val();
    if (elm.value <= 0 || $("#inTotal").val() <= 0) {
        elm.value = 1;
        $("#planDePagos").css("display", "none");
    } else if (elm.value > 1) {
        $("#planDePagos").css("display", "");
        var html = "";
        
        for (var i = 0; i < elm.value; i++) {
            html += "<div class='row justify-content-start inCuotaDetalle'>";
            html += "                            <div class='col-2'>                                                                                                                ";
            html += "                               #";
            html += "                               <input type='number' class='form-control inNumero'  aria-label='Default' placeholder='0' value='" + (i + 1) + "'  disabled=''/>";
            html += "                           </div>";
            html += "                           <div class='col-5'>                                                                                                                ";
            html += "                            Fecha: <input type='text' class='form-control inFecha' aria-label='Default' placeholder='0' value='" + fecha + "'/>";
            html += "                           </div>";
            html += "                           <div class='col-5'>                                                                                                                ";
            html += "                               Monto: <input type='number' class='form-control montoCuota' aria-label='Default' placeholder='0' value='" + $("#inTotal").val() / elm.value + "' data-last='" + $("#inTotal").val() / elm.value + "' onchange='calculatePlan(this);'/>";
            html += "                           </div>";

            html += "                       </div>";
        }
        $("#planDetalle").html(html);


    } else {
        $("#planDePagos").css("display", "none");
    }

}


function calculatePlan(elm) {
    var datos = $("#planDetalle").find(".montoCuota");
    
    $.each(datos, function (i, obj) {
        

        if (obj == elm) {
        
        } else {

           $(obj).val((obj.value - ((elm.value - elm.dataset.last) / (datos.length - 1))).toFixed(3));
        }
    
    })
    elm.dataset.last = elm.value;
    
}

function calculatePlan(elm) {
    var datos = $("#planDetalle").find(".montoCuota");
    
    $.each(datos, function (i, obj) {
        

        if (obj == elm) {
        
        } else {

           $(obj).val((obj.value - ((elm.value - elm.dataset.last) / (datos.length - 1))).toFixed(3));
        }
    
    })
    elm.dataset.last = elm.value;
    
}
function getPlanDetalle() {
    var datos = $("#planDetalle").find(".inCuotaDetalle");
    dataCuotas = [];
    $.each(datos, function (i, obj) {
        console.log(obj);
    dataCuotas.push({
            numero: $(obj).find(".inNumero")[0].value,
            fecha: $(obj).find(".inFecha")[0].value,
            cuota: $(obj).find(".montoCuota")[0].value
        });
        
    
    })
console.log(dataCuotas);
    
}

