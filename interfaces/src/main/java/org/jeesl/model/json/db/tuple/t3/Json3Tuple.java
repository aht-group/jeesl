package org.jeesl.model.json.db.tuple.t3;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.io.db.tuple.AbstractJsonTuple;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Json3Tuple <A extends EjbWithId, B extends EjbWithId, C extends EjbWithId> extends AbstractJsonTuple
{
	public static final long serialVersionUID=1;

	@JsonIgnore
	private A ejb1;
	public A getEjb1() {return ejb1;}
	public void setEjb1(A ejb1) {this.ejb1=ejb1;}
	
	@JsonIgnore
	private B ejb2;
	public B getEjb2() {return ejb2;}
	public void setEjb2(B ejb2) {this.ejb2=ejb2;}
	
	@JsonIgnore
	private C ejb3;
	public C getEjb3() {return ejb3;}
	public void setEjb3(C ejb3) {this.ejb3=ejb3;}
	
	@JsonProperty("id1")
	private Long id1;
	public Long getId1() {return id1;}
	public void setId1(Long id1) {this.id1 = id1;}
	
	@JsonProperty("id2")
	private Long id2;
	public Long getId2() {return id2;}
	public void setId2(Long id2) {this.id2 = id2;}
	
	@JsonProperty("id3")
	private Long id3;
	public Long getId3() {return id3;}
	public void setId3(Long id3) {this.id3 = id3;}
	
	@JsonProperty("gi1")
	private Integer gi1;
	public Integer getGi1() {return gi1;}
	public void setGi1(Integer gi1) {this.gi1 = gi1;}
}