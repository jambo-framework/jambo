package com.jambo.tools.codegen.wizards.params;

public class Field {
	private String name;
	private int sqlType;
	private Class javaType;
	private int sqlLength;
	private boolean n;
	private boolean nn;
	private boolean l;
	private boolean nm;
	private boolean e;
	private boolean nl;
	private boolean m;
	private boolean ne;
	private boolean in;
	private boolean nin;
	private boolean k;
	private boolean nk;
	
	private boolean[] selection = new boolean[12];
	public boolean isSelected(int i){
		return selection[i];
	}
	public void setSelected(int i, boolean selected){
		selection[i] = selected;
	}
	
	public boolean isIn() {
		return in;
	}
	public void setIn(boolean in) {
		this.in = in;
	}
	public boolean isK() {
		return k;
	}
	public void setK(boolean k) {
		this.k = k;
	}
	public boolean isL() {
		return l;
	}
	public void setL(boolean l) {
		this.l = l;
	}
	public boolean isM() {
		return m;
	}
	public void setM(boolean m) {
		this.m = m;
	}
	public boolean isN() {
		return n;
	}
	public void setN(boolean n) {
		this.n = n;
	}
	public boolean isNe() {
		return ne;
	}
	public void setNe(boolean ne) {
		this.ne = ne;
	}
	public boolean isNin() {
		return nin;
	}
	public void setNin(boolean nin) {
		this.nin = nin;
	}
	public boolean isNk() {
		return nk;
	}
	public void setNk(boolean nk) {
		this.nk = nk;
	}
	public boolean isNl() {
		return nl;
	}
	public void setNl(boolean nl) {
		this.nl = nl;
	}
	public boolean isNm() {
		return nm;
	}
	public void setNm(boolean nm) {
		this.nm = nm;
	}
	public boolean isNn() {
		return nn;
	}
	public void setNn(boolean nn) {
		this.nn = nn;
	}
	public Field(String name, int sqlType, Class javaType, int sqlLength){
		this.name = name;
		this.sqlType = sqlType;
		this.javaType = javaType;
		this.sqlLength = sqlLength;
	}
	public Field(String name, int sqlType){
		this.name = name;
		this.sqlType = sqlType;
	}
	public Field(String name){
		this.name = name;
	}
	public boolean isE() {
		return e;
	}
	public void setE(boolean e) {
		this.e = e;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSqlType() {
		return sqlType;
	}
	public void setSqlType(int sqlType) {
		this.sqlType = sqlType;
	}
	public Class getJavaType() {
		return javaType;
	}
	public void setJavaType(Class javaType) {
		this.javaType = javaType;
	}
	public int getSqlLength() {
		return sqlLength;
	}
	public void setSqlLength(int sqlLength) {
		this.sqlLength = sqlLength;
	}
	
}
