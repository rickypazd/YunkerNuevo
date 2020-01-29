/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var url = "../pagClienteController";

var canvas = document.getElementById('myCanvas');
var context = canvas.getContext('2d');
var $canvas = $("#myCanvas");
var img = new Image();
var url;


$(document).ready(function () {
    var id = getQueryVariable("id");
    if (id > 0) {
        getRepuesto(id);
    }


});

function getRepuesto(id) {
     var obje = {tipo: 5, id_relacion: parseInt(id), id_usuario: 0 , fecha : "now()", busqueda : "" };
     
    miPost(url, {evento: "getRepuestoId", id: id, seguimiento:"["+JSON.stringify(obje)+"]"}, function (resp) {
        var json = $.parseJSON(resp.resp);
        $("#nombre").html(json.nombre);
        $("#codigo").html("Codigo: " + json.codigo);
        $("#categoria").html(json.nombrecat + " - " + json.nombresubcat);
        $("#precio").html(json.precio + " Bs.");
        $("#foto_perfil").attr("src", "../" + json.url_foto);
        var detalle = json.detalle;
        $.each(detalle, function (i, obj) {
            $("#detalle").append(obj.detalle + ": ");
            $("#detalle").append(obj.valor + " </br>");
        });
        var vehiculos = json.vehiculos;
        if (vehiculos != null) {
            cargar_vehiculos(vehiculos);
        }
        var urlEsquema = json.url_foto_esquema;
        if (urlEsquema != null) {
            $("#conten_cambas").css("display", "");
            cargar_imagen_canvas("../" + urlEsquema);
        }
        var partes = json.partes;
        if (partes != null) {
           
            cargar_parte(partes);
        }

        //alert(resp);
    });
}


function cargar_vehiculos(arr) {
    $("#vehiculos").html("");
    $.each(arr, function (i, obj) {
        var html = "<div data-obj='" + JSON.stringify(obj) + "' class='item'>";
        html += "               <div class='row'>";
        html += "                     <div class='col-2'>";
        html += "                         <h6>" + obj.clave + "</h6>";
        html += "                     </div>";
        html += "                     <div class='col-1'>";
        html += "                         <img src='../" + obj.url_foto_marca + "' class='' height='50px' alt='' />";
        html += "                      </div>";
        html += "                     <div class='col-2'>";
        html += "                         <h6>" + obj.nombre_marca + "</h6>";
        html += "                     </div>";
        html += "                     <div class='col-1'>";
        html += "                         <img src='../" + obj.url_foto_modelo + "' class='' height='50px' alt='' />";
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

        html += "                 </div>";
        html += "            </div>";
        $("#vehiculos").append(html);
    });
}


function cargar_parte(arr) {
    $("#lista_parte").html("");
    //var context = $canvas.getContext('2d');
    $.each(arr, function (i, obj) {
        var html = "<div data-obj='" + JSON.stringify(obj) + "' id='idparte" + obj.id + "' onmouseout='removeCol(this);' class='item parteiten'>";
        html += "               <div class='row'>";
        html += "                     <div class='col-2'>";
        html += "                         <h6>" + obj.codigo + "</h6>";
        html += "                     </div>";
        html += "                     <div class='col-2'>";
        html += "                         <img src='../" + obj.url_foto + "' class='' height='50px' alt='' />";
        html += "                      </div>";
        html += "                     <div class='col-4'>";
        html += "                         <h6>" + obj.nombre + "</h6>";
        html += "                     </div>";
        html += "                     <div class='col-2'>";
        html += "                         <h6>" + obj.precio + " Bs.</h6>";
        html += "                     </div>";
        html += "                     <div class='col-2'>";
        html += "                         <a class='btn btn-primary btn-sm' id='addItemToCart' href='repuesto.html?id="+obj.id+"' >VER</a>";
        html += "                     </div>";
        
        html += "                 </div>";
        html += "            </div>";
        $("#lista_parte").append(html);
        if (obj.tipo = 3) {
            
            $canvas.drawRect({
                layer: true,
                strokeStyle: 'black',
                strokeWidth: 2,
                name: obj.id + "",
                x: obj.x, y: obj.y,
                fromCenter: false,
                width: Math.abs(obj.width),
                height: Math.abs(obj.height),
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
                    $(".parteiten").removeClass("backColorVerde");
                    $("#idparte" + $(layer).attr('name')).addClass("backColorVerde");
                    
                    $('html, body').animate({
                        scrollTop: ($("#idparte" + $(layer).attr('name')).offset().top - 300)
                    }, 100);
                }
            });
           
        }
    });
}

function removeCol(evt){
    $(evt).removeClass("backColorVerde");
}