
package org.jeesl.model.json.module.workflow;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="workflow")
public class JsonWorkflow implements Serializable
{
    private final static long serialVersionUID = 1L;
 
	@JsonProperty("id")
	private Long id;
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}
	
	@JsonProperty("currentStage")
	private JsonWorkflowStage currentStage;
	public JsonWorkflowStage getCurrentStage() {return currentStage;}
	public void setCurrentStage(JsonWorkflowStage currentStage) {this.currentStage = currentStage;}


}