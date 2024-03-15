package org.jeesl.model.json.io.db.tuple.container;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.io.db.tuple.instance.JsonTuple1;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonTuples1 <T extends EjbWithId> implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("tuples")
	private List<JsonTuple1<T>> tuples;
	public List<JsonTuple1<T>> getTuples() {if(Objects.isNull(tuples)) {tuples = new ArrayList<>();} return tuples;}
	public void setTuples(List<JsonTuple1<T>> tuples) {this.tuples = tuples;}

	private boolean ejbLoaded;
	public boolean isEjbLoaded() {return ejbLoaded;}
	public void setEjbLoaded(boolean ejbLoaded) {this.ejbLoaded = ejbLoaded;}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}
}