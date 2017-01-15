//$Id: ClassName.java,v 1.1 2006/09/08 11:25:52 liaojinbo Exp $
package com.jambo.tools.codegen.hbm2java;

public class ClassName {
	private String fullyQualifiedName = null;
	private String packageName = null;
	private String name = null;
	private boolean primitive = false;
	private boolean isArray = false;

	public void setFullyQualifiedName(String fullyQualifiedName) {
		setFullyQualifiedName(fullyQualifiedName, false);
	}

	public void setFullyQualifiedName(String fullyQualifiedName, boolean isPrimitive) {
		this.fullyQualifiedName = fullyQualifiedName;
		primitive = isPrimitive;
		if (!isPrimitive) {

			if (fullyQualifiedName != null) {

				int lastDot = fullyQualifiedName.lastIndexOf(".");
				if (lastDot < 0) {
					name = fullyQualifiedName;
					packageName = null;
				} else {
					name = fullyQualifiedName.substring(lastDot + 1);
					packageName = fullyQualifiedName.substring(0, lastDot);
				}
			} else {
				name = fullyQualifiedName;
				packageName = null;
			}
		} else {
			name = fullyQualifiedName;
			packageName = null;
		}

	}

	public String getFullyQualifiedName() {
		return this.fullyQualifiedName;
	}

	public String getPackageName() {
		return this.packageName;
	}

	public String getName() {
		return this.name;
	}

	public boolean inJavaLang() {
		return "java.lang".equals(packageName);
	}

	public boolean inSamePackage(ClassName other) {
		return other.packageName == this.packageName
			|| (other.packageName != null && other.packageName.equals(this.packageName));
	}

	public boolean equals(Object other) {
		ClassName otherClassName = (ClassName) other;
		return otherClassName.fullyQualifiedName.equals(fullyQualifiedName);
	}
	/**
	 * Method isPrimitive.
	 * @return boolean
	 */
	public boolean isPrimitive() {
		return primitive;
	}
	/**
	 * Method setIsArray.
	 * @param b
	 */
	public void setIsArray(boolean b) {
		isArray = b;
	}
	/**
	 * Method isArray.
	 * @return boolean
	 */
	public boolean isArray() {
		return isArray;
	}

	public String toString() {
		return getFullyQualifiedName();
	}

}
