package org.jeesl.model.json.system.status;

import java.io.Serializable;
import java.util.List;

import org.jeesl.model.json.io.label.JsonTranslation;
import org.jeesl.model.json.module.attribute.JsonAttributeSet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="container")
public class JsonContainer implements Serializable
{
	public static final long serialVersionUID=1;

	public JsonContainer() {}
	
	@JsonProperty("status")
	private List<JsonStatus> status;
	public List<JsonStatus> getStatus() {return status;}
	public void setStatus(List<JsonStatus> status) {this.status = status;}
	
	@JsonProperty("categories")
	private List<JsonCategory> categories;
	public List<JsonCategory> getCategories() {return categories;}
	public void setCategories(List<JsonCategory> categories) {this.categories = categories;}
	
	@JsonProperty("sectors")
	private List<JsonSector> sectors;
	public List<JsonSector> getSectors() {return sectors;}
	public void setSectors(List<JsonSector> sectors) {this.sectors = sectors;}
	
	@JsonProperty("translations")
	private List<JsonTranslation> translations;
	public List<JsonTranslation> getTranslations() {return translations;}
	public void setTranslations(List<JsonTranslation> translations) {this.translations = translations;}
	
	@JsonProperty("attributeSet")
	private JsonAttributeSet attributeSet;
	public JsonAttributeSet getAttributeSet() {return attributeSet;}
	public void setAttributeSet(JsonAttributeSet attributeSet) {this.attributeSet = attributeSet;}
}