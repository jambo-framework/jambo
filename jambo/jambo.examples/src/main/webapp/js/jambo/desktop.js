var zTree;
var setting = {
    view: {
        dblClickExpand: false,
        showLine: true,
        selectedMulti: false
    },
    data: {
        simpleData: {
            enable:true,
            idKey: "id",
            pIdKey: "pId",
            rootPId: ""
        }
    },
    callback: {
        beforeClick: function(treeId, treeNode) {
            var zTree = $.fn.zTree.getZTreeObj("tree");
            if (treeNode.isParent) {
                zTree.expandNode(treeNode);
                return false;
            } else {
                return true;
            }
        },
        onClick: function(treeId, treeNode) {
            $('#myModal').modal('hide');
        }
    }
};

var zDemoNodes =[
    //{id:1, pId:0, name:"[demo] 框架范例演示页面", url:"javascript:openmenu();", open:false},
    {id:1, pId:0, name:"[demo] 框架范例演示页面", url:"javascript:tabMan.openTabByUrl(1,\"范例\",contextPath+\"/redirect.do?url=example/examlist\")", open:false, target:"_self"},

    {id:2, pId:0, name:"NG3产品中心", open:true},
    {id:201, pId:2, name:"系统管理", url:"excheck/checkbox"},
    {id:206, pId:2, name:"产品定义", url:"excheck/checkbox_nocheck"},
    {id:207, pId:2, name:"报表统计", url:"excheck/checkbox_chkDisabled"},
];

function TabManager(tabname, win){
    this.MAX_TAB_COUNT = 8;
    this.tabDivObj = $("#" + tabname);
    this.tabDivName = tabname;
    this.winDivObj = win;
}

TabManager.prototype.openTabByUrl = function(tabId, name, url){
    if (this.tabDivObj.children().length < this.MAX_TAB_COUNT){
        if (!this.isExisted(tabId) ) {
            var shtml = '';

            shtml += "<li id='tab_"+tabId+"'>";
            shtml += "<a href='#"+tabId+"' id='tab_aid_"+tabId+"' data-toggle='tab'>"+name+"<i class='btn icon-remove btn_close' onclick='javascript:tabMan.closeTab("+tabId+")'></i></a>";
            shtml += "</li>";
            this.tabDivObj.append(shtml);

            shtml = '';
            shtml += "<div id='" + tabId +"' class='tab-pane'>";
            shtml += "<iframe src='" + url + "' width='100%' frameborder='0' onload='Javascript:setWinHeight(this)'></iframe>";
            shtml += "</div>";
            this.winDivObj.append(shtml);
        }
        this.activeById(tabId);
    } else {
        alert(msgTabLimit + this.MAX_TAB_COUNT);
    }
}

TabManager.prototype.isExisted = function(tabId){
    return $("#tab_"+tabId).attr('id') == 'tab_'+tabId;
}

TabManager.prototype.test = function(){
    alert("test!");
}

TabManager.prototype.closeTab = function(tabId){
    selectedTab = $("#tab_"+tabId);
    selectedTab.remove();

    selectedTabWin = $("#"+tabId);
    selectedTabWin.remove();

    this.activeById(0);
}

TabManager.prototype.getCount = function(){
    return this.tabDivObj.getCount();
}

TabManager.prototype.activeById = function(tabId){
    this.selectById(tabId).tab("show");

//    $('#myTab a[href="#profile"]').tab('show'); // Select tab by name
//    $('#myTab a:first').tab('show'); // Select first tab
//    $('#myTab a:last').tab('show'); // Select last tab
//    $('#myTab li:eq(2) a').tab('show');
}

TabManager.prototype.selectById = function(tabId){
    return $("#"+this.tabDivName+" a[href='#"+tabId+"'][id=tab_aid_"+tabId+"]");
}

