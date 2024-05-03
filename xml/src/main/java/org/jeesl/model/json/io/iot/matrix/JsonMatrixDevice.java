package org.jeesl.model.json.io.iot.matrix;

import java.io.Serializable;
import java.util.List;

import org.jeesl.model.json.system.status.JsonMode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="device")
public class JsonMatrixDevice implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("id")
	private Long id;
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	@JsonProperty("code")
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}
	
	@JsonProperty("name")
	private String name;
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}

	@JsonProperty("rows")
	private Integer rows; 
	public Integer getRows() {return rows;}
	public void setRows(Integer rows) {this.rows = rows;}
	
	@JsonProperty("columns")
	private Integer columns;
	public Integer getColumns() {return columns;}
	public void setColumns(Integer columns) {this.columns = columns;}
	
	@JsonProperty("brightness")
	private Integer brightness;
	public Integer getBrightness() {return brightness;}
	public void setBrightness(Integer brightness) {this.brightness = brightness;}
	
	@JsonProperty("mode")
	private JsonMode mode;
	public JsonMode getMode() {return mode;}
	public void setMode(JsonMode mode) {this.mode = mode;}

	@JsonProperty("data")
	private List<String> data;
	public List<String> getData() {return data;}
	public void setData(List<String> data) {this.data = data;}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();	
		return sb.toString();
	}
}