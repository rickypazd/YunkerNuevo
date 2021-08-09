/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var inpUrl = document.getElementById("inpUrl");
var Respuesta = document.getElementById("respuesta");

function pedir(){
    var respVal = inpUrl.value;
    
var jqxhr = $.get( respVal, function(resp) {
  alert( "success" );
  alert(resp);
})
  .done(function() {
    alert( "second success" );
  })
  .fail(function(e) {
    alert( "error: "+e );
  })
  .always(function() {
    alert( "finished" );
  });
    
}