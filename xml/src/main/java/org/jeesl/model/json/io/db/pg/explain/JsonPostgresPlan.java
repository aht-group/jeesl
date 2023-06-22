package org.jeesl.model.json.io.db.pg.explain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="artifact")
public class JsonPostgresPlan implements Serializable
{
	public static final long serialVersionUID=1;
	
	@JsonProperty("Node Type")
	private String nodeType;
	public String getNodeType() {return nodeType;}
	public void setNodeType(String nodeType) {this.nodeType = nodeType;}
	
	
}