/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



var url = "../pagClienteController";

$(document).ready(function () {
    getMarcasTop6();
   
});

function getMarcasTop6() {

    miPost(url, {evento: "getMarcasTop6"}, function (resp) {
        var json = $.parseJSON(resp.resp);
        $.each(json,function(i,obj){
            $("#conMarcasTop").append(cargarMarca(obj));
        });
        cargar_repuestosTop8();
//        alert(resp);
    });
}


function cargarMarca(obj){
     var html = "";
     html+="<div class='category col-lg-2 col-md-2 col-sm-4 col-xs-6'>";
     html+="                       <a href='tienda.html'>";
     html+="                           <img src='../"+obj.url_foto+"' alt='1' style='max-height: 100px;' />";
     html+="                           <p>"+obj.nombre+"</p>";
     html+="                       </a>";
     html+="                   </div>";
     return html;
}


function cargar_repuestosTop8(){
      miPost(url, {evento: "getRepuestosTop8"}, function (resp) {
          $("#repuestosTop8cont").html("");
        var json = $.parseJSON(resp.resp);
        
        $.each(json,function(i,obj){
            cargarItenRepuesto(obj);
        });
        
    });
}

function cargarItenRepuesto(obj){
    var html = "<div class='col-lg-3 col-md-4 col-sm-6'>";
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
    $("#repuestosTop8cont").append(html);
}