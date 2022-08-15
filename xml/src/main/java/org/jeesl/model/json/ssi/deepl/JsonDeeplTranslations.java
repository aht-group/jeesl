package org.jeesl.model.json.ssi.deepl;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="translations")
public class JsonDeeplTranslations implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("translations")
	private List<JsonDeeplTranslation> translations;
	public List<JsonDeeplTranslation> getTranslations() {return translations;}
	public void setTranslations(List<JsonDeeplTranslation> translations) {this.translations = translations;}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();	
		return sb.toString();
	}
}