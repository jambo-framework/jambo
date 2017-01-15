//页面框架初始化，没用的已经清理，如果以后用到的再加入
$(document).ready(function() {
	"use strict";
	// ------ DO NOT CHANGE ------- //
	$(".flot-bar,.flot-pie,.flot,.flot-multi").bind("plothover", function (event, pos, item) {
		if (item) {
			var y;
			if(event.currentTarget.className === 'flot flot-bar'){
				y = Math.round(item.datapoint[1]);
			} else if(event.currentTarget.className === 'flot flot-pie') {
				y = Math.round(item.datapoint[0])+"%";
			} else if(event.currentTarget.className === 'flot flot-line'){
				y = (Math.round(item.datapoint[1] * 1000)/1000);
			} else {
				y = (Math.round(item.datapoint[1]*1000)/1000)+"€";
			}
			$("#tooltip").remove();
			showTooltip(pos.pageX, pos.pageY,"Value = "+y);
		}
		else {
			$("#tooltip").remove();         
		}
	});

	function showTooltip(x, y, contents) {
		$('<div id="tooltip">' + contents + '</div>').css( {
			top: y + 5,
			left: x + 10
		}).appendTo("body").show();
	}
	$('.deleteRow').click(function(e){
		e.preventDefault();
		$(this).parents('tr').fadeOut();
	});

	$(".mini > li > a").hover(function(e){
	e.stopPropagation();
	if(!$(this).parent().hasClass("open")){
		$(this).find(".label").stop().animate({
			top: '-10px'
		},200, function(){
			$(this).stop().animate({top: '-6px'},100);
		});
	}
	}, function(){});

	$('.main-nav > li.active > a').click(function(e){
		if($(window).width() <= 767){
			e.preventDefault();
			if($(this).hasClass('open') && (!$(this).hasClass('toggle-collapsed'))){
				$(this).removeClass('open');
				$(this).parents('.main-nav').find('li').each(function(){
					$(this).find('.collapsed-nav').addClass('closed');
					$(this).hide();
				});
				$(this).parent().show();
				$(this).parent().removeClass('open');
			} else {
				if($(this).hasClass('toggle-collapsed')){
					$(this).parent().addClass('open');
				}
				if($(this).hasClass("open")){
					$(this).parents('.main-nav').find('li').not('.active').each(function(){
						$(this).find('.collapsed-nav').addClass('closed');
						$(this).hide();
					});
					$(this).removeClass("open");
				} else {
					$(this).addClass('open');
					$(this).parents('.main-nav').find('li').show();
				}
			}
		}
	});

	$('.toggle-collapsed').each(function(){
		if($(this).hasClass('on-hover')){
			$(this).click(function(e){e.preventDefault();});
			$(this).parent().hover(function(){
				$(this).addClass("open");
				$(this).find('.collapsed-nav').slideDown();
				$(this).find('img').attr("src", 'img/toggle-subnav-up-white.png');
			}, function(){
				$(this).removeClass("open");
				$(this).find('.collapsed-nav').slideUp();
				$(this).find('img').attr("src", 'img/toggle-subnav-down.png');
			});
		} else {
			$(this).click(function(e){
				e.preventDefault();
				if($(this).parent().find('.collapsed-nav').is(":visible")){
					$(this).parent().removeClass("open");
					$(this).parent().find('.collapsed-nav').slideUp();
					$(this).find('img').attr("src", 'img/toggle-subnav-down.png');
				} else {
					$(this).parent().addClass("open");
					$(this).parent().find('.collapsed-nav').slideDown();
					$(this).find('img').attr("src", 'img/toggle-subnav-up-white.png');
				}
			});
		}
	});

	$('.collapsed-nav li a').hover(function(){
		if(!$(this).parent().hasClass('active')){
			$(this).stop().animate({
				marginLeft: '5px'
			}, 300);
		}
	}, function(){
	$(this).stop().animate({
			marginLeft: '0'
		}, 100);
	});
	//$('a.preview').live('mouseover mouseout mousemove click',function(e){
	//		if(e.type === 'mouseover'){
	//			$('body').append('<div id="image_preview"><img src="'+$(this).attr('href')+'" width="150"></div>');
	//			$("#image_preview").fadeIn();
	//		} else if(e.type === 'mouseout') {
	//			$("#image_preview").remove();
	//		} else if(e.type === 'mousemove'){
	//			$("#image_preview").css({
	//				top:e.pageY+10+"px",
	//				left:e.pageX+10+"px"
	//			});
	//		} else if(e.type === 'click'){
	//			$("#image_preview").remove();
	//		}
	//	});

	$('.sel_all').click(function(){
		$(this).parents('table').find('.selectable-checkbox').prop('checked', this.checked);
	});
	// ------ END DO NOT CHANGE ------- //

	//校验测试
	//$.validator.setDefaults({
	//	debug: true
	//})

	// - validation
	if($('.validate').length > 0){
		$('.validate').validate({
			errorPlacement:function(error, element){
					element.parents('.controls').append(error);
			},
			highlight: function(label) {
				$(label).closest('.control-group').removeClass('error success').addClass('error');
			},
			success: function(label) {
				label.addClass('valid').closest('.control-group').removeClass('error success').addClass('success');
			}
		});
	}
	// - wizard
	if($(".wizard").length > 0){
		$(".wizard").formwizard({ 
				formPluginEnabled: true,
				validationEnabled: true,
				focusFirstInput : false,
				validationOptions: {
				highlight: function(label) {
					$(label).closest('.control-group').addClass('error');
				},
				success: function(label) {
					label.addClass('valid').closest('.control-group').addClass('success');
				}
				},
				formOptions :{
					success: function(){
					},
					beforeSubmit: function(){
						$('#myModal').modal('show');
					},
				dataType: 'json',
				resetForm: true
			}	
		});
	}
	// - tooltips
	$(".tip").tooltip();
	// - timepicker
	if($('.timepicker').length > 0){
		$('.timepicker').timepicker({
			defaultTime: 'current',
			minuteStep: 1,
			disableFocus: true,
			template: 'dropdown'
		});
	}
	// - datepicker
	if($('.datepick').length > 0){
		$('.datepick').datepicker();	
	}

	// - masked inputs
	if($('.mask_date').length > 0){
		$(".mask_date").inputmask("9999/99/99");	
	}
	if($('.mask_phone').length > 0){
		$(".mask_phone").inputmask("(999) 999-9999");
	}
	if($('.mask_serialNumber').length > 0){
		$(".mask_serialNumber").inputmask("9999-9999-99");	
	}
	if($('.mask_productNumber').length > 0){
		$(".mask_productNumber").inputmask("AAA-9999-A");	
	}
});

