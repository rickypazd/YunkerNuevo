/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var canvas = document.getElementById('myCanvas');
var context = canvas.getContext('2d');
var $canvas = $("#myCanvas");

var url;
function cargar_imagen_canvas(obj) {
    var img = new Image();
    this.url =PARAMS.url_image+ obj.url_foto_esquema;
    
    img.onload = function () {
        canvas.width = img.width;
    canvas.height = img.height;
    $canvas.drawImage({
            layer: true,
            name: 'image',
            source: url,
            x: 0, y: 0,
            width: img.width,
            height:img.height,
            fromCenter: false
        });
        buscarParte();
    }
    img.src= url;
    // img.src = url;


}




var flag2 = 0;
var flag = 0;
var click = null;


canvas.addEventListener("mousedown", function (evt) {
    flag = 0;
    flag2 = 1;
    click = getMousePos(canvas, evt);


}, false);

canvas.addEventListener("mousemove", function (evt) {
    if (flag2 == 1) {

        var mousePos = getMousePos(canvas, evt);
        var mousePos2 = click;
        var width = mousePos.x - mousePos2.x;
        var heigh = mousePos.y - mousePos2.y;

        $canvas.removeLayer("lineSelec").drawLayers();
        $canvas.drawRect({
            layer: true,
            strokeStyle: 'black',
            strokeWidth: 2,
            name: 'lineSelec',
            x: mousePos2.x, y: mousePos2.y,
            fromCenter: false,
            width: width,
            height: heigh
        });




    }
    flag = 1;
}, false);
var data_img;
canvas.addEventListener("mouseup", function (evt) {
    flag2 = 0;
    if (flag === 0) {
//                    console.log("click");
//                    var mousePos = getMousePos(canvas, evt);
//                    var message = 'Mouse click ' + mousePos.x + ',' + mousePos.y;
//                    writeMessage(canvas, message);

    } else if (flag === 1) {
        console.log("drag");
        var mousePos = getMousePos(canvas, evt);
        var mousePos2 = click;
        var mouseFinal = {"x": 0, "y": 0};
        var width = mousePos.x - mousePos2.x;
        if (width < 20 && width > -20) {
            $canvas.removeLayer("lineSelec").drawLayers();
            return;
        }
        var heigh = mousePos.y - mousePos2.y;
        //  context.lineWidth = "1";
        //  context.strokeStyle = "black";
        if (width > 0) {

            mouseFinal.x = mousePos2.x;
        } else {
            mouseFinal.x = mousePos.x;
        }

        if (heigh > 0) {
            mouseFinal.y = mousePos2.y;
        } else {
            mouseFinal.y = mousePos.y;
        }

        //context.rect(mouseFinal.x, mouseFinal.y, Math.abs(width), Math.abs(heigh));
        //context.stroke();
        $canvas.removeLayer("lineSelec").drawLayers();
        var croppedCan = crop(canvas, {x: mouseFinal.x, y: mouseFinal.y}, {x: Math.abs(width + 1), y: Math.abs(heigh + 1)});

        var img = document.getElementById("img_url_foto");

        $('#selectMetod').modal('show');
//        $('#myModal').modal('show');


        var data = croppedCan.toDataURL("image/png");
        data_img = data;
        $("#foto_recorte").attr("src", data);
        $("#foto_recorte").data("posx", mouseFinal.x);
        $("#foto_recorte").data("posy", mouseFinal.y);
        $("#foto_recorte").data("poswi", width + 1);
        $("#foto_recorte").data("poshe", heigh + 1);





    }
}, false);

function addExist() {
    $('#selectMetod').modal('toggle');
    $('#aggParteExis').modal('show');
}
function addNew() {
    $('#selectMetod').modal('toggle');
    $('#myModal').modal('show');
}

function dataURItoBlob(dataURI) {
// convert base64/URLEncoded data component to raw binary data held in a string
    var byteString;
    if (dataURI.split(',')[0].indexOf('base64') >= 0)
        byteString = atob(dataURI.split(',')[1]);
    else
        byteString = unescape(dataURI.split(',')[1]);
// separate out the mime component
    var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];
// write the bytes of the string to a typed array
    var ia = new Uint8Array(byteString.length);
    for (var i = 0; i < byteString.length; i++) {
        ia[i] = byteString.charCodeAt(i);
    }
    return new Blob([ia], {type: mimeString});
}

function crop(can, a, b) {
    // get your canvas and a context for it
    var ctx = can.getContext('2d');

    // get the image data you want to keep.
    var imageData = ctx.getImageData(a.x, a.y, b.x, b.y);

    // create a new cavnas same as clipped size and a context
    var newCan = document.createElement('canvas');
    newCan.width = b.x;
    newCan.height = b.y;
    var newCtx = newCan.getContext('2d');

    // put the clipped image on the new canvas.
    newCtx.putImageData(imageData, 0, 0);

    return newCan;
}
function clearCanvas(context, canvas) {
    context.clearRect(0, 0, canvas.width, canvas.height);
    var w = canvas.width;
    canvas.width = 1;
    canvas.width = w;
}


