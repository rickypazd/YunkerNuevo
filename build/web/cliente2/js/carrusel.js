/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var state = {
    frame: {
        fps: 50,
        vel: 2,
        initTime: 0,
        curTime: 0,
    },
    
    width:300,
    width2:500,
    
};

var elemnts;
var elemnts2;
function initCarrusel() {
    document.getElementById("carruselList").addEventListener("mouseover", function(){
        state.frame.vel = 1;
    });
    document.getElementById("carruselList").addEventListener("mouseout", function(){
        state.frame.vel = 2;
    });
 elemnts = document.getElementsByClassName("carruselIten");    
 elemnts2 = document.getElementsByClassName("carruselIten2");    
    render(state);
    
}


function render(state) {

    for (var i = 0; i < elemnts.length; i++) {    
        var left =parseFloat(elemnts[i].style.left || (i*-state.width+window.innerWidth),10)+state.frame.vel;
      elemnts[i].style.left =left+"px";
      elemnts[i].style.width =state.width+"px";
            if(elemnts[i].offsetLeft > window.innerWidth){
                if(i==0){
                    elemnts[i].style.left = (parseFloat(elemnts[elemnts.length-1].style.left,10)-state.width)+"px";
                }else{
                      elemnts[i].style.left = (parseFloat(elemnts[i-1].style.left,10)-state.width)+"px";
                }
                
            }
    }
    for (var i = 0; i < elemnts2.length; i++) {    
        var left =parseFloat(elemnts2[i].style.left || (i*-state.width2+window.innerWidth),10)+state.frame.vel;
      elemnts2[i].style.left =left+"px";
      elemnts2[i].style.width =state.width2+"px";
            if(elemnts2[i].offsetLeft > window.innerWidth){
                if(i==0){
                    elemnts2[i].style.left = (parseFloat(elemnts2[elemnts2.length-1].style.left,10)-state.width2)+"px";
                }else{
                      elemnts2[i].style.left = (parseFloat(elemnts2[i-1].style.left,10)-state.width2)+"px";
                }
                
            }
    }


    sleep((1000 / state.frame.fps)).then(() => {
            render(state);
    });
}

function sleep(time) {
    return new Promise((resolve) => setTimeout(resolve, time));
}
