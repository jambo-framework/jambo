<#include "/inc/headinc.ftl"/>
<html>
<head>
    <title>范例程序</title>
</head>
<body>
<br/>
<h2>范例程序	</h2>
<div>
    <ul>
        <li><a  href="${contextPath}/example/example1/example1Action/list.do" >1. CRUD单表基本操作示例程序(默认实现,不查询总页数)</a></li>
        <li><a  href="${contextPath}/example/example1/example1PartAction/list.do" >2. 查询表的部分字段(查询总页数，ajax刷新)</a></li>
        <li><a  href="${contextPath}/example/example2/example2Action/xQuery1.do" >3.复杂查询1(列出有职员的公司职员列表)，两表关联查询(没搞)</a></li>
        <li><a  href="${contextPath}/example/example2/example2Action/xQuery2.do" >4.复杂查询2(列出有职员的公司职员列表),自定SQL查询(没搞)</a></li>
        <li><a  href="${contextPath}/example/example2/example2Action/xQuery3.do" >5.复杂查询3(公司职员人数统计)，组合VO(没搞)</a></li>
        <br/>
        <li><a  href="${contextPath}/example/example2/example2Action/list.do" >A.使用<Strong>BaseSpringAction</Strong>的单表基本操作示例程序</a></li>
        <br/>
        <li><a  href="${contextPath}/example/dubbo1/dubbo1Action/list.do" >调用dubbo的单表基本操作示例程序(默认实现,不查询总页数)</a></li>
        <br/>
        <li><a  href="${contextPath}/redirect.do?url=example/example3/selectorDemo" >a. 标签 &lt;j:selector/&gt; 演示</a></li>
        <li><a  href="${contextPath}/example/example3/demoValidate.ftl" >b. JQuery输入框校验演示</a></li>
        <li><a  href="${contextPath}/redirect.do?url=example/security/xssTest" >c.安全测试,XSS消息过滤</a></li>
        <li><a  href="${contextPath}/redirect.do?url=example/security/crsfTest" >d.安全测试,CRSF防范,打开CRSF的TOKEN检查后,只有这个页面能用</a></li>
    </ul>
</div>
</div>
</body>
</html>