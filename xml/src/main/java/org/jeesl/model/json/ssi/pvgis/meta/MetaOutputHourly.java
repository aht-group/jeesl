package org.jeesl.model.json.ssi.pvgis.meta;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MetaOutputHourly
{
	private String type;
	public String getType() { return type; }
	public void setType(String type) { this.type = type; }

	private String timestamp;
	public String getTimestamp() { return timestamp; }
	public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

	@JsonProperty("variables")
	private Map<String, MetaVariable> variables;
	public Map<String, MetaVariable> getVariables() { return variables; }
	public void setVariables(Map<String, MetaVariable> variables) { this.variables = variables; }
}