/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var urlRepuestos = "repuestosController";
var cantidad = 10;
var pagina = 0;
$(document).ready(function () {
    $("#list_partes").data("pagina", 0);

    pagina = 0;
    $("#list_partes").html("");
//    Lista_repuesto();
});
function buscarParte() {

    $("#list_partes").data("pagina", 0);
    pagina = 0;
    $("#list_partes").html("");
    Lista_repuesto();
}

var enPedido = false;
var $canvas = $("#myCanvas");
function Lista_repuesto() {

    pagina = $("#list_partes").data("pagina");

    var limite = $("#resultados").data("cantidad");
    if ((pagina * 10) >= limite) {
        return;
    }
    if (enPedido) {
        return;
    }
    var id_padre = getQueryVariable("id");

    if (id_padre > 0) {

    } else {
        return;
    }

    // mostrar_progress();
    enPedido = true;
    $("#img-carga").css("display", "");
    var busqueda = $("#in_buscar_parte").val() || "";
    $.post(urlRepuestos, {evento: "getAll_repuesto_partes_paginationJSON", busqueda: busqueda, cantidad: 10, pagina: pagina, id_padre: id_padre, TokenAcceso: "servi12sis3"}, function (resp) {
        // cerrar_progress();
        enPedido = false;
        $("#img-carga").css("display", "none");
        if (resp != null) {
            var obj = $.parseJSON(resp);
            if (obj.estado != "1") {
                alert(obj.mensaje);
            } else {
                if (obj.resp == null) {

                }
                var json = $.parseJSON(obj.resp);
                var arr = json.arrayjson;
                $("#resultados").html("(" + json.count + ")");

                if (json.count == 0) {
                    $("#resultados").data("cantidad", 1);
                    pagina++;
                    $("#list_partes").data("pagina", pagina);
                    return;
                }
                $("#resultados").data("cantidad", json.count);
                $("#list_partes").html("");
                //var context = $canvas.getContext('2d');
                $.each(arr, function (i, obj) {
                    var html = "<div data-obj='" + JSON.stringify(obj) + "' id='idparte" + obj.id + "' class='list-group-item list-group-item-action flex-column align-items-start parteiten'>";
                    html += "               <div class='row iten_repuesto_row'>";
                    html += "                     <div class='col-2'>";
                    html += "                         <h6>" + obj.codigo + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-1'>";
                    html += "                         <img src='"+PARAMS.url_image + obj.url_foto + "' class='' height='60px' alt='' />";
                    html += "                      </div>";
                    html += "                     <div class='col-3'>";
                    html += "                         <h6>" + obj.nombre + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-2'>";
                    html += "                         <h6>" + obj.precio + " Bs.</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-4'>";
                    html += "                   <div class='col-12' style='text-align: center;'>";
                    html += "                        <div class='col-4 btnAction btnEditar' onclick='editar_parte(this);'><img src='img/lapiz.svg' alt=''/><p>Editar</p></div>";
                    html += "                        <div class='col-4 btnAction btnEliminar' onclick='eliminar_parte(this);'>  <img src='img/bote-de-basura.svg' alt=''/><p>Eliminar</p></div>";
                    html += "                        <div class='col-4 btnAction btnVer' onclick='ver_parte(this);'>  <img src='img/cheuron.svg' alt=''/><p>Ver</p></div>";
                    html += "                    </div>";
                    html += "                     </div>";
                    html += "                 </div>";
                    html += "            </div>";
                    $("#list_partes").append(html);

                  
                        var objPos = obj.jsonpos;
                        $canvas.drawRect({
                            layer: true,
                            strokeStyle: 'black',
                            strokeWidth: 2,
                            name: obj.id + "",
                            x: objPos.x, y: objPos.y,
                            fromCenter: false,
                            width: Math.abs(objPos.width),
                            height: Math.abs(objPos.height),
                            mouseover: function (layer) {
                                $(this).animateLayer(layer, {
                                    fillStyle: '#66666666'
                                }, 500);
                                $(this).css("cursor", "pointer");
                                $(this).css("cursor", "pointer");
                            },
                            mouseout: function (layer) {
                                $(this).animateLayer(layer, {
                                    fillStyle: '#ffffff00'
                                }, 500);
                            },
                            click: function (layer) {
                                // Spin star
                                $(".parteiten").css("background-color", "#fff");
                                $("#idparte" + $(layer).attr('name')).css("background-color", "#66666666");
                                $('html, body').animate({
                                    scrollTop: ($("#idparte" + $(layer).attr('name')).offset().top - 300)
                                }, 100);
                            }
                        });
//                    } else {
//                        var html = "<div data-obj='" + JSON.stringify(obj) + "' id='idparte" + obj.id + "' class='list-group-item list-group-item-action flex-column align-items-start parteiten'>";
//                        html += "               <div class='row iten_repuesto_row'>";
//                        html += "                     <div class='col-2'>";
//                        html += "                         <h6>" + obj.codigo + "</h6>";
//                        html += "                     </div>";
//                        html += "                     <div class='col-1'>";
//                        html += "                         <img src='" + obj.url_foto + "' class='' height='60px' alt='' />";
//                        html += "                      </div>";
//                        html += "                     <div class='col-4'>";
//                        html += "                         <h6>" + obj.nombre + "</h6>";
//                        html += "                     </div>";
//                        html += "                     <div class='col-2'>";
//                        html += "                         <h6>" + obj.precio + "</h6>";
//                        html += "                     </div>";
//                        html += "                     <div class='col-3'>";
//                        html += "                   <div class='row' style='text-align: center;'>";
//                        html += "                        <div class='col-4 btnAction btnEditar' onclick='editar_parte(this);'><img src='img/lapiz.svg' alt=''/><p>Editar</p></div>";
//                        html += "                        <div class='col-4 btnAction btnEliminar' onclick='eliminar_parte(this);'>  <img src='img/bote-de-basura.svg' alt=''/><p>Eliminar</p></div>";
//                        html += "                        <div class='col-4 btnAction btnVer' onclick='registrarParteExist("+obj.id+");'>  <img src='img/cheuron.svg' alt=''/><p>Agregar</p></div>";
//                        html += "                    </div>";
//                        html += "                     </div>";
//                        html += "                 </div>";
//                        html += "            </div>";
//                        $("#listRepSinEsquema").append(html);
//                    }
                });
                pagina++;
                $("#list_partes").data("pagina", pagina);

            }
        }

    });
}

