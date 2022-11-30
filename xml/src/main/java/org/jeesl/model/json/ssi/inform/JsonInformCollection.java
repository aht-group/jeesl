package org.jeesl.model.json.ssi.inform;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="rating")
public class JsonInformCollection implements Serializable
{
	public static final long serialVersionUID=1;

	private Integer year;
	public Integer getYear() {return year;}
	public void setYear(Integer year) {this.year = year;}

	@JsonProperty("locationName")
	private String locationName;
	public String getLocationName() {return locationName;}
	public void setLocationName(String locationName) {this.locationName = locationName;}
	
	@JsonProperty("locationCountry")
	private String locationCountry;
	public String getLocationCountry() {return locationCountry;}
	public void setLocationCountry(String locationCountry) {this.locationCountry = locationCountry;}
	
	@JsonProperty("locationArea")
	private String locationArea;
	public String getLocationArea() {return locationArea;}
	public void setLocationArea(String locationArea) {this.locationArea = locationArea;}

	@JsonProperty("ratings")
	private List<JsonInformRisk> ratings;
	public List<JsonInformRisk> getRatings() {return ratings;}
	public void setRatings(List<JsonInformRisk> ratings) {this.ratings = ratings;}
	
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();	
		return sb.toString();
	}
}