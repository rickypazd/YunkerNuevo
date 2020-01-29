/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var url = "repuestosController";
var urlAutos = "autosController";
$(document).ready(function () {
    var id = getQueryVariable("id");
    $("#editarPerfil").attr("href", "RepEdit.html?id=" + id);
    $("#editarVehiculos").attr("href", "RepEdit.html?id=" + id);
    $("#btnAggParte").attr("href", "RepParteCreate.html?id=" + id);
    mostrar_progress();
    $.post(url, {TokenAcceso: "servi12sis3", evento: "getRepuestoByIdJson", id: id}, function (resp) {
        cerrar_progress();
        if (resp != null) {
            var objec = $.parseJSON(resp);
            if (objec.estado != "1") {
                alert(objec.mensaje);
            } else {
                var obj = $.parseJSON(objec.resp);
                var objRep = obj.repuesto;
                var objCategoria = $.parseJSON(obj.categoria);
                //alert(obj);   

                $("#nombre_perfil").html(objRep.nombre);
                $("#foto_perfil").attr("src", objRep.url_foto);
                if (objCategoria.length > 1) {
                    $("#p_categoria").html(objCategoria[1].nombre);
                    $("#p_subcat").html(objCategoria[0].nombre);
                }else{
                    $("#p_categoria").html(objCategoria[0].nombre);
                }

                $("#p_nombre").html(objRep.nombre);
                $("#p_codigo").html(objRep.codigo);
                $("#p_precio").html(objRep.precio);
                if (objRep.id_padre) {
                    agg_padre({id: objRep.id_padre, nombre: "ver en esquema"});
                }
                $("#pCantidad").html(objRep.cantidad || 0);
                if (objRep.url_foto_esquema != null) {
                    $("#contenEsquema").css("display", "");
                    cargar_imagen_canvas(objRep);
                } else {
                    $("#contensinEsquema").css("display", "");
                }
                $("#qrcode").qrcode({
                    text: "{tipo:1,id:" + objRep.id + ",pag:'yunkerRep'}"
                });

                cargar_vehiculos_activos();

//                var lista = obj.arr_detalle;
//                $.each(lista, function (i, obj) {
//                agg_detalle_parte(obj);
//                });
//                var listajson = obj.json;
//                $.each(listajson, function (i, obj) {
//                agg_padre(obj);
//                });
//                $("#p_apellidopa").html(obj.apellido_pa);
//                $("#p_apellidoma").html(obj.apellido_ma);
//                $("#p_usuario").html(obj.usuario);


            }
        }

    });
});
function agg_padre(obj) {
    var html = "";
    html += " \t\t / \t<a href='RepPerfil.html?id=" + obj.id + "'>" + obj.nombre + "</a>";
    $("#list_padres").prepend(html);
}
function agg_detalle_parte(obj) {
    var html = "";
    html += " <div class='row'>";
    html += "                                            <div class='col-md-6'>";
    html += "                                         <label>" + obj.detalle + "</label>";
    html += "                                     </div>";
    html += "                                     <div class='col-md-6'>";
    html += "                                         <p>" + obj.valor + "</p>";
    html += "                                     </div>";
    html += "                                 </div>";
    $("#lista_detalle").append(html);
}

function agg_vehiculo_rep() {
    $("#AggVehiculo").modal("show");
    var id = getQueryVariable("id");
    var busqueda = $("#in_buscar_ve_dis").val() || "";
    mostrar_progress();
    $.post(urlAutos, {TokenAcceso: "servi12sis3", evento: "getAll_autos_disponibles", id: id, busqueda: busqueda}, function (resp) {
        cerrar_progress();
        if (resp != null) {
            var objec = $.parseJSON(resp);
            if (objec.estado != "1") {
                alert(objec.mensaje);
            } else {
                $("#list_autos_agg").html("");
                if (objec.resp == null) {
                    $("#resultados_veh").html("( " + 0 + " )");
                    return;
                }
                var obj = $.parseJSON(objec.resp);
                //alert(obj);   


                $("#resultados_veh").html("( " + obj.length + " )");
                $.each(obj, function (i, obj) {
                    var html = "<div data-obj='" + JSON.stringify(obj) + "' class='list-group-item list-group-item-action flex-column align-items-start'>";
                    html += "               <div class='row'>";
                    html += "                     <div class='col-1'>";
                    html += "                         <h6>" + obj.clave + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-1'>";
                    html += "                         <img src='" + obj.url_foto_marca + "' class='' height='60px' alt='' />";
                    html += "                      </div>";
                    html += "                     <div class='col-2'>";
                    html += "                         <h6>" + obj.nombre_marca + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-1'>";
                    html += "                         <img src='" + obj.url_foto_modelo + "' class='' height='60px' alt='' />";
                    html += "                      </div>";
                    html += "                     <div class='col-2'>";
                    html += "                         <h6>" + obj.nombre_modelo + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-2'>";
                    html += "                         <h6>" + obj.anho + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-2'>";
                    html += "                         <h6>" + obj.nombre_version + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-1'>";
                    html += "                   <div class='row' justify-content-center  style='text-align: center;'>";
//                    html += "                        <div class='col-4 btnAction btnEditar' onclick='editar(this);'><img src='img/lapiz.svg' alt=''/><p>Editar</p></div>";
//                        html += "                        <div class='col-4 btnAction btnEliminar' onclick='eliminar(this);'>  <img src='img/bote-de-basura.svg' alt=''/><p>Eliminar</p></div>";
                    html += "                        <div class='col-12 btnAction btnVer' onclick='agg_auto_a_rep(this);'>  <img src='img/cheuron.svg' alt=''/><p>Agg</p></div>";
                    html += "                    </div>";
                    html += "                     </div>";
                    html += "                 </div>";
                    html += "            </div>";
                    $("#list_autos_agg").append(html);
                });
//                $("#p_apellidopa").html(obj.apellido_pa);
//                $("#p_apellidoma").html(obj.apellido_ma);
//                $("#p_usuario").html(obj.usuario);


            }
        }

    });
}

