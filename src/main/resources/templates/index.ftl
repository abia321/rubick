<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
<head>
    <title>Rubick</title>
    <link rel="stylesheet" href="/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/bootstrap-table.css">
</head>
<body>
    <div><h1>Rubick</h1></div>
    <div style="text-align: left;margin:0 auto;width: 80%; ">
        <div class="form-group">
            <label>please choose the index :</label>
            <select class="form-control" id="index" onchange="changeField()">
                <option value="0">default</option>
                <#list indexList as item>
                    <option value="${item.index}">${item.index}</option>
                </#list>
            </select>
        </div>
        <div>Only Return 1000 Messages</div>
        Please Input Range Query Value Like This:<label style="color:red"><strong>gt:1000 & lte:2000</strong></label>
        <div class="form-inline">
            <ol>
                <li id="li1">
                    <select id="logic1" class="form-control">
                        <option value="must">must</option>
                        <option value="must_not">must_not</option>
                        <option value="should">should</option>
                    </select>
                    <select id="field1" class="form-control"></select>
                    <select id="op1" class="form-control">
                        <option value="term">term</option>
                        <option value="match">match</option>
                        <option value="range">range</option>
                        <option value="prefix">prefix</option>
                        <option value="wildcard">wildcard(not recommand)</option>
                    </select>
                    <input id="value1" type="text" class="form-control">
                    <input id="add1" type="button" class="btn" value="+" onclick="addQueryFilter()">
                </li>
            </ol>
        </div>
        <div class="form-group">
            <input id="Go" type="button" value="Go" onclick="search()">
        </div>
        <table id="table" data-toggle="table"
               data-toolbar="#toolbar"
               data-striped="true"
               data-pagination="true"
               data-pagination-loop="true"
               data-single-select="true"
               data-side-pagination="client"
               data-page-size="10">
            <thead>
            <tr>
                <#--<th data-field="column0" data-sortable="true">ID</th>-->
                <#--<th data-field="column1" data-sortable="true">Item Name</th>-->
            </tr>
            </thead>
        </table>
    </div>
</body>
<script type="text/javascript" src="/js/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/js/bootstrap-table.js"></script>
<script type="text/javascript" src="/js/base.js"></script>
</html>