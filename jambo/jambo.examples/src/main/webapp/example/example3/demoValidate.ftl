<#include "/inc/headinc.ftl"/>
<head>
<title>验证界面</title>
</head>

<body>
<form id="formItem">
<h2>jquery validate 验证样例</h2>
<h3><font color="red">常用验证</font></h3>
<table border="1" cellpadding="0" cellspacing="0">
	<tr height="30px">
		<td width="250px">
			是否输入验证：
		</td>
		<td width="350px">
			<input name="required" class="required"/>	
		</td>		
		<td width="250px">
			是否输入验证(带参数)：
		</td>
		<td width="350px">
			<input name="requiredMsg" class="required" msg="信息1"/>
		</td>	
	</tr>
    <tr height="30px">
		<td width="250px">
			email验证：
		</td>
		<td width="350px">
			<input name="email" class="required email" msg="email"/>
		</td>

		<td width="250px">
			url验证：
		</td>
		<td width="350px">
			<input name="url" class="required url" msg="url"/>
		</td>
	</tr>
	
	<tr height="30px">
		<td width="250px">
			最小长度（3）：
		</td>
		<td width="350px">
			<input name="minlength" class="required" minlength="3" msg="信息2"/>
		</td>
		<td width="250px">
			最大长度（6）：
		</td>
		<td width="350px">
			<textarea name="maxlength" class="required" maxlength="6" msg="信息3"></textarea>
		</td>
	</tr>
    <tr height="30px">
		<td width="250px">
			最大值验证（10）：
		</td>
		<td width="350px">
			<input name="max" class="required" max="10" msg="信息4"/>
		</td>
		<td width="250px">
			最小值验证（5）：
		</td>
		<td width="350px">
			<input name="min" class="required" min="5" msg="信息5"/>
		</td>
	</tr>
    <tr>
		<td width="250px">密码（相等验证）</td>
		<td width="350px">
			<input type="password" name="password" id="password" class="required" minlength="3" msg="密码"/>
		</td>
		<td width="250px">密码确认（相等验证）</td>
		<td width="350px">
			<input type="password" name="confirm_password" class="required" minlength="3" equalTo="#password" msg="密码" />
		</td>
	</tr>
    <tr>
		<td width="250px">比较验证（小于）</td>
		<td width="350px">
			<input name="dataLess" id="dataLess" dataLegalMore="#dataMore" dataType="STR" value="2009-05-17" msg="起始"/>
		</td>
		<td width="250px">说明：dataType类型现在有两种，"INT"代表数字比较，"STR"代表字符串、日期、时间比较</td>
		<td width="350px">
			<input name="dataMore" id="dataMore" dataLegalLess="#dataLess" dataType="STR" value="2009-05-17" msg="结束"/>
		</td>
	</tr>
	    <tr>
		<td width="250px">比较验证（小于等于）</td>
		<td width="350px">
			<input name="dataLessEqual" id="dataLessEqual" dataLegalMoreEqual="#dataMoreEqual" dataType="STR" value="2009-05-17" msg="起始2"/>
		</td>
		<td width="250px">说明：dataType类型现在有两种，"INT"代表数字比较，"STR"代表字符串、日期、时间比较</td>
		<td width="350px">
			<input name="dataMoreEqual" id="dataMoreEqual" dataLegalLessEqual="#dataLessEqual" dataType="STR" value="2009-05-17" msg="结束2"/>
		</td>
	</tr>
    <tr>
		<td width="250px">ajax验证</td>
		<td width="350px">
			<input name="remote" class="required" remote="test.xml"/>
		</td>
		<td width="250px">&nbsp;</td>
		<td width="350px">
			&nbsp;
		</td>
	</tr>
</table>
PS:常用的日期验证未添加，日期一般是从日期控件中选择，不能手动编辑，暂不需验证，以后如有需要，再根据日期显示格式加上验证器<br>
<br>
<h3><font color="red">数字验证</font></h3>
<table border="1" cellpadding="0" cellspacing="0">
	<tr height="30px">
		<td width="250px">
			数字(不可带正负号，整数位：4)：
		</td>
		<td width="350px">
			<input name="number" class="required number" msg="数字" integerLen="4"/>
		</td>
		<td width="250px">
			整数：（整数位：3）
		</td>
		<td width="350px">
			<input name="integer" class="required integer" msg="整数" integerLen="3"/>
		</td>
	</tr>
    <tr height="30px">
		<td width="250px">
			小数(不可带正负号，整数位：3，小数位数，2)：
		</td>
		<td width="350px">
			<input name="floatNum" class="required floatNum" msg="不可带正负号小数" integerLen="3" decimalLen="2"/>
		</td>
		<td width="250px">
			小数：（整数位：4，小数位3）
		</td>
		<td width="350px">
			<input name="float" class="required float" msg="小数" integerLen="4" decimalLen="3"/>
		</td>
	</tr>
