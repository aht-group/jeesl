package org.jeesl.model.json.ssi.inform;

import java.io.Serializable;

import org.jeesl.model.json.system.status.JsonCategory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="rating")
public class JsonInformRating implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("risk")
	private JsonInformRisk risk;
	public JsonInformRisk getRisk() {return risk;}
	public void setRisk(JsonInformRisk risk) {this.risk = risk;}

	@JsonProperty("value")
	private Double value;
	public Double getValue() {return value;}
	public void setValue(Double value) {this.value = value;}

	@JsonProperty("category")
	private JsonCategory category;
	public JsonCategory getCategory() {return category;}
	public void setCategory(JsonCategory category) {this.category = category;}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();	
		return sb.toString();
	}
}