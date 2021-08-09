/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var url = "../pagClienteController";
var objCategorias;
var objMarcas;
var id_usuario = 0;
var busquedastr = "";
$(document).ready(function () {

    getCategoriasAndMarcas();

});
function getCategoriasAndMarcas() {
    var object;
    if (sessionStorage.getItem("last_getCategoriasAndMarcas") > new Date().getTime() - 5000) {

        object = sessionStorage.getItem("getCategoriasAndMarcas");
        var json = $.parseJSON(object);
        this.objCategorias = json.categorias;
        this.objMarcas = json.marcas;
        cargarCategorias();
        cargarMarcas();

    } else {
        miPost(url, {evento: "getCategoriasAndMarcas"}, function (resp) {
            object = resp.resp;

            sessionStorage.setItem("getCategoriasAndMarcas", object);
            sessionStorage.setItem("last_getCategoriasAndMarcas", new Date().getTime());
            var json = $.parseJSON(object);
            this.objCategorias = json.categorias;
            this.objMarcas = json.marcas;
            cargarCategorias();
            cargarMarcas();
//        alert(resp);
        }, true);
    }



}

function cargarCategorias() {
    $.each(objCategorias, function (i, obj) {
        var resp = createCategoriaItn(obj);
        if (resp != null && obj.sub_categorias.length > 0) {
            $("#accordion").append(resp);
            cargarSubCategorias(obj);
        }

    });
}
function createCategoriaItn(obj) {
    if (obj.sub_categorias == null)
        return null;
    var html = "";
    html += "<div class='card'>";
    html += "                                        <div class='' id='heading" + obj.id + "'>";
    html += "                                            <div class='mb-0' data-toggle='collapse' data-target='#collapse" + obj.id + "' aria-expanded='true' aria-controls='collapse" + obj.id + "'>";



    html += "      <div class='funkyradio'>";
    html += "      <div class='funkyradio-success'>";
    html += "<input type='checkbox' name='categoriacb" + obj.id + "' value='" + JSON.stringify(obj) + "' id='categoriacb" + obj.id + "' onchange='CategoriaOnChange(this);'/>";
    html += "<label for='categoriacb" + obj.id + "'>";
    html += obj.nombre + " (" + obj.sub_categorias.length + ")";
    html += "<img src='" +PARAMS.url_image + obj.url_foto + "' alt='' style='width:auto; height:50px;' class='clear '/>";
    html += ".</label>";
    html += "</div>";
    html += "</div>";

    html += "                                            </div> ";
    html += "                                        </div>";
    html += "                                        <div id='collapse" + obj.id + "' class='collapse' aria-labelledby='heading" + obj.id + "' data-parent='#accordion'>";
    html += "                                            <div class='card-body' id='contenSubCat" + obj.id + "'>";
    html += "                                            </div>";
    html += "                                        </div>";
    html += "                                    </div>";

    return html;
}

function cargarSubCategorias(categoria) {

    $.each(categoria.sub_categorias, function (i, obj) {
        $("#contenSubCat" + categoria.id + "").append(createSubCategoriaItn(obj));
    });
}
function createSubCategoriaItn(obj) {
    var html = "";
    html += "<label>";
    html += "<input type='checkbox' name='SubCat" + obj.id + "' value='" + JSON.stringify(obj) + "' id='cbSubCat" + obj.id + "' onchange='SubCategoriaOnChange(this);'>";
    html += obj.nombre;
    html += "</label>";
    return html;
}
function SubCategoriaOnChange(itn) {
    var data = JSON.parse($(itn).val());
    var id = $(itn).attr("id");
    if ($(itn).is(":checked")) {
        var obje = {tipo: 1, id_relacion: data.id, id_usuario: id_usuario, fecha: "now()", busqueda: busquedastr, nombre: data.nombre};
        $("#filSubCategoria").append("<br><span data-busqueda='" + JSON.stringify(obje) + "'><a href='#" + id + "' id='fil" + id + "'>" + data.nombre + "</a><a href='javascript:void(0);' onclick='clickElm(\"" + id + "\");' alt='Eliminar.'>X</a></span>");
    } else {
        $("#filSubCategoria").find("#fil" + id).parent().remove();
    }
    segBusqueda();
//    alert(data);
}
function CategoriaOnChange(itn) {

    var data = JSON.parse($(itn).val());
    var id = $(itn).attr("id");
    if ($(itn).is(":checked")) {
        var obje = {tipo: 0, id_relacion: data.id, id_usuario: id_usuario, fecha: "now()", busqueda: busquedastr, nombre: data.nombre};
        $("#filCategoria").append("<span data-busqueda='" + JSON.stringify(obje) + "'><a href='#" + id + "' id='fil" + id + "'>" + data.nombre + "</a><a href='javascript:void(0);' onclick='clickElm(\"" + id + "\");' alt='Eliminar.'>X ,</a></span>");
    } else {
        $("#filCategoria").find("#fil" + id).parent().remove();
    }
    segBusqueda();
//    alert(data);
}

