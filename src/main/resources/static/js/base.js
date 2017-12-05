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
    var arr=new Array();
    for(var j=1;j<=count;j++){
        if($("#li"+j+"").length==0){
            continue ;
        }
        if($('#value'+j).val().length==0){
            continue ;
        }
        var json= {
            "logic":$('#logic'+j+' option:selected').val(),
            "field":$('#field'+j+' option:selected').val(),
            "op":$('#op'+j+' option:selected').val(),
            "value":$('#value'+j).val(),
        }
        arr.push(JSON.stringify(json));
    }
    if(arr.length==0){
        alert("Please input the value");
        return ;
    }
    // alert(arr);
    $.ajax({
        url :"search",
        type : 'get',
        dataType : "json",
        async : false,
        success : function(returnValue) {
            // 未查询到相应的列，展示默认列
            if (returnValue.retCode == "0") {
                //没查到列的时候把之前的列再给它
                myColumns = $table.bootstrapTable('getOptions').columns[0];
            } else {
                // 异步获取要动态生成的列
                var arr = returnValue.data;
                $.each(arr, function(i, item) {
                    myColumns.push({
                        "field" : item.labelColumnCode,
                        "title" : item.labelColumnName,
                        "hide" : true,
                        "align" : 'center',
                        "valign" : 'middle'
                    });
                });
            }
            console.log(myColumns);
            return myColumns;
        }
    });

}

function clearFilter() {
    if(count>0){
        for(var i=2;i<=count;i++){
            $("#li"+i+"").remove();
        }
    }
}

function showTable() {
    var $table = $('#table');
    $table.bootstrapTable({
        url: "search",
        method: 'get',//请求方法
        striped: true,//是否显示行间隔色
        pageSize: 10,//每页的记录行数（*）
        dataType: "json",
        toolbar: '#toolbar',
        pagination: true, //分页
        singleSelect: false,
        // data-locale:"zh-US" , //表格汉化
        search: true, //显示搜索框
        sidePagination: "server", //服务端处理分页
        ////查询参数,每次调用是会带上这个参数，可自定义
        queryParams: function(params) {
            // var subcompany = $('#subcompany option:selected').val();
            // var name = $('#name').val();
            return {
                pageNumber: params.offset+1,
                pageSize: params.limit
                // companyId:subcompany,
                // name:name
            };
        },
        columns: [
            {
                title: '活动名称',
                field: 'name',
                align: 'center',
                valign: 'middle',
                sortable: true
            },
            {
                title: '参与人数',
                field: 'participationCounts',
                align: 'center',
                sortable: true
            }
            // {
            //     title: '操作',
            //     field: 'id',
            //     align: 'center',
            //     formatter:function(value,row,index){
            //         var e = '<a href="#" mce_href="#" onclick="edit(\''+ row.id + '\')">编辑</a> ';
            //         var d = '<a href="#" mce_href="#" onclick="del(\''+ row.id +'\')">删除</a> ';
            //         return e+d;
            //     }
            // }
        ],
        onSort : function (name,order) {
            alert(name+order);
        }
    });
}