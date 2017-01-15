package com.jambo.jop.infrastructure.db;

import com.jambo.jop.exception.JOPException;
import com.jambo.jop.infrastructure.config.CoreConfigInfo;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.*;


/**
 * <p>Title: BaseListVO</p>
 *
 * <p>Description: ListVO基类. 支持动态条件。</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: jambo-framework Tech Ltd.</p>
 *
 * @author HuangBaiming
 * @author He Kun
 *
 * @version 1.0
 */
public class DBQueryParam implements Serializable {
    //操作类型 新增，删除，更新
    public static final int OPERATE_TYPE_CREATE = 0;
    public static final int OPERATE_TYPE_DELETE = 1;
    public static final int OPERATE_TYPE_UPDATE = 2;

	private static final long serialVersionUID = -4795615512992486279L;  //序列化唯一标识

    //当前是页码
    private String _pageno = CoreConfigInfo.DEFAULT_PAGE;
    //每页多少行
    private String _pagesize = String.valueOf(CoreConfigInfo.DEFAULT_PAGE_SIZE);

    //排序字段
    private String _orderby;
    //升序，降序
    private String _desc;
    private boolean ascending = true;
    //返回的数据数目
    private String _datasize;
    private String _firstitems;
    
    //列举出只需要查询的字段名称
    private List selectFields;
    
//  列举出只需要查询的字段名称
    private String selectFieldsString;
    /**
     * hekun 返回的部分字段的java class类型，只需设置VO中没有的属性的类型
     * Map fieldClass = new HashMap();
	   fieldClass.put("companystate", Short.class);
	   param.setSelectFieldsClass(fieldClass);
     */
    private Map selectFieldsClass;
    
    /**
     * hekun 设置部分字段查询时返回的元素使用VO类型，默认使用Map类型，以节省资源. 注意，必须保证部分字段都是vo的属性，否则仍然会Map类型
     */
    private boolean selectFieldsUseVOType;
    
    //查询条件, 主要设置固定参数，也可设置形如：_se_xxx的动态参数
    private Map queryConditions ;

    //web层使用的属性，标识已选中的对象, //hekun: 为所选中的对象的主键集合，按逗号分隔
    private String[] _selectitem;

    /**
     * QUERY_TYPE_COUNT_AND_DATA 查询类型,查询所有内容,即同时查询数据和查询分页所需要的数据总量
     * QUERY_TYPE_COUNT_ONLY 查询类型,只查询符合条件的数据总量
     * QUERY_TYPE_DATA_ONLY 查询类型,只查询数据，不查询总量，效率比较高
     * 默认是不查询总数，即默认值为：QUERY_TYPE_DATA_ONLY。
     */
    private int queryType = BaseDAO.QUERY_TYPE_DATA_ONLY;

    /**
     * hekun 仅使用固定参数。即只按照named sql query中定义的固定参数，
     * 根据listVO属性和 queryConditions参数设置其值，而不会追加 _se_xxx 这样的动态参数
     */
    private boolean useFixedParamOnly; 

    //主键ID,在打开编辑页面时使用
    private String _pk;

    //以下新增的属性是配合dubbo这种微服务框架时使用的属性
    //serialNo用于作为跟后台交互的的每次请求凭证
    //流水号 guid
    private String serialNo;
    //操作员对象
    private DBAccessUser operator;

    //操作类型 新增，删除，更新
    private int operateType = 0;

