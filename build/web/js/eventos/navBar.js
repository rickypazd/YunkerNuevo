/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function () {
    cargar();

});
function verPagPadre(pag) {
    window.top.ver_pagina(pag);
}

function cargar() {
    var foto = "img/Sin-foto1.jpg";
    if (sessionStorage.getItem("usr_log")) {

        var usr_log = $.parseJSON(sessionStorage.getItem("usr_log"));
        foto = usr_log.url_foto;
    }


    var html = "";
    html = " <div id='right-panel' class='right-panel'>";
    html += "        <header id='header' class='header'>";
    html += "            <div class='top-left'>";
    html += "                <div class='navbar-header'>";
    html += "                    <a class='navbar-brand' href='./'><img src='img/logofullpart.png'  height='40' alt='Logo'/></a>";
    html += "                    <a class='navbar-brand hidden' href='./'><img src='img/logo2.png' alt='Logo'></a>";
    html += "                    <a id='menuToggle' class='menutoggle'><i class='fa fa-bars'></i></a>";
    html += "                </div>";
    html += "            </div>";
    html += "            <div class='top-right'>";
    html += "                <div class='header-menu'>";
    html += "                    <div class='user-area dropdown float-right'>";
    html += "                        <button type='button'  class='dropdown-toggle active' style='border:none;' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>";
    html += "                            <img class='user-avatar rounded-circle' src='" + foto + "' alt=''>";
    html += "                        </button>";
    html += "                        <div class='dropdown-menu user-menu'>";
    html += "                            <a class='nav-link' href='#'><i class='fa fa-user'></i>Mi perfil</a>";
    html += "                            <a class='nav-link' href='#'><i class='fa fa-user'></i>Notificaciones<span class='count'>13</span></a>";
    html += "                            <a class='nav-link' href='#'><i class='fa fa-cog'></i>Ajustes</a>";
    html += "                            <button class='' onclick='cerrar_cession();' ><i class='fa fa-power -off'></i>Cerrar sesion</button>";
    html += "                        </div>";
    html += "                    </div>";
    html += "                </div>";
    html += "            </div>";
    html += "        </header>";
    $("#right-panel").prepend(html);
    html = "<aside id='left-panel' class='left-panel'>";
    html += "        <nav class='navbar navbar-expand-sm navbar-default'>";
    html += "            <div id='main-menu' class='main-menu collapse navbar-collapse'>";
    html += "                <ul class='nav navbar-nav' id='listaBotones'>";
    html += "                    <li class='nav-item'  data-url='index.html'>";
    html += "                        <a href='index.html'><i class='menu-icon fa fa-laptop'></i>Inicio</a>";
    html += "                    </li>";
    html += "                    <li class='menu-title'>RRHH</li>";
    html += "                    <li class='nav-item' data-url='rolesypermisos.html'><a href='rolesypermisos.html'><i class='menu-icon fa fa-unlock-alt'></i>Roles y permisos.</a></li>";
    html += "                    <li class='nav-item' data-url='UsrList.html'><a href='UsrList.html'><i class='menu-icon fa fa-user-plus'></i>Usuarios.</a></li>";
    html += "                    <li class='menu-title'>Vehiculos</li>";
    html += "                    <li class='nav-item' data-url='VehMarcaList.html'><a href='VehMarcaList.html'><i class='menu-icon fa fa-car'></i>Marcas.</a></li>";
    html += "                    <li class='nav-item' data-url='VehModeloList.html' ><a href='VehModeloList.html'><i class='menu-icon fa fa-car'></i>Modelos.</a></li>";
    html += "                    <li class='nav-item' data-url='VehList.html' ><a href='VehList.html'><i class='menu-icon fa fa-car'></i>Vehiculos.</a></li>";
    html += "                    <li class='menu-title'>Repuestos</li>";
    html += "                    <li class='nav-item' data-url='RepCategoriaList.html' ><a href='RepCategoriaList.html'><i class='menu-icon fa fa-cog'></i>Categorias.</a></li>";
    html += "                    <li class='nav-item' data-url='RepList.html' ><a href='RepList.html'><i class='menu-icon fa fa-cog'></i>Repuestos.</a></li>";
    html += "                    <li class='menu-title'>Almacen</li>";
    html += "                    <li class='nav-item' data-url='AlmacenList.html' ><a href='AlmacenList.html'><i class='menu-icon fa fa-cog'></i>Alamacenes.</a></li>";
    html += "                    <li class='nav-item' data-url='CompraRep.html' ><a href='CompraRep.html'><i class='menu-icon fa fa-cog'></i>Comprar repuesto.</a></li>";
    html += "                    <li class='nav-item' data-url='CompraList.html' ><a href='CompraList.html'><i class='menu-icon fa fa-cog'></i>Historial de compras.</a></li>";
    html += "                    <li class='nav-item' data-url='VentaRep.html' ><a href='VentaRep.html'><i class='menu-icon fa fa-cog'></i>Vender repuesto.</a></li>";
    html += "                    <li class='nav-item' data-url='VentaList.html' ><a href='VentaList.html'><i class='menu-icon fa fa-cog'></i>Historial de ventas..</a></li>";
    html += "                    <li class='menu-title'>Indicadores</li>";
    html += "                    <li class='nav-item' data-url='IndicadorINEG.html' ><a href='IndicadorINEG.html'><i class='menu-icon fa fa-cog'></i>Compras y ventas.</a></li>";
    html += "                </ul>";
    html += "           </div><!-- /.navbar-collapse -->";
    html += "       </nav>";
    html += "   </aside>";
    $("body").prepend(html);
    var iten = $("#listaBotones").find("li[data-url='" + getUrl() + "']");
    $(iten).attr("class", "nav-item active");
}


function cerrar_cession() {
    sessionStorage.removeItem("usr_log");
    window.location.href = "Login.html";

}