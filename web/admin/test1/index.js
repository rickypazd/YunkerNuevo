/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function(){
    //alert("Hellow world")  //ESTE ES EL MAIN DE UN JQUERY
    
    cargarLista("Test1");

});

function cargarLista (txt){
    var elmt = "<h1>"+txt+"</h1>";
    $("#mylista").append(elmt);
}