package org.jeesl.model.json.module.survey.question;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value = "question")
public class JsonOption implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("id")
	private Long id;
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}
	
	@JsonProperty("position")
	private Integer position;
	public Integer getPosition() {return position;}
	public void setPosition(Integer position) {this.position = position;}
	
	@JsonProperty("code")
	private String code;
	public String getCode(){return code;}
	public void setCode(String code){this.code = code;}
	@JsonIgnore public boolean isSetCode() {return code!=null;}
	
	@JsonProperty("label")
	private String label;
	public String getLabel(){return label;}
	public void setLabel(String label){this.label = label;}
	@JsonIgnore public boolean isSetLabel() {return label!=null;}
	
	@JsonProperty("description")
	private String description;
	public String getDescription() {return description;}
	public void setDescription(String description) {this.description = description;}
	@JsonIgnore public boolean isSetDescription() {return description!=null;}
	
	@JsonProperty("row")
	private Boolean row;
	public Boolean getRow() {return row;}
	public void setRow(Boolean row) {this.row = row;}
	@JsonIgnore public boolean isSetRow() {return row!=null;}
	
	@JsonProperty("column")
	private Boolean column;
	public Boolean getColumn() {return column;}
	public void setColumn(Boolean column) {this.column = column;}
	@JsonIgnore public boolean isSetColumn() {return column!=null;}
	
	@JsonProperty("cell")
	private Boolean cell;
	public Boolean getCell() {return cell;}
	public void setCell(Boolean cell) {this.cell = cell;}
	@JsonIgnore public boolean isSetCell() {return cell!=null;}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}
}