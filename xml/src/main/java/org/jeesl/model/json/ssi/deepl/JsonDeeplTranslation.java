package org.jeesl.model.json.ssi.deepl;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="translations")
public class JsonDeeplTranslation implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("detected_source_language")
	private String sourceLang;
	public String getSourceLang() {return sourceLang;}
	public void setSourceLang(String sourceLang) {this.sourceLang = sourceLang;}

	@JsonProperty("text")
	private String text;
	public String getText() {return text;}
	public void setText(String text) {this.text = text;}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();	
		return sb.toString();
	}
}