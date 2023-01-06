package org.jeesl.model.json.module.ts;
import java.io.Serializable;
import java.util.List;

import org.jeesl.model.json.system.status.JsonBridge;
import org.jeesl.model.json.system.status.JsonEntity;
import org.jeesl.model.json.system.status.JsonInterval;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonTsBridge implements Serializable
{
	private final static long serialVersionUID = 1L;

	@JsonProperty("code")
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}

	@JsonProperty("entity")
	private JsonEntity entity;
	public JsonEntity getEntity() {return entity;}
	public void setEntity(JsonEntity entity) {this.entity = entity;}
}