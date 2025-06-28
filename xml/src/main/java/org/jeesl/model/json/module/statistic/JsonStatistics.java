package org.jeesl.model.json.module.statistic;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="statistisc")
public class JsonStatistics implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("values")
	private List<JsonStatisticValue> values;

	public List<JsonStatisticValue> getValues() {
		return values;
	}
	public void setValues(List<JsonStatisticValue> values) {
		this.values = values;
	}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();	
		return sb.toString();
	}
}