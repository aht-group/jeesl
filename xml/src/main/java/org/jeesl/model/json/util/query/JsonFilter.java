package org.jeesl.model.json.util.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="filter")
public class JsonFilter implements Serializable
{
	public static final long serialVersionUID=1;
	public enum Type{idList,idSingle,date}
	
	@JsonProperty("localeCode")
	private String localeCode;
	public String getLocaleCode() {return localeCode;}
	public void setLocaleCode(String localeCode) {this.localeCode = localeCode;}

	@JsonProperty("items")
	private List<JsonFilterItem> items;
	public List<JsonFilterItem> getItems() {if(items==null) {items = new ArrayList<JsonFilterItem>();}return items;}
	public void setItems(List<JsonFilterItem> items) {this.items = items;}


	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		
		return sb.toString();
	}
}