function clickElm(id) {

    $("#" + id).click();
}

///////////////////////////////MARCASSS///////////////

function cargarMarcas() {
    $.each(objMarcas, function (i, obj) {
        var resp = createMarcaItn(obj);
        $("#acorMarcas").append(resp);
        if (obj.modelos != null) {
            cargarModelos(obj);
        }

    });
    segBusqueda();
}
function createMarcaItn(obj) {

    var html = "";
    html += "<div class='card'>";
    html += "                                        <div class='card-header' id='heading" + obj.id + "'>";
    html += "                                            <div class='mb-0' data-toggle='collapse' data-target='#collapse" + obj.id + "' aria-expanded='true' aria-controls='collapse" + obj.id + "'>";
    html += "<label>";
    html += "<input type='checkbox' name='marcacb" + obj.id + "' value='" + JSON.stringify(obj) + "' id='marcacb" + obj.id + "' onchange='marcaOnChange(this);'>";
    if (obj.modelos != null) {
        html += obj.nombre + " (" + obj.modelos.length + ")";
    } else {
        html += obj.nombre + " (0)";
    }

    html += "<img src='" +PARAMS.url_image + obj.url_foto + "' alt='' style='width:auto; height:50px;' class='clear clearChecks'/>";
    html += "</label>";
    html += "                                            </div> ";
    html += "                                        </div>";
    html += "                                        <div id='collapse" + obj.id + "' class='collapse' aria-labelledby='heading" + obj.id + "' data-parent='#acorMarcas'>";
    html += "                                            <div class='card-body' id='contenModelos" + obj.id + "'>";
    html += "                                            </div>";
    html += "                                        </div>";
    html += "                                    </div>";

    return html;
}

function cargarModelos(marca) {

    $.each(marca.modelos, function (i, obj) {
        $("#contenModelos" + marca.id + "").append(createModeloItn(obj));
    });
}
function createModeloItn(obj) {
    var html = "";
    html += "<div class='d-flex col-12 '>";
    html += "<div class='p align-self-center'>";
    html += "<input type='checkbox' name='Modelo" + obj.id + "' value='" + JSON.stringify(obj) + "' id='cbModelo" + obj.id + "' onchange='ModeloOnChange(this);'>";
    html += obj.nombre;
    html += "</div>";
    html += "<img src='" +PARAMS.url_image + obj.url_foto + "' alt='' style='width:auto; height:65px;' class='ml-auto p-2'/>";
    html += "</div>";
    return html;
}
function ModeloOnChange(itn) {
    var data = JSON.parse($(itn).val());
    var id = $(itn).attr("id");
    if ($(itn).is(":checked")) {
        var obje = {tipo: 3, id_relacion: data.id, id_usuario: id_usuario, fecha: "now()", busqueda: busquedastr, nombre: data.nombre};
        $("#filModelo").append("<span data-busqueda='" + JSON.stringify(obje) + "'><a href='#" + id + "' id='fil" + id + "'>" + data.nombre + "</a><a href='javascript:void(0);' onclick='clickElm(\"" + id + "\");' alt='Eliminar.'>X</a></span>");
    } else {
        $("#filModelo").find("#fil" + id).parent().remove();
    }
    segBusqueda();
//    alert(data);
}
function marcaOnChange(itn) {

    var data = JSON.parse($(itn).val());
    var id = $(itn).attr("id");
    if ($(itn).is(":checked")) {
        var obje = {tipo: 2, id_relacion: data.id, id_usuario: id_usuario, fecha: "now()", busqueda: busquedastr, nombre: data.nombre};
        $("#filMarca").append("<span data-busqueda='" + JSON.stringify(obje) + "'><a href='#" + id + "' id='fil" + id + "'>" + data.nombre + "</a><a href='javascript:void(0);' onclick='clickElm(\"" + id + "\");' alt='Eliminar.'>X ,</a></span>");
    } else {
        $("#filMarca").find("#fil" + id).parent().remove();
    }
    segBusqueda();
//    alert(data);
}

