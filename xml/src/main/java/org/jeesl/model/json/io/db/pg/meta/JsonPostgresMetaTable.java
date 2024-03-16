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
	
	@JsonProperty("tablespace")
	private JsonPostgresTablespace tablespace;
	public JsonPostgresTablespace getTablespace() {return tablespace;}
	public void setTablespace(JsonPostgresTablespace tablespace) {this.tablespace = tablespace;}
	
	@JsonProperty("scheme")
	private String scheme;
	public String getScheme() {return scheme;}
	public void setScheme(String scheme) {this.scheme = scheme;}

	@JsonProperty("code")
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}
	
	@JsonProperty("columns")
	private List<JsonPostgresMetaColumn> columns;
	public List<JsonPostgresMetaColumn> getColumns() {return columns;}
	public void setColumns(List<JsonPostgresMetaColumn> columns) {this.columns = columns;}

	@JsonProperty("primaryKeys")
	private List<JsonPostgresMetaConstraint> primaryKeys;
	public List<JsonPostgresMetaConstraint> getPrimaryKeys() {return primaryKeys;}
	public void setPrimaryKeys(List<JsonPostgresMetaConstraint> primaryKeys) {this.primaryKeys = primaryKeys;}

	@JsonProperty("foreignKeys")
	private List<JsonPostgresMetaConstraint> foreignKeys;
	public List<JsonPostgresMetaConstraint> getForeignKeys() {return foreignKeys;}
	public void setForeignKeys(List<JsonPostgresMetaConstraint> foreignKeys) {this.foreignKeys = foreignKeys;}
	
	@JsonProperty("uniqueKeys")
	private List<JsonPostgresMetaConstraint> uniqueKeys;
	public List<JsonPostgresMetaConstraint> getUniqueKeys() {return uniqueKeys;}
	public void setUniqueKeys(List<JsonPostgresMetaConstraint> uniqueKeys) {this.uniqueKeys = uniqueKeys;}
	
	@JsonProperty("indexes")
	private List<JsonPostgresMetaConstraint> indexes;
	public List<JsonPostgresMetaConstraint> getIndexes() {return indexes;}
	public void setIndexes(List<JsonPostgresMetaConstraint> indexes) {this.indexes = indexes;}
}