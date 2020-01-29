/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    var carrito = sessionStorage.getItem("Carrito");
    if (carrito != null) {
        var arr = JSON.parse(carrito);
        var total = 0;
        $.each(arr, function (i, obj) {
            $("#itensCar").append(itenCarritoPa(obj));
           total+= (obj.precio * obj.cantidad);
          
            
        });
         $("#SubTotalCar").html(total+" Bs.");
         $("#totalCar").html(total+" Bs.");
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
            recalcule($button.parent().find("input"));
            e.preventDefault();
        });
    }


});



function itenCarritoPa(obj) {
   // obj = $.parseJSON(obj);

    var html = " <tr class='item' data-carrito='" + JSON.stringify(obj) + "'>";
    html += "                            <td class='thumb'><a href='repuesto.html?id=" + obj.id + "'><img src='../" + obj.url_foto + "' alt='img'></a></td>";
    html += "                            <td class='name'><a href='repuesto.html?id=" + obj.id + "'>" + obj.nombre + "</a></td>";
    html += "                            <td class='price'>" + obj.precio + " Bs.</td>";
    html += "                            <td class='qnt-count'>";
    html += "                                <a class='incr-btn' href='#'>-</a>";
    html += "                                <input class='quantity form-control' type='text' value='" + obj.cantidad + "' onchange='recalcule(this);'>";
    html += "                                <a class='incr-btn' href='#'>+</a>";
    html += "                            </td>";
    html += "                            <td class='total'>" + (obj.precio * obj.cantidad) + " Bs.</td>";
    html += "                            <td class='delete' onclick='eliminarCarrito(this);' ><i class='icon-delete'></i></td>";
    html += "                        </tr>";
    return html;
}