//配合code2name 里的 picker用的脚本 begin--
function openPicker(control,definition,condition, dbflag) {
    if(definition == null || definition =="") {
        alert("definition is required!");
        return ;
    }

    definition = window.encodeURIComponent(definition);
    var url = contextPath +"/pickerAction/list.do?definition=" + definition + "&_pageno=1&_pagesize=15";

    if(condition!=null) {
        condition = window.encodeURIComponent(condition);
        url = url +"&condition=" + condition;
    }

    if(dbflag!=null) {
        dbflag = window.encodeURIComponent(dbflag);
        url = url +"&dbFlag=" + dbflag;
    }

    var buttonID = control.name;
    if(buttonID == null || buttonID == "") {
        alert("Must set the name property for this selector business!");
        return false;
    }
    url = url +"&btnid=" + buttonID +"&" + new Date();

    if(window.ActiveXObject){ //IE
        var returnValue = window.showModalDialog(url, window, "dialogWidth=425px;dialogHeight=500px;status:no;scroll=yes;");
        if(returnValue == null ){
            return false;
        }
        setpickervalue(buttonID,  rtn[0],  rtn[1]);
    }else{  //非IE
        window.open(url, 'newwindow','height=500,width=425,top=150,left=300,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
    }
}

function setpickervalue(buttonID, code, name) {
    var selectorID = buttonID.substring(0, buttonID.indexOf("_button"));
    var selectorTextID = selectorID + "_text";

    var codeCtrl = document.getElementById( selectorID );
    var nameCtrl = document.getElementById( selectorTextID );

    if(codeCtrl!=null) codeCtrl.value = code;
    if(nameCtrl!=null) nameCtrl.value = name;
}

function selectCode(btnid, code, name) {
    if(window.ActiveXObject){ //IE
        var a = new Array(2);
        a[0] = code;
        a[1] = name;

        window.returnValue = a;
        window.close();
    }else{ //非IE
        if(window.opener) {
            window.opener.setpickervalue(btnid, code,  name);
        }
        window.close();
    }
}
//配合code2name 里的 picker用的脚本 --end

//桌面iframe高度自动调整
function setWinHeight(obj){
    var win=obj;
    if (document.getElementById) {
        if (win.contentDocument && win.contentDocument.body.offsetHeight)
            win.height = win.contentDocument.body.scrollHeight;
        else if(win.Document && win.Document.body.scrollHeight)
            win.height = win.Document.body.scrollHeight;
    }
}
