package org.jeesl.model.json.module.rmmv;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName(value="classifications")
public class JsonRmmvClassifications implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("classifications")
	private List<JsonRmmvClassification> classifications;
	public List<JsonRmmvClassification> getClassifications() {return classifications;}
	public void setClassifications(List<JsonRmmvClassification> classifications) {this.classifications = classifications;}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		
		return sb.toString();
	}
}