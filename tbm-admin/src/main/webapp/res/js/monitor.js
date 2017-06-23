$(function(){
    var showNum = 7;//展示数据个数
    var reqTime = 1000;
    var baseData = {
        labels : ["start"],
        datasets : [
            {
                fillColor : "rgba(52,166,129,0)",
                strokeColor : "rgba(52,166,129,1)",
                pointColor : "rgba(52,166,129,1)",
                pointStrokeColor : "#fff",
                data : [0]
            },
            {
                fillColor : "rgba(162,201,242,0)",
                strokeColor : "rgba(162,201,242,1)",
                pointColor : "rgba(162,201,242,1)",
                pointStrokeColor : "#fff",
                data : [0]
            },
            {
                fillColor : "rgba(42,56,77,0)",
                strokeColor : "rgba(42,56,77,1)",
                pointColor : "rgba(42,56,77,1)",
                pointStrokeColor : "#fff",
                data : [0]
            },
            {
                fillColor : "rgba(172,172,172,0)",
                strokeColor : "rgba(172,172,172,1)",
                pointColor : "rgba(172,172,172,1)",
                pointStrokeColor : "#fff",
                data : [0]
            }
        ]
    }

    var options = {barValueSpacing: 10};

    //堆
    var heapData = JSON.parse(JSON.stringify(baseData));
    var heapIndex = 1;
    var heapCtx = document.getElementById("heapChart").getContext("2d");
    var heapChart = new Chart(heapCtx).Line(heapData,options);
    //非堆
    var nonHeapData = JSON.parse(JSON.stringify(baseData));
    var nonHeapCtx = document.getElementById("nonHeapChart").getContext("2d");
    var nonHeapChart = new Chart(nonHeapCtx).Line(nonHeapData,options);

    setInterval(function() {     
        $.get('http://120.76.24.129:8080/monitor?info=memory-summary-info').done(function(data){
            var result = JSON.parse(data).data;
            if(heapIndex>= showNum){
                heapChart.removeData();
                nonHeapChart.removeData();
            }
            heapChart.addData([byteToMB(result.heapInfo.committed),byteToMB(result.heapInfo.init),byteToMB(result.heapInfo.max),byteToMB(result.heapInfo.used),], getTimeStr(result.timestamp));
            nonHeapChart.addData([byteToMB(result.nonHeapInfo.committed),byteToMB(result.nonHeapInfo.init),byteToMB(result.nonHeapInfo.max),byteToMB(result.nonHeapInfo.used),], getTimeStr(result.timestamp));
            heapIndex++;
        });
    }, reqTime);


    //类
    var classCtx = document.getElementById("classChart").getContext("2d");
    drawChart('http://120.76.24.129:8080/monitor?info=class-load-info',classCtx,'Pie',{},function(result){
        return [{value: result.loadedClassCount,color:"rgba(52,166,129,0.7)"},{value: result.unloadedClassCount,color:"rgba(162,201,242,1)"}];
    });
    setInterval(function() {     
        // $.get('http://120.76.24.129:8080/monitor?info=class-load-info').done(function(data){
        //     var result = JSON.parse(data).data;
        //     var classData = [{value: result.loadedClassCount,color:"rgba(52,166,129,0.7)"},{value: result.unloadedClassCount,color:"rgba(162,201,242,1)"}];
        //     var classChart = new Chart(classCtx).Pie(classData,{});
        // });
        drawChart('http://120.76.24.129:8080/monitor?info=class-load-info',classCtx,'Pie',{},function(result){
            return [{value: result.loadedClassCount,color:"rgba(52,166,129,0.7)"},{value: result.unloadedClassCount,color:"rgba(162,201,242,1)"}];
        });
    }, 5000);

    //jvm
    var jvmInf = $("#jvmInf");
    $.get('http://120.76.24.129:8080/monitor?info=jvm-info').done(function(data){
        var result = JSON.parse(data).data;
        var rowHtml = [];
        for(var attr in result){
            rowHtml.push('<tr><td>'+attr+'</td>');
            rowHtml.push('<td>'+result[attr]+'</td></tr>');
        }
        jvmInf.append($(rowHtml.join('')));
    });

});


function byteToMB(str){
    return parseInt(str)/1024/1024;
}
function getTimeStr(str){
    var date = new Date(parseInt(str));
    var hours = date.getHours() >= 10 ? date.getHours() : '0'+date.getHours();
    var minutes = date.getMinutes() >= 10 ? date.getMinutes() : '0'+date.getMinutes();
    var seconds = date.getSeconds() >= 10 ? date.getSeconds() : '0'+date.getSeconds();
    return hours + ':' + minutes + ':' +seconds;
}

function drawChart(url,ctx,chartFunName,options,fun){
    $.get(url).done(function(data){
        var result = JSON.parse(data).data;
        var data = fun(result);
        var chart = new Chart(ctx)[chartFunName](data,options);
    });
}













