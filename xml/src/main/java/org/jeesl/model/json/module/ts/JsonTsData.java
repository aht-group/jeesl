package org.jeesl.model.json.module.ts;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="data")
public class JsonTsData implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("id")
	private Long id;
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}
	@JsonIgnore public boolean isSetId() {return id!=null;}
	
	@JsonProperty("series")
	private JsonTsSeries series;
	public JsonTsSeries getSeries() {return series;}
	public void setSeries(JsonTsSeries series) {this.series = series;}

	@JsonProperty("localDateTime")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss.SSS")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime localDateTime;
	public LocalDateTime getLocalDateTime() {return localDateTime;}
	public void setLocalDateTime(LocalDateTime localDateTime) {this.localDateTime = localDateTime;}
	
	//Here we may introduce other patterns if required, e.g.
	// * zonedDateTime
	// * offsetDateTime
	
	@JsonProperty("value")
	private Double value;
	public Double getValue() {return value;}
	public void setValue(Double value) {this.value = value;}
	@JsonIgnore public boolean isSetValue() {return value!=null;}

	@JsonProperty("points")
	private List<JsonTsPoint> points;
	public List<JsonTsPoint> getPoints() {return points;}
	public void setPoints(List<JsonTsPoint> points) {this.points = points;}

	@JsonProperty("vbaRecord")
	private String vbaRecord;
	public String getVbaRecord() {return vbaRecord;}
	public void setVbaRecord(String vbaRecord) {this.vbaRecord = vbaRecord;}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}
}