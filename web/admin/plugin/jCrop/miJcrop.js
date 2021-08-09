/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var $selection;
var $firs;
var $canvas;
var init = false;
var opt = {
    x1: 100,
    y2: 100,
    x2: 100,
    y2: 100,
    layer: ['cutes'],
};
var okfunction;
var layers;
var layersini;


function addCut($canva, layer,layerinit, okfunctio) {
    if (init) {
        return;
    }
    layersini =layerinit;
    okfunction = okfunctio;
    init = true;
    $canvas = $canva;
    layers= layer;
    opt.id = layer.id;
    opt.x1 = layerinit.x1;
    opt.y1 = layerinit.y1;
    opt.x2 = layerinit.x2;
    opt.y2 = layerinit.y2;
    render(null, " ");


}
var tempx2;
var tempy2;
function render(event, tipe) {
        
      if(opt.x1 < layers.x1){
          opt.x1 = layers.x1;
         
      }
      if(opt.x2 > layers.x2){
          opt.x2 = layers.x2;
      }
      if(opt.y1 < layers.y1){
          opt.y1 = layers.y1;
      }
      if(opt.y2 > layers.y2){
          opt.y2 = layers.y2;
      }
    
      if(opt.x1 > opt.x2){
          tempx2=opt.x1;
          opt.x1 = opt.x2;
          opt.x2 = tempx2;
      }
      if(opt.y1 > opt.y2){
          tempy2=opt.y1;
          opt.y1 = opt.y2;
          opt.y2 = tempy2;
      }
      if((opt.y2-opt.y1)<38){
          
          opt.y2 += 20;
      }
      if((opt.x2-opt.x1)<30){
          opt.x2 += 30;
      }
   
    if (tipe != 'addSel') {
        $canvas.removeLayer("addSel");
        addSel();
    }
    if (tipe != 'addx1y1') {
        $canvas.removeLayer("addx1y1");
        addx1y1();
    }
    if (tipe != 'addx2y1') {
        $canvas.removeLayer("addx2y1");
        addx2y1();
    }
    if (tipe != 'addx3y1') {
        $canvas.removeLayer("addx3y1");
        addx3y1();
    }
    if (tipe != 'addx1y2') {
        $canvas.removeLayer("addx1y2");
        addx1y2();
    }

    if (tipe != 'addx3y2') {
        $canvas.removeLayer("addx3y2");
        addx3y2();
    }
    if (tipe != 'addx1y3') {
        $canvas.removeLayer("addx1y3");
        addx1y3();
    }
    if (tipe != 'addx2y3') {
        $canvas.removeLayer("addx2y3");
        addx2y3();
    }
    if (tipe != 'addx3y3') {
        $canvas.removeLayer("addx3y3");
        addx3y3();
    }
    if (tipe != 'btnAdd') {
        $canvas.removeLayer("btnAdd");
        $canvas.removeLayer("myText");
        btnAdd();
    }
    if (tipe != 'btnCancel') {
        $canvas.removeLayer("btnCancel");
        $canvas.removeLayer("myTextCancel");
        btnCancel();
    }



}

function addSel() {
    $canvas.drawRect({
        layer: true,
        draggable: true,
        name: 'addSel',
        groups: opt.layer,
//        dragGroups: opt.layer,
        strokeDash: [3],
        strokeStyle: 'black',
        strokeWidth: 2,
        x: opt.x1, y: opt.y1,
        width: (opt.x2 - opt.x1), height: (opt.y2 - opt.y1),
        fromCenter: false,
        mouseover: function (layer) {
            $canvas.css('cursor', 'grab');

        },
        drag: function (event) {
            

            opt.x1 = event.x;
            opt.y1 = event.y;
            opt.x2 = opt.x1 + (event.width);
            opt.y2 = opt.y1 + (event.height);
            render(event, "addSel");
            $canvas.css('cursor', 'grabbing');
        },
        dragstop: function (event) {
            

            render(event,"");
        },
        

    });
}

