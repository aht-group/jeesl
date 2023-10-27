package org.jeesl.model.json.io.ssi.core;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="nat")
public class JsonSsiNat implements Serializable
{
	public static final long serialVersionUID=1;
	
	@JsonProperty("id")
	private Long id;
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	@JsonProperty("listenHost")
	private JsonSsiHost listenHost;
	public JsonSsiHost getListenHost() {return listenHost;}
	public void setListenHost(JsonSsiHost listenHost) {this.listenHost = listenHost;}

	@JsonProperty("listenPort")
	private Integer listenPort;
	public Integer getListenPort() {return listenPort;}
	public void setListenPort(Integer listenPort) {this.listenPort = listenPort;}

	@JsonProperty("destinationHost")
	private JsonSsiHost destinationHost;
	public JsonSsiHost getDestinationHost() {return destinationHost;}
	public void setDestinationHost(JsonSsiHost destinationHost) {this.destinationHost = destinationHost;}
	
	@JsonProperty("destinationPort")
	private Integer destinationPort;
	public Integer getDestinationPort() {return destinationPort;}
	public void setDestinationPort(Integer destinationPort) {this.destinationPort = destinationPort;}
}