</table>
<br>
<h3><font color="red">追加的常用验证</font></h3>
<table border="1" cellpadding="0" cellspacing="0">
	<tr height="30px">
		<td width="250px">
			ip验证：
		</td>
		<td width="350px">
			<input name="ip" class="required ip" msg="ip地址"/>
		</td>
		<td width="250px">
			端口验证：
		</td>
		<td width="350px">
			<input name="port" class="required port" msg="端口"/>
		</td>
	</tr>
    
	<tr height="30px">
		<td width="250px">
			身份证验证：
		</td>
		<td width="350px">
			<input type="text" name="idcard" class="required idcard" msg="身份证号码"/>
		</td>
		<td width="250px">
			邮编验证：
		</td>
		<td width="350px">
			<input type="text" name="zipCode" class="required zipCode" msg="邮编"/>
		</td>
	</tr>	
	
	<tr height="30px">
		<td width="250px">
			用户名验证：
		</td>
		<td width="350px">
			<input type="text" name="userName" class="required userName" msg="用户名"/>
		</td>
		<td width="250px">
			手机号码验证：
		</td>
		<td width="350px">
			<input type="text" name="mobile" class="required mobile" msg="手机号码"/>
		</td>
	</tr>
	
	<tr height="30px">
		<td width="250px">
			电话号码验证：
		</td>
		<td width="350px">
			<input type="text" name="phone" class="required mobilephone" msg="电话号码"/>
		</td>
		<td width="250px">
			传真验证：
		</td>
		<td width="350px">
			<input type="text" name="fax" class="required fax" msg="传真号码"/>
		</td>
	</tr>
	
	<tr height="30px">
		<td width="250px">
			图片格式验证(gif、jpg或png)：
		</td>
		<td width="350px">
			<input type="file" name="pic" class="requiredSel pic" msg="图片"/>
		</td>
		<td width="250px">
			文本格式验证(doc、ppt、xls或txt)：
		</td>
		<td width="350px">
			<input type="file" name="doc" class="requiredSel doc" msg="文本"/>
		</td>
	</tr>
</table>
<br>
<h3><font color="red">单选框、复选框、下拉框验证</font></h3>
<table border="1" cellpadding="0" cellspacing="0">
	<tr height="30px">
		<td width="250px">
			单选框验证：
		</td>
		<td width="350px" colspan="3">
			<input type="radio" name="radio" class="requiredSel" msg="等级"/>1
			<input type="radio" name="radio"/>2
			<input type="radio" name="radio"/>3
		</td>
	</tr>	
	
	<tr height="30px">
		<td width="250px">
			复选框验证：（必选）
		</td>
		<td width="350px">
			<input type="checkbox" name="checkbox" class="requiredSel" msg="开发人员"/>张思伟
			<input type="checkbox" name="checkbox"/>李昭良
			<input type="checkbox" name="checkbox"/>彭少林
            <input type="checkbox" name="checkbox"/>叶道鄂
		</td>
        <td width="250px">
			复选框验证：（最多选择项:3）
		</td>
		<td width="350px">
			<input type="checkbox" name="checkbox2" class="requiredSel" message="请选择不多于3个开发人员" maxlength="3"/>张思伟
			<input type="checkbox" name="checkbox2"/>李昭良
			<input type="checkbox" name="checkbox2"/>彭少林
            <input type="checkbox" name="checkbox2"/>叶道鄂
		</td>
    </tr>
    <tr height="30px">
        <td width="250px">
			复选框验证：（最少选择项:2）
		</td>
		<td width="350px" colspan="3">
			<input type="checkbox" name="checkbox3" class="requiredSel" message="请选择不少于2个开发人员" minlength="2"/>张思伟
			<input type="checkbox" name="checkbox3"/>李昭良
			<input type="checkbox" name="checkbox3"/>彭少林
            <input type="checkbox" name="checkbox3"/>叶道鄂
		</td>
	</tr>
	
	<tr height="30px">
		<td width="250px">
			下拉框验证：
		</td>
		<td width="350px">
			<select name="select" class="requiredSel" msg="设计人员">
				<option value=""></option>
				<option value="1">杨雪鸿</option>
				<option value="2">张锋超</option>
                <option value="3">罗周杰</option>
                <option value="4">李名豪</option>
			</select>
		</td>
        <td width="250px">
			下拉框验证：（最多选择项:3）
		</td>
		<td width="350px">
			<select name="select2" class="requiredSel" message="请选择不多于3个设计人员" maxlength="3" multiple="multiple">
				<option value="1">杨雪鸿</option>
				<option value="2">张锋超</option>
                <option value="3">罗周杰</option>
                <option value="4">李名豪</option>
			</select>
		</td>
	</tr>
    <tr height="30px">
        <td width="250px">
			下拉框验证：（最少选择项:2）
		</td>
		<td width="350px" colspan="3">
			<select name="select3" class="requiredSel" message="请选择不少于2个设计人员" minlength="2" multiple="multiple">
				<option value="1">杨雪鸿</option>
				<option value="2">张锋超</option>
                <option value="3">罗周杰</option>
                <option value="4">李名豪</option>
			</select>
		</td>
	</tr>
</table>
PS:1、单选框、复选框是否输入验证，自定义消息msg="xxx"只需要在一组元素的第一个定义即可。<br>
2、最多选项和最少选项验证比较特殊，用得也比较少，暂不支持自定义参数传入构造出错提示信息，提示信息必须用"message"属性由开发人员自行编写<br>
<br>
<input type="submit" value="提交" /><br>
<br>
<hr />
<font color="red"><strong>注意：</strong></font>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
除了单选框、复选框一组使用一个名字外，不同的验证表单元素name属性值不能相同。
</form>
</body>
</html>
