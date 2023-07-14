package org.jeesl.model.json.io.db.pg.meta;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="constraint")
public class JsonPostgresMetaConstraint implements Serializable
{
	public static final long serialVersionUID=1;
	
	@JsonProperty("code")
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}
	
	@JsonProperty("localColumn")
	private String localColumn;
	public String getLocalColumn() {return localColumn;}
	public void setLocalColumn(String localColumn) {this.localColumn = localColumn;}
	
	@JsonProperty("referencedTable")
	private String referencedTable;
	public String getReferencedTable() {return referencedTable;}
	public void setReferencedTable(String referencedTable) {this.referencedTable = referencedTable;}

	@JsonProperty("referencedColumn")
	private String referencedColumn;
	public String getReferencedColumn() {return referencedColumn;}
	public void setReferencedColumn(String referencedColumn) {this.referencedColumn = referencedColumn;}
}