<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3">
<head>
    <title>Rubick</title>
    <link rel="stylesheet" href="/css/bootstrap.min.css">
    <link rel="stylesheet" href="/css/bootstrap-table.css">
</head>
<body>
    <div style="text-align: left;margin:0 auto;width: 1000px; ">
        <div class="form-group">
            <label>please choose the index :</label>
            <select class="form-control" id="index" onchange="changeField()">
                <option value="0">请选择</option>
                <#list indexList as item>
                    <option value="${item.index}">${item.index}</option>
                </#list>
            </select>
        </div>
        <div class="form-group">
            <ol>
                <li id="li1">
                    <select class="form-control" id="logic1">
                        <option value="must">must</option>
                        <option value="must_not">must_not</option>
                        <option value="should">should</option>
                    </select>
                    <select class="form-control" id="field1"></select>
                    <select class="form-control" id="op1">
                        <option value="term">term</option>
                        <option value="match">match</option>
                        <option value="range">range</option>
                        <option value="prefix">prefix</option>
                        <option value="wildcard">wildcard(not recommand)</option>
                    </select>
                    <input id="value1" type="text" placeholder="please input like this with range query:gt:1000&lte 2000">
                    <input id="add1" type="button" value="+" onclick="addQueryFilter()">
                </li>
            </ol>
        </div>
        <div class="form-group">
            <input id="Go" type="button" value="Go" onclick="search()">
            <input id="refresh" type="button" value="refresh" onclick="refresh()">
        </div>
        <table id="table" data-toggle="table"
               data-toolbar="#toolbar"
               data-striped="true"
               data-pagination="true"
               data-pagination-loop="true"
               data-single-select="true"
               data-side-pagination="client"
               data-search="true"
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