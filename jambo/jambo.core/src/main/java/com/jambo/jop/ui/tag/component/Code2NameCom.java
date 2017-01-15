package com.jambo.jop.ui.tag.component;

import com.jambo.jop.infrastructure.db.DBAccessUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author He Kun
 * 
 */
public class Code2NameCom {//extends Component {

	private static Logger log = LoggerFactory.getLogger(Code2NameCom.class);

	private String definition, code, dbFlag, split;

	private DBAccessUser user;

//	public Code2NameCom(ValueStack valueStack) {
//		super(valueStack);
//	}

	public String getSplit() {
		return split;
	}

	public void setSplit(String split) {
		this.split = split;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDbFlag() {
		return dbFlag;
	}

	public void setDbFlag(String dbFlag) {
		this.dbFlag = dbFlag;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public DBAccessUser getUser() {
		return user;
	}

	public void setUser(DBAccessUser user) {
		this.user = user;
	}

    /*
	public boolean end(Writer writer, String body) {
		if (definition != null && definition.trim().length() > 0 && code != null && code.trim().length() > 0) {

			String codeValue = (String) getStack().findValue(code, String.class);

			// 如果没找到codeValue就没必要进行转换
			if (codeValue == null) {
				return super.end(writer, "");
			}
			String dbFlagValue = dbFlag != null ? (String) getStack().findValue(dbFlag, String.class) : null;

			if (dbFlagValue == null) {
				dbFlagValue = user.getDbFlag();
			}

			try {

				if (dbFlagValue == null) {
					dbFlagValue = CoreConfigInfo.COMMON_DB_NAME;
				}
				String[] codeValues;

				// 添加了split字段对codeValue进行分割,对分割的字符进行code2name
				if (split != null) {
					codeValues = StringUtils.split(codeValue, split);
				} else {
					codeValues = new String[] { codeValue };
				}
				StringBuffer codeNames = new StringBuffer();
				for (int i = 0; i < codeValues.length; i++) {
					// 做request生命周期内的缓存--start-------
					HttpServletRequest request = (HttpServletRequest)this.getStack().findValue("request");
					String codeName = (String)request.getAttribute("JOPCode2Name:" + definition + codeValues[i]);
					if (codeName == null) {
						codeName = Code2NameUtils.code2Name(definition, codeValues[i], dbFlagValue);
						request.setAttribute("JOPCode2Name:" + definition + codeValues[i], codeName); 
					}
					// 做request生命周期内的缓存--end---------

					if (codeName == null) {
						codeName = codeValues[i];
					}
					codeNames.append(codeName);
					if (split != null && i != codeValues.length - 1) {
						codeNames.append(split);
					}
				}

				writer.write(codeNames.toString());
			} catch (Exception e) {
				e.printStackTrace();
				if (log.isErrorEnabled())
					log.error(e.getMessage(), e);
				return super.end(writer, String.valueOf(codeValue));
			}
		}
		return super.end(writer, "");
	}
       */
}
