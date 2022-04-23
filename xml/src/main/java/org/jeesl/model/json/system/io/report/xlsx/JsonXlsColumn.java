package org.jeesl.model.json.system.io.report.xlsx;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="column")
public class JsonXlsColumn implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("nr")
	private Integer nr;
	public Integer getNr() {return nr;}
	public void setNr(Integer nr) {this.nr = nr;}
	
	@JsonProperty("key")
	private String key;
	public String getKey() {return key;}
	public void setKey(String key) {this.key = key;}

	@JsonProperty("code")
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}
}