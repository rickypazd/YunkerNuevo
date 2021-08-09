var urlIn = "indicadoresController";
am4core.ready(function () {

// Themes begin

    mostrar_progress();
    $.post(urlIn, {
        TokenAcceso: "servi12sis3",
        evento: "getIndicadorINEG"
    }, function (resp) {
        cerrar_progress();
        if (resp != null) {
            var obj = $.parseJSON(resp);
            if (obj.estado != 1) {
                alert(obj.mensaje);
            } else {
                //cargarChart(obj);
                var arr = $.parseJSON(obj.resp);
                  var data = [];
                var open = 100;
                var close = 250;
                $.each(arr,function(i,obj){
                    
                    data.push({date: new Date(obj.fecha), compra: obj.compra || 0, venta: obj.venta || 0});
                });
              
                cargarChart(data);
               
            }
        }
    });


}); // end am4core.ready()

function cargarChart(json) {
    am4core.useTheme(am4themes_animated);
// Themes end

    var chart = am4core.create("chartdiv", am4charts.XYChart);
    chart.hiddenState.properties.opacity = 0; // this creates initial fade-in


    chart.data = json;

    var dateAxis = chart.xAxes.push(new am4charts.DateAxis());

    var valueAxis = chart.yAxes.push(new am4charts.ValueAxis());
    valueAxis.tooltip.disabled = true;

    var series = chart.series.push(new am4charts.LineSeries());
    series.dataFields.dateX = "date";
    series.dataFields.openValueY = "compra";
    series.dataFields.valueY = "venta";
    series.tooltipText = "compra: {openValueY.value} venta: {valueY.value}";
    series.sequencedInterpolation = true;
    series.fillOpacity = 0.3;
    series.defaultState.transitionDuration = 1000;
    series.tensionX = 0.8;

    var series2 = chart.series.push(new am4charts.LineSeries());
    series2.dataFields.dateX = "date";
    series2.dataFields.valueY = "compra";
    series2.sequencedInterpolation = true;
    series2.defaultState.transitionDuration = 1500;
    series2.stroke = chart.colors.getIndex(6);
    series2.tensionX = 0.8;

    chart.cursor = new am4charts.XYCursor();
    chart.cursor.xAxis = dateAxis;
    chart.scrollbarX = new am4core.Scrollbar();
}