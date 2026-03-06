package org.jeesl.model.json.ssi.openmeteo;

import java.time.LocalDate;
import java.util.List;

import org.jeesl.model.json.module.calendar.JsonCalendarTimezone;
import org.jeesl.model.json.system.status.JsonScope;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName(value="request")
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
	
	@JsonProperty("dates")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonDeserialize(contentUsing = LocalDateDeserializer.class)
	@JsonSerialize(contentUsing = LocalDateSerializer.class)
	private List<LocalDate> dates;
	public List<LocalDate> getDates() {return dates;}
	public void setDates(List<LocalDate> dates) {this.dates = dates;}
}