package org.jeesl.model.json.io.db.pg.meta;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="snapshot")
public class JsonPostgresMetaTable implements Serializable
{
	public static final long serialVersionUID=1;
	
	@JsonProperty("code")
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}
	
	@JsonProperty("columns")
	private List<JsonPostgresMetaColumn> columns;
	public List<JsonPostgresMetaColumn> getColumns() {return columns;}
	public void setColumns(List<JsonPostgresMetaColumn> columns) {this.columns = columns;}

	@JsonProperty("foreignKeys")
	private List<JsonPostgresMetaConstraint> foreignKeys;
	public List<JsonPostgresMetaConstraint> getForeignKeys() {return foreignKeys;}
	public void setForeignKeys(List<JsonPostgresMetaConstraint> foreignKeys) {this.foreignKeys = foreignKeys;}
}