<#include "/inc/headinc.ftl"/>
<html>
<head>
    <title>ng3 content</title>
    <script src="${contextPath}/js/jquery.peity.js"></script>
</head>
<body>
<div class="main">
    <div class="container-fluid">
        <div class="content">
            <div class="row-fluid no-margin">
                <div class="span6">
                    <div class="box">
                        <div class="box-head">
                            <a href="${contextPath}/redirect.do?url=example/examlist">点击查看范例页面，或者点击右上方按钮打开菜单树，可以查看范例页面</a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row-fluid no-margin">
                <div class="span12">
                    <ul class="quickstats">
                        <li>
                            <div class="small-chart" data-color="6a9d29" data-stroke="345010" data-type="line">5,3,9,6,5,9,7,3,5,10</div>
                            <div class="chart-detail">
                                <span class="amount green">+ 9834.41 $</span>
                                <span class="description">Current balance</span>
                            </div>
                        </li>
                        <li>
                            <div class="small-chart" data-color="2c5b96" data-stroke="102c50" data-type="bar">2,5,4,6,5,4,7,8</div>
                            <div class="chart-detail">
                                <span class="amount">128.32</span>
                                <span class="description">Orders per month</span>
                            </div>
                        </li>
                        <li>
                            <div class="small-chart" data-color="962c2c" data-stroke="651111" data-type="pie">6/10</div>
                            <div class="chart-detail">
                                <span class="amount">60%</span>
                                <span class="description">Member qoute</span>
                            </div>
                        </li>
                        <li>
                            <div class="small-chart" data-color="2c5b96" data-stroke="102c50" data-type="line">521,500,481,429,550,521</div>
                            <div class="chart-detail">
                                <span class="amount">521.3 / month</span>
                                <span class="description">Unique visitors rate</span>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="row-fluid">
                <div class="span6">
                    <div class="box">
                        <div class="box-head">
                            <h3>Recent purchases</h3>
                        </div>
                        <div class="box-content box-nomargin"><div class="alert alert-error">
                            <strong>Feature!</strong> Check this awesome custom animation. Click in the table on <strong>'mark as pending'</strong> (2nd action button) !
                        </div>
                            <table class="table table-striped table-bordered">
                                <thead>
                                <tr>
                                    <th>Customer</th>
                                    <th>Product</th>
                                    <th class='mobile-hide'>Date</th>
                                    <th>Income</th>
                                    <th>Actions</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>
                                        <a href="#" data-title="Lorem ipsum" data-content="Here is some lorem ipsum content">Lorem ipsum</a>
                                    </td>
                                    <td>
                                        The awesome shirt
                                    </td>
                                    <td class='mobile-hide'>
                                        Jul 21, 2012
                                    </td>
                                    <td class='green'>
                                        + 21,4 $
                                    </td>
                                    <td class='actions'>
                                        <div class="btn-group">
                                            <a href="#" class='btn btn-mini tip' title="Show details">
                                                <i class="glyphicon glyphicon-search glyphicon-white"></i>
                                            </a>
                                            <a href="#" class='btn btn-mini tip animateRow' data-target=".pendingContainer" data-user='1' data-date='3' data-title='2' title="Mark as pending">
                                                <i class="glyphicon glyphicon-file glyphicon-white"></i>
                                            </a>
                                            <a href="#" class='btn btn-mini tip deleteRow' title="Remove">
                                                <i class="glyphicon glyphicon-remove glyphicon-white"></i>
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <a href="#" data-title="Lorem ipsum" data-content="Content of Takimata ... bla">Takimata</a>
                                    </td>
                                    <td>
                                        Water
                                    </td>
                                    <td class='mobile-hide'>
                                        Jul 20, 2012
                                    </td>
                                    <td class='green'>
                                        + 1,75 $
                                    </td>
                                    <td class='actions'>
                                        <div class="btn-group">
                                            <a href="#" class='btn btn-mini tip' title="Show details">
                                                <i class="glyphicon glyphicon-search glyphicon-white"></i>
                                            </a>
                                            <a href="#" class='btn btn-mini tip animateRow' data-target=".pendingContainer" data-user='1' data-date='3' data-title='2' title="Mark as pending">
                                                <i class="glyphicon glyphicon-file glyphicon-white"></i>
                                            </a>
                                            <a href="#" class='btn btn-mini tip deleteRow' title="Remove">
                                                <i class="glyphicon glyphicon-remove glyphicon-white"></i>
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <a href="#" data-title="Lorem ipsum" data-content="Content of Accusam">Accusam</a>
                                    </td>
                                    <td>
                                        Headset
                                    </td>
                                    <td class='mobile-hide'>
                                        Jul 21, 2012
                                    </td>
                                    <td class='green'>
                                        + 61,91 $
                                    </td>
                                    <td class='actions'>
                                        <div class="btn-group">
                                            <a href="#" class='btn btn-mini tip' title="Show details">
                                                <i class="glyphicon glyphicon-search glyphicon-white"></i>
                                            </a>
                                            <a href="#" class='btn btn-mini tip animateRow' data-target=".pendingContainer" data-user='1' data-date='3' data-title='2' title="Mark as pending">
                                                <i class="glyphicon glyphicon-file glyphicon-white"></i>
                                            </a>
                                            <a href="#" class='btn btn-mini tip deleteRow' title="Remove">
                                                <i class="glyphicon glyphicon-remove glyphicon-white"></i>
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <a href="#" data-title="Lorem ipsum" data-content="Content of Consetetur">Consetetur</a>
                                    </td>
                                    <td>
                                        LCD TV
                                    </td>
                                    <td class='mobile-hide'>
                                        Jul 20, 2012
                                    </td>
                                    <td class='green'>
                                        + 739,99 $
                                    </td>
                                    <td class='actions'>
                                        <div class="btn-group">
                                            <a href="#" class='btn btn-mini tip' title="Show details">
                                                <i class="glyphicon glyphicon-search glyphicon-white"></i>
                                            </a>
                                            <a href="#" class='btn btn-mini tip animateRow' data-target=".pendingContainer" data-user='1' data-date='3' data-title='2' title="Mark as pending">
                                                <i class="glyphicon glyphicon-file glyphicon-white"></i>
                                            </a>
                                            <a href="#" class='btn btn-mini tip deleteRow' title="Remove">
                                                <i class="glyphicon glyphicon-remove glyphicon-white"></i>
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <a href="#" data-title="Lorem ipsum" data-content="Content of Vero">Vero</a>
                                    </td>
                                    <td>
                                        Keyboard
                                    </td>
                                    <td class='mobile-hide'>
                                        Jul 19, 2012
                                    </td>
                                    <td class='green'>
                                        + 99,99 $
                                    </td>
                                    <td class='actions'>
                                        <div class="btn-group">
                                            <a href="#" class='btn btn-mini tip' title="Show details">
                                                <i class="glyphicon glyphicon-search glyphicon-white"></i>
                                            </a>
                                            <a href="#" class='btn btn-mini tip animateRow' data-target=".pendingContainer" data-user='1' data-date='3' data-title='2' title="Mark as pending">
                                                <i class="glyphicon glyphicon-file glyphicon-white"></i>
                                            </a>
                                            <a href="#" class='btn btn-mini tip deleteRow' title="Remove">
                                                <i class="glyphicon glyphicon-remove glyphicon-white"></i>
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="span3">
                    <div class="box">
                        <div class="box-head">
                            <h3>Comments</h3>
                        </div>
                        <div class="box-content box-nomargin">
                            <table class="table table-striped table-bordered">
                                <thead>
                                <tr>
                                    <th>Comment</th>
                                    <th>Actions</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>Consetetur sadipscing elitr</td>
                                    <td class='actions_two'>
                                        <div class="btn-group">
                                            <a href="#" class="btn btn-mini tip" title="Rate"><i class="glyphicon glyphicon-star-empty glyphicon-white"></i></a>
                                            <a href="#" class='btn btn-mini tip' title="Block"><i class="glyphicon glyphicon-ban-circle glyphicon-white"></i></a>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>Lorem ipsum est aliquip laboris amet aliqua laboris laborum fugiat aute aliquip in est quis nulla elit sit. </td>
                                    <td class='actions_two'>
                                        <div class="btn-group">
                                            <a href="#" class="btn btn-mini tip" title="Rate"><i class="glyphicon glyphicon-star-empty glyphicon-white"></i></a>
                                            <a href="#" class='btn btn-mini tip' title="Block"><i class="glyphicon glyphicon-ban-circle glyphicon-white"></i></a>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>Lorem ipsum dolor sed sed quis Excepteur non. </td>
                                    <td class='actions_two'>
                                        <div class="btn-group">
                                            <a href="#" class="btn btn-mini tip" title="Rate"><i class="glyphicon glyphicon-star-empty glyphicon-white"></i></a>
                                            <a href="#" class='btn btn-mini tip' title="Block"><i class="glyphicon glyphicon-ban-circle glyphicon-white"></i></a>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>Lorem ipsum est sunt dolor officia exercitation ut sed ut. </td>
                                    <td class='actions_two'>
                                        <div class="btn-group">
                                            <a href="#" class="btn btn-mini tip" title="Rate"><i class="glyphicon glyphicon-star-empty glyphicon-white"></i></a>
                                            <a href="#" class='btn btn-mini tip' title="Block"><i class="glyphicon glyphicon-ban-circle glyphicon-white"></i></a>
                                        </div>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="span6">
                    <div class="box">
                        <div class="box-head">
                            <h3>Messages</h3>
                            <span class="label label-info">3</span>
                        </div>
                        <div class="box-content">
                            <ul class="messages">
                                <li class="user1">
                                    <a href="#"><i class="glyphicon glyphicon-align-justify glyphicon-white"></i></a>
                                    <div class="info">
                                        <span class="arrow"></span>
                                        <div class="detail">
                                        <span class="sender">
                                            <strong>Username</strong> says:
                                        </span>
                                            <span class="time">15 minutes ago</span>
                                        </div>
                                        <div class="message">
                                            <p>Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.</p>
                                        </div>
                                    </div>
                                </li>
                                <li class="user2">
                                    <a href="#"><i class="glyphicon glyphicon-align-justify glyphicon-white"></i></a>
                                    <div class="info">
                                        <span class="arrow"></span>
                                        <div class="detail">
                                        <span class="sender">
                                            <strong>Username</strong> says:
                                        </span>
                                            <span class="time">15 minutes ago</span>
                                        </div>
                                        <div class="message">
                                            <p>Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum..</p>
                                            <p>
                                                At vero eos et accusam et justo duo dolores et ea rebum.
                                            </p>
                                        </div>
                                    </div>
                                </li>
                            </ul>
                        </div>

                    </div>
                </div>

            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    //工作台初始化
    jQuery(function($){
        // - quickstats, 就是最前那个chart
        if($('.small-chart').length > 0){
            $('.small-chart').each(function(){
                var color = "#" + $(this).data('color');
                var stroke = "#" + $(this).data('stroke');
                var type = $(this).data('type');
                $(this).peity(type, {
                    colour: color,
                    colours: ['#dddddd', color],
                    diameter: 32,
                    strokeColour: stroke,
                    width: 60,
                    height:32
                });
            });
        }
    });
</script>
</body>
</html>