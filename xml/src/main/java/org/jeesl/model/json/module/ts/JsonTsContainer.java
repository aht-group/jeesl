package org.jeesl.model.json.module.ts;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="series")
public class JsonTsContainer implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("series")
	private List<JsonTsSeries> series;
	public List<JsonTsSeries> getSeries() {return series;}
	public void setSeries(List<JsonTsSeries> series) {this.series = series;}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}
}