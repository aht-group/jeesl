package org.jeesl.model.json.ssi.postgres;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="analysis")
public class JsonPostgresAnalysis implements Serializable
{
	public static final long serialVersionUID=1;
	
	
	
	@JsonProperty("Plan")
	private JsonPostgresPlan plan;
	public JsonPostgresPlan getPlan() {return plan;}
	public void setPlan(JsonPostgresPlan plan) {this.plan = plan;}
	
	@JsonProperty("Triggers")
	private List<JsonPostgresTrigger> triggers;
	public List<JsonPostgresTrigger> getTriggers() {return triggers;}
	public void setTriggers(List<JsonPostgresTrigger> triggers) {this.triggers = triggers;}

}