/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var url = "comprasController";
var cantidad = 10;
var pagina = 0;


$(window).on("load", function () {


    if (!sessionStorage.getItem("fechas")) {
        var dd, mm, yyyy;
        var today;

        today = new Date.today().add(1).months();

        dd = String(today.getDate()).padStart(2, '0');
        mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
        yyyy = today.getFullYear();
        document.getElementById("inpFechaFin").value = yyyy + '-' + mm + "-" + "01";
        today = new Date();

        dd = String(today.getDate()).padStart(2, '0');
        mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
        yyyy = today.getFullYear();
        document.getElementById("inpFecha").value = yyyy + '-' + mm + "-" + "01";
    } else {

        var data = JSON.parse(sessionStorage.getItem("fechas"));
        document.getElementById("inpFechaFin").value = data.ff
        document.getElementById("inpFecha").value = data.fi
    }

    $("#list_marcas").data("pagina", 0);
    pagina = 0;
    $("#list_marcas").html("");
    Lista_repuesto();
});

function buscar() {
    $("#list_marcas").data("pagina", 0);
    pagina = 0;
    $("#list_marcas").html("");
    if ($("#inpFecha").val()) {
        if ($("#inpFecha").val() > $("#inpFechaFin").val()) {
            alert("Fecha inicio deve ser menor ");
        } else {

            Lista_repuesto();
        }
    }



}

var enPedido = false;
function Lista_repuesto() {

    pagina = $("#list_marcas").data("pagina");

    var limite = $("#resultados").data("cantidad");
    if ((pagina * 10) >= limite) {
        return;
    }


    if (enPedido) {
        return;
    }
    var busq = $("#in_buscar").val() || "";
    // mostrar_progress();
    enPedido = true;
    $("#img-carga").css("display", "");
    var busqueda = $("#in_buscar").val() || "";
    var fecha = $("#inpFecha").val() || "";
    var fecha_fin = $("#inpFechaFin").val() || "";
    sessionStorage.setItem("fechas", JSON.stringify({fi: fecha, ff: fecha_fin}));
    if (pagina == 0) {
         $("#gananciaTotal").html("0 Bs." );
         $("#total_Venta").html("0 Bs." );
        $.post(url, {evento: "getGananciaTotal", fecha: fecha, fechaFin: fecha_fin, TokenAcceso: "servi12sis3"}, function (resp) {
            var json = JSON.parse(resp);
            var gan = JSON.parse(json.resp);

            $("#gananciaTotal").html(gan.ganancia_total.toFixed(2) + " Bs." );
            if(!gan.total_deuda){
                $("#total_Venta").html(gan.total_venta.toFixed(2) + " Bs.");
            }
            if(gan.total_deuda.toFixed(1) == 0){
                $("#total_Venta").html(gan.total_venta.toFixed(2) + " Bs.");
            }else{
                $("#total_Venta").html(gan.total_venta.toFixed(2) + " Bs. </br> Deuda:" +gan.total_deuda.toFixed(2));
            }
            

        });
    }


    $.post(url, {evento: "getVentasPagination", busqueda: busqueda, fecha: fecha, fechaFin: fecha_fin, cantidad: 10, pagina: pagina, TokenAcceso: "servi12sis3"}, function (resp) {
        // cerrar_progress();
        enPedido = false;
        $("#img-carga").css("display", "none");
        if (resp != null) {
            var obj = $.parseJSON(resp);
            if (obj.estado != "1") {

            } else {
                var json = $.parseJSON(obj.resp);
                var arr = json.arrayjson;
                $("#resultados").html("(" + json.count + ")");

                if (json.count == 0) {
                    return;
                }
                $("#resultados").data("cantidad", json.count);
                var gananciaTotal = 0;

                $.each(arr, function (i, obj) {
                    var html = "<div data-obj='" + JSON.stringify(obj) + "' class='list-group-item list-group-item-action flex-column align-items-start'>";
                    html += "               <div class='row iten_repuesto_row'>";
                    html += "                     <div class='col-2'>";
                    html += "                         <h6>" + obj.codigo + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-2'>";
                    html += "                         <h6>" + obj.fecha + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-2'>";
                    html += "                         <h6>" + obj.nit + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-2'>";
                    html += "                         <h6>" + obj.nombre_cliente + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-1'>";
                    html += "                         <h6>" + obj.ganancias.ingreso_total + " Bs.</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-1'>";
                    html += "                         <h6>" + obj.ganancias.ganancias + " Bs.</h6>";

                    html += "                     </div>";
                    html += "                     <div class='col-2'>";
                    html += "                   <div class='col-12 d-flex justify-content-center p-0' style='text-align: center;'>";
//                    html += "                        <div class='col-4 btnAction btnEditar' onclick='editar(this);'><img src='img/lapiz.svg' alt=''/><p>Editar</p></div>";
//                    html += "                        <div class='col-4 btnAction btnEliminar' onclick='eliminar(this);'>  <img src='img/bote-de-basura.svg' alt=''/><p>Eliminar</p></div>";
                    html += "                        <div class='col-4 btnAction btnVer' onclick='ver(this);'>  <img src='img/cheuron.svg' alt=''/><p>Ver</p></div>";
                    html += "                    </div>";
                    html += "                     </div>";
                    html += "                 </div>";
                    html += "            </div>";
                    $("#list_marcas").append(html);
                });
                pagina++;
                $("#list_marcas").data("pagina", pagina);

            }
        }

    });
}

window.onscroll = function () {
    var scrollHeight = $(document).height();
    var scrollPosition = $(window).height() + $(window).scrollTop();
    if ((scrollHeight - scrollPosition) < 1) {
        Lista_repuesto();
    }

};

function editar(itn) {
    var padre = $(itn).parent().parent().parent().parent();
    var data = $(padre).data("obj");

    window.location.href = "RepEdit.html?id=" + data.id;

}
function eliminar(itn) {
    var padre = $(itn).parent().parent().parent().parent();
    itnSelect = padre;
    var data = $(padre).data("obj");
    $("#marName").html(data.nombre);
    $("#btn_eliminar").data("id", data.id);
    $("#confirm-delete").modal("show");

    //  alert(data.id);
}
var itnSelect;
function ver(itn) {
    var padre = $(itn).parent().parent().parent().parent();
    var data = $(padre).data("obj");
    window.location.href = "VentaRepPerfil.html?id=" + data.id;

}

function ok_eliminar(itn) {
    var id = $(itn).data("id");
    mostrar_progress();
    $.post(url, {evento: "eliminar_repuesto", id: id, TokenAcceso: "servi12sis3"}, function (resp) {
        cerrar_progress();
        if (resp != null) {
            var obj = $.parseJSON(resp);
            if (obj.estado != "1") {
                alert(obj.mensaje);
            } else {
                var arr = parseFloat(obj.resp);
                $("#confirm-delete").modal("toggle");
                $(itnSelect).css("display", "none");

                // alert(arr);

            }
        }

    });
}