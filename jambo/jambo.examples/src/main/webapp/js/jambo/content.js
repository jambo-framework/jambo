/*
 * public method for the page: content.jsp
 *
 * notice! the global var: 
 *          contextPath, formItem
 *      the global method��
 *          ev_checkval()
 */

function doReturn(cmdReturn) {
    formItem.action = contextPath + cmdReturn;
    formItem.method = "GET";
    formItem.submit();
}

function doSave(cmdSave) {
    var ret = ev_checkval();   
    if ( ret ) {
        enable();
        formItem.action = contextPath + cmdSave;
        formItem.submit();
    }
    return false;
}

///////////////////////////////////
function enable(form) {
    if (form == null) {
        form = document.all("formItem");
    }

    for (var i = 0; i < form.elements.length; i++) {
        if (form.elements(i).disabled == true && form.elements(i).name != 'DNPTS')
            form.elements(i).disabled = false;
    }
}

var addfilemaxnumber = 1;
function selState(){
	if (addfilemaxnumber <= 10) {
		str = "div_file" + addfilemaxnumber;
		document.all(str).style.display = "block";
		addfilemaxnumber ++;
		}
	}
var addFileStyle = new Array(10);
function fulladdFileStyle () {
	var str = "";
	for(i=0;i<addFileStyle.length;i++) {
		str += "<option>"+ addFileStyle[i] +"</option>"
		}
	return str;
	}
function addfile(){
	var addf;
	addf = document.createElement("<div class=\"div_file2\"></div>");
	orc = document.all("addfile").appendChild(addf);
	otext = "<table class=\"table_style\" ID=\"Table1\">"
	otext+= "<tr class=\"table_style_content\">"
	otext+= "<td width=12% align=right>��������������</td>"
	otext+= "<td align=left><select class=\"form_selects_on\">"+ fulladdFileStyle()+"</select>&nbsp;&nbsp;<input type=\"button\" value=\"ɾ ��\" class=\"cancel\" onclick=\"removefile(this)\"></td>"					
	otext+= "</tr>"
	otext+= "<tr class=\"table_style_content\">"
	otext+= "<td width=12% align=right>�ļ�·����</td>"
	otext+= "<td align=left><input type=\"File\" class=\"form_input_files\" name=\"upload1\" ID=\"File1\" /></td>"				
	otext+= "</tr>"
	otext+= "</table>" 
	orc.innerHTML = otext;
	}
function removefile(obj) {
	while (obj.tagName.toUpperCase() != "DIV") {
		obj = obj.parentNode;
		}
	obj.removeNode (true);
	}