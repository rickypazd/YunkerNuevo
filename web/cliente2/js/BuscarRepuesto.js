/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var divListaSelected = document.getElementById("divListaSelected");
var divListaMarcas = document.getElementById("divListaMarcas");
var divListaModelos = document.getElementById("divListaModelos");
var divListaVersiones = document.getElementById("divListaVersiones");
var divListaAutos = document.getElementById("divListaAutos");
var divListaCategorias = document.getElementById("divListaCategorias");
var divListaRepuestos = document.getElementById("divListaRepuestos");
function init() {
    
    var listaSelecSession = sessionStorage.getItem("repuesto_busqueda_cliente") || "";
    if(listaSelecSession.length >1){
        divListaSelected.innerHTML = listaSelecSession;
        var list = divListaSelected.getElementsByClassName("itenRepAutoM");
        list[list.length-1].click();
    }else{
        cargarRepAutoMarca();
    }
    
}
init();

function noEncontrado(Elm){
     var html = "<p>No se encontro elementos</p>";
          

            Elm.innerHTML += html;
    
}


function cargarRepAutoMarca() {

    seleccionar("cargarRepAutoMarca", null);
    var wheres = [
        {
            key: "estado",
            ope: "=",
            val: 0
        }
    ];
    var select = [
    ];
    divListaMarcas.innerHTML = "<div class='col-12'>Marcas<hr></div>";
    Peticion.getBySelectWheres("rep_auto_marca", select, wheres, function (resp) {

        var arr = JSON.parse(resp);
        if(arr.length==0){
           noEncontrado(divListaMarcas); 
           return;
        }
        for (var i = 0; i < arr.length; i++) {

            var html = "<div class='col-2 itenRepAutoM' onclick=\"cargarRepAutoModelo('" + window.btoa(JSON.stringify(arr[i])) + "',this)\">";
            html += "<img src='../admin/" + arr[i].url_foto + "' class='' height='60px' alt='' />";
            html += "<p>" + arr[i].nombre + "</p>";
            html += "</div>";

            divListaMarcas.innerHTML += html;
        }
    });
}
function cargarRepAutoModelo(obj, elm) {


    seleccionar("cargarRepAutoModelo", elm);

    obj = JSON.parse(window.atob(obj));

    var wheres = [
        {
            key: "id_rep_auto_marca",
            ope: "=",
            val: obj.id
        },
        {
            key: "estado",
            ope: "=",
            val: 0
        }
    ];
    var select = [
    ];
    divListaModelos.innerHTML = "<div class='col-12'>Modelos<hr></div>";
    Peticion.getBySelectWheres("rep_auto_modelo", select, wheres, function (resp) {

        var arr = JSON.parse(resp);
        if(arr.length==0){
           noEncontrado(divListaModelos); 
           return;
        }
        for (var i = 0; i < arr.length; i++) {
            var html = "<div class='col-2 itenRepAutoM' onclick=\"cargarRepAutoVersion('" + window.btoa(JSON.stringify(arr[i])) + "',this)\">";
            html += "<img src='../admin/" + arr[i].url_foto + "' class='' height='60px' alt='' />";
            html += "<p>" + arr[i].nombre + "</p>";
            html += "</div>";
            divListaModelos.innerHTML += html;
        }
    });
}
function cargarRepAutoVersion(obj, elm) {

    seleccionar("cargarRepAutoVersion", elm);
    obj = JSON.parse(window.atob(obj));
    var wheres = [
        {
            key: "id_rep_auto_modelo",
            ope: "=",
            val: obj.id
        },
        {
            key: "estado",
            ope: "=",
            val: 0
        }
    ];
    var select = [
    ];
    divListaVersiones.innerHTML = "<div class='col-12'>Versiones<hr></div>";
    Peticion.getBySelectWheres("rep_auto_version", select, wheres, function (resp) {

        var arr = JSON.parse(resp);
            if(arr.length==0){
           noEncontrado(divListaVersiones); 
           return;
        }
        for (var i = 0; i < arr.length; i++) {
            var html = "<div class='col-2 itenRepAutoM' onclick=\"cargarRepAuto('" + window.btoa(JSON.stringify(arr[i])) + "',this)\">";
            html += "<p>" + arr[i].nombre + "</p>";
            html += "</div>";

            divListaVersiones.innerHTML += html;
        }
    });
}
function cargarRepAuto(obj, elm) {

    seleccionar("cargarRepAuto", elm);
    obj = JSON.parse(window.atob(obj));
    var wheres = [
        {
            key: "id_version",
            ope: "=",
            val: obj.id
        },
        {
            key: "estado",
            ope: "=",
            val: 0
        }
    ];
    var select = [
    ];
    divListaAutos.innerHTML = "<div class='col-12'>Anhos<hr></div>";
    Peticion.getBySelectWheres("rep_auto", select, wheres, function (resp) {

        var arr = JSON.parse(resp);
               if(arr.length==0){
           noEncontrado(divListaAutos); 
           return;
        }
        for (var i = 0; i < arr.length; i++) {
            var html = "<div class='col-2 itenRepAutoM' onclick=\"cargarCategorias('" + window.btoa(JSON.stringify(arr[i])) + "',this)\">";
            html += "<p>" + arr[i].anho + "</p>";
            html += "</div>";
            divListaAutos.innerHTML += html;
        }
    });
}


