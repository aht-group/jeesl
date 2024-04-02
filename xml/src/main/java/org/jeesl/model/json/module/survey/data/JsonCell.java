package org.jeesl.model.json.module.survey.data;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="matrix")
public class JsonCell implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("id")
	private Long id;
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	@JsonProperty("row")
	private Long row;
	public Long getRow() {return row;}
	public void setRow(Long row) {this.row = row;}

	@JsonProperty("column")
	private Long column;
	public Long getColumn() {return column;}
	public void setColumn(Long column) {this.column = column;}
	
	@JsonProperty("answer")
	private JsonAnswer answer;
	public JsonAnswer getAnswer() {return answer;}
	public void setAnswer(JsonAnswer answer) {this.answer = answer;}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();	
		return sb.toString();
	}
}