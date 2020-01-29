/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var url = "../pagClienteController";
var objCategorias;
var objMarcas;
var id_usuario= 0;
var busquedastr= "";
$(document).ready(function () {
    getCategoriasAndMarcas();
   
});

function getCategoriasAndMarcas() {

    miPost(url, {evento: "getCategoriasAndMarcas"}, function (resp) {
        var json = $.parseJSON(resp.resp);
        this.objCategorias = json.categorias;
        this.objMarcas = json.marcas;
        cargarCategorias();
        cargarMarcas();
         segBusqueda();
//        alert(resp);
    });
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
    html += "                                        <div class='card-header' id='heading" + obj.id + "'>";
    html += "                                            <div class='mb-0' data-toggle='collapse' data-target='#collapse" + obj.id + "' aria-expanded='true' aria-controls='collapse" + obj.id + "'>";

    html += "<label>";
    html += "<input type='checkbox' name='categoriacb" + obj.id + "' value='" + JSON.stringify(obj) + "' id='categoriacb" + obj.id + "' onchange='CategoriaOnChange(this);'>";
    html += obj.nombre + " (" + obj.sub_categorias.length + ")";


    html += "</label>";
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
        var obje = {tipo: 1, id_relacion: data.id, id_usuario: id_usuario , fecha : "now()", busqueda : busquedastr };
        $("#filSubCategoria").append("<span data-busqueda='"+JSON.stringify(obje)+"'><a href='#" + id + "' id='fil" + id + "'>" + data.nombre + "</a><a href='javascript:void(0);' onclick='clickElm(\"" + id + "\");' alt='Eliminar.'>X</a></span>");
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
          var obje = {tipo: 0, id_relacion: data.id, id_usuario: id_usuario , fecha : "now()", busqueda : busquedastr };
        $("#filCategoria").append("<span data-busqueda='"+JSON.stringify(obje)+"'><a href='#" + id + "' id='fil" + id + "'>" + data.nombre + "</a><a href='javascript:void(0);' onclick='clickElm(\"" + id + "\");' alt='Eliminar.'>X ,</a></span>");
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

    html += "<img src='../" + obj.url_foto + "' alt='' style='width:auto; height:50px;' class='clear clearChecks'/>";
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

    html += "<img src='../" + obj.url_foto + "' alt='' style='width:auto; height:65px;' class='ml-auto p-2'/>";
    html += "</div>";
    return html;
}
function ModeloOnChange(itn) {
    var data = JSON.parse($(itn).val());
    var id = $(itn).attr("id");
    if ($(itn).is(":checked")) {
          var obje = {tipo: 3, id_relacion: data.id, id_usuario: id_usuario , fecha : "now()", busqueda : busquedastr };
        $("#filModelo").append("<span data-busqueda='"+JSON.stringify(obje)+"'><a href='#" + id + "' id='fil" + id + "'>" + data.nombre + "</a><a href='javascript:void(0);' onclick='clickElm(\"" + id + "\");' alt='Eliminar.'>X</a></span>");
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
          var obje = {tipo: 2, id_relacion: data.id, id_usuario: id_usuario , fecha : "now()", busqueda : busquedastr };
        $("#filMarca").append("<span data-busqueda='"+JSON.stringify(obje)+"'><a href='#" + id + "' id='fil" + id + "'>" + data.nombre + "</a><a href='javascript:void(0);' onclick='clickElm(\"" + id + "\");' alt='Eliminar.'>X ,</a></span>");
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

         var obje = {tipo: 4, id_relacion: data.id, id_usuario: id_usuario , fecha : "now()", busqueda : busquedastr };
        $("#filPrecio").append("<span data-busqueda='"+JSON.stringify(obje)+"'><a href='#" + id + "' id='fil" + id + "'>" + data.text + "</a><a href='javascript:void(0);' onclick='clickElm(\"" + id + "\");' alt='Eliminar.'>X ,</a></span>");

    }
    segBusqueda();
}

function segBusqueda(){
    var filcat= $("#filCategoria").find("span");  //0
    var filsubcat= $("#filSubCategoria").find("span") ; //1
    var filmarca= $("#filMarca").find("span");  //2
    var filmodelo= $("#filModelo").find("span"); //3
    var busqueda = [];
    $.each(filcat , function(i,obj){
       busqueda.push($(obj).data("busqueda")); 
    });
    $.each(filsubcat , function(i,obj){
       busqueda.push($(obj).data("busqueda")); 
    });
    $.each(filmarca , function(i,obj){
       busqueda.push($(obj).data("busqueda")); 
    });
    $.each(filmodelo , function(i,obj){
       busqueda.push($(obj).data("busqueda")); 
    });
    cargar_repuestos(busqueda);
}
function cargar_repuestos(busqueda){
      miPost(url, {evento: "getBusqueda", busqueda: JSON.stringify(busqueda)}, function (resp) {
          $("#listaRep").html("");
        var json = $.parseJSON(resp.resp);
        
        $.each(json,function(i,obj){
            cargarIten(obj);
        });
        
    });
}

function cargarIten(obj){
    var html = "<div class='col-lg-4 col-md-6 col-sm-12'>";
    html+="                                <div class='tile'>";
    html+="                                    <div class='badges'>";
    html+="                                        <span class='sale'></span>";
    html+="                                    </div>";
    html+="                                    <div class='price-label'>"+obj.precio+" Bs.</div>";
    html+="                                    <a href='repuesto.html?id="+obj.id+"'><img class='foto_rep' src='../"+obj.url_foto+"' alt='1'/></a>";
    html+="                                    <div class='footer'>";
    html+="                                        <a href='#'>"+obj.nombre+"</a>";
    html+="                                        <span>Codigo: "+obj.codigo+"</span>";
    var cant = obj.cantidad || 0;
    html+="                                        <span>Cantidad: "+cant+"</span>";
    html+="                                        <span>"+obj.nombrecat+"</span>";
    html+="                                        <span>"+obj.nombresubcat+"</span>";
    html+="                                        <div class='tools'>";
    html+="                                            <div class='rate'>";
    html+="                                                <span class='active'></span>";
    html+="                                                <span class='active'></span>";
    html+="                                                <span class='active'></span>";
    html+="                                                <span></span>";
    html+="                                                <span></span>";
    html+="                                            </div>";
    html+="                                            <!--Add To Cart Button-->";
    html+="                                            <a class='add-cart-btn' href='javaScript:void(0);' onclick='addToCarrito("+JSON.stringify(obj)+");'><span>Agregar al carrito</span><i class='icon-shopping-cart'></i></a>";
    html+="                                            <!--Share Button-->";
    html+="                                            <div class='share-btn'>";
    html+="                                                <div class='hover-state'>";
    html+="                                                    <a class='fa fa-facebook-square' href='#'></a>";
    html+="                                                    <a class='fa fa-twitter-square' href='#'></a>";
    html+="                                                    <a class='fa fa-google-plus-square' href='#'></a>";
    html+="                                                </div>";
    html+="                                                <i class='fa fa-share'></i>";
    html+="                                            </div>";
    html+="                                            <!--Add To Wishlist Button-->";
    html+="                                            <a class='wishlist-btn' href='#'>";
    html+="                                                <div class='hover-state'>Deseados</div>";
    html+="                                                <i class='fa fa-plus'></i>";
    html+="                                            </a>";
    html+="                                        </div>";
    html+="                                    </div>";
    html+="                                </div>";
    html+="                            </div>";
    $("#listaRep").append(html);
}