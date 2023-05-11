package org.jeesl.model.json.io.db.tuple.container;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.io.db.tuple.instance.JsonTuple4;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonTuples4 <A extends EjbWithId, B extends EjbWithId, C extends EjbWithId, D extends EjbWithId> implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("tuples")
	private List<JsonTuple4<A,B,C,D>> tuples;
	public List<JsonTuple4<A,B,C,D>> getTuples() {if(tuples==null){tuples = new ArrayList<JsonTuple4<A,B,C,D>>();} return tuples;}
	public void setTuples(List<JsonTuple4<A,B,C,D>> tuples) {this.tuples = tuples;}


	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		
		return sb.toString();
	}
}