function cargarCategorias(obj, elm) {
    seleccionar("cargarCategorias", elm);

    obj = JSON.parse(window.atob(obj));
    var id_padre = obj.id;
    divListaCategorias.innerHTML = "<div class='col-12'>Categorias<hr></div>";
    $.post("../admin/repuestosController", {TokenAcceso: "servi12sis3", evento: "getAll_rep_categoriaJSONbyIdAuto", id: obj.id}, function (resp) {
        if (resp == "error") {
//                alert(resp);
            return;
        }
        var json = JSON.parse(resp);
        var arr = JSON.parse(json.resp);
        for (var i = 0; i < arr.length; i++) {
            var arrCat = JSON.parse(arr[i].json);

            for (var j = 0; j < arrCat.length; j++) {
                arrCat[j].id_auto = id_padre;
                if (arrCat[j].id_padre > 0) {
                    var objcat = document.getElementById("cat_" + arrCat[0].id);
                    var objsubca = document.getElementById("subcat_" + arrCat[j].id);
                    if (objsubca == null) {

                        var html = "<div id='subcat_" + arrCat[j].id + "' class='itenRepAutoM subcategoriaclas col-6' onclick=\"cargarRepuesto('" + window.btoa(JSON.stringify(arrCat[j])) + "',this)\">";
                        if(arrCat[j].url_foto != null){
                            html += "<img src='../admin/" + arrCat[j].url_foto + "' class='' height='60px' alt='' />";
                        }
                        
                        html += "<p>" + arrCat[j].nombre + "  (" + arr[i].count + ")</p>";
                        html += "</div>";
                        objcat.innerHTML += html;
                    }

                } else {
                    var obj = document.getElementById("cat_" + arrCat[j].id);
                    if (obj == null) {
                        var html = "<div id='cat_" + arrCat[j].id + "' class='col-3 catcontent'>";
                        html += "<div class='col-12 itenRepAutoM categoriaclas' onclick=\"cargarRepuesto('" + window.btoa(JSON.stringify(arrCat[j])) + "',this)\">";
                        html += "<img src='../admin/" + arrCat[j].url_foto + "' class='' height='60px' alt='' />";
                        html += "<p>" + arrCat[j].nombre + "  (" + arr[i].count + ")</p>";
                        html += "</div>";
                        html += "</div>";
                        divListaCategorias.innerHTML += html;
                    }
                }

            }


        }
    });


}
function cargarRepuesto(obj, elm) {
    elm.classList.remove("col-12");
    elm.classList.add("col-2");
    seleccionar("cargarRepuesto", elm);

    obj = JSON.parse(window.atob(obj));
    divListaRepuestos.innerHTML = "<div class='col-12'>Repuestos <hr></div>";

    $.post("../admin/repuestosController", {TokenAcceso: "servi12sis3", evento: "getAll_rep_by_auto_cat", idAuto: obj.id_auto, idCat: obj.id}, function (resp) {
        if (resp == "error") {
//                alert(resp);
            return;
        }
        var json = JSON.parse(resp);
        var arr = JSON.parse(json.resp);
        for (var i = 0; i < arr.length; i++) {
            var html = "<div id='cat_" + arr[i].id + "' class='col-4'>";
            html += "<div class='col-12 itenRepAutoM' onclick=\"window.location.href=\'repuesto.html?id="+arr[i].id+"'\"  target='_blank'>";
            html += "<p>Codigo: " + arr[i].codigo + "</p>";
            html += "<img src='../admin/" + arr[i].url_foto + "' class='' height='60px' alt='' />";
            html += "<p>" + arr[i].nombre + "</p>";
            html += "</div>";
            html += "</div>";
            divListaRepuestos.innerHTML += html;
        }
    });


}

function seleccionar(type, elm) {

    var index = 0;
    divListaMarcas.style.display = "none";
    divListaModelos.style.display = "none";
    divListaVersiones.style.display = "none";
    divListaAutos.style.display = "none";
    divListaCategorias.style.display = "none";
    divListaRepuestos.style.display = "none";
    switch (type) {
        case "cargarRepAutoMarca":
            divListaMarcas.style.display = "";
            index = 1;
            break;
        case "cargarRepAutoModelo":
            divListaModelos.style.display = "";
            index = 2;
            break;
        case "cargarRepAutoVersion":
            divListaVersiones.style.display = "";
            index = 3;
            break;
        case "cargarRepAuto":
            index = 4;
            divListaAutos.style.display = "";
            break;
        case "cargarCategorias":
            index = 5;
            divListaCategorias.style.display = "";
            break;
        case "cargarRepuesto":
            index = 6;
            divListaRepuestos.style.display = "";

            break;
    }

    var list = divListaSelected.getElementsByClassName("itenRepAutoM");
    for (var i = list.length; i > index; i--) {
        divListaSelected.removeChild(list[i - 1]);
    }
    if (elm != null) {

        elm.classList.remove("col-6");
        elm.classList.remove("categoriaclas");
        elm.classList.remove("subcategoriaclas");
        
        divListaSelected.appendChild(elm);
    }
    sessionStorage.setItem("repuesto_busqueda_cliente",divListaSelected.innerHTML);
}