function cargar_vehiculos_activos() {

    var id = getQueryVariable("id");
    var busqueda = "";
    //mostrar_progress();
    $.post(urlAutos, {TokenAcceso: "servi12sis3", evento: "getAll_autos_activos", id: id, busqueda: busqueda}, function (resp) {
        //cerrar_progress();
        if (resp != null) {
            var objec = $.parseJSON(resp);
            if (objec.estado != "1") {
                alert(objec.mensaje);
            } else {
                if (objec.resp == null) {
                    return;
                }
                var obj = $.parseJSON(objec.resp);
                //alert(obj);   


                $("#list_autos_activos").html("");
                $.each(obj, function (i, obj) {
                    var html = "<div data-obj='" + JSON.stringify(obj) + "' class='list-group-item list-group-item-action flex-column align-items-start'>";
                    html += "               <div class='row iten_repuesto_row'>";
                    html += "                     <div class='col-1'>";
                    html += "                         <h6>" + obj.clave + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-1'>";
                    html += "                         <img src='" + obj.url_foto_marca + "' class='' height='60px' alt='' />";
                    html += "                      </div>";
                    html += "                     <div class='col-2'>";
                    html += "                         <h6>" + obj.nombre_marca + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-1'>";
                    html += "                         <img src='" + obj.url_foto_modelo + "' class='' height='60px' alt='' />";
                    html += "                      </div>";
                    html += "                     <div class='col-2'>";
                    html += "                         <h6>" + obj.nombre_modelo + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-1'>";
                    html += "                         <h6>" + obj.anho + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-2'>";
                    html += "                         <h6>" + obj.nombre_version + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-2'>";
                    html += "                   <div class='row' style='text-align: center;'>";
//                    html += "                        <div class='col-4 btnAction btnEditar' onclick='editar(this);'><img src='img/lapiz.svg' alt=''/><p>Editar</p></div>";

                    if (obj.id_repuesto == id) {
                        html += "                        <div class='col-6 btnAction btnEliminar' onclick='eliminar_rep_auto(this);'>  <img src='img/bote-de-basura.svg' alt=''/><p>Eliminar</p></div>";
                    }
//                    html += "                        <div class='col-6 btnAction btnVer' onclick='ver_rep_auto(this);'>  <img src='img/cheuron.svg' alt=''/><p>Ver</p></div>";
                    html += "                    </div>";
                    html += "                     </div>";
                    html += "                 </div>";
                    html += "            </div>";
                    $("#list_autos_activos").append(html);
                });
//                $("#p_apellidopa").html(obj.apellido_pa);
//                $("#p_apellidoma").html(obj.apellido_ma);
//                $("#p_usuario").html(obj.usuario);


            }
        }

    });
}


