package org.jeesl.model.json.db.tuple.two;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.io.db.tuple.AbstractJsonTuple;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Json2Tuple <X extends EjbWithId, Y extends EjbWithId> extends AbstractJsonTuple
{
	public static final long serialVersionUID=1;

	@JsonIgnore
	private X ejb1;
	public X getEjb1() {return ejb1;}
	public void setEjb1(X ejb1) {this.ejb1=ejb1;}
	
	@JsonIgnore
	private Y ejb2;
	public Y getEjb2() {return ejb2;}
	public void setEjb2(Y ejb2) {this.ejb2=ejb2;}
	
	@JsonProperty("id1")
	private Long id1;
	public Long getId1() {return id1;}
	public void setId1(Long id1) {this.id1 = id1;}
	
	@JsonProperty("id2")
	private Long id2;
	public Long getId2() {return id2;}
	public void setId2(Long id2) {this.id2 = id2;}
}