package org.jeesl.model.json.io.db.tuple.instance;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonTuple2 <A extends EjbWithId, B extends EjbWithId> extends JsonTuple1<A>
{
	public static final long serialVersionUID=1;

	@JsonProperty("id2")
	private Long id2;
	public Long getId2() {return id2;}
	public void setId2(Long id2) {this.id2 = id2;}
	
	@JsonIgnore
	private B ejb2;
	public B getEjb2() {return ejb2;}
	public void setEjb2(B ejb2) {this.ejb2=ejb2;}
}