function addx1y1() {
    $canvas.drawRect({
        layer: true,
        draggable: true,
        name: 'addx1y1',
        groups: opt.layer,
//        dragGroups: opt.layer,
        strokeStyle: 'black',
        strokeWidth: 1,
        x: (opt.x1 - 3), y: (opt.y1 - 3),
        width: 6, height: 6,
        fromCenter: false,
        mouseover: function (layer) {
            $canvas.css('cursor', 'nwse-resize');

        },

        drag: function (event) {
            opt.x1 = event.x + 3;
            opt.y1 = event.y + 3;
            render(event, "addx1y1");

        },  dragstop: function (event) {
            

            render(event,"");
        },

    });
}
function addx2y1() {
    $canvas.drawRect({
        layer: true,
        draggable: true,
        name: 'addx2y1',
        groups: opt.layer,
//        dragGroups: opt.layer,
        strokeStyle: 'black',
        strokeWidth: 1,
        x: ((opt.x1 + ((opt.x2 - opt.x1) / 2)) - 3), y: (opt.y1 - 3),
        width: 6, height: 6,
        restrictDragToAxis: 'y',
        fromCenter: false,
        mouseover: function (layer) {
            $canvas.css('cursor', 'ns-resize');

        },

        drag: function (event) {
            
            opt.y1 = event.y + 3;
            render(event, "addx2y1");

        },  dragstop: function (event) {
            

            render(event,"");
        },

    });
}
function addx3y1() {
    $canvas.drawRect({
        layer: true,
        draggable: true,
        name: 'addx3y1',
        groups: opt.layer,
//        dragGroups: opt.layer,
        strokeStyle: 'black',
        strokeWidth: 1,
        x: opt.x2 - 3, y: (opt.y1 - 3),
        width: 6, height: 6,

        fromCenter: false,
        mouseover: function (layer) {
            $canvas.css('cursor', 'nesw-resize');

        },

        drag: function (event) {
            opt.x2 = event.x + 3;
            opt.y1 = event.y + 3;

            render(event, "addx3y1");

        },  dragstop: function (event) {
            

            render(event,"");
        },

    });
}
function addx1y2() {
    $canvas.drawRect({
        layer: true,
        draggable: true,
        name: 'addx1y2',
        groups: opt.layer,
//        dragGroups: opt.layer,
        strokeStyle: 'black',
        strokeWidth: 1,
        x: opt.x1 - 3, y: ((opt.y1 + ((opt.y2 - opt.y1) / 2)) - 3),
        width: 6, height: 6,
        restrictDragToAxis: 'x',
        fromCenter: false,
        mouseover: function (layer) {
            $canvas.css('cursor', 'ew-resize');

        },

        drag: function (event) {
            opt.x1 = event.x + 3;


            render(event, "addx1y2");

        },  dragstop: function (event) {
            

            render(event,"");
        },

    });
}