window.onscroll = function () {
    var scrollHeight = $(document).height();
    var scrollPosition = $(window).height() + $(window).scrollTop();
    if ((scrollHeight - scrollPosition) < 1) {
        // Lista_repuesto();
    }

};

function editar_parte(itn) {
    var padre = $(itn).parent().parent().parent().parent();
    var data = $(padre).data("obj");

    window.location.href = "RepEdit.html?id=" + data.id;

}
function eliminar_parte(itn) {
    var padre = $(itn).parent().parent().parent().parent();
    itnSelect = padre;
    var data = $(padre).data("obj");
    $("#marName").html(data.nombre);
    $("#btn_eliminar_part").data("id", data.id);
    $("#confirm-delete-part").modal("show");

    //  alert(data.id);
}
var itnSelect;
function ver_parte(itn) {
    var padre = $(itn).parent().parent().parent().parent();
    var data = $(padre).data("obj");
    window.location.href = "RepPerfil.html?id=" + data.id;

}

function ok_eliminar_part(itn) {
    var id = $(itn).data("id");
    mostrar_progress();
    $.post("repuestosController", {evento: "eliminar_repuesto", id: id, TokenAcceso: "servi12sis3"}, function (resp) {
        cerrar_progress();
        if (resp != null) {
            var obj = $.parseJSON(resp);
            if (obj.estado != "1") {
                alert(obj.mensaje);
            } else {
                var arr = parseFloat(obj.resp);
                $("#confirm-delete-part").modal("toggle");
                $(itnSelect).css("display", "none");

                // alert(arr);

            }
        }

    });
}