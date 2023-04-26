package org.jeesl.model.json.module.news;

import java.io.Serializable;
import java.util.List;

import org.jeesl.model.json.io.label.JsonTranslation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="feed")
public class JsonNewsFeed implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("translations")
	private List<JsonTranslation> translations;
	public List<JsonTranslation> getTranslations() {return translations;}
	public void setTranslations(List<JsonTranslation> translations) {this.translations = translations;}

	@JsonProperty("id")
	private Long id;
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}
	
	@JsonProperty("items")
	private List<JsonNewsItem> items;

	public List<JsonNewsItem> getItems() {
		return items;
	}
	public void setItems(List<JsonNewsItem> items) {
		this.items = items;
	}
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}
}