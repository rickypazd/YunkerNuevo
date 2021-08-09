/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var canvas = document.getElementById('myCanvas');
var context = canvas.getContext('2d');
var $canvas = $("#myCanvas");
var opte = {
    layer: "Almacen",
    layerButon: "btnes"
}
var alamcen = [
    {
        id: 1,
        nombre: 'a',
        x1: 0,
        y1: 0,
        x2: 300,
        y2: 300,
    },
    {
        id: 2,
        nombre: 'a',
        x1: 400,
        y1: 100,
        x2: 800,
        y2: 300,
    }
];
var id;
$(document).ready(function () {
    id = getQueryVariable("id") || 0;
    if (id <= 0) {
        alert("eroor");
        return;
    }

    createAlmacen({
        id: 0,
        nombre: 'almacen',
        x1: 0,
        y1: 0,
        x2: canvas.width,
        y2: canvas.height,
    });
    $.post("almacenController", {
        evento: "getAll_sub_almacen", TokenAcceso: "servi12sis3",
        id_almacen: id
    }, function (resp) {
        if (resp != null) {
            var obj = $.parseJSON(resp);
            if (obj.estado != "1") {
                alert(obj.mensaje);
            } else {
                var json = $.parseJSON(obj.resp);

                $.each(json, function (i, obj) {
                    createAlmacen(obj);
                });
            }
        }

    });
//    createAlmacen(alamcen[0]);
    //   loadWind();
//    createAlmacen(alamcen[1]);


});
function createAlmacen(obj) {


    $canvas.drawRect({
        layer: true,
//        draggable: true,
        name: 'almacen' + obj.id,
        data: obj,
        groups: opte.layer,
//        dragGroups: opt.layer,
//        strokeDash: [3],
        fillStyle: '#00000000',
        strokeStyle: 'black',
        strokeWidth: 1,
        x: obj.x1, y: obj.y1,
        width: (obj.x2 - obj.x1), height: (obj.y2 - obj.y1),
        fromCenter: false,
        mouseover: function (layer) {
            // crearBoton(layer);
            layer.fillStyle = '#00000066';
        },
        mouseout: function (layer) {
            layer.fillStyle = '#00000000';
            layer.strokeWidth = 1;
            $canvas.removeLayerGroup(opte.layerButon[0]);
        },
    }).restoreCanvas({
        layer: true
    });
    crearOpciones(obj);
}

var select;
function crearOpciones(obj) {
    let color = "#007bff";
    var x = obj.x1 + 6;
    var y = obj.y1 + 3;
    $canvas.drawLine({
        layer: true,
        groups: ["option" + obj.id],
        name: 'iconshow' + obj.id,
        fromCenter: true,
        strokeStyle: color,
        strokeWidth: 3,
        x1: x, y1: y,
        x2: x + 8, y2: y + 10,
        x3: x + 16, y3: y

    });
    $canvas.drawRect({
        layer: true,
        groups: ["option" + obj.id],
//        draggable: true,
        name: 'btnCancel' + obj.id,
//        dragGroups: opt.layer,
        strokeStyle: color,
        fillStyle: '#ffffff00',
        data: obj,
        strokeWidth: 1,
        x: obj.x1, y: obj.y1,
        width: (obj.x2 - obj.x1), height: 18,
        cornerRadius: 0,
        fromCenter: false,
        mouseover: function (layer) {
            $canvas.css('cursor', 'pointer');
        }, click: function (evt) {
            $canvas.removeLayerGroup('optionPanel');
            if (evt.name != select) {
                select = evt.name;
                crearOpcionesPanel(evt);
            } else {
                select = null;
            }

        },
    });


    $canvas.drawText({
        layer: true,
        name: 'textAjus' + obj.id,
        groups: ["option" + obj.id],
        fillStyle: color,
        strokeWidth: 2,
        x: obj.x1 + 30, y: obj.y1 + 2,
        fontSize: '10pt',
        text: obj.nombre,
        fromCenter: false,
    });
}

function crearOpcionesPanel(layer) {
    let color = "#007bff";

    $canvas.drawRect({
        layer: true,
        groups: ["optionPanel"],
//        draggable: true,
        name: 'contenPanel',
//        dragGroups: opt.layer,
        strokeStyle: color,
        fillStyle: '#00000000',
        strokeWidth: 1,
        x: (layer.x), y: (layer.y + layer.height),
        width: 240, height: 23,
        cornerRadius: 0,
        fromCenter: false,
        mouseout: function (layer) {
            $canvas.removeLayerGroup('optionPanel');
            select = null;
        },
    });
    if (layer.data.id != 0) {
        addButtonEditar(layer.data, (layer.x) + 80, (layer.y + layer.height));
        addButtondel(layer.data, (layer.x) + 160, (layer.y + layer.height));
    }
    addButtonAdd(layer.data, (layer.x), (layer.y + layer.height));


}



