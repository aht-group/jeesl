package org.jeesl.model.json.survey;

import java.io.Serializable;
import java.util.List;

import org.jeesl.model.json.module.survey.data.JsonCell;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="matrix")
public class Matrix implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("cells")
	private List<JsonCell> cells;
	public List<JsonCell> getCells() {return cells;}
	public void setCells(List<JsonCell> cells) {this.cells = cells;}


	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();	
		return sb.toString();
	}
}