package org.jeesl.model.json.io.db.tuple;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="tuples")
public class JsonTuples extends AbstractJsonTuple implements Serializable
{
	public static final long serialVersionUID=1;
	
//	public enum Agg{count,sum,min,avg,max}
	
	public JsonTuples() {}
	
	@JsonProperty("tuples")
	private List<JsonTuple> tuples;
	public List<JsonTuple> getTuples() {return tuples;}
	public void setTuples(List<JsonTuple> tuples) {this.tuples = tuples;}
}