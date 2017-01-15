

<base href="http://localhost:8180/">

<meta charset="utf-8">
<meta name="viewport" content="width=device-width">

<meta name="author" content="www.jambo-framework.com" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>


<script type="text/javascript" language="javascript">
    var contextPath = "";
    var msgInputCorrectEmail = "Email格式不对";
    var msgInput = "输入不正确";
    var msgInputNotNull ="输入不能为空";
    var msgInvalidDate = "日期格式不对，格式应为yyyy-mm-dd";
    var msgInvalidDateYyyymmdd = "日期格式不对，格式应为yyyymmdd";
    var msgInvalidNumberFormat = "请输入数字";
    var msgInvalidTime = "日期时间格式不对，格式应为yyyy-mm-dd hh:mm:ss";
</script>

<!-- css and javascript -->
<link rel="stylesheet" href="/css/bootstrap.css">
<link rel="stylesheet" href="/css/bootstrap-responsive.css">
<link rel="stylesheet" href="/css/font-awesome.min.css" />
<link rel="stylesheet" href="/css/jfstyle.css">

<script src="/js/jquery.js"></script>

<script src="/js/bootstrap.js"></script>
<script src="/js/jquery.validate.min.js"></script>

<script src="/js/jambo/jframe.js"></script>
<script src="/js/jambo/messages.js"></script>


<script type="text/javascript" src="/js/jambo/list.js"></script>
<script type="text/javascript" language="javascript">
    var msgNoSelected="请选择记录"
    var msgConfirmDelete="确定要删除这些记录吗？"
</script>



<html>
<head>
    <title>selector</title>
</head>

<body style="padding: 1px 1px 1px 1px;">
<div class="content" style="padding: 1px 1px 1px 1px;">
    <div class="row-fluid">
        <form id="formList" name="formList" action="pickerAction/list.do" class="form-search" style="margin-bottom: 5px">
            <div class="row-fluid force-margin">

                <input id="_orderby" name="_orderby" type="hidden" value=""/>
                <input id="_desc" name="_desc" type="hidden" value=""/>
                <input id="_pageno" name="_pageno" type="hidden" value="2"/>
                <input id="_pagesize" name="_pagesize" type="hidden" value="15"/>
                <input type="hidden" name="_rowcount" value="0"/>

                <input type="hidden" name="definition" value="#COMPANY"/>
                <!-- textfield name="condition" key="condition"/ -->
                <input type="hidden" name="dbFlag" value=""/>

                <div class="box" style="margin-top: 10px">
                    <div class="box-head">
                        <h3>选择器</h3>

                        <div class="actions">
                            <ul>
                                <li>
                                    <a href="javascript:void(0);" onclick="doQuery();"
                                       name="btnQuery" class="btn btn-square">查询</a>
                                </li>
                            </ul>
                        </div>
                    </div>



                    <div class="box-qrytext">
                        <div class="row-fluid force-margin">
                            <div class="form-group">
                                <label for="code" class="control-label">编码:</label>
                                <input id="code" name="code" class="input-square" type="text" value=""/>

                                <label for="name" class="control-label">名称:</label>
                                <input id="name" name="name" class="input-square" type="text" value=""/>
                            </div>
                        </div>
                    </div>

                    <div class="box-content box-nomargin">
                        <table class='table table-striped table-bordered'>
                            <thead>
                            <tr>
                                <th>编码</th>
                                <th>名称</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr align="center">
                                <td><a href="javascript:selectCode('', '' ,'');">空值</a></td>
                                <td>&nbsp;</td>
                            </tr>

                            <tr>
                                <td><a href="javascript:selectCode('', '20','s');">20</a></td>
                                <td>s</td>
                            </tr>

                            <tr>
                                <td><a href="javascript:selectCode('', '21','r');">21</a></td>
                                <td>r</td>
                            </tr>

                            <tr>
                                <td><a href="javascript:selectCode('', '22','rr');">22</a></td>
                                <td>rr</td>
                            </tr>

                            <tr>
                                <td><a href="javascript:selectCode('', '23','d');">23</a></td>
                                <td>d</td>
                            </tr>

                            <tr>
                                <td><a href="javascript:selectCode('', '25','tt');">25</a></td>
                                <td>tt</td>
                            </tr>

                            <tr>
                                <td><a href="javascript:selectCode('', '26','qq');">26</a></td>
                                <td>qq</td>
                            </tr>

                            <tr>
                                <td><a href="javascript:selectCode('', '27','qq');">27</a></td>
                                <td>qq</td>
                            </tr>

                            <tr>
                                <td><a href="javascript:selectCode('', '28','v');">28</a></td>
                                <td>v</td>
                            </tr>

                            <tr>
                                <td><a href="javascript:selectCode('', '29','x');">29</a></td>
                                <td>x</td>
                            </tr>

                            <tr>
                                <td><a href="javascript:selectCode('', '30','b');">30</a></td>
                                <td>b</td>
                            </tr>

                            <tr>
                                <td><a href="javascript:selectCode('', '31','ffff');">31</a></td>
                                <td>ffff</td>
                            </tr>

                            <tr>
                                <td><a href="javascript:selectCode('', '33','dd');">33</a></td>
                                <td>dd</td>
                            </tr>

                            <tr>
                                <td><a href="javascript:selectCode('', '35','中文');">35</a></td>
                                <td>中文</td>
                            </tr>

                            <tr>
                                <td><a href="javascript:selectCode('', '36','aadsf');">36</a></td>
                                <td>aadsf</td>
                            </tr>

                            <tr>
                                <td><a href="javascript:selectCode('', '37','111');">37</a></td>
                                <td>111</td>
                            </tr>

                            </tbody>
                        </table>

                        <div class="box-foot">
                            <strong id="nav_total_page" hidden>
                                总计
                                <code>0</code>
                                页
                            </strong>
                            &nbsp;
                            <strong>当前第
                                <code id="nav_pageno" name="mav_pageno">2</code>
                                页
                            </strong>
                            <div class="actions">
                                <ul>
                                    <li id="nav_first_pan" >
                                        <a href="javascript:showFirstPage('', 'tableid');" class="btn btn-square tip" >
                                            首页</a>
                                    </li>
                                    <li id="nav_previsous_pan" >
                                        <a href="javascript:showPreviousPage('', 'tableid');" class="btn btn-square tip">
                                            上一页</a>
                                    </li>
                                    <li id="nav_next_pan" >
                                        <a href="javascript:showNextPage('', 'tableid');" class="btn btn-square tip">
                                            下一页</a>
                                    </li>
                                    <li id="nav_last_pan" hidden>
                                        <a href="javascript:showLastPage('', 'tableid');" class="btn btn-square tip">
                                            末页</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>
