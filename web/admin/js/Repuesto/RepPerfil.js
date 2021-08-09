/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var url = "repuestosController";
var urlAutos = "autosController";
var auto = {nombre: "nombre"};
$(document).ready(function () {
    var id = getQueryVariable("id");
    $("#editarPerfil").attr("href", "RepEdit.html?id=" + id);
    $("#editarVehiculos").attr("href", "RepEdit.html?id=" + id);
    $("#btnAggParte").attr("href", "RepParteCreate.html?id=" + id);
    $("#file-1d").fileinput({
           maxFileCount: 10,

    })
    addSubmitFoto();
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
                console.log(objRep);
                $("#nombre_perfil").html(objRep.nombre);
                document.getElementById("btnSubirFoto").dataset.id=objRep.id;
                $("#foto_perfil").attr("src",PARAMS.url_image+objRep.url_foto);
                if (objCategoria.length > 1) {
                    $("#p_categoria").html(objCategoria[1].nombre);
                    $("#p_subcat").html(objCategoria[0].nombre);
                } else {
                    $("#p_categoria").html(objCategoria[0].nombre);
                }

                $("#p_nombre").html(objRep.nombre);
                auto = objRep;
                $("#p_codigo").html(objRep.codigo);
                $("#p_precio").html(cant = Intl.NumberFormat('de-DE').format(objRep.precio || 0) + " Bs.");
                if (objRep.id_padre) {
                    agg_padre({id: objRep.id_padre, nombre: "ver en esquema"});
                }
                $("#pCantidad").html(objRep.cantidad || 0);
                $("#pAlmacen").html(objRep.almacen || 0);
                if (objRep.url_foto_esquema != null) {
                    $("#contenEsquema").css("display", "");
                    cargar_imagen_canvas(objRep);
                } else {
                    $("#contensinEsquema").css("display", "");
                }
                $("#qrcode").qrcode({
                    text: JSON.stringify({tipo: 1, id: objRep.id, pag: 'yunkerRep'})
                });
                loadFotos(objRep.fotos);

                cargar_detalle_parte();
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

function loadFotos(arr) {
    if(!arr){
        return;
    }
    var html = "";
    for (var i = 0; i < arr.length; i++) {
        html += "  <div class='col-4 '>";
        html += "<div class='fotoContainer'>";
        html += "<div class='fotoRemove' onclick='removeFoto("+arr[i].id+");'>X</div>";
        html += "<img src='"+PARAMS.url_image+arr[i].url+"' alt=''/>";
        html += "</div>";
        html += "</div>";
    }
    $("#listaFotos").html(html);
}

function removeFoto(id){
    $.post("repuestosController", {TokenAcceso: "servi12sis3", evento: "removeFoto", id: id}, function (resp) {
        window.location.reload();
    });
}
function addSubmitFoto() {
    
    $("#submitformFoto").submit(function (event) {
        event.preventDefault();

        var file = $("#file-1d").val() || null;
        var exito = true;

        if (file == null) {
            $("#file-1d").css("background-color", "#f386ab");
            alert("Es nesesario seleccioar un archibo.");
            exito = false;
        } else {
            $("#file-1d").css("background-color", "#ffffff");
        }
        var id_usr = parseFloat(getQueryVariable("id"));
        if (id_usr > 0) {
            $("#id_usr1").val(id_usr);
        } else {
            exito = false;
            alert("Ocurrio un Error por favor vuelva a intentarlo.");
            window.location.reload();
        }
        if (exito) {

            mostrar_progress();
            var formData = new FormData($("#submitformFoto")[0]);
            $.ajax({
                url: "repuestosController",
                type: 'POST',
                data: formData,
                async: false,
                cache: false,
                contentType: false,
                processData: false,
                success: function (returndata) {
                    cerrar_progress();
                    if (returndata != null) {
                        window.location.reload();

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
}
function agg_detalle_rep() {
    agg_detalle_repuesto();
}

function agg_detalle_repuesto() {
    var html = "<div class='list-group-item list-group-item-action flex-column align-items-start'>";
    html += "               <div class='row iten_repuesto_row'>";
    html += "                     <div class='col-4'>";
    html += "                         <input type='text' name='detalle' class='form-control color-input' placeholder='Nombre detalle'>";
    html += "                     </div>";
    html += "                     <div class='col-4'>";
    html += "                         <input type='text' name='valor' class='form-control color-input' placeholder='Valor'>";
    html += "                     </div>";
    html += "                     <div class='col-4'>";

    html += "                   <div class='col-12 d-flex justify-content-center  align-items-center p-0 h-100' style='text-align: center;'>";
//                    html += "                        <div class='col-4 btnAction btnEditar' onclick='editar(this);'><img src='img/lapiz.svg' alt=''/><p>Editar</p></div>";
    html += "                        <div class='col-6 btnAction btnEliminar d-flex justify-content-center  align-items-center' onclick='eliminar(this);'>  <img src='img/bote-de-basura.svg' alt=''/><p>Eliminar</p></div>";
    html += "                        <div class='col-6 btnAction btnVer d-flex justify-content-center  align-items-center' onclick='guardar_detalle(this);'>  <img src='img/cheuron.svg' alt=''/><p>Guardar</p></div>";
//    html += "                    </div>";
    html += "                     </div>";
    html += "                 </div>";
    html += "            </div>";
    $("#lista_detalle").append(html);
}
function guardar_detalle(itn) {
    var detalle = $(itn).parent().parent().parent().find("input[name=detalle]").val();
    var valor = $(itn).parent().parent().parent().find("input[name=valor]").val();
    var exito = true;
    if (detalle != null && detalle.length > 0) {
        $(itn).parent().parent().parent().find("input[name=detalle]").css("background", "#ffffff");
    } else {
        $(itn).parent().parent().parent().find("input[name=detalle]").css("background", "#df5b5b");
        exito = false;
    }
    if (valor != null && valor.length > 0) {
        $(itn).parent().parent().parent().find("input[name=valor]").css("background", "#ffffff");
    } else {
        $(itn).parent().parent().parent().find("input[name=valor]").css("background", "#df5b5b");
        exito = false;
    }
    if (!exito) {
        return;
    }

    var id = getQueryVariable("id");
    mostrar_progress();
    var data = {};
    data.detalle = detalle;
    data.valor = valor;
    data.id_repuesto = id;
    data.estado = 0;

    var obj = {
        nombre_tabla: "rep_detalle",
        data: [data]
    };


    $.post("jsonController", {evento: "insertar", json: JSON.stringify(obj)}, function (resp) {
        cerrar_progress();
        if (resp.length > 0) {
            window.location.reload();
        }



    });
}
function eliminar_rep_detalle(id) {
    mostrar_progress();
    var data = {};
    data.estado = "1";
    data.id = id;

    var obj = {
        nombre_tabla: "rep_detalle",
        data: [data]
    };


    $.post("jsonController", {evento: "actualizar", json: JSON.stringify(obj)}, function (resp) {
        cerrar_progress();
        if (resp.length > 0) {
            window.location.reload();
        }



    });

}
function editarRepuesto() {
    var id = getQueryVariable("id");
    window.location.href = "RepPerfilEdit.html?id=" + id;
}
function editarCampo(campo) {
    $("#editarCampo").modal("show");
    var html = "";

    var valor = "";
    var btn_editar_campo = document.getElementById("btn_editarCampo");
    switch (campo) {
        case "codigo":
            html += "      <div class='form-group'>";
            html += "                            <label for='inedit_codigo' class='label-titulo' >Codigo</label>";
            html += "                            <input type='text' name='nombre' class='form-control color-input' id='inedit_codigo'  placeholder='Codigo' value='" + auto.codigo + "'>";
            html += "                       </div>  ";
            btn_editar_campo.onclick = function () {

                editPost("codigo", $("#inedit_codigo").val());

            };

            break;
        case "p_nombre":
            html += "      <div class='form-group'>";
            html += "                            <label for='inedit_nombre' class='label-titulo' >Nombre</label>";
            html += "                            <input type='text' name='nombre' class='form-control color-input' id='inedit_nombre'  placeholder='Nombre' value='" + auto.nombre + "'>";
            html += "                       </div>  ";
            btn_editar_campo.onclick = function () {


                valor = $("#inedit_nombre").val();
                editPost("nombre", valor);

            };
            break;
        case "p_precio":
            html += "      <div class='form-group'>";
            html += "                            <label for='inedit_precio' class='label-titulo' >Precio</label>";
            html += "                            <input type='text' name='nombre' class='form-control color-input' id='inedit_precio'  placeholder='Codigo' value='" + (auto.precio || 0) + "'>";
            html += "                       </div>  ";
            btn_editar_campo.onclick = function () {


                valor = $("#inedit_precio").val();
                editPost("precio", valor);

            };

            break;
    }

    $("#modalEditBody").html(html);


}
function editPost(campo, valor) {
    var id = getQueryVariable("id");
    mostrar_progress();
    var data = {};
    data.id = id;
    data[campo] = valor;
    var obj = {
        nombre_tabla: "repuesto",
        data: [data]
    };


    $.post("jsonController", {evento: "actualizar", json: JSON.stringify(obj)}, function (resp) {
        cerrar_progress();
        if (resp.length > 0) {
            window.location.reload();
        }



    });
}

function agg_padre(obj) {
    var html = "";
    html += " \t\t / \t<a href='RepPerfil.html?id=" + obj.id + "'>" + obj.nombre + "</a>";
    $("#list_padres").prepend(html);
}
function agg_detalle_parte(obj) {
    var html = "";
    html += " <div class='row'>";
    html += "                                            <div class='col-md-4'>";
    html += "                                         <label>" + obj.detalle + "</label>";
    html += "                                     </div>";
    html += "                                     <div class='col-md-4'>";
    html += "                                         <p>" + obj.valor + "</p>";
    html += "                                     </div>";
    html += "                                     <div class='col-md-4'>";
    html += "                   <div class='col-12 d-flex justify-content-center  align-items-center p-0 h-100' style='text-align: center;'>";
//                    html += "                        <div class='col-4 btnAction btnEditar' onclick='editar(this);'><img src='img/lapiz.svg' alt=''/><p>Editar</p></div>";
    html += "                        <div class='col-6 btnAction btnEliminar d-flex justify-content-center  align-items-center' onclick='eliminar_rep_detalle(" + obj.id + ");'>  <img src='img/bote-de-basura.svg' alt=''/><p>Eliminar</p></div>";
//    html += "                        <div class='col-6 btnAction btnVer d-flex justify-content-center  align-items-center' onclick='guardar_detalle(this);'>  <img src='img/cheuron.svg' alt=''/><p>Guardar</p></div>";
//    html += "                    </div>";
    html += "                     </div>";
    html += "                                     </div>";
    html += "                                </div> <hr>";
    $("#lista_detalle").append(html);
}
function cargar_detalle_parte() {
    var id = getQueryVariable("id");
    mostrar_progress();
    var data = {
        key: "id_repuesto",
        ope: "=",
        val: id + ""
    };
    var data1 = {
        key: "estado",
        ope: "=",
        val: "0"
    };

    var obj = {
        nombre_tabla: "rep_detalle",
        wheres: [data, data1]
    };


    $.post("jsonController", {evento: "getByWheres", json: JSON.stringify(obj)}, function (resp) {
        cerrar_progress();
        if (resp != "error") {

            var lista = JSON.parse(resp);
            $.each(lista, function (i, obj) {
                agg_detalle_parte(obj);
            });
        }



    });
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
                    html += "                     <div class='col-2'>";
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
                    html += "                     <div class='col-2'>";
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
                    html += "                     <div class='col-1'>";
                    html += "                   <div class='col-12 d-flex justify-content-center  align-items-center p-0 h-100' style='text-align: center;'>";
//                    html += "                        <div class='col-4 btnAction btnEditar d-flex justify-content-center  align-items-center' onclick='editar(this);'><div><img src='img/lapiz.svg' alt=''/><p>Editar</p></div></div>";
                    if (obj.id_repuesto == id) {

                        html += "                        <div class='col-12 btnAction btnEliminar d-flex justify-content-center  align-items-center'  onclick='eliminar_rep_auto(this);'>  <div><img src='img/bote-de-basura.svg' alt=''/><p>Eliminar</p></div></div>";
                    }


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
            var objec = $.parseJSON(resp);
            if (objec.estado != "1") {
                alert(objec.mensaje);
            } else {

                //$("#confirm-delete").modal("toggle");
                $(padre).css("display", "none");
                var obj = $.parseJSON(objec.resp);
                //alert(obj);   


//               
                $("#list_autos_activos").html("");
                $.each(obj, function (i, obj) {
                    var html = "<div data-obj='" + JSON.stringify(obj) + "' class='list-group-item list-group-item-action flex-column align-items-start'>";
                    html += "               <div class='row iten_repuesto_row'>";
                    html += "                     <div class='col-2'>";
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
                    html += "                     <div class='col-1'>";
                    html += "                   <div class='col-12 d-flex justify-content-center  align-items-center p-0 h-100' style='text-align: center;'>";
//                    html += "                        <div class='col-4 btnAction btnEditar d-flex justify-content-center  align-items-center' onclick='editar(this);'><div><img src='img/lapiz.svg' alt=''/><p>Editar</p></div></div>";
                    if (obj.id_repuesto == id) {

                        html += "                        <div class='col-12 btnAction btnEliminar d-flex justify-content-center  align-items-center'  onclick='eliminar_rep_auto(this);'>  <div><img src='img/bote-de-basura.svg' alt=''/><p>Eliminar</p></div></div>";
                    }


                    html += "                    </div>";

                    html += "                     </div>";
                    html += "                 </div>";
                    html += "            </div>";
                    $("#list_autos_activos").append(html);
                });

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

function cargar_lista_cardex() {

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
    windowContent += '    width: 55mm; height: 25mm; ';
    windowContent += '    padding: 0;';
    windowContent += '    margin: 0;';

    windowContent += '}';
    windowContent += 'img {';
    windowContent += '    position: absolute;';
    windowContent += '    left: 1mm;';
    windowContent += '    height: auto;';
    windowContent += '    width: 23mm;';
    windowContent += '    padding: 0;';
    windowContent += '    margin: 0;';
    windowContent += '} ';
    windowContent += 'p{';

    windowContent += '    position: absolute;';
    windowContent += '    right: 1mm;';
    windowContent += '    width: 23mm;';
    windowContent += '    font-weight: 900;';

    windowContent += '    font-size: 3.5mm;';
    windowContent += '      text-align: center;';
    windowContent += '    margin: 0;';
    windowContent += '    padding: 0;';

    windowContent += '}';
    windowContent += '</style>';
    windowContent += '</head>';
    windowContent += '<body >';

    windowContent += '<img src="' + dataUrl + '">';
    windowContent += '<p>' + auto.nombre + '</p>';


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
//    $("#p_nombre").printArea();
}