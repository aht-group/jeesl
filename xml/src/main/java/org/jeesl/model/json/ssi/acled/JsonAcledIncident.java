package org.jeesl.model.json.ssi.acled;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="response")
public class JsonAcledIncident implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("id")
	private Long id;
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	@JsonProperty("mainType")
	private String mainType;
	public String getMainType() {return mainType;}
	public void setMainType(String mainType) {this.mainType = mainType;}

	@JsonProperty("subType")
	private String subType;
	public String getSubType() {return subType;}
	public void setSubType(String subType) {this.subType = subType;}

	@JsonProperty("country")
	private JsonAcledCountry country;
	public JsonAcledCountry getCountry() {return country;}
	public void setCountry(JsonAcledCountry country) {this.country = country;}

	@JsonProperty("admin1")
	private JsonAcledAdmin1 admin1;
	public JsonAcledAdmin1 getAdmin1() {return admin1;}
	public void setAdmin1(JsonAcledAdmin1 admin1) {this.admin1 = admin1;}

	@JsonProperty("location")
	private String location;
	public String getLocation() {return location;}
	public void setLocation(String location) {this.location = location;}

	@JsonProperty("admin2")
	private String admin2;
	public String getAdmin2() {return admin2;}
	public void setAdmin2(String admin2) {this.admin2 = admin2;}

	@JsonProperty("admin3")
	private String admin3;
	public String getAdmin3() {return admin3;}
	public void setAdmin3(String admin3) {this.admin3 = admin3;}

	@JsonProperty("date")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate date;
	public LocalDate getDate() {return date;}
	public void setDate(LocalDate date) {this.date = date;}

	@JsonProperty("latitude")
	private Double latitude;
	public Double getLatitude() {return latitude;}
	public void setLatitude(Double latitude) {this.latitude = latitude;}

	@JsonProperty("longitude")
	private Double longitude;
	public Double getLongitude() {return longitude;}
	public void setLongitude(Double longitude) {this.longitude = longitude;}

	@JsonProperty("actor1")
	private JsonAcledActor actor1;
	public JsonAcledActor getActor1() {return actor1;}
	public void setActor1(JsonAcledActor actor1) {this.actor1 = actor1;}

	@JsonProperty("actor2")
	private JsonAcledActor actor2;
	public JsonAcledActor getActor2() {return actor2;}
	public void setActor2(JsonAcledActor actor2) {this.actor2 = actor2;}

	@JsonProperty("sources")
	private List<JsonAcledSource> sources;
	public List<JsonAcledSource> getSources() {return sources;}
	public void setSources(List<JsonAcledSource> sources) {this.sources = sources;}

	@JsonProperty("description")
	private String description;
	public String getDescription() {return description;}
	public void setDescription(String description) {this.description = description;}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}
}