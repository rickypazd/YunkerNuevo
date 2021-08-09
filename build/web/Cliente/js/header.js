/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


const PARAMS = {
    url_image:"../images/"
}

$(document).ready(function () {


    $("#header").load("header.html #header", function (resp) {
        //alert("ok");
        $('.menu-toggle').click(function () {
            $('.menu').toggleClass('expanded');
        });
        var state="";
        document.getElementById("contenBuscarMouse").addEventListener('mouseleave', function (evnt) {
            state = document.getElementById("respConteBusqueda").innerHTML;
            document.getElementById("respConteBusqueda").innerHTML = "";
        });
        document.getElementById("contenBuscarMouse").addEventListener('mouseenter', function (evnt) {
            
            document.getElementById("respConteBusqueda").innerHTML = state;
        });
        //Submenu Toggle

        cargar_carrito();

    });


});


function showMenu() {
    var mainMenu = document.getElementById("mainMenu");
    console.log(mainMenu.className);
    if (mainMenu.className.includes("oculto")) {
        console.log(true);
        mainMenu.className = mainMenu.className.replace("oculto", "");
    } else {
        mainMenu.className += " oculto";
    }
}

function cargar_carrito() {
    var carrito = sessionStorage.getItem("Carrito");
    if (carrito != null) {
        var arr = JSON.parse(carrito);
        $("#cantidadCarrito").html(arr.length);
        $.each(arr, function (i, obj) {
            $("#itensCarrito").append(itenCarrito(obj));
        });
        calcularTotal();
    }
}
function addToCarrito(obj) {
    var cantidad = 1;
    var carrito = sessionStorage.getItem("Carrito");
    var arr;
    if (carrito != null) {
        arr = JSON.parse(carrito);
        var index = arr.findIndex(item => item.id === obj.id);
//        alert(index);
        if (index >= 0) {
            alert("producto ya agregado.");
            return;
        }

        //arr.push(obj);


    }
    var objCarrito = {id: obj.id, url_foto: obj.url_foto || "", nombre: obj.nombre, precio: obj.precio, cantidad: 1, total: (obj.precio * cantidad), obj: obj};
    $("#ModalAddCartBody").html(itenCarritomodal(objCarrito));
    $(".incr-btn").on("click", function (e) {
        var $button = $(this);
        var oldValue = $button.parent().find("input").val();
        if ($button.text() == "+") {
            var newVal = parseFloat(oldValue) + 1;
        } else {
            // Don't allow decrementing below 1
            if (oldValue > 1) {
                var newVal = parseFloat(oldValue) - 1;
            } else {
                newVal = 1;
            }
        }

        $button.parent().find("input").val(newVal);

        var obj = $button.parent().find("input");
        var val = $(obj).val();
        //alert(val);
        var padre = $(obj).parent().parent();
        var objCarrito = $(padre).data("carrito");
        objCarrito.cantidad = val;
        objCarrito.total = (objCarrito.precio * val);
        $(padre).data("carrito", objCarrito);
        $(padre).find(".total").html(objCarrito.total + " Bs.");
        $("#BtmAgregarCarrito").attr("onclick", "okAddCarrito('" + JSON.stringify(objCarrito) + "')");
        e.preventDefault();
    });

    $("#BtmAgregarCarrito").attr("onclick", "okAddCarrito('" + JSON.stringify(objCarrito) + "')");

    $("#ModalAddCart").modal();

}

