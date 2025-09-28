package org.jeesl.model.json.io.db.tuple;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="tuple")
public class JsonTuple extends AbstractJsonTuple implements Serializable
{
	public static final long serialVersionUID=1;
	
//	public enum Agg{count,sum,min,avg,max}
	
	public JsonTuple() {}
	
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
	
	@JsonProperty("id4")
	private Long id4;
	public Long getId4() {return id4;}
	public void setId4(Long id4) {this.id4 = id4;}
	
	@JsonProperty("l1")
	private Long l1;
	public Long getL1() {return l1;}
	public void setL1(Long l1) {this.l1 = l1;}
}