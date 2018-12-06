package com.sinovatech.search.utils.web;

import org.springframework.beans.propertyeditors.CustomNumberEditor;

public class LongEditor extends CustomNumberEditor {
	public LongEditor() {
		super(Long.class, true);
	}

	@Override
	public void setAsText(String text) {
		if ((text == null) || text.trim().equals("")) {
			setValue(null);
		} else {
			Long value = null;
			try {
				// 按照标准的数字格式尝试转换
				value = Long.parseLong(text);
			} catch (NumberFormatException e) {
				// 尝试去除逗号 然后再转换
				text = text.replace(",", "");
				value = Long.parseLong(text);
			}
			// 转好之后将值返给被映射的属性
			setValue(value);
		}
	}

}
