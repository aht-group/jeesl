package org.jeesl.model.json.ssi.openmeteo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName(value="observation")
public class OpenMeteoObservation
{
	@JsonProperty("latitude")
	private double latitude;
	public double getLatitude() {return latitude;}
	public void setLatitude(double latitude) {this.latitude = latitude;}

	@JsonProperty("longitude")
	private double longitude;
	public double getLongitude() {return longitude; }
	public void setLongitude(double longitude) {this.longitude = longitude;}

	@JsonProperty("generationtime_ms")
	private double generationtimeMs;
	public double getGenerationtimeMs() {return generationtimeMs;}
	public void setGenerationtimeMs(double generationtimeMs) {this.generationtimeMs = generationtimeMs;}
	
	@JsonProperty("utc_offset_seconds")
	private int utcOffsetSeconds;
	public int getUtcOffsetSeconds() {return utcOffsetSeconds;}
	public void setUtcOffsetSeconds(int utcOffsetSeconds) {this.utcOffsetSeconds = utcOffsetSeconds;}

	@JsonProperty("timezone")
	private String timezone;
	public String getTimezone() {return timezone;}
	public void setTimezone(String timezone) {this.timezone = timezone;}

	@JsonProperty("timezone_abbreviation")
	private String timezoneAbbreviation;
	public void setTimezoneAbbreviation(String timezoneAbbreviation) {this.timezoneAbbreviation = timezoneAbbreviation;}
	public String getTimezoneAbbreviation() {return timezoneAbbreviation;}

	@JsonProperty("elevation")
	private double elevation;
	public void setElevation(double elevation) {this.elevation = elevation;}
	public double getElevation() {return elevation;}

	@JsonProperty("hourly_units")
	private OpenMeteoHourlyUnits units;
	public OpenMeteoHourlyUnits getUnits() {return units;}
	public void setUnits(OpenMeteoHourlyUnits units) {this.units = units;}

	@JsonProperty("hourly")
	private OpenMeteoHourlyData data;
	public OpenMeteoHourlyData getData() {return data;}
	public void setData(OpenMeteoHourlyData data) {this.data = data;}
}
