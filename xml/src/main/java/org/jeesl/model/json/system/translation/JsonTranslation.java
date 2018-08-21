package org.jeesl.model.json.system.translation;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="translation")
public class JsonTranslation implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("entity")
	private String entity;
	public String getEntity() {return entity;}
	public void setEntity(String entity) {this.entity = entity;}
	@JsonIgnore public boolean isSetEntity() {return entity!=null;}

	@JsonProperty("code")
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}
	@JsonIgnore public boolean isSetCode() {return code!=null;}
	
	@JsonProperty("xpath")
	private String xpath;
	public String getXpath() {return xpath;}
	public void setXpath(String xpath) {this.xpath = xpath;}
	
	@JsonProperty("multilang")
	private Map<String,String> multiLang;
	
	public Map<String, String> getMultiLang() {
		return multiLang;
	}
	public void setMultiLang(Map<String, String> multiLang) {
		this.multiLang = multiLang;
	}
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		
		return sb.toString();
	}
}