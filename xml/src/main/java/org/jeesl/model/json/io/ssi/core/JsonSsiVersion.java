package org.jeesl.model.json.io.ssi.core;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="version")
public class JsonSsiVersion implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("code")
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}
	
	@JsonProperty("fqcn")
	private String fqcn;
	public String getFqcn() {return fqcn;}
	public void setFqcn(String fqcn) {this.fqcn = fqcn;}

	@JsonProperty("version")
	private String version;
	public String getVersion() {return version;}
	public void setVersion(String version) {this.version = version;}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();	
		return sb.toString();
	}
}