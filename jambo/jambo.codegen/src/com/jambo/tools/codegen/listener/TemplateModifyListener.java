package com.jambo.tools.codegen.listener;

import java.util.Map;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;

import com.jambo.tools.codegen.util.TemplatesUtils;

public class TemplateModifyListener implements ModifyListener {
	Text text ;
	String value ;
	Map map ;
	
	public TemplateModifyListener(Text t, Map combo){
		text = t ;
		map = combo ;
	}

	public void modifyText(ModifyEvent e) {
		String comValue = ((Combo)e.widget).getText() ;
		if (null == comValue||comValue.trim().length()==0){
			return ;
		}
		String t = TemplatesUtils.getInstence().getString(comValue) ;
		if (null != t && t.trim().length() >0){
			text.setText(t) ;
			value = comValue ;
			return ;
		}
		if (null==value || !value.equals(comValue)){
			value = comValue ;
			text.setText(TemplatesUtils.getInstence().getFileContent((String)map.get(value))) ;
		}
	}
}
