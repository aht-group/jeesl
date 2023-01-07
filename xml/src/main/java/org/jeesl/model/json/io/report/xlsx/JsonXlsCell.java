package org.jeesl.model.json.io.report.xlsx;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="cell")
public class JsonXlsCell implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("column")
	private JsonXlsColumn column;
	public JsonXlsColumn getColumn() {return column;}
	public void setColumn(JsonXlsColumn column) {this.column = column;}
	
	@JsonProperty("valueString")
	private String valueString;
	public String getValueString() {return valueString;}
	public void setValueString(String valueString) {this.valueString = valueString;}
}