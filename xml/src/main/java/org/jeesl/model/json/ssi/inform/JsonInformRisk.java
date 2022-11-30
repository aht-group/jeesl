package org.jeesl.model.json.ssi.inform;

import java.io.Serializable;

import org.jeesl.model.json.system.status.JsonCategory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="risk")
public class JsonInformRisk implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("collection")
	private JsonInformCollection collection;
	public JsonInformCollection getCollection() {return collection;}
	public void setCollection(JsonInformCollection collection) {this.collection = collection;}

	@JsonProperty("category")
	private JsonCategory category;
	public JsonCategory getCategory() {return category;}
	public void setCategory(JsonCategory category) {this.category = category;}
	
	@JsonProperty("value")
	private Double value;
	public Double getValue() {return value;}
	public void setValue(Double value) {this.value = value;}
	
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();	
		return sb.toString();
	}
}