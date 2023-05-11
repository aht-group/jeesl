package org.jeesl.model.json.io.db.tuple.instance;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonTuple3 <A extends EjbWithId, B extends EjbWithId, C extends EjbWithId> extends JsonTuple2<A,B>
{
	public static final long serialVersionUID=1;
	
	@JsonProperty("id3")
	private Long id3;
	public Long getId3() {return id3;}
	public void setId3(Long id3) {this.id3 = id3;}
	
	@JsonIgnore
	private C ejb3;
	public C getEjb3() {return ejb3;}
	public void setEjb3(C ejb3) {this.ejb3=ejb3;}
	
	
	@JsonProperty("gi1")
	private Integer gi1;
	public Integer getGi1() {return gi1;}
	public void setGi1(Integer gi1) {this.gi1 = gi1;}
}