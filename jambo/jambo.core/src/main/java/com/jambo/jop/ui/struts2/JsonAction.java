package com.jambo.jop.ui.struts2;

import com.jambo.jop.infrastructure.db.DataPackage;
import com.jambo.jop.ui.jqtable.ColumnSet;
import com.jambo.jop.ui.jqtable.JSONKey;
import com.jambo.jop.ui.jqtable.Page;
import com.jambo.jop.ui.spring.BaseAction;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class JsonAction extends BaseAction {
	/**
	 * 报出异常信息
	 * 
	 * @param msg
	 */
	public void writeJSONError(String msg) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JSONKey.IS_SUCCESS, Boolean.valueOf(false));
		jsonObject.put(JSONKey.MESSAGE, msg);
//		renderHtml(jsonObject.toString());
	}

	/**
	 * 
	 * 写JSON列表对象 - 对象为ServiceResult
	 * 
	 * @param serviceResult
	 * @param resultFields
	 */
	public void writeJSONDataPackage(DataPackage serviceResult, String[] resultFields) {
		List<ColumnSet> set = new ArrayList<ColumnSet>();
		for (String dtl : resultFields) {
			ColumnSet dtlSet = new ColumnSet(dtl, "");
			set.add(dtlSet);
		}
		this.writeJSONDataPackage(serviceResult, set);
	}

	/**
	 * 
	 * 写JSON列表对象 - 对象为DataPackage
	 * 
	 * @param dp
	 */
	public void writeJSONDataPackage(DataPackage dp, List resultFields) {
		try {
			// Assert.notEmpty(args);
			JSONObject jsonObject = new JSONObject();
			// 业务成功
			List list = dp.getDatas();
			List dataList = new ArrayList();
			// 转换页内数据
			parseData(list, dataList, resultFields);

			Page page = new Page(dp.getRowCount(), dp.getPageSize(), dp.getPageNo());
			// 是否成功
			jsonObject.put(JSONKey.IS_SUCCESS, true);
			// //总记录数
			// jsonObject.put(JSONKey.TOTALRECORDS,
			// Integer.valueOf(page.getRows()));
			// //总页数
			// jsonObject.put(JSONKey.TOTALPAGE,
			// Integer.valueOf(page.getNumbers()));
			// //当前页
			// jsonObject.put(JSONKey.CURRENTPAGE,
			// Integer.valueOf(page.getCurrent()));
			// //页内记录数
			// jsonObject.put(JSONKey.PAGESIZE,
			// Integer.valueOf(page.getSize()));
			// 回写翻页用类
			jsonObject.put(JSONKey.PAGE, page);

			// 页内数据
			jsonObject.put(JSONKey.DATAS, JSONArray.fromObject(dataList));
//			renderHtml(jsonObject.toString());
		} catch (Exception e) {
			writeJSONError(e.getMessage());
		}
	}

	/**
	 * 处理查询结果
	 * 
	 * @param list
	 * @param dataList
	 * @param args
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	protected void parseData(List list, List dataList, List args) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		int size = args.size();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Object element = (Object) iter.next();
			Map dataMap = new HashMap(size);
			for (int i = 0; i < size; i++) {
				ColumnSet dtlSet = (ColumnSet) args.get(i);// 取ColumnSet对象
				if (dtlSet.isNonData())
					continue;
				// 类型转换
				Object value = null;
				String dataKey = dtlSet.getDataKey();
				String tdKey = dtlSet.getKey();
				if (dataKey.indexOf('.') > -1) {
					String[] arrArg = StringUtils.split(dataKey, '.');
					value = element;
					for (int j = 0; j < arrArg.length - 1; j++) {
						value = getProperty(value, arrArg[j]);
						if (value != null) {
							value = getProperty(value, arrArg[j + 1]);
						} else {
							value = null;
							break;
						}
					}// end for
				} else {
					value = PropertyUtils.getProperty(element, dataKey);
				}// end if
				dataMap.put(tdKey, fixValueForJSON(value));// 使用表格KEY进行数据保存
			}
			dataList.add(dataMap);
		}
	}

	/**
	 * 修正对象值(限用于writeJSON)
	 * 
	 * @param value
	 *            对象值
	 * @return 修正后的值
	 */
	private Object fixValueForJSON(Object value) {
		Object retObject = value;
		if (value instanceof java.util.Date) {
			// retObject = new java.sql.Timestamp(((java.util.Date) value)
			// .getTime());
			retObject = new String(value.toString());
		}
		return retObject;
	}

	/**
	 * 对PropertyUtils.getProperty的补充,可以取得Map里对应key的值
	 * 
	 * @param value
	 * @param arg
	 * @return
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	private Object getProperty(Object value, String key) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Object obj = null;
		if (value instanceof Map) {// 判断value是否Map的实现,因为Map已经目标接口,不需要isAssignableFrom来判断超类
			// 如果value是Map的子类,直接取KEY
			Map m = (Map) value;
			obj = m.get(key);

		} else {
			obj = PropertyUtils.getProperty(value, key);
		}

		return obj;
	}

	/**
	 * 返回列配置信息
	 * 
	 * @return the colSet
	 */
	public String getShowCols() {

		return JSONArray.fromObject(getsetCols()).toString();
	}

	/**
	 * 必须由子类实现
	 * 
	 * @return
	 */
	public List<ColumnSet> getsetCols() {
		// 由子类实现
		return null;
	}
}
