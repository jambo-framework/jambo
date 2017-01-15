<%@ page language="java" contentType="text/html;charset=utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>desktop</title>
    <meta name="description" content="">

    <%@ include file="inc/headinc.jsp" %>

    <link rel="stylesheet" href="<%=contextPath %>/css/ztree.css">

    <script src="<%=contextPath %>/js/jquery.ztree.all.js"></script>
    <script src="<%=contextPath %>/js/jambo/desktop.js"></script>
</head>
<body>
<div id="title">
    <div class="container">
        <a href="javascript:;" id="menu-trigger" class="dropdown-toggle" data-toggle="dropdown" data-target="#">
            <i class="icon-cog"></i>
        </a>

        <div id="top-nav">
            <ul>
                <li class="dropdown">
                    <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown">
                        其它系统
                        <b class="caret"></b>
                    </a>

                    <ul class="dropdown-menu pull-right">
                        <li><a href="javascript:;">产品中心</a></li>
                        <li><a href="javascript:;">实时销售中心</a></li>
                        <li><a href="javascript:;">HSC</a></li>
                        <li><a href="javascript:;">能力开放平台</a></li>
                    </ul>
                </li>
            </ul>

            <ul class="pull-right">
                <li><a href="javascript:;">新消息<span class="label label-important">1024</span></a></li>
                <li><a href="#"><img src="<%=contextPath %>/img/control-power.png" >退出</a></li>
            </ul>
        </div> <!-- /#top-nav -->
    </div> <!-- /.container -->
</div> <!-- /#title -->

<div class="topbar">
    <div class="container-fluid">
        <div class='company' style=" margin-top: 0px; margin-bottom: 0px;">
            <img style="height:40px" src="<%=contextPath %>/img/jambo.gif"/>Jambo Framework</div>
        <form action="#">
            <input type="text" value="Search here...">
        </form>
        <ul class='mini'>
            <li class='dropdown dropdown-noclose supportContainer'>
                <a href="#" class='dropdown-toggle' data-toggle="dropdown">
                    业务受理
                </a>
                <ul class="dropdown-menu pull-right custom custom-dark">
                    <li class='custom'>
                        <div class="title">
                            What is the question?
                            <span>Jul 22, 2012 by <a href="#" class='pover' data-title="Hover me" data-content="User information comes here">Hover me</a></span>
                        </div>
                        <div class="action">
                            <div class="btn-group">
                                <a href="#" class='tip btn btn-mini' title="Show ticket"><i class="icon-search icon-white"></i></a>
                                <a href="#" class='tip btn btn-mini' title="Delete ticket"><i class="icon-remove icon-white"></i></a>
                            </div>
                        </div>
                    </li>
                    <li class='custom'>
                        <div class="title">
                            How can i do this and that?
                            <span>Jul 19, 2012 by <a href="#" class='pover' data-title="Username" data-content="User information comes here">Username</a></span>
                        </div>
                        <div class="action">
                            <div class="btn-group">
                                <a href="#" class='tip btn btn-mini' title="Show ticket"><i class="icon-search icon-white"></i></a>
                                <a href="#" class='tip btn btn-mini' title="Delete ticket"><i class="icon-remove icon-white"></i></a>
                            </div>
                        </div>
                    </li>
                    <li class='custom'>
                        <div class="title">
                            I want more support tickets!
                            <span>Jul 19, 2012 by <a href="#" class='pover' data-title="Lorem" data-content="User information comes here">Lorem</a></span>
                        </div>
                        <div class="action">
                            <div class="btn-group">
                                <a href="#" class='tip btn btn-mini' title="Show ticket"><i class="icon-search icon-white"></i></a>
                                <a href="#" class='tip btn btn-mini' title="Delete ticket"><i class="icon-remove icon-white"></i></a>
                            </div>
                        </div>
                    </li>
                    <li class='custom custom-hidden'>
                        <div class="title">
                            This is a great feature, BUT...
                            <span>Jul 18, 2012 by <a href="#" class='pover' data-title="Lorem" data-content="User information comes here">Ipsum</a></span>
                        </div>
                        <div class="action">
                            <div class="btn-group">
                                <a href="#" class='tip btn btn-mini' title="Show ticket"><i class="icon-search icon-white"></i></a>
                                <a href="#" class='tip btn btn-mini' title="Delete ticket"><i class="icon-remove icon-white"></i></a>
                            </div>
                        </div>
                    </li>
                    <li class='custom custom-hidden'>
                        <div class="title">
                            I want more colors! How?
                            <span>Jul 16, 2012 by <a href="#" class='pover' data-title="Lorem" data-content="User information comes here">Lorem</a></span>
                        </div>
                        <div class="action">
                            <div class="btn-group">
                                <a href="#" class='tip btn btn-mini' title="Show ticket"><i class="icon-search icon-white"></i></a>
                                <a href="#" class='tip btn btn-mini' title="Delete ticket"><i class="icon-remove icon-white"></i></a>
                            </div>
                        </div>
                    </li>
                    <li class="custom">
                        <div class="expand_custom">
                            <a href="#">Show all support tickets</a>
                        </div>
                    </li>
                </ul>
            </li>
            <li class='dropdown pendingContainer'>
                <a href="#" data-toggle="dropdown" class='dropdown-toggle'>
                    产品订购
                </a>
                <ul class="dropdown-menu pull-right custom custom-dark">
                    <li class='custom'>
                        <div class="title">
                            Pending order #1
                            <span>Jul 22, 2012 by <a href="#" class='pover' data-title="Hover me" data-content="User information comes here">Hover me</a></span>
                        </div>
                        <div class="action">
                            <div class="btn-group">
                                <a href="#" class='tip btn btn-mini' title="Show order"><i class="icon-search icon-white"></i></a>
                                <a href="#" class='tip btn btn-mini' title="Delete order"><i class="icon-remove icon-white"></i></a>
                            </div>
                        </div>
                    </li>
                </ul>
            </li>
            <li class='dropdown messageContainer'>
                <a href="#" class='dropdown-toggle' data-toggle='dropdown'>
                    客户资料
                </a>
                <ul class="dropdown-menu pull-right custom custom-dark">
                    <li class='custom'>
                        <div class="title">
                            Hello, whats your name?
                            <span>Jul 22, 2012 by <a href="#" class='pover' data-title="Hover me" data-content="User information comes here">Hover me</a></span>
                        </div>
                        <div class="action">
                            <div class="btn-group">
                                <a href="#" class='tip btn btn-mini' title="Show message"><i class="icon-search icon-white"></i></a>
                                <a href="#" class='tip btn btn-mini' title="Reply message"><i class="icon-comment icon-white"></i></a>
                            </div>
                        </div>
                    </li>
                </ul>
            </li>
            <li>
                <a href="#">
                    业务查询
                </a>
            </li>
            <li class='dropdown pendingContainer'>
                <a href="#" class='dropdown-toggle' data-toggle='modal' data-target="#myModal">
                    菜单树
                </a>
                <!-- 模态框（Modal） -->
                <div class="modal fade" id="myModal" tabindex="-1" role="dialog"
                     aria-labelledby="myModalLabel" aria-hidden="true" hidden>
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close"
                                        data-dismiss="modal" aria-hidden="true">
                                    &times;
                                </button>
                                <h4 class="modal-title" id="myModalLabel">
                                    菜单树
                                </h4>
                            </div>
                            <div id="menutree" class="modal-body ztree">
                            </div>
                        </div><!-- /.modal-content -->
                    </div><!-- /.modal -->
                </div>
                <!-- 模态框（Modal） -->
            </li>
            <li class='dropdown pendingContainer'>
                <a href="#" class='dropdown-toggle' data-toggle='dropdown'>
                    工作台设置
                </a>
                <ul class="dropdown-menu pull-right custom custom-dark" style="min-width: 400px">
                    <li class='custom'>
                        <div class="title">
                            <a href="#" class='pover' data-title="工作台个性化" data-content="工作台个性化">工作台个性化设置</a>
                        </div>
                    </li>
                    <li class='custom'>
                        <div class="title">
                            <span>主题切换</span>
                        </div>
                        <div class="action">
                            <div class="btn-group">
                                <a href="#" class='tip btn btn-mini' title="黑">黑</a>
                                <a href="#" class='tip btn btn-mini' title="蓝">蓝</a>
                            </div>
                        </div>
                    </li>
                </ul>
            </li>
        </ul>
    </div>
