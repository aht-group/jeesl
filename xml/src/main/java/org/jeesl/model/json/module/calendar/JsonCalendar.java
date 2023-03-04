
package org.jeesl.model.json.module.calendar;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="calendar")
public class JsonCalendar implements Serializable
{
    private final static long serialVersionUID = 1L;
 
	@JsonProperty("id")
	private Long id;
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}
	
	@JsonProperty("code")
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}
		
	@JsonProperty("items")
	private List<JsonCalendarItem> items;
	public List<JsonCalendarItem> getItems() {return items;}
	public void setItems(List<JsonCalendarItem> items) {this.items = items;}

}