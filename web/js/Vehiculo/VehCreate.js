/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var url = "autosController";
$(document).ready(function () {
    $("#art_año").datepicker({
        format: "yyyy",
        viewMode: "years",
        minViewMode: "years"
    });
    cargar_marcas();
   
});

function cargar_marcas() {
    mostrar_progress();
    $.post(url, {
        evento: "getAll_rep_auto_marca",
        TokenAcceso: "servi12sis3"
    }, function (resp) {
        cerrar_progress();
        if (resp != null) {
            var obj = $.parseJSON(resp);
            if (obj.estado != 1) {
                //error
                alert(obj.mensaje);
            } else {
                //exito
                //alert(resp);
                $("#lista_marca").html("");
                cargar_lista_marca($.parseJSON(obj.resp));

            }
        }
    });
}

function cargar_lista_marca(arr) {
    $.each(arr, function (i, obj) {
        cargar_iten_marca(obj);
    });
}

function cargar_iten_marca(obj) {
    var url_foto = "img/Sin_imagen.png";
    if (obj.url_foto.length > 0) {
        url_foto = obj.url_foto;
    }
    var html = "<li onclick='seleccionar_marca(" + JSON.stringify(obj) + ")'>";
    html += "   <img src='" + url_foto + "' height='50' width='80' alt=''/>";
    html += "   <span>" + obj.nombre + "</span>";
    html += "</li>";
    $("#lista_marca").append(html);
}

var id_marca;

function seleccionar_marca(item) {
    var obj = item;
    id_marca = obj.id;
    $("#art_marca").val(obj.nombre);
    $("#art_marca").data("id", obj.id);
    $(".bd-marca").modal('toggle');
    $("#art_modelo").removeAttr("disabled");
    $("#buscar_version").css("display","none");
     $("#lista_version").html("");
}


function cargar_version_auto(id) {
    var html = "";
    //mostrar_progress();
    $.post(url, {TokenAcceso: "servi12sis3", evento: "get_rep_auto_version_by_id_rep_auto_modelo", id:id}, function (resp) {
        //  cerrar_progress();
        if (resp != null) {
            var obj = $.parseJSON(resp);
            if (obj.estado != "1") {
                alert(obj.mensaje);
            } else {
                var arr = $.parseJSON(obj.resp);
                $.each(arr, function (i, obj) {
                    html += "<li class='list-group-item' data-obj='" + JSON.stringify(obj) + "' onclick='agregar_version(this);'>" + obj.nombre + "</li>";
                });
                $("#lista_version").html(html);
            }
        }
    });
}

function agregar_version(iten) {
    $(iten).attr("onclick", "quitar_version(this);");
    $(iten).appendTo("#lista_version_agregadas");
}

function quitar_version(iten) {
    $(iten).attr("onclick", "agregar_version(this);");
    $(iten).appendTo("#lista_version");
}

function obtener_modelo() {
    var html = "";
    mostrar_progress();
    $.post(url, {TokenAcceso: "servi12sis3", evento: "get_rep_auto_modelo_by_id_rep_auto_marca", id: id_marca}, function (resp) {
        cerrar_progress();
        if (resp != null) {
            var obj = $.parseJSON(resp);
            if (obj.estado != "1") {
                alert(obj.mensaje);
            } else {
                var arr = $.parseJSON(obj.resp);
                $.each(arr, function (i, obj) {
                    html+= "<li class='list-group-item' data-obj='" + JSON.stringify(obj) + "' onclick='agregar_iten_modelo(this);'>";
                    html += "   <img src='" + obj.url_foto + "' height='50' width='80' alt=''/>";
                    html += "   <span>" + obj.nombre + "</span>";
                    html += "</li>";
                   
                });
                $("#lista_modelo").html(html);
            }
        }
    });
}

function agregar_iten_modelo(iten) {
    var obj = $(iten).data("obj");
    $("#art_modelo").val(obj.nombre);
    $("#art_modelo").data("id", obj.id);
    $(".bd-modelo").modal('toggle');
    $("#buscar_version").css("display","");
     cargar_version_auto(obj.id);
}

function agg_nuevo_version(){
    var id = $("#art_modelo").data("id");
    
    window.location.href = "VehModeloPerfil.html?id="+id;
}

function registrar_Vehiculo() {
    mostrar_progress();
    var exito = true;
    var clave = $("#art_clave").val() || null;
    var año = $("#art_año").val() || null;
    var id_marca = $("#art_marca").data("id");
    var marca = $("#art_marca").val() || null;
    var id_modelo = $("#art_modelo").data("id");
    var modelo = $("#art_modelo").val() || null;
    var TokenAcceso = "servi12sis3";
    var usr_log = $.parseJSON(sessionStorage.getItem("usr_log"));
    if (clave != null && clave.length > 0) {
        $("#art_clave").css("background", "#ffffff");
    } else {
        $("#art_clave").css("background", "#df5b5b");
        exito = false;
    }
    if (año != null && año.length > 0) {
        $("#art_año").css("background", "#ffffff");
    } else {
        $("#art_año").css("background", "#df5b5b");
        exito = false;
    }
    if (marca != null && marca.length > 0 && id_marca > 0) {
        $("#art_marca").css("background", "#ffffff");
    } else {
        $("#art_marca").css("background", "#df5b5b");
        exito = false;
    }
    if (modelo != null && modelo.length > 0 && id_modelo > 0) {
        $("#art_modelo").css("background", "#ffffff");
    } else {
        $("#art_modelo").css("background", "#df5b5b");
        exito = false;
    }
    var lista = [];
    var array = $("#lista_version_agregadas").find("li");
    $.each(array, function (i, obj) {
        lista.push($(obj).data("obj"));
    });

    if (exito) {
        $.post(url,
                {
                    evento: "registrar_rep_auto",
                    TokenAcceso: TokenAcceso,
                    id_usr: usr_log.id,
                    clave: clave,
                    anho: año,
                    versiones: JSON.stringify(lista)
                }, function (respuesta) {
            cerrar_progress();
            if (respuesta != null) {
                var obj = $.parseJSON(respuesta);
                if (obj.estado != 1) {
                    alert(obj.mensaje);
                } else {
                    //exito                    

                    window.history.back();
                }
            }
        });
    } else {
        cerrar_progress();
    }
}