function okAddCarrito(obj) {
    obj = $.parseJSON(obj);
    var carrito = sessionStorage.getItem("Carrito");
    var arr;
    if (carrito != null) {
        arr = JSON.parse(carrito);

        arr.push(obj);

        sessionStorage.setItem("Carrito", JSON.stringify(arr));
    } else {
        arr = [];
        arr.push(obj);
        sessionStorage.setItem("Carrito", JSON.stringify(arr));
    }
    $("#itensCarrito").append(itenCarrito(obj));
    $("#cantidadCarrito").html(arr.length);
    calcularTotal();
    $("#ModalAddCart").modal('toggle');
}
function itenCarrito(obj) {
    //obj = $.parseJSON(obj);
    var html = "<tr class='item itCar" + obj.id + "'>";
    html += "                            <td><div class='delete'></div><a href='repuesto.html?id=" + obj.id + "'>" + obj.nombre + "</a></td>";
    html += "                            <td><input type='text' value='" + obj.cantidad + "' disabled></td>";
    html += "                            <td class='price'>" + obj.precio + " Bs.</td>";
    html += "                        </tr>";
    return html;
}
function itenCarritomodal(obj) {
    var html = " <tr class='item' data-carrito='" + JSON.stringify(obj) + "'>";
    html += "                            <td class='thumb'><a href='repuesto.html?id=" + obj.id + "'><img src='../admin/" + obj.url_foto + "' alt='img'></a></td>";
    html += "                            <td class='name'><a href='repuesto.html?id=" + obj.id + "'>" + obj.nombre + "</a></td>";
    html += "                            <td class='price'>" + (obj.precio ? (obj.precio + " Bs.") : ("Sin asignar")) + "</td>";
    html += "                            <td class='qnt-count' >";
    html += "                                <a class='incr-btn' href='#'>+</a>";
    html += "                                <input class='quantity form-control' type='text' value='" + obj.cantidad + "' onchange='recalcule(this);' />";
    html += "                                <a class='incr-btn' href='#'>-</a>";
    html += "                            </td>";
    html += "                            <td class='total'>" + obj.total + " Bs.</td>";

    html += "                        </tr>";
    return html;
}
function calcularTotal() {
    var carrito = sessionStorage.getItem("Carrito");
    var arr;
    if (carrito != null) {
        arr = JSON.parse(carrito);
        var total = 0;
        $.each(arr, function (i, obj) {
            //$("#itensCar").append(itenCarritoPa(obj));
            total += (obj.precio * obj.cantidad);


        });
        $("#SubTotalCar").html(total + " Bs.");
        $("#totalCarrito").html(total + " Bs.");
        $("#totalCar").html(total + " Bs.");
    }
}
function recalcule(obj) {
    var val = $(obj).val();
    //alert(val);
    var padre = $(obj).parent().parent();
    var objCarrito = $(padre).data("carrito");
    objCarrito.cantidad = val;
    objCarrito.total = (objCarrito.precio * val);
    $(padre).data("carrito", objCarrito);
    $(padre).find(".total").html(objCarrito.total + " Bs.");
    $("body").find(".itCar" + objCarrito.id).find("input").val(val);
    $("#BtmAgregarCarrito").attr("onclick", "okAddCarrito('" + JSON.stringify(objCarrito) + "')");
    var carrito = sessionStorage.getItem("Carrito");
    var arr;
    if (carrito != null) {
        arr = JSON.parse(carrito);
        var index = arr.findIndex(item => item.id === objCarrito.id);
        arr.splice(index, 1, objCarrito);


        //arr.push(obj);

        sessionStorage.setItem("Carrito", JSON.stringify(arr));
    }
    //  alert(objCarrito.nombre);
    calcularTotal();

}
function eliminarCarrito(obj) {

    //alert(val);
    var padre = $(obj).parent();
    var objCarrito = $(padre).data("carrito");

    $("body").find(".itCar" + objCarrito.id).remove();

    var carrito = sessionStorage.getItem("Carrito");
    var arr;
    if (carrito != null) {
        arr = JSON.parse(carrito);
        var index = arr.findIndex(item => item.id === objCarrito.id);
        arr.splice(index, 1);
        $(padre).remove();
        $("#cantidadCarrito").html(arr.length);
        //arr.push(obj);

        sessionStorage.setItem("Carrito", JSON.stringify(arr));
    }
    //  alert(objCarrito.nombre);
    calcularTotal();

}
function miPost(url, json, event, isLoad) {
    json.TokenAcceso = "servi12sis3";
    if (!isLoad)
        mostrar_progress();
    $.post(url, json, function (resp) {
        cerrar_progress();
        if (resp != null) {
            var obj = $.parseJSON(resp);
            if (obj.estado != "1") {
//                alert(obj.mensaje);
            } else {
                event(obj);
            }
        }
    });
}
var enPedido = {
    is: false,
    lastTime: new Date(),
};

var _enpedido = {
    last: 0,
    cur:0,
    cant:0,
    
};
function buscar(inp) {
    _enpedido.cur = new Date();
    _enpedido.cant+=1;
    if(_enpedido.cant==1){
        _enpedido.last = new Date();
        buscar_in_server(inp);
        
    }
    
}

function sleep(time) {
    return new Promise((resolve) => setTimeout(resolve, time));
}
function buscar_in_server(inp) {
    
    if(new Date() - _enpedido.cur < 600){
         sleep((200)).then(() => {
            buscar_in_server(inp);
            });
        return;
    }
    mostrar_progress();
    _enpedido.cant=0;
    
    var respConte = document.getElementById("respConteBusqueda");
    var busqueda = $(inp).val() || 0;
    if (!busqueda) {
        respConte.innerHTML = "";
        cerrar_progress();
        return;
    }
    if (busqueda.length <= 3) {
        respConte.innerHTML = "";
        cerrar_progress();
        return;
    }
     respConte.innerHTML = "";
    miPost("../pagClienteController", {evento: "getBuscar",pag:0, busqueda: busqueda}, function (resp) {
        cerrar_progress();
        respConte.innerHTML = "";
        var json = $.parseJSON(resp.resp);
        var html ="";
        $.each(json, function (i, obj) {
            html += " <div class='catalog-grid' style='display: flex; justify-content: center; padding:0;'>";
            html += "               <div class='tile ' style='width: 50%; display: inline-flex;' >";
            html += "                    <!--                                        <div class='badges'> <span class='sale'></span> </div>-->";
            html += "                    <div class='price-label'> " + (obj.precio ? (obj.precio + " Bs.") : ("Sin asignar")) + "</div> ";
            html += "                    <a href='repuesto.html?id=" + obj.repuesto.id + "' ><img class='foto_rep' src='../admin/" + obj.repuesto.url_foto + "' alt='1'></a>";
            html += "                </div>";
            html += "                <div class='tile ' style=' width: 50% !important; display: inline-flex; '>";
            var nombre = obj.repuesto.nombre || "";
            html += "                    <div class='footer' style='height:100%;' > <a href='repuesto.html?id=" + obj.repuesto.id + "'>" + nombre + "</a>";
            html += "                        <span>Codigo:" + obj.repuesto.codigo + "</span> <span>";
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
            html += " </span>                       <div class='tools' >";
//            html += "                            <div class='rate'> <span class='active'></span> <span class='active'></span> <span";
//            html += "                                    class='active'></span> <span></span> <span></span> </div>";
            html += "                            <a class='add-cart-btn' href='javaScript:void(0);' onclick='addToCarrito("+JSON.stringify(obj.repuesto)+");'><span>Agregar al carrito</span><i class='icon-shopping-cart'></i></a>";

            html += "                        </div>";
            html += "                    </div>";
            html += "                </div>";
            html += "            </div>";
          
        });
              respConte.innerHTML += html;
         

        
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