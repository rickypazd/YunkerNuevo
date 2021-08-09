/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var url = "admin/adminController";
$(document).ready(function(){
    //aki
    //
    //
    //
    //post({
    //cojntenido del post;
    //alert();
    //});
    //calc_dist();
});

function calc_dist(){
    var id = $("#id_car").val();
        $.post(url,{evento:"get_dist_carrera",id:id},function(resp){
                 alert(resp);
                 $("#img").attr("src","img/loc/img.jpg");
         });
     }