package org.jeesl.model.json.io.fr;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="frStorage")
public class JsonFrStorage implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("id")
	private Long id;
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	@JsonProperty("containers")
	private List<JsonFrContainer> containers;
	public List<JsonFrContainer> getContainers() {return containers;}
	public void setContainers(List<JsonFrContainer> containers) {this.containers = containers;}
}