    public DBQueryParam() {
        genSerialNo();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public void genSerialNo(){
        serialNo = String.valueOf(UUID.randomUUID());
    }

    public DBAccessUser getOperator() {
        return operator;
    }

    public void setOperator(DBAccessUser operator) {
        this.operator = operator;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    //编辑类型,在编辑页面保存时判断是新增,还是保存
    private String cmd;


	public String get_desc() {
        return _desc;
    }

    public void set_desc(String _desc) {
        this._desc = _desc;
        this.ascending = !"1".equals(_desc); //不为降序则都看作升序。
    }

    public String get_orderby() {
        return _orderby;
    }

    public void set_orderby(String _orderby) {
        this._orderby = _orderby;
    }

    public String get_pageno() {
        return _pageno;
    }

    public void set_pageno(String _pageno) {
        this._pageno = _pageno;
    }

    public String get_pagesize() {
        return _pagesize;
    }

    public String get_datasize() {
        return _datasize;
    }

    public void set_pagesize(String _pagesize) {
        this._pagesize = _pagesize;
    }

    public void set_datasize(String _datasize) {
        this._datasize = _datasize;
    }

    public void setAscending(boolean ascending)
    {
    	this.ascending = ascending;
    }
    
    public boolean isAscending()
    {
    	return ascending;
    }
    
    /**
     * 使用动态查询参数。语法规则不变。添加条件时不需要给类添加属性，以Map形式添加即可。
     * 例如：
     * getQueryConditions.put("_sk_oprcode","example");
     * getQueryConditions.put("_ne_logid",new Long(1232));
     * 当既有 静态条件值，又有动态条件值时，以静态为优先。
     * @return
     * @author He Kun
     */
	public Map getQueryConditions() {
		if(this.queryConditions == null)
			this.queryConditions = new LinkedHashMap();
		return this.queryConditions;
	}

	public void setQueryConditions(Map queryConditions) {
		this.queryConditions = queryConditions;
	}

	public String get_firstitems() {
		return _firstitems;
	}

	public void set_firstitems(String _firstitems) {
		this._firstitems = _firstitems;
	}

	public List getSelectFields() {
		return selectFields;
	}

	public void setSelectFields(List selectFields) {
		this.selectFields = selectFields;
		if(selectFields!=null) {
			StringBuffer buffer = new StringBuffer(32);
			for (int i = 0; i < selectFields.size(); i++) {
				buffer.append(selectFields.get(i));
				if(i < selectFields.size() - 1)
					buffer.append(",");
			}
			this.selectFieldsString = buffer.toString();
		}
	}    
	

	
    public String[] get_selectitem() {
        return _selectitem;
    }

    public void set_selectitem(String[] _selectitem) {
        this._selectitem = _selectitem;
    }

    public String get_pk() {
		return _pk;
	}

	public void set_pk(String _pk) {
		this._pk = _pk;
	}

	public boolean isUseFixedParamOnly() {
		return useFixedParamOnly;
	}

	public void setUseFixedParamOnly(boolean useFixedParamOnly) {
		this.useFixedParamOnly = useFixedParamOnly;
	}

	public Map getSelectFieldsClass() {
		return selectFieldsClass;
	}

	public void setSelectFieldsClass(Map selectFieldsClass) {
		this.selectFieldsClass = selectFieldsClass;
	}

	public String getSelectFieldsString() {	
		return selectFieldsString;
	}

	public void setSelectFieldsString(String selectFieldsString) {
		
		if( selectFieldsString == null) return ;
		this.selectFieldsString = selectFieldsString;
		String[] fields = selectFieldsString.split(",");
		if(fields.length >  0) {
			List list = new ArrayList(fields.length );
			
			for (int i = 0; i < fields.length; i++) {
				list.add( StringUtils.trim(fields[i]) );
			}
			this.selectFields = list;
		}
	}

	public boolean isSelectFieldsUseVOType() {
		return selectFieldsUseVOType;
	}

	public void setSelectFieldsUseVOType(boolean selectFieldsUseVOType) {
		this.selectFieldsUseVOType = selectFieldsUseVOType;
	}

    public int getQueryType() {
        return queryType;
    }

    public void setQueryType(int queryType) {
        if (queryType == BaseDAO.QUERY_TYPE_DATA_ONLY || queryType == BaseDAO.QUERY_TYPE_COUNT_ONLY || queryType == BaseDAO.QUERY_TYPE_COUNT_AND_DATA || queryType == BaseDAO.QUERY_TYPE_NATIVE_SQL){
            this.queryType = queryType;
        } else {
            throw new JOPException("Query Type must be [QUERY_TYPE_DATA_ONLY, QUERY_TYPE_COUNT_ONLY, QUERY_TYPE_COUNT_AND_DATA, QUERY_TYPE_NATIVE_SQL]!");
        }
    }

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }
}
