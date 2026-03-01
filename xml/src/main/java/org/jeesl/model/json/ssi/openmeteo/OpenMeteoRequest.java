package org.jeesl.model.json.ssi.openmeteo;

import java.util.List;

import org.jeesl.model.json.module.calendar.JsonCalendarTimezone;
import org.jeesl.model.json.system.status.JsonScope;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName(value="forecast")
public class OpenMeteoRequest
{
	@JsonProperty("latitude")
	private double latitude;
	public double getLatitude() {return latitude;}
	public void setLatitude(double latitude) {this.latitude = latitude;}

	@JsonProperty("longitude")
	private double longitude;
	public double getLongitude() {return longitude; }
	public void setLongitude(double longitude) {this.longitude = longitude;}

	@JsonProperty("timezone")
	private JsonCalendarTimezone timezone;
	public JsonCalendarTimezone getTimezone() {return timezone;}
	public void setTimezone(JsonCalendarTimezone timezone) {this.timezone = timezone;}

	@JsonProperty("scopes")
	private List<JsonScope> scopes;
	public List<JsonScope> getScopes() {return scopes;}
	public void setScopes(List<JsonScope> scopes) {this.scopes = scopes;}
	
}
