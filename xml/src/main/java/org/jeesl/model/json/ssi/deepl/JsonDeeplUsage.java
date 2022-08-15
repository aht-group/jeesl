package org.jeesl.model.json.ssi.deepl;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="usage")
public class JsonDeeplUsage implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("character_count")
	private Long count;
	public Long getCount() {return count;}
	public void setCount(Long count) {this.count = count;}
	
	@JsonProperty("character_limit")
	private Long limit;
	public Long getLimit() {return limit;}
	public void setLimit(Long limit) {this.limit = limit;}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();	
		return sb.toString();
	}
}