package com.jambo.jop.ui.init.convert;

import java.text.SimpleDateFormat;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

/**
 * 格式转换类(java.util.Date)
 * 
 * @author baiming 2006-8-31
 */
public class UtilDateConverter implements Converter {

	final static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");

	final static SimpleDateFormat datetimeFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private Object defaultValue = null;

	private boolean useDefault = true;

	public UtilDateConverter() {
		this.defaultValue = null;
		this.useDefault = false;
	}

	public UtilDateConverter(Object defaultValue) {

		this.defaultValue = defaultValue;
		this.useDefault = true;

	}

	public Object convert(Class type, Object value) {

		if (value == null || value.toString().trim().length() == 0) {
			if (useDefault) {
				return (defaultValue);
			} else {
				throw new ConversionException("No value specified");
			}
		}

		if (value instanceof java.util.Date) {
			return (value);
		}

		if (value instanceof String) {
			if (value.toString().length() == 10) {
				try {
					return dateFormat.parse((String) value);
				} catch (Exception ex) {
					throw new ConversionException(
							"unexpected format yyyy-MM-dd", ex);
				}
			}else{
				try {
					return datetimeFormat.parse((String) value);
				} catch (Exception ex) {
					throw new ConversionException(
							"unexpected format yyyy-MM-dd HH:mm:ss", ex);
				}
			}
		} else {
			throw new ConversionException("unexpected type:" + value.getClass());
		}
	}
}
