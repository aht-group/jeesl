package org.jeesl.model.json.io.db.pg.meta;

import java.io.Serializable;

import org.jeesl.model.json.system.status.JsonType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="column")
public class JsonPostgresMetaColumn implements Serializable
{
	public static final long serialVersionUID=1;
	
	@JsonProperty("code")
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}
	
	@JsonProperty("position")
	private Integer position;
	public Integer getPosition() {return position;}
	public void setPosition(Integer position) {this.position = position;}

	@JsonProperty("type")
	private JsonType type;
	public JsonType getType() {return type;}
	public void setType(JsonType type) {this.type = type;}
	
	@JsonProperty("table")
	private JsonPostgresMetaTable table;
	public JsonPostgresMetaTable getTable() {return table;}
	public void setTable(JsonPostgresMetaTable table) {this.table = table;}
}