function addx3y2() {
    $canvas.drawRect({
        layer: true,
        draggable: true,
        name: 'addx3y2',
        groups: opt.layer,
//        dragGroups: opt.layer,
        strokeStyle: 'black',
        strokeWidth: 1,
        x: (opt.x2 - 3), y: ((opt.y1 + ((opt.y2 - opt.y1) / 2)) - 3),
        width: 6, height: 6,
        restrictDragToAxis: 'x',
        fromCenter: false,
        mouseover: function (layer) {
            $canvas.css('cursor', 'ew-resize');

        },  dragstop: function (event) {
            

            render(event,"");
        },

        drag: function (event) {
            opt.x2 = event.x + 3;


            render(event, "addx3y2");

        },

    });
}
function addx1y3() {
    $canvas.drawRect({
        layer: true,
        draggable: true,
        name: 'addx1y3',
        groups: opt.layer,
//        dragGroups: opt.layer,
        strokeStyle: 'black',
        strokeWidth: 1,
        x: (opt.x1 - 3), y: (opt.y2 - 3),
        width: 6, height: 6,

        fromCenter: false,
        mouseover: function (layer) {
            $canvas.css('cursor', 'nesw-resize');

        },

        drag: function (event) {
            opt.x1 = event.x + 6;
            opt.y2 = event.y + 6;


            render(event, "addx1y3");

        },  dragstop: function (event) {
            

            render(event,"");
        },

    });
}
function addx2y3() {
    $canvas.drawRect({
        layer: true,
        draggable: true,
        name: 'addx2y3',
        groups: opt.layer,
//        dragGroups: opt.layer,
        strokeStyle: 'black',
        strokeWidth: 1,
        x: ((opt.x1 + ((opt.x2 - opt.x1) / 2)) - 3), y: (opt.y2 - 3),
        width: 6, height: 6,
        restrictDragToAxis: 'y',
        fromCenter: false,
        mouseover: function (layer) {
            $canvas.css('cursor', 'ns-resize');

        },

        drag: function (event) {

            opt.y2 = event.y + 3;


            render(event, "addx2y3");

        },  dragstop: function (event) {
            

            render(event,"");
        },

    });
}
function addx3y3() {
    $canvas.drawRect({
        layer: true,
        draggable: true,
        name: 'addx3y3',
        groups: opt.layer,
//        dragGroups: opt.layer,
        strokeStyle: 'black',
        strokeWidth: 1,
        x: (opt.x2 - 3), y: (opt.y2 - 3),
        width: 6, height: 6,

        fromCenter: false,
        mouseover: function (layer) {
            $canvas.css('cursor', 'nwse-resize');

        },

        drag: function (event) {
            opt.x2 = event.x + 3;
            opt.y2 = event.y + 3;


            render(event, "addx3y3");

        },  dragstop: function (event) {
            

            render(event,"");
        },

    });
}
function btnAdd() {
    $canvas.drawRect({
        layer: true,
        groups: opt.layer,
//        draggable: true,
        name: 'btnAdd',
//        dragGroups: opt.layer,
        strokeStyle: 'black',
        fillStyle: '#fff',
        strokeWidth: 1,
        x: (opt.x1+20), y: (opt.y1 - 25),
        width: 60, height: 20,

        fromCenter: false,
        mouseover: function (layer) {
            $canvas.css('cursor', 'pointer');

        }, click: function (evt) {
            okfunction(opt,layersini);
            $canvas.removeLayerGroup(opt.layer[0]);
            init = false;
        },

    });
    $canvas.drawText({
        layer: true,
        name: 'myText',
        groups: opt.layer,
        fillStyle: '#000',
        strokeWidth: 3,
        x: (opt.x1 + 23), y: (opt.y1 - 23),
        fontSize: '10pt',
        text: 'Agregar',
        fromCenter: false,

    });

}
function btnCancel() {
    $canvas.drawRect({
        layer: true,
        groups: opt.layer,
//        draggable: true,
        name: 'btnCancel',
//        dragGroups: opt.layer,
        strokeStyle: 'black',
        fillStyle: '#fff',
        strokeWidth: 1,
        x: (opt.x1 + 80), y: (opt.y1 - 25),
        width: 60, height: 20,

        fromCenter: false,
        mouseover: function (layer) {
            $canvas.css('cursor', 'pointer');

        }, click: function (evt) {
            $canvas.removeLayerGroup(opt.layer[0]);
            init = false;
        },

    });
    $canvas.drawText({
        layer: true,
        name: 'myTextCancel',
        groups: opt.layer,
        fillStyle: '#000',
        strokeWidth: 2,
        x: (opt.x1 +83), y: (opt.y1 - 23),
        fontSize: '10pt',
        text: 'Cancelar',
        fromCenter: false,

    });
    

}