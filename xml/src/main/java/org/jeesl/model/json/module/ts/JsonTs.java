package org.jeesl.model.json.module.ts;
import java.io.Serializable;
import java.util.List;

import org.jeesl.model.json.system.status.JsonBridge;
import org.jeesl.model.json.system.status.JsonEntity;
import org.jeesl.model.json.system.status.JsonInterval;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonTs implements Serializable
{
	private final static long serialVersionUID = 1L;

	@JsonProperty("scope")
	private JsonTsScope scope;
	public JsonTsScope getScope() {return scope;}
	public void setScope(JsonTsScope scope) {this.scope = scope;}

	@JsonProperty("interval")
	private JsonInterval interval;
	public JsonInterval getInterval() {return interval;}
	public void setInterval(JsonInterval interval) {this.interval = interval;}

	@JsonProperty("entity")
	private JsonEntity entity;
	public JsonEntity getEntity() {return entity;}
	public void setEntity(JsonEntity entity) {this.entity = entity;}

	@JsonProperty("bridge")
	private JsonBridge bridge;
	public JsonBridge getBridge() {return bridge;}
	public void setBridge(JsonBridge bridge) {this.bridge = bridge;}

	@JsonProperty("transaction")
	private List<JsonTransaction> transaction;
	public List<JsonTransaction> getTransaction() {return transaction;}
	public void setTransaction(List<JsonTransaction> transaction) {this.transaction = transaction;}

	@JsonProperty("data")
    private List<JsonTsData> data;
	public List<JsonTsData> getData() {return data;}
	public void setData(List<JsonTsData> data) {this.data = data;}
}