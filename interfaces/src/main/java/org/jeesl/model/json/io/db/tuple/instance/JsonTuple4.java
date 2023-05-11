package org.jeesl.model.json.io.db.tuple.instance;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonTuple4 <A extends EjbWithId, B extends EjbWithId, C extends EjbWithId, D extends EjbWithId> extends JsonTuple3<A,B,C>
{
	public static final long serialVersionUID=1;

	@JsonIgnore
	private D ejb4;
	public D getEjb4() {return ejb4;}
	public void setEjb4(D ejb4) {this.ejb4=ejb4;}
		
	@JsonProperty("id4")
	private Long id4;
	public Long getId4() {return id4;}
	public void setId4(Long id4) {this.id4 = id4;}
}