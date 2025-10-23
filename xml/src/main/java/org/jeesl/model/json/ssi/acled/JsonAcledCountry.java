package org.jeesl.model.json.ssi.acled;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="country")
public class JsonAcledCountry implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("id")
	private Long id;
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	@JsonProperty("numcode")
	private Long numcode;
	public Long getNumcode() {return id;}
	public void setNumcode(Long id) {this.id = id;}

	@JsonProperty("iso3")
	private String iso3;
	public String getIso3() {return iso3;}
	public void setIso3(String iso3) {this.iso3 = iso3;}

	@JsonProperty("name")
	private String name;
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}

	@JsonProperty("nicename")
	private String nicename;
	public String getNicename() {return name;}
	public void setNicename(String name) {this.name = name;}

	@JsonProperty("events")
	private Integer events;
	public Integer getEvents() {return events;}
	public void setEvents(Integer events) {this.events = events;}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}
}