function addButtonEditar(nombre, x, y) {
    let color = "#efc041";
    $canvas.drawRect({
        layer: true,
        groups: ["optionPanel", ],
//        draggable: true,
//        dragGroups: opt.layer,
        strokeStyle: color,
        fillStyle: color,
        strokeWidth: 1,
        data: nombre,
        x: x, y: y,
        width: 80, height: 23,
//        cornerRadius: 10,
        fromCenter: false,
        cursors: {
            // Show pointer on hover
            mouseover: 'pointer',
        },
        click: function (evt) {
            var layer = $canvas.getLayer('almacen' + evt.data.id_padre);
            addCut($canvas, layer.data, evt.data, function (data, data2) {
                $.post("almacenController", {
                    evento: "edit_almacen_sec", TokenAcceso: "servi12sis3",
                    id: data2.id,
                    x1: data.x1,
                    y1: data.y1,
                    x2: data.x2,
                    y2: data.y2

                }, function (resp) {
                    if (resp != null) {
                        var obj = $.parseJSON(resp);
                        if (obj.estado != "1") {
                            alert(obj.mensaje);
                        } else {
                            window.location.reload();
                        }
                    }
                    //createAlmacen(data);
                    //$("#confirm-addalma").modal('toggle');
                });
            });

            $canvas.removeLayerGroup('optionPanel');
            select = null;
        },
    });
    x += 6;
    y += 6;
    $canvas.drawImage({
        layer: true,
        groups: ["optionPanel", 'btneditar'],
        source: 'img/lapiz.svg',
        x: x + 4, y: y + 4,
        width: 18,
        height: 18,
    });
    text = $canvas.drawText({
        layer: true,
        groups: ["optionPanel", 'btneditar'],
        fillStyle: "#fff",
        strokeWidth: 2,
        x: x + 20, y: y,
        fontSize: '10pt',
        text: 'Editar',
        fromCenter: false,
    });
}
function addButtondel(nombre, x, y) {
    let color = "#df6059";
    $canvas.drawRect({
        layer: true,
        groups: ["optionPanel", ],
//        draggable: true,
//        dragGroups: opt.layer,
        strokeStyle: color,
        fillStyle: color,
        data: nombre,
        strokeWidth: 1,
        x: x, y: y,
        width: 80, height: 23,
//        cornerRadius: 10,
        fromCenter: false,
        cursors: {
            // Show pointer on hover
            mouseover: 'pointer',
        },
        click: function (evt) {

            $canvas.removeLayerGroup('optionPanel');
            select = null;
            $.post("almacenController", {evento: "eliminar_almacen_sec", id: evt.data.id, TokenAcceso: "servi12sis3"}, function (resp) {
                cerrar_progress();
                if (resp != null) {
                    var obj = $.parseJSON(resp);
                    if (obj.estado != "1") {
                        alert(obj.mensaje);
                    } else {

                        window.location.reload();

                        // alert(arr);

                    }
                }

            });

        }
    });
    x += 6;
    y += 6;
    $canvas.drawImage({
        layer: true,
        groups: ["optionPanel", 'btneditar'],
        source: 'img/bote-de-basura.svg',
        x: x + 4, y: y + 4,
        width: 18,
        height: 18,
    });
    text = $canvas.drawText({
        layer: true,
        groups: ["optionPanel", 'btneditar'],
        fillStyle: "#fff",
        strokeWidth: 2,
        x: x + 20, y: y,
        fontSize: '10pt',
        text: 'Eliminar',
        fromCenter: false,
    });
}
function addButtonAdd(nombre, x, y) {
    let color = "#6db946";
    $canvas.drawRect({
        layer: true,
        groups: ["optionPanel", ],
//        draggable: true,
//        dragGroups: opt.layer,
        strokeStyle: color,
        data: nombre,
        fillStyle: color,
        strokeWidth: 1,
        x: x, y: y,
        width: 80, height: 23,
//        cornerRadius: 10,
        fromCenter: false,
        cursors: {
            // Show pointer on hover
            mouseover: 'pointer',
        },
        click: function (evt) {
            evt.data.y1 += 18;
            var data = evt.data;
            data.x1 += 20;
            data.x2 -= 20;
            data.y1 += 20;
            data.y2 -= 20;
            addCut($canvas, evt.data, data, function (data, data2) {

                $('#btnConfir').data("obje", data);
                $('#btnConfir').val("");
                $('#btnConfir').on("click", function () {

                    var obj = $('#btnConfir').data("obje");
                    obj.nombre = $("#inpNombre").val() || "";

                    $.post("almacenController", {
                        evento: "registrar_sub_almacen", TokenAcceso: "servi12sis3",
                        nombre: obj.nombre,
                        id_padre: obj.id,
                        id_almacen: id,
                        x1: obj.x1,
                        y1: obj.y1,
                        x2: obj.x2,
                        y2: obj.y2

                    }, function (resp) {
                        createAlmacen(data);
                        $("#confirm-addalma").modal('toggle');
                    });

                });

                $("#confirm-addalma").modal('show');


            });

            $canvas.removeLayerGroup('optionPanel');
            select = null;
        },
    });
    x += 6;
    y += 6;
    $canvas.drawImage({
        layer: true,
        groups: ["optionPanel", 'btneditar'],
        source: 'img/aggicon.svg',
        x: x + 4, y: y + 4,
        width: 18,
        height: 18,
    });
    text = $canvas.drawText({
        layer: true,
        groups: ["optionPanel", 'btneditar'],
        fillStyle: "#fff",
        strokeWidth: 2,
        x: x + 20, y: y,
        fontSize: '10pt',
        text: 'Agregar',
        fromCenter: false,
    });
}

function loadWind() {
    $canvas.drawRect({
        layer: true,
//        draggable: true,
        mask: true,
        name: 'blue',
        fillStyle: '#00000066',
        x: 0, y: 0,
        fromCenter: false,
        width: canvas.width, height: canvas.height
    })

// Restore blue mask

}