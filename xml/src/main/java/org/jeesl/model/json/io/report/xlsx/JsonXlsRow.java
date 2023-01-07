package org.jeesl.model.json.io.report.xlsx;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="row")
public class JsonXlsRow implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("nr")
	private Integer nr;
	public Integer getNr() {return nr;}
	public void setNr(Integer nr) {this.nr = nr;}

	@JsonProperty("code")
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}
	
	@JsonProperty("cells")
	private List<JsonXlsCell> cells;
	public List<JsonXlsCell> getCells() {return cells;}
	public void setCells(List<JsonXlsCell> cells) {this.cells = cells;}
}