function agg_auto_a_rep(itn) {
    var padre = $(itn).parent().parent().parent().parent();
    var data = $(padre).data("obj");
    var id = getQueryVariable("id");
    mostrar_progress();
    $.post(urlAutos, {evento: "agg_auto_to_rep", id_auto: data.id, id_rep: id, TokenAcceso: "servi12sis3"}, function (resp) {
        cerrar_progress();
        if (resp != null) {
            var obj = $.parseJSON(resp);
            if (obj.estado != "1") {
                alert(obj.mensaje);
            } else {
                var arr = obj.resp;
                //$("#confirm-delete").modal("toggle");
                $(padre).css("display", "none");
                //var obj = $.parseJSON(objec.resp);
                //alert(obj);   


                $("#list_autos_activos").html("");
                $.each(arr, function (i, obj) {
                    var html = "<div data-obj='" + JSON.stringify(obj) + "' class='list-group-item list-group-item-action flex-column align-items-start'>";
                    html += "               <div class='row iten_repuesto_row'>";
                    html += "                     <div class='col-1'>";
                    html += "                         <h6>" + obj.clave + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-1'>";
                    html += "                         <img src='" + obj.url_foto_marca + "' class='' height='60px' alt='' />";
                    html += "                      </div>";
                    html += "                     <div class='col-2'>";
                    html += "                         <h6>" + obj.nombre_marca + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-1'>";
                    html += "                         <img src='" + obj.url_foto_modelo + "' class='' height='60px' alt='' />";
                    html += "                      </div>";
                    html += "                     <div class='col-2'>";
                    html += "                         <h6>" + obj.nombre_modelo + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-2'>";
                    html += "                         <h6>" + obj.anho + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-2'>";
                    html += "                         <h6>" + obj.nombre_version + "</h6>";
                    html += "                     </div>";
                    html += "                     <div class='col-1'>";
                    html += "                   <div class='row' style='text-align: center;'>";
//                    html += "                        <div class='col-4 btnAction btnEditar' onclick='editar(this);'><img src='img/lapiz.svg' alt=''/><p>Editar</p></div>";
                    if (obj.id_repuesto == id) {
                        html += "                        <div class='col-6 btnAction btnEliminar' onclick='eliminar_rep_auto(this);'>  <img src='img/bote-de-basura.svg' alt=''/><p>Eliminar</p></div>";
                    }
                    html += "                        <div class='col-6 btnAction btnVer' onclick='ver_rep_auto(this);'>  <img src='img/cheuron.svg' alt=''/><p>Ver</p></div>";
                    html += "                    </div>";
                    html += "                     </div>";
                    html += "                 </div>";
                    html += "            </div>";
                    $("#list_autos_activos").append(html);
                });
                // alert(arr);

            }
        }

    });
    //window.location.href = "VehMarcaEdit.html?id=" + data.id;
}

function eliminar_rep_auto(itn) {
    var padre = $(itn).parent().parent().parent().parent();
    var data = $(padre).data("obj");
    var id = getQueryVariable("id");
    mostrar_progress();
    $.post(urlAutos, {evento: "eliminar_auto_to_rep", id_auto: data.id, id_rep: id, TokenAcceso: "servi12sis3"}, function (resp) {
        cerrar_progress();
        if (resp != null) {
            var obj = $.parseJSON(resp);
            if (obj.estado != "1") {
                alert(obj.mensaje);
            } else {
                var arr = obj.resp;
                //$("#confirm-delete").modal("toggle");
                $(padre).css("display", "none");
                //var obj = $.parseJSON(objec.resp);
                //alert(obj);   
                // alert(arr);

            }
        }

    });
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

function ver_conductor(id) {
    window.location.href = "VehiculoPerf.html?id=" + id;
}


function imprimirqr() {


    var dataUrl = document.querySelector('canvas').toDataURL("image/png");
    // alert(dataUrl);
    //mpt to save base64 string to server using this var  

    let windowContent = '<!DOCTYPE html>';
    windowContent += '<html>';
    windowContent += '<head><title>Print canvas</title>';

    windowContent += '<style>';
    windowContent += 'html{';
    windowContent += 'background-color: #666666;';
    windowContent += '}';
//        windowContent += '@Page {';
//        windowContent += '    size: landscape;';
//        windowContent += '}';
    windowContent += 'body{';
    windowContent += '    background-color: #000;';
    windowContent += '    width: 50mm; height: 25mm; text-align: center;';

    windowContent += '}';
    windowContent += 'img {';
    windowContent += '    height: 100%;';
    windowContent += '    width: auto;';
    windowContent += '    margin: 0;';
    windowContent += '}';
    windowContent += 'p{';
    windowContent += '    font-size: 2mm;';
    windowContent += '    margin: 0;';
    windowContent += '}';
    windowContent += '</style>';
    windowContent += '</head>';
    windowContent += '<body >';
    windowContent += '<img src="' + dataUrl + '">';

    windowContent += '</body>';
    windowContent += '</html>';
    const printWin = window.open('', '');
    printWin.document.open();
    printWin.document.write(windowContent);
    printWin.document.addEventListener('load', function () {
        printWin.focus();
        printWin.print();
        printWin.document.close();
        printWin.close();
    }, true);
    //$("#p_nombre").printArea();
}