package org.jeesl.model.json.io.db.pg.explain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="artifact")
public class JsonPostgresTrigger implements Serializable
{
	public static final long serialVersionUID=1;
	
	@JsonProperty("Trigger Name")
	private String triggerName;
	public String getTriggerName() {return triggerName;}
	public void setTriggerName(String triggerName) {this.triggerName = triggerName;}
	
	@JsonProperty("Constraint Name")
	private String constraintName;
	public String getConstraintName() {return constraintName;}
	public void setConstraintName(String constraintName) {this.constraintName = constraintName;}

	@JsonProperty("Time")
	private Double time;
	public Double getTime() {return time;}
	public void setTime(Double time) {this.time = time;}
	
	@JsonProperty("Calls")
	private Integer  calls;
	public Integer getCalls() {return calls;}
	public void setCalls(Integer calls) {this.calls = calls;}
	
}