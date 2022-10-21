package org.jeesl.model.json.ssi.acled;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="data")
@JsonPropertyOrder({"id", "iso", "date", "year" })
public class JsonAcledData implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("data_id")
	private Long id;
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	@JsonProperty("iso")
	private String iso;
	public String getIso() {return iso;}
	public void setIso(String iso) {this.iso = iso;}

	@JsonProperty("event_date")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate date;
	public LocalDate getDate() {return date;}
	public void setDate(LocalDate date) {this.date = date;}
	
	@JsonProperty("year")
	private Integer year;
	public Integer getYear() {return year;}
	public void setYear(Integer year) {this.year = year;}
	
	@JsonProperty("event_type")
	private String mainType;
	public String getMainType() {return mainType;}
	public void setMainType(String mainType) {this.mainType = mainType;}
	
	@JsonProperty("sub_event_type")
	private String subType;
	public String getSubType() {return subType;}
	public void setSubType(String subType) {this.subType = subType;}
	
	
	@JsonProperty("location")
	private String location;
	public String getLocation() {return location;}
	public void setLocation(String location) {this.location = location;}
	
	@JsonProperty("notes")
	private String notes;
	public String getNotes() {return notes;}
	public void setNotes(String notes) {this.notes = notes;}
	
	@JsonProperty("iso3")
	private String iso3;
	public String getIso3() {return iso3;}
	public void setIso3(String iso3) {this.iso3 = iso3;}
	
	
	
	
	@JsonProperty("country")
	private String country;
	public String getCountry() {return country;}
	public void setCountry(String country) {this.country = country;}
	
	@JsonProperty("admin1")
	private String admin1;
	public String getAdmin1() {return admin1;}
	public void setAdmin1(String admin1) {this.admin1 = admin1;}

	@JsonProperty("event_count")
	private int count;
	public int getCount() {return count;}
	public void setCount(int count) {this.count = count;}
	

	
	@JsonProperty("latitude")
	private Double latitude;
	public Double getLatitude() {return latitude;}
	public void setLatitude(Double latitude) {this.latitude = latitude;}

	@JsonProperty("longitude")
	private Double longitude;
	public Double getLongitude() {return longitude;}
	public void setLongitude(Double longitude) {this.longitude = longitude;}
	
	
//	@JsonProperty("actor_type_id")
//	private Integer actorTypeCode;
//	public Integer getActorTypeCode() {return actorTypeCode;}
//	public void setActorTypeCode(Integer actorTypeCode) {this.actorTypeCode = actorTypeCode;}

//	@JsonProperty("actor_type_name")
//	private String actorTypeName;
//	public String getActorTypeName() {return actorTypeName;}
//	public void setActorTypeName(String actorTypeName) {this.actorTypeName = actorTypeName;}
//
//	@JsonProperty("actor_name")
//	private String actor;
//	public String getActor() {return actor;}
//	public void setActor(String actor) {this.actor = actor;}
	
	@JsonProperty("actor1")
	private String actor1;
	public String getActor1() {return actor1;}
	public void setActor1(String actor1) {this.actor1 = actor1;}
	
	@JsonProperty("actor2")
	private String actor2;
	public String getActor2() {return actor2;}
	public void setActor2(String actor2) {this.actor2 = actor2;}
	
	@JsonProperty("source")
	private String source;
	public String getSource() {return source;}
	public void setSource(String source) {this.source = source;}
	
	@JsonProperty("region_name")
	private String region;
	public String getRegion() {return region;}
	public void setRegion(String region) {this.region = region;}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();	
		return sb.toString();
	}
}