function mostrar() {
    var archivo = document.getElementById("myFile").files[0];
    var reader = new FileReader();
    if (archivo) {
        reader.readAsDataURL(archivo);
        reader.onloadend = function () {
            //   document.getElementById("img").src = reader.result;
            img.src = reader.result;
            img.onload = function () {
                context.drawImage(img, 0, 0, canvas.width, canvas.height);
            };
        }
    }
}
function writeMessage(canvas, message) {
    var context = canvas.getContext('2d');
    context.clearRect(0, 0, canvas.width, canvas.height);
    context.font = '18pt Calibri';
    context.fillStyle = 'black';
    context.fillText(message, 10, 25);
}
function getMousePos(canvas, evt) {
    var rect = canvas.getBoundingClientRect();
    return {
        x: evt.clientX - rect.left,
        y: evt.clientY - rect.top
    };
}




function registrar_parte_esquema() {

    mostrar_progress();
    var exito = true;
    var nombre = $("#text_nombre").val() || null;
    var serie = $("#text_serie").val() || null;
    var precio = $("#text_precio").val() || null;
    var foto = data_img || null;
    var posx = $("#foto_recorte").data("posx");
    var posy = $("#foto_recorte").data("posy");
    var poswi = $("#foto_recorte").data("poswi");
    var poshe = $("#foto_recorte").data("poshe");

    var id_padre = getQueryVariable("id");
    var TokenAcceso = "servi12sis3";
    var usr_log = $.parseJSON(sessionStorage.getItem("usr_log"));
    if (nombre != null && nombre.length > 0) {
        $("#text_nombre").css("background", "#ffffff");
    } else {
        $("#text_nombre").css("background", "#df5b5b");
        exito = false;
    }
    if (serie != null && serie.length > 0) {
        $("#text_serie").css("background", "#ffffff");
    } else {
        $("#text_serie").css("background", "#df5b5b");
        exito = false;
    }
    if (precio != null && precio.length > 0) {
        $("#text_precio").css("background", "#ffffff");
    } else {
        $("#text_precio").css("background", "#df5b5b");
        exito = false;
    }


    var lista = $("#list_marcas").find(".iten_repuesto_row");
    var list = [];
    var inpdetalle, inpvalor;
    var detalle, valor;
    $.each(lista, function (i, obj) {
        inpdetalle = $(lista[i]).find("input[name=detalle]");
        inpvalor = $(lista[i]).find("input[name=valor]");
        detalle = $(inpdetalle).val() || null;
        valor = $(inpvalor).val() || null;
        if (valor != null && valor.length > 0) {
            $(inpvalor).css("background", "#ffffff");
        } else {
            $(inpvalor).css("background", "#df5b5b");
            exito = false;
        }
        if (detalle != null && detalle.length > 0) {
            $(inpdetalle).css("background", "#ffffff");
        } else {
            $(inpdetalle).css("background", "#df5b5b");
            exito = false;
        }
        valor = $(lista[i]).find("input[name=valor]").val();
        list.push({detalle: detalle, valor: valor});
    });

    if (id_padre > 0) {
        //exito
        $("input[name=id_padre]").val(id_padre);
    } else {
        alert("Ocurrio algun problema. Disculpe las molestias.");
        window.location.href = "index.html";
    }
    if (exito) {
        mostrar_progress();
        var formData = new FormData($("#submitform2")[0]);
        formData.append("foto", dataURItoBlob(data_img));
        formData.append("x", posx);
        formData.append("y", posy);
        formData.append("width", poswi);
        formData.append("height", poshe);
        formData.append('list_detalle', JSON.stringify(list));
        $.ajax({
            url: "repuestosController",
            type: 'POST',
            data: formData,
            contentType: false,
            cache: false,
            processData: false,
            success: function (resp)
            {
                cerrar_progress();
                if (resp != null) {
                    var obj = $.parseJSON(resp);
                    if (obj.estado != 1) {
                        alert(obj.mensaje);
                    } else {
                        alert(obj.mensaje);
                        var obje = $.parseJSON(obj.resp);
                        window.location.href = 'RepPerfil.html?id=' + obje.id;
                    }
                }
            }
        });
    } else {
        cerrar_progress();
    }
}


function registrarParteExist(id) {

    mostrar_progress();
    var exito = true;

    var foto = data_img || null;
    var posx = $("#foto_recorte").data("posx");
    var posy = $("#foto_recorte").data("posy");
    var poswi = $("#foto_recorte").data("poswi");
    var poshe = $("#foto_recorte").data("poshe");

    var id_padre = getQueryVariable("id");
    var TokenAcceso = "servi12sis3";

    if (id_padre > 0) {
        //exito
        $("input[name=id_padre]").val(id_padre);
    } else {
        alert("Ocurrio algun problema. Disculpe las molestias.");
        window.location.href = "index.html";
    }
    if (exito) {
        mostrar_progress();
        var formData = new FormData();
        formData.append("evento", "registrar_repuesto_existente_parte_esquema");
        formData.append("TokenAcceso", TokenAcceso);
        formData.append("id_padre", id_padre);
        formData.append("id_rep", id);
        formData.append("foto", dataURItoBlob(data_img));
        formData.append("x", posx);
        formData.append("y", posy);
        formData.append("width", poswi);
        formData.append("height", poshe);

        $.ajax({
            url: "repuestosController",
            type: 'POST',
            data: formData,
            contentType: false,
            cache: false,
            processData: false,
            success: function (resp)
            {
                cerrar_progress();
                if (resp != null) {
                    var obj = $.parseJSON(resp);
                    if (obj.estado != 1) {
                        alert(obj.mensaje);
                    } else {
                        alert(obj.mensaje);
                        var obje = $.parseJSON(obj.resp);
                        window.location.href = 'RepPerfil.html?id=' + obje.id;
                    }
                }
            }
        });
    } else {
        cerrar_progress();
    }
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