function selectPrice(itn) {
    var data = JSON.parse($(itn).val());
    var id = $(itn).attr("id");
    if ($(itn).hasClass("active")) {
        $(itn).removeClass("active");
        $("#filPrecio").find("#fil" + id).parent().remove();
    } else {
        $(itn).addClass("active");
        var obje = {tipo: 4, id_relacion: data.id, id_usuario: id_usuario, fecha: "now()", busqueda: busquedastr, nombre: data.text};
        $("#filPrecio").append("<span data-busqueda='" + JSON.stringify(obje) + "'><a href='#" + id + "' id='fil" + id + "'>" + data.text + "</a><a href='javascript:void(0);' onclick='clickElm(\"" + id + "\");' alt='Eliminar.'>X ,</a></span>");

    }
    segBusqueda();
}

function segBusqueda() {
    var filcat = $("#filCategoria").find("span"); //0
    var filsubcat = $("#filSubCategoria").find("span"); //1
    var filmarca = $("#filMarca").find("span"); //2
    var filmodelo = $("#filModelo").find("span"); //3
    var busqueda = "";
    $.each(filcat, function (i, obj) {
        busqueda += " " + ($(obj).data("busqueda")).nombre + " ";
    });
    $.each(filsubcat, function (i, obj) {
        busqueda += " " + ($(obj).data("busqueda")).nombre + " ";
    });
    $.each(filmarca, function (i, obj) {
        busqueda += " " + ($(obj).data("busqueda")).nombre + " ";
    });
    $.each(filmodelo, function (i, obj) {
        busqueda += " " + ($(obj).data("busqueda")).nombre + " ";
    });

    cargar_repuestos(busqueda);
}
function cargar_repuestos(busqueda) {
//    mostrar_progress();
    if (sessionStorage.getItem("last_getBuscarTienda") == busqueda) {
        var json = $.parseJSON(sessionStorage.getItem("getBuscarTienda"));
        $.each(json, function (i, obj) {
              document.getElementById("cargando-icon").style.opacity=0;
            cargarIten(obj);
        });
    } else {
        miPost(url, {evento: "getBuscarTienda", pag: 0, busqueda: JSON.stringify(busqueda)}, function (resp) {
            $("#listaRep").html("");
//        cerrar_progress()
            document.getElementById("cargando-icon").style.opacity=0;
            sessionStorage.setItem("getBuscarTienda", resp.resp);
            sessionStorage.setItem("last_getBuscarTienda", busqueda);
            var json = $.parseJSON(resp.resp);
            $.each(json, function (i, obj) {
                cargarIten(obj);
            });
           

        }, true);
    }


}

function cargarIten(obj) {
    var html = "<div class='col-lg-4 col-md-6 col-sm-12'>";
    html += "                                <div class='tile'>";
    html += "                                    <div class='badges'>";
    html += "                                        <span class='sale'></span>";
    html += "                                    </div>";
    html += "                                    <div class='price-label'>" + (obj.precio ? (obj.precio + " Bs.") : ("Sin asignar")) + "</div>";
    html += "                                    <a href='repuesto.html?id=" + obj.repuesto.id + "'><img class='foto_rep' src='" + PARAMS.url_image +obj.repuesto.url_foto + "' alt='1'/></a>";
    html += "                                    <div class='footer'>";
    var nombre = obj.repuesto.nombre || "";
    html += "                                        <a href='#'>" + nombre + "</a>";
    html += "                                        <span>Codigo: " + obj.repuesto.codigo + "</span>";
    if (obj.rep_categoria_rec) {
        obj.rep_categoria_rec = JSON.parse(obj.rep_categoria_rec);
    }
    html += "                         <span>";
    html += (obj.rep_categoria_rec[0].nombre || "(ES) ");
    html += (obj.rep_categoria_rec[0].name || "(EN) ");
    html += "</span>";


    if (obj.rep_categoria_rec.length > 1) {
        html += "                         <span>" + (obj.rep_categoria_rec[1].nombre || "");
        html += (obj.rep_categoria_rec[1].name || "") + "</span>";
    }

    if (obj.rep_detalle != null) {
        for (var i = 0; i < obj.rep_detalle.length; i++) {
            if (obj.rep_detalle[i].valor.length > 0) {

                html += "                         <span>" + obj.rep_detalle[i].detalle + " ";
                html += obj.rep_detalle[i].valor + "</span>";

            }

        }
    }
    html += "                                        <div class='tools'>";

    html += "                                            <!--Add To Cart Button-->";
    html += "                                            <a class='add-cart-btn' href='javaScript:void(0);' onclick='addToCarrito(" + JSON.stringify(obj.repuesto) + ");'><span>Agregar al carrito</span><i class='icon-shopping-cart'></i></a>";
    html += "                                            <!--Share Button-->";

    html += "                                        </div>";
    html += "                                    </div>";
    html += "                                </div>";
    html += "                            </div>";
    $("#listaRep").append(html);
}