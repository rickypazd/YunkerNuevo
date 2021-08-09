/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var url = "repuestosController";
var cantidad = 10;
var pagina = 0;
$(document).ready(function () {
  
    $("#list_marcas").data("pagina", 0);
    pagina = 0;
    $("#list_marcas").html("");
    Lista_repuesto();
});

var tiempo = new Date().getTime();
var cantPed = 0;
var isRun = false;
function buscar() {
    tiempo = new Date().getTime();
    cantPed++;
}
buscarPendient();
function buscarPendient() {

    if (cantPed > 0) {
        var timp = new Date().getTime();
        if (timp - tiempo < 250) {
            isRun = true;
            timp = new Date().getTime();
        } else {
            $("#list_marcas").data("pagina", 0);
            pagina = 0;
            $("#list_marcas").html("");
            isRun = false;
            cantPed = 0;
            Lista_repuesto();
        }
    }
    setTimeout(buscarPendient, 100);
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
    if($("#fil_soloesquemas").is(':checked')){
        busq +=" url_foto_esquema...\".images";
//        alert("si");
    }
    if($("#fil_solopadres").is(':checked')){
        busq +=" id_padre...null";
//        alert("si");
    }
    // mostrar_progress();
    enPedido = true;
    $("#img-carga").css("display", "");
//    var busqueda = $("#in_buscar").val() || "";
    $.post(url, {evento: "getAll_repuesto_paginationJSON", busqueda: busq, cantidad: 10, pagina: pagina, TokenAcceso: "servi12sis3"}, function (resp) {
        // cerrar_progress();
        enPedido = false;
        $("#img-carga").css("display", "none");
        if (resp != null) {

            var obj = $.parseJSON(resp);
            if (obj.estado != "1") {
                alert(obj.mensaje);
            } else {

                var resp = obj.resp || "";
                if (resp.length <= 0) {
                    return;
                }
                var jsonresp = $.parseJSON(resp);
                var json = $.parseJSON(jsonresp.data);

                var cant = jsonresp.cantidad || 0;
                $("#resultados").html("(" + cant + ")");

                if (json.count == 0) {
                    return;
                }
                $("#resultados").data("cantidad", jsonresp.cantidad);
                $.each(json, function (i, obj) {
                    var html = "<div data-obj='" + JSON.stringify(obj.obj.repuesto) + "' class='list-group-item list-group-item-action flex-column align-items-start'>";
                    html += "               <div class='row iten_repuesto_row' >";
                    html += "                     <div class='col-1'>";
                    html += "                         <h6>" + obj.obj.repuesto.codigo + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-1'>";
                    html += "                         <img src='" +PARAMS.url_image+ obj.obj.repuesto.url_foto + "' class='' height='60px' alt='' />";
                    html += "                      </div>";
                    html += "                     <div class='col-2'>";
                    var nombre = obj.obj.repuesto.nombre || "";
                    html += "                         <h6>" + nombre + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-2'>";
                    html += "                     <div>";
                    html += "                         <h6>" + (obj.obj.categorias[0].nombre || "") + "</h6>";
                    html += "                         <h6>" + (obj.obj.categorias[0].name || "") + "</h6>";

                    html += "                         <hr/>";
                    if (obj.obj.categorias.length > 1) {
                        html += "                         <h6>" + (obj.obj.categorias[1].nombre || "") + "</h6>";
                        html += "                         <h6>" + (obj.obj.categorias[1].name || "") + "</h6>";
                    }

                    html += "                     </div>";
                    html += "                     </div>";
                    html += "                     <div class='col-2'>";
                    html += "                     <div>";
                    if (obj.obj.detalle != null) {
                        for (var i = 0; i < obj.obj.detalle.length; i++) {
                            if (obj.obj.detalle[i].valor.length > 0) {

                                html += "                         <h6>" + obj.obj.detalle[i].detalle + "</h6>";
                                html += "                         <h6>" + obj.obj.detalle[i].valor + "</h6>";

                                html += "                         <hr/>";
                            }

                        }
                    }



                    html += "                     </div>";
                    html += "                     </div>";
                    html += "                     <div class='col-1'>";
                    var cant = obj.obj.repuesto.precio || 0;
                    cant = Intl.NumberFormat('de-DE').format(cant);
                    html += "                         <h6>" + cant + " Bs.</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-1'>";
                    var cant = obj.obj.cantidad || 0;
                    html += "                         <h6>" + cant + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-2'>";
                    html += "                   <div class='col-12 d-flex justify-content-center  align-items-center p-0 h-100' style='text-align: center;'>";
//                    html += "                        <div class='col-4 btnAction btnEditar d-flex justify-content-center  align-items-center' onclick='editar(this);'><div><img src='img/lapiz.svg' alt=''/><p>Editar</p></div></div>";
                    html += "                        <div class='col-4 btnAction btnEliminar d-flex justify-content-center  align-items-center' onclick='eliminar(this);'>  <div><img src='img/bote-de-basura.svg' alt=''/><p>Eliminar</p></div></div>";
                    html += "                        <div class='col-4 btnAction btnVer d-flex justify-content-center  align-items-center' onclick='ver(this);'> <div> <img src='img/cheuron.svg' alt=''/><p>Ver</p></div></div>";
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
        $("#img-carga").css("display", "none");

    });
}

window.onscroll = function () {
    
    var scrollHeight = $(document).height();
    var scrollPosition = $(window).height() + $(window).scrollTop();
    if ((scrollHeight - scrollPosition) < 1) {
        if (!enPedido) {
            Lista_repuesto();
        }


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
    window.location.href = "RepPerfil.html?id=" + data.id;

}
function verTop(itn) {
    var padre = $(itn).parent();
    var data = $(padre).data("obj");
    window.location.href = "RepPerfil.html?id=" + data.id;

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