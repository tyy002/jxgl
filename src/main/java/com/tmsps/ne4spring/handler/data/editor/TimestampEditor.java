package com.tmsps.ne4spring.handler.data.editor;

import java.sql.Timestamp;
import org.springframework.beans.propertyeditors.PropertiesEditor;

public class TimestampEditor extends PropertiesEditor {
	public void setAsText(String text) throws IllegalArgumentException {
		if ((text == null) || (text.equals(""))) {
			return;
		}
		setValue(Timestamp.valueOf(text));
	}
}
