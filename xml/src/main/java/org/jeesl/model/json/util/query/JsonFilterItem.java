package org.jeesl.model.json.util.query;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="item")
public class JsonFilterItem implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("className")
	private String className;
	public String getClassName() {return className;}
	public void setClassName(String className) {this.className = className;}

	@JsonProperty("type")
	private String type;
	public String getType() {return type;}
	public void setType(String type) {this.type = type;}

	@JsonProperty("ids")
	private List<Long> ids;
	public List<Long> getIds() {return ids;}
	public void setIds(List<Long> ids) {this.ids = ids;}
	
	@JsonProperty("date")
	private Date date;
	public Date getDate() {return date;}
	public void setDate(Date date) {this.date = date;}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		
		return sb.toString();
	}
}