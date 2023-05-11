package org.jeesl.model.json.io.db.tuple.container;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.io.db.tuple.instance.JsonTuple3;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonTuples3 <A extends EjbWithId, B extends EjbWithId, C extends EjbWithId> implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("tuples")
	private List<JsonTuple3<A,B,C>> tuples;
	public List<JsonTuple3<A,B,C>> getTuples() {if(tuples==null){tuples = new ArrayList<JsonTuple3<A,B,C>>();} return tuples;}
	public void setTuples(List<JsonTuple3<A,B,C>> tuples) {this.tuples = tuples;}


	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		
		return sb.toString();
	}
}