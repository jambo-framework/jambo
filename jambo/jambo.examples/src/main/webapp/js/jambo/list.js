/*
 * public method for the page: list.jsp
 *
 * notice! the list.jsp must have global var :
 *      contextPath, formList,
 *      _orderby, _desc,
 *      _pageno, _pagesize, _rowcount
 */
function checkAll(FO,BO,CO) {
    if (FO == null) {
        FO = "document.formList";
    }else{
    	FO = "document." + FO;
    }
    if (BO == null) {
        BO = "_selectitem";
    }
    if (CO == null) {
    	CO = FO + ".allbox";
    	}else{
    	CO = FO + "." + CO;
    	}
    var sis = eval(FO).all(BO);
    
    if (sis != null) {
        if (sis.length != null) {
            for (var i = 0; i < sis.length; i++) {
                var e = sis[i];
                if (e.type == 'checkbox') {
                    e.checked = eval(CO).checked;
                    /*
                    if (e.checked)
                        hL(e);
                    else
                        dL(e);
                        */
                }
            }
        } else {
            var e = sis;
            if (e.type == 'checkbox') {
                e.checked = eval(CO).checked;
                /*
                if (e.checked)
                    hL(e);
                else
                    dL(e);
                    */
            }
        }
    }
}

function checkOne(FO,BO,CO) {
	/*
    if (CB.checked)
        hL(CB);
    else
        dL(CB);
     */
    if (FO == null) {
        FO = "document.formList";
    }else{
    	FO = "document." + FO;
    }
    if (BO == null) {
//        BO = "_selectitem";
        BO = "getElementsByName('_selectitem')";
    }
    if (CO == null) {
    	CO = FO + ".allbox";
    	}else{
    	CO = FO + "." + CO;
    	}

    var TB = TO = 0;
//    var sis = eval(FO + "." + BO);
    var sis = eval("document.getElementsByName('_selectitem')");
    if (sis != null) {
        if (sis.length != null) {
            for (var i = 0; i < sis.length; i++) {
                var e = sis[i];
                if (e.type == 'checkbox') {
                    TB++;
                    if (e.checked)
                        TO++;
                }
            }
            if (TO == TB){
                eval(CO).checked = true;
                }
            else{
                eval(CO).checked = false;
                }
        } else {
            var e = sis;
            if (e.type == 'checkbox') {
                eval(CO).checked = e.checked;
            }
        }
    }
}

function doNew(cmdNew) {
//    var url = addParam(contextPath + cmdNew, 'CMD', 'NEW');
    var url = contextPath + cmdNew;
    formList.action = url;  
    formList.submit();
}

function doEdit(cmdEdit) {
//    var url = addParam(contextPath + cmdNew, 'CMD', 'NEW');
    var url = contextPath + cmdEdit;
    formList.action = url;
    formList.submit();
}

function doDelete(cmdDelete) {
    var TO = true;
//    var sis = formList.all("_selectitem");
//    var sis = $("_selectitem");
//    var sis = $(":checkbox[name='_selectitem'][checked]")
    var sis = $("input[name='_selectitem']:checkbox:checked");
    if (forincheck(TO,sis,msgConfirmDelete)){
    	formList.action = contextPath + cmdDelete;
        formList.method = "POST";
    	formList.submit();    	
    
    }  
}

function doQuery(cmdQuery){
	resetPage();		
	if(cmdQuery != null && cmdQuery !="")
		formList.action = contextPath + cmdQuery;
		
	if(document.formList.onsubmit == null || document.formList.onsubmit())
		document.formList.submit();
}
function doExcelOut(url) {
    if(ev_check()){
     	setExcelOutPage(url);	
     }        	
}

function forincheck(TO,sis,msg){
    if (sis != null) {
        if (sis.length != null) {
            for (var i = 0; i < sis.length; i++) {
                var e = sis[i];
                if (e.type == 'checkbox') {
                    if (e.checked)
                        TO = false;
                }
            }
        } else {
            var e = sis;
            if (e.type == 'checkbox') {
                if (e.checked)
                    TO = false;
            }
        }
    }

    if (TO) {
        alert(msgNoSelected);
        return false;
    }

    if (!confirm(msg)) {
        return false;
    }
    return true;
}

function forincheckByRadio(TO,sis,msg){
    if (sis != null) {
        if (sis.length != null) {
            for (var i = 0; i < sis.length; i++) {
                var e = sis[i];
                if (e.type == 'radio') {
                    if (e.checked)
                        TO = false;
                }
            }
        } else {
            var e = sis;
            if (e.type == 'radio') {
                if (e.checked)
                    TO = false;
            }
        }
    }

    if (TO) {
        alert(msgNoSelected);
        return false;
    }
	if (msg != null && msg != "") {
    	if (!confirm(msg)) {
        	return false;
    	}
    }
    return true;
}

function doOrderby(field) {   
    if (document.getElementsByName('_orderby')[0].value != field) { // asc
        document.getElementsByName('_orderby')[0].value = field;
        document.getElementsByName('_desc')[0].value = "";
    }
    else if (document.getElementsByName('_desc')[0].value == "") { // desc
        document.getElementsByName('_desc')[0].value = "1";
    } else { // don't order
        document.getElementsByName('_orderby')[0].value = "";
        document.getElementsByName('_desc')[0].value = "";
    }
     if (document.formList.onsubmit == null || document.formList.onsubmit())
        document.formList.submit();
}

