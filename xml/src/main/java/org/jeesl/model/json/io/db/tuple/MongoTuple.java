package org.jeesl.model.json.io.db.tuple;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="tuple")
public class MongoTuple extends AbstractJsonTuple implements Serializable
{
	public static final long serialVersionUID=1;
	
	public MongoTuple() {}
	
	@JsonProperty("_id")
	private JsonTuple tuple;
	public JsonTuple getTuple() {return tuple;}
	public void setTuple(JsonTuple tuple) {this.tuple = tuple;}
	
	
	
}