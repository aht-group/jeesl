package org.jeesl.model.json.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.model.survey.JeeslSimpleSurvey;
import org.jeesl.model.json.module.survey.question.JsonSection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="template")
public class JsonTemplate implements Serializable,JeeslSimpleSurvey
{
	public static final long serialVersionUID=1;

	@JsonProperty("id")
	private long id;
	public long getId() {return id;}
	public void setId(long id) {this.id = id;}
	
	@JsonProperty("code")
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}

	@JsonProperty("sections")
	private List<JsonSection> sections;
	public List<JsonSection> getSections() {if(sections==null){sections = new ArrayList<JsonSection>();} return sections;}
	public void setSections(List<JsonSection> sections) {this.sections = sections;}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		
		return sb.toString();
	}
}