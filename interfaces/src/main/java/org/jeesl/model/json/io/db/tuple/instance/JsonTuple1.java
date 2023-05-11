package org.jeesl.model.json.io.db.tuple.instance;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.io.db.tuple.AbstractJsonTuple;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonTuple1 <A extends EjbWithId> extends AbstractJsonTuple
{
	public static final long serialVersionUID=1;

	@JsonProperty("id1")
	private Long id1;
	public Long getId1() {return id1;}
	public void setId1(Long id1) {this.id1 = id1;}
	
	@JsonIgnore
	private A ejb1;
	public A getEjb1() {return ejb1;}
	public void setEjb1(A ejb1) {this.ejb1=ejb1;}
}