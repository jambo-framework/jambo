package com.jambo.jop.ui.jqtable;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Page {

	public static final Page EMPTY = new Page(-1, -1, -1);
	
	/**
	 * 总记录数
	 */
	private int rows;
	
	/**
	 * 页面显示记录数(条数)
	 */
	private int size;
	
	/**
	 * 当前页码
	 */
	private int current;
	
	/**
	 * 总页数
	 */
	private int numbers;

	private final int first = 1;
	/**
	 * 页配置对象
	 * @param rows 所有记录总数
	 * @param size 页内记录数
	 * @param current 当然页码
	 */
	public Page(int rows, int size, int current) {
		this.rows = rows;
		this.size = size;
		this.current = current;

		init();
	}

	private void init() {
		initNumbers();
		rangeOfCurrent();
	}

	private void initNumbers() {
		numbers = (int) Math.ceil((double) rows / size);
	}

	private void rangeOfCurrent() {
		current = current < first ? first : current > numbers ? numbers : current;
	}

	/**
	 * 返回总记录数
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * 返回页面显示记录数
	 */
	public int getSize() {
		return size;
	}

	/**
	 * 返回当前页码
	 */
	public int getCurrent() {
		return current;
	}

	/**
	 * 返回总页数
	 */
	public int getNumbers() {
		return numbers;
	}

	/**
	 * 返回第一页
	 */
	public int getFirst() {
		return first;
	}

	/**
	 * 返回最后页,参考 {@link #getNumbers()}方法
	 */
	public int getLast() {
		return getNumbers();
	}

	/**
	 * 是否有下一页
	 */
	public boolean isNext() {
		return getCurrent() < getLast();
	}

	/**
	 * 是否有上一页
	 */
	public boolean isPrevious() {
		return getCurrent() > getFirst();
	}

	/**
	 * 取查询数据库时的 org.hibernate.Query.setFirstResult() 方法的参数
	 */
	public int getFirstResult() {
		return (current - 1) * size;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @param current the current to set
	 */
	public void setCurrent(int current) {
		this.current = current;
	}

}
