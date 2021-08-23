package org.jeesl.model.json.util.time;

import java.io.Serializable;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="day")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonDay implements Serializable,EjbWithId
{
	public static final long serialVersionUID=1;

	@JsonProperty("id")
	private long id;
	public long getId() {return id;}
	public void setId(long id) {this.id = id;}

	@JsonProperty("nr")
	private int nr;
	public int getNr() {return nr;}
	public void setNr(int nr) {this.nr = nr;}
	
	private Integer month;
	public Integer getMonth() {return month;}
	public void setMonth(Integer month) {this.month = month;}
	
	private Integer year;
	public Integer getYear() {return year;}
	public void setYear(Integer year) {this.year = year;}

	@JsonProperty("name")
	private String name;
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	
	@JsonProperty("code")
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}

	@JsonProperty("weekend")
	private boolean weekend;
	public boolean isWeekend() {return weekend;}
	public void setWeekend(boolean weekend) {this.weekend = weekend;}
	
	@JsonProperty("today")
	private boolean today;
	public boolean isToday() {return today;}
	public void setToday(boolean today) {this.today = today;}
	
	@Override public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(id).append("]");
		sb.append(" ").append(year).append(".").append(month).append(".").append(nr);
		return sb.toString();
	}
	
	@Override public boolean equals(Object object){return (object instanceof JsonDay) ? id == ((JsonDay) object).getId() : (object == this);}
	@Override public int hashCode(){return new HashCodeBuilder(23,43).append(id).toHashCode();}
}