/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var canvas = document.getElementById('myCanvas');
var context = canvas.getContext('2d');
var $canvas = $("#myCanvas");
var flag2 = 0;
var flag = 0;
var click = null;

var mousePosition;
//objs

var cont = [];
var itnHov = null;
$(document).ready(function () {
    var obj = createRect({nombre: 'idConten', x: 100, y: 100, width: Math.abs(500), height: Math.abs(500)});
    cont.push(obj);
});

function addListener() {


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
            if (itnHov == null) {
                mousePosition = mousePos;
                $canvas.removeLayer("lineSelec").drawLayers();
                createRectTemp({nombre: 'lineSelec', x: mousePos2.x, y: mousePos2.y, width: width, height: heigh});

            } else {
                var data = itnHov.data;
                if (mousePos.x > data.x && mousePos.x < (data.width + data.x)) {
                    mousePosition = mousePos;
                    $canvas.removeLayer("lineSelec").drawLayers();
                    createRectTemp({nombre: 'lineSelec', x: mousePos2.x, y: mousePos2.y, width: width, height: heigh});
                }
            }






        }
        flag = 1;
    }, false);
    var data_img;
    canvas.addEventListener("mouseup", function (evt) {
        flag2 = 0;
        if (flag === 1) {
            console.log("drag");
            var mousePos = mousePosition;
            var mousePos2 = click;
            var mouseFinal = {"x": 0, "y": 0};
            var width = mousePos.x - mousePos2.x;
            if (width < 20 && width > -20) {
                $canvas.removeLayer("lineSelec").drawLayers();
                return;
            }
            var heigh = mousePos.y - mousePos2.y;
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
            var obj = createRect({nombre: 'idConten' + (cont.length + 1), x: mouseFinal.x, y: mouseFinal.y, width: Math.abs(width), height: Math.abs(heigh)});
            cont.push(obj);
        }
    }, false);
}
function getMousePos(canvas, evt) {
    var rect = canvas.getBoundingClientRect();
    return {
        x: evt.clientX - rect.left,
        y: evt.clientY - rect.top
    };
}


function createRect(obj) {
    $canvas.drawRect({
        layer: true,
        strokeStyle: 'black',
        strokeWidth: 2,
        name: obj.nombre,
        data: obj,
        id: obj.nombre + "id",
        x: obj.x, y: obj.y,
        draggable: true,
        fromCenter: false,
        width: obj.width,
        height: obj.height,
        mouseover: function (layer) {

            if (flag2 != 1) {
                itnHov = layer;
            }
        },
        mouseout: function (layer) {
            if (flag2 != 1) {
                itnHov = null;
            }
        },
        click: function (layer) {
            // Spin star

            addCut($canvas, layer,function(obj){
                alert(obj);
            });
            //alert(layer.data);
//            $(".parteiten").css("background-color", "#fff");
//            $("#idparte" + $(layer).attr('name')).css("background-color", "#66666666");
//            $('html, body').animate({
//                scrollTop: ($("#idparte" + $(layer).attr('name')).offset().top - 300)
//            }, 100);
        }
    });
    return obj;
}
function createRectTemp(obj) {
    $canvas.drawRect({
        layer: true,
        strokeStyle: 'black',
        strokeWidth: 2,
        name: obj.nombre,
        data: obj,
        x: obj.x, y: obj.y,
        fromCenter: false,
        width: obj.width,
        height: obj.height

    });
    return obj;
}