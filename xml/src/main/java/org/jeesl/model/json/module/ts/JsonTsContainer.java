package org.jeesl.model.json.module.ts;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="container")
public class JsonTsContainer implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("series")
	private List<JsonTsSeries> series;
	public List<JsonTsSeries> getSeries() {return series;}
	public void setSeries(List<JsonTsSeries> series) {this.series = series;}
	
	@JsonProperty("multiPoints")
	private List<JsonTsMultipoint> multiPoints;
	public List<JsonTsMultipoint> getMultiPoints() {return multiPoints;}
	public void setMultiPoints(List<JsonTsMultipoint> multiPoints) {this.multiPoints = multiPoints;}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}
}