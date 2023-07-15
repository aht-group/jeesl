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
	
	@JsonProperty("remoteTable")
	private String remoteTable;
	public String getRemoteTable() {return remoteTable;}
	public void setRemoteTable(String remoteTable) {this.remoteTable = remoteTable;}

	@JsonProperty("remoteColumn")
	private String remoteColumn;
	public String getRemoteColumn() {return remoteColumn;}
	public void setRemoteColumn(String remoteColumn) {this.remoteColumn = remoteColumn;}
}