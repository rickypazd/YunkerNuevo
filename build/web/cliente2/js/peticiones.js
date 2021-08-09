/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var urlAdmin = "../admin/jsonController";

var Peticion = {
    selectAll: function (tableName, callBack) {
        var json = {
            nombre_tabla: tableName,
        };
        $.post(urlAdmin, {evento: "selectAll", json: JSON.stringify(json)}, function (resp) {
            if (resp == "error") {
                alert(resp);
                return;
            }
            callBack(resp);
        });
    },
    getByWheres: function (tableName, wheres, callBack) {
        var json = {
            nombre_tabla: tableName,
            wheres: wheres,
        };
        $.post(urlAdmin, {evento: "getByWheres", json: JSON.stringify(json)}, function (resp) {
            if (resp == "error") {
                alert(resp);
                return;
            }
            callBack(resp);
        });
    },
    getBySelectWheres: function (tableName, select, wheres, callBack) {
        var json = {
            nombre_tabla: tableName,
            select: select,
            wheres: wheres,
        };
        $.post(urlAdmin, {evento: "getBySelectWheres", json: JSON.stringify(json)}, function (resp) {
            if (resp == "error") {
//                alert(resp);
                callBack("[]");
                return;
            }

            callBack(resp);
        });
    },
    actualizar: function (tableName, data, callBack) {
        if (!Array.isArray(data)) {
            data = [data];
        }
        var json = {
            nombre_tabla: tableName,
            data: data,
        };
        $.post(urlAdmin, {evento: "actualizar", json: JSON.stringify(json)}, function (resp) {
            if (resp == "error") {
                alert(resp);
                return;
            }
            callBack(resp);
        });
    },
    insertar: function (tableName, data, callBack) {
        if (!Array.isArray(data)) {
            data = [data];
        }
        var json = {
            nombre_tabla: tableName,
            data: data,
        };
        $.post(urlAdmin, {evento: "insertar", json: JSON.stringify(json)}, function (resp) {
            if (resp == "error") {
                alert(resp);
                return;
            }
            callBack(resp);
        });
    },
    eliminar: function (tableName, data, callBack) {
        if (!Array.isArray(data)) {
            data = [data];
        }
        for (var i = 0; i < data.length; i++) {

            data[i].estado = 0;
        }
        var json = {
            nombre_tabla: tableName,
            data: data,
        };
        $.post(urlAdmin, {evento: "actualizar", json: JSON.stringify(json)}, function (resp) {
            if (resp == "error") {
                alert(resp);
                return;
            }
            callBack(resp);
        });
    },
};