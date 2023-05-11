package org.jeesl.model.json.io.db.tuple.container;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.io.db.tuple.instance.JsonTuple2;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonTuples2 <X extends EjbWithId, Y extends EjbWithId> implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("tuples")
	private List<JsonTuple2<X,Y>> tuples;
	public List<JsonTuple2<X,Y>> getTuples() {if(tuples==null){tuples = new ArrayList<JsonTuple2<X,Y>>();} return tuples;}
	public void setTuples(List<JsonTuple2<X,Y>> tuples) {this.tuples = tuples;}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		
		return sb.toString();
	}
}