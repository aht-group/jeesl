package org.jeesl.model.json.io.db.pg.meta;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="snapshot")
public class JsonPostgresMetaTable implements Serializable
{
	public static final long serialVersionUID=1;
	
	@JsonProperty("name")
	private String name;
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	
	@JsonProperty("constraints")
	private List<JsonPostgresMetaConstraint> constraints;
	public List<JsonPostgresMetaConstraint> getConstraints() {return constraints;}
	public void setConstraints(List<JsonPostgresMetaConstraint> constraints) {this.constraints = constraints;}
}