</div>
<div class="tabs">
    <div class="box">
        <div class="container-fluid">
            <div class="box-head tabs-main tabs">
                <ul id="tabs" class="nav nav-tabs nav-tabs-main" style="border-bottom-width:0px">
                    <%--同时显示的标签太多会导致折行而无法显示，所以限制同时只能打开5个标签--%>
                    <li class='active'>
                        <a href="#0" data-toggle="tab"><i class="icon-home icon-white"></i>工作台</a>
                    </li>
                    <li class="dropdown">
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                            <i class="icon-caret-down bigger-110 width-auto"></i>
                        </a>

                        <ul class="dropdown-menu dropdown-info">
                            <li>
                                <%--<a data-toggle="tab" href="#1">test1</a>--%>
                            </li>
                            <li>
                                <%--<a data-toggle="tab" href="#2">test2</a>--%>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>

<div class="main" style="margin-top: 4px;">
    <div class="container-fluid">
        <div  id="tabWin" class="tab-content">
            <div class="tab-pane active" id="0">
                <iframe src="<%=contextPath %>/redirect.do?url=frame/desktop" width="100%" frameborder="0" onload="Javascript:setWinHeight(this)"></iframe>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    tabMan = new TabManager("tabs", $("#tabWin"));

    //工作台初始化
    jQuery(function($){
        //显示演示用的菜单树，项目中应该改为读取真正的菜单树
        var t = $("#menutree");
        t = $.fn.zTree.init(t, setting, zDemoNodes);
        var zTree = $.fn.zTree.getZTreeObj("menutree");
        zTree.selectNode(zTree.getNodeByParam("id", 1));
    });
</script>
</body>
</html>