function showFirstPage(url, tablid) {
    if (isNaN(parseInt($("#_pageno").val())) ||
        isNaN(parseInt($("#_pagesize").val()))) {
        return;
    }

    $("#_pageno").val(1);

    if (url != null && url != '') {
        ajaxQuery(url, tablid);
    } else {
        $("#_pageno").val(1)
        //document.getElementsByName('_pageno')[0].value = 1;

        if (document.formList.onsubmit == null || document.formList.onsubmit())
            document.formList.submit();
    }
}

function showPreviousPage(url, tablid) {
    if (isNaN(parseInt($("#_pageno").val())) ||
        isNaN(parseInt($("#_pagesize").val()))) {
        return;
    }

    var pageNo = parseInt($("#_pageno").val());

    if (pageNo > 1) {
        $("#_pageno").val(pageNo - 1)
        //document.getElementsByName('_pageno')[0].value = pageNo - 1;

        if (url != null && url != '') {
            ajaxQuery(url, tablid);
        } else {
            if (document.formList.onsubmit == null || document.formList.onsubmit())
                document.formList.submit();
        }
    }
}

function showNextPage(url, tablid) {
    if (isNaN(parseInt(parseInt($("#_pageno").val()))) ||
        isNaN(parseInt(parseInt($("#_pagesize").val())))) {
        return;
    }

    var pageNo = parseInt($("#_pageno").val());
    $("#_pageno").val(pageNo + 1)
    //document.getElementsByName('_pageno')[0].value = pageNo + 1;

    if (url != null && url != '') {
        ajaxQuery(url, tablid);
    } else {
        if (document.formList.onsubmit == null || document.formList.onsubmit())
            document.formList.submit();
    }
}

function showLastPage(url, tablid) {
    if (isNaN(parseInt($("#_pageno").val())) ||
        isNaN(parseInt($("#_pagesize").val())) ||
        isNaN(parseInt($("#_rowcount").val()))) {
        return;
    }

    var pageCount = Math.ceil(parseInt($("#_rowcount").val()) /
                              parseInt($("#_pagesize").val()));

    if (pageCount > 0) {
        $("#_pageno").val(pageCount)
        //document.getElementsByName('_pageno')[0].value = pageCount;
        if (url != null && url != '') {
            ajaxQuery(url, tablid);
        } else {
            if (document.formList.onsubmit == null || document.formList.onsubmit())
                document.formList.submit();
        }
    }
}

function resetPage() {
    $("#_pageno").val(1);
    //document.getElementsByName('_pageno')[0].value = '1';
}

function resetForm() {
    $("#_orderby").val("");
    $("#_desc").val("");
    $("#_pageno").val(1);
    //document.getElementsByName('_orderby')[0].value = '';
    //document.getElementsByName('_desc')[0].value = '';
    //document.getElementsByName('_pageno')[0].value = '1';
}

var ie = document.all ? 1 : 0;

function hL(E){
  if (ie) {
    while (E.tagName!="TR")
      E=E.parentElement;
  } else {
    while (E.tagName!="TR")
      E=E.parentNode;
  }
}

function dL(E){
  if (ie) {
    while (E.tagName!="TR")
      E=E.parentElement;
  } else {
    while (E.tagName!="TR")
      E=E.parentNode;
  }
}
function toallcheck(str2,str3) {
       	if (eval("document.all."+ str2+".length") != null) {
       		for (var n = 0 ; n < eval("document.all."+ str2+".length") ; n++) {
        		if(!(eval("document.all."+ str2+"[n].checked"))) {return}
        	}
       	}else{
       		if(!(eval("document.all."+ str2+".checked"))) {return}
       	}
        	eval("document.all."+ str3+".checked = true")
        }

$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [ o[this.name] ];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

function ajaxQuery(url, tableid) {
    var p =  $('#formList').serializeObject();

    $.ajax({
        type:'post',
        url: url,
        data : p,
        success: function(json) {
            ajaxRefreshTable(tableid, json);
        },
        error : function() {
            ajaxShowError()
        }
    });
}

function ajaxShowError(){
    alert("query error!");
}

function ajaxRefreshTable(tableid, dp){
    if (dp != null) {
        var table = $("#" + tableid);
        if (table != null){
            table.empty();

            var datas = dp.datas;

            $.each(datas, function(index){
                var obj = datas[index];

                //把null 替换为空字符串
                $.each(obj, function(index){
                    if (obj[index] == null)  obj[index] = "";
                });

                ajaxUpdateTableLine(table, obj, index);
            });

            ajaxUpdateNavpage(dp.totalPage, dp.pageNo, dp.isLast);
            ajaxUpdateMessage();
        }
    }
}

function ajaxUpdateMessage(){
    var infopan = $("#infopan");
    if (infopan != null){
        infopan.hide();
    }
}

function ajaxUpdateNavpage(totalPage, pageNo, isLast){
    if (totalPage > 0)
        $("#nav_total_page").show();
    else $("#nav_total_page").hide();

    if (pageNo > 1){
        $("#nav_first_pan").show();
        $("#nav_previsous_pan").show();
    } else{
        $("#nav_first_pan").hide();
        $("#nav_previsous_pan").hide();
    }

    if (pageNo <= totalPage)
        $("#nav_last_pan").show();
    else $("#nav_last_pan").hide();

    if (isLast)
        $("#nav_next_pan").hide();
    else $("#nav_next_pan").show();

    $("#nav_pageno").text(pageNo);
}
