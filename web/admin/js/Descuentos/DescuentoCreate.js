/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var url = "adminController";
function registrarDescuento() {
    var descuento = $("#text_marca_nombre").val() || null;
    var usr_log = $.parseJSON(sessionStorage.getItem("usr_log"));
    var exito = true;
    if (descuento != null && descuento.length > 0) {
        $("#text_marca_nombre").css("background", "#ffffff");
    } else {
        $("#text_marca_nombre").css("background", "#df5b5b");
        exito = false;
    }
    
    if(exito){
         mostrar_progress();
         $.post(url, {evento: "registrarDescuento", descuento: descuento, usr_log:usr_log.id, TokenAcceso: "servi12sis3"}, function (data) {
            if (data != null) {
                    var obj = $.parseJSON(data);
                    if (obj.estado != "1") {
                        alert(obj.mensaje);
                    } else {
                          window.history.back();

                    }
                }
        
         });
    }
}

function changeDescuento(){
      var descuento = $("#text_marca_nombre").val() || null;
    if (descuento<0 ) {
        $("#text_marca_nombre").val(0);
    } 
    if (descuento>100 ) {
        $("#text_marca_nombre").val(100);

    } 
}