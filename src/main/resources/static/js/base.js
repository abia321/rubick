var arr=new Array();
var count=0;

function changeField(){
    var index= $('#index option:selected').val();
    $.ajax({
        url:"/base/field",
        type:"GET",
        dataType:"json",
        data:{
            "index":index
        },
        success:function (data) {
            $("#field1").empty();
            clearFilter();
            $('#value1').val('');
            arr=[];
            count=0;
            if(data.length==0){
                return ;
            }
            for(var i=0;i<data.length;i++){
                $("#field1").append("<option value="+data[i]+">"+data[i]+"</option>");
            }
            arr=data;
            count+=1;
        }
    })
}

function addQueryFilter() {
    if(arr.length==0){
        alert("Please select the index first!");
        return ;
    }
    count+=1;
    $("ol").append("<li id='li"+count+"'><select class=\"form-control\" id=\"logic"+count+"\">\n" +
        "                        <option value=\"must\">must</option>\n" +
        "                        <option value=\"must_not\">must_not</option>\n" +
        "                        <option value=\"should\">should</option>\n" +
        "                    </select>\n" +
        "                    <select class=\"form-control\" id=\"field"+count+"\"></select>\n" +
        "                    <select class=\"form-control\" id=\"op"+count+"\">\n" +
        "                        <option value=\"term\">term</option>\n" +
        "                        <option value=\"match\">match</option>\n" +
        "                        <option value=\"range\">range</option>\n" +
        "                        <option value=\"prefix\">prefix</option>\n" +
        "                        <option value=\"wildcard\">wildcard(not recommand)</option>\n" +
        "                    </select>\n" +
        "                    <input id=\"value"+count+"\" type=\"text\" placeholder=\"please input like this with range query:100<x<=1000 or x<=1000\">\n" +
        "                    <input id=\"add"+count+"\" type=\"button\" value=\"+\" onclick=\"addQueryFilter()\">" +
        "                    <input id=\"remove"+count+"\" type=\"button\" value=\"-\" onclick=\"removeQueryFilter("+count+")\"></li>");
    for(var i=0;i<arr.length;i++){
        $("#field"+count).append("<option value="+arr[i]+">"+arr[i]+"</option>");
    }
}

function removeQueryFilter(id) {
    // count-=1;
    if(id==1){
        alert("Could not remove the first filter");
        return ;
    }
    $("#li"+id+"").remove();
}

function search() {
    if(count<=0){
        clearFilter();
        alert("The param illegal,please try again.");
        return ;
    }
    var json="[";
    for(var j=1;j<=count;j++){
        if($("#li"+j+"").length==0){
            continue ;
        }
        if($('#value'+j).val().length==0){
            continue ;
        }
        json=json+"{\"logic\":\""+$('#logic'+j+' option:selected').val()+"\",\"field\":\""+$('#field'+j+' option:selected').val()+"\",\"op\":\""+$('#op'+j+' option:selected').val()+"\",\"value\":\""+$('#value'+j).val()+"\"}";
        if(j!=count){
            json=json+",";
        }
    }
    json=json+"]";
    var index= $('#index option:selected').val();
    var columns = [];
    var result;
    $.ajax({
        url :"/base/column",
        type : 'post',
        data:{
            "index":index,
            "queryFilter":json
        },
        dataType : "json",
        async : false,
        success : function(data) {
            if (data.column.length==0) {
                return ;
            } else {
                $.each(data.column, function(i, item) {
                    columns.push({ "field": item, "title": item, "sortable": true });
                });
                result=data.data;
            }
        }
    });
    $('#table').bootstrapTable('destroy').bootstrapTable({
        data: result,
        columns: columns
    });
}

function clearFilter() {
    if(count>0){
        for(var i=2;i<=count;i++){
            $("#li"+i+"").remove();
        }
    }
}
