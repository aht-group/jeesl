package org.jeesl.model.json.module.survey.question;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.model.survey.JeeslSimpleSurveySection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties
@JsonRootName(value="section")
public class JsonSection implements Serializable,JeeslSimpleSurveySection
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
	@JsonIgnore public boolean isSetCode() {return code!=null;}
	
	@JsonProperty("name")
	private String name;
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	@JsonIgnore public boolean isSetName() {return name!=null;}
	
	@JsonProperty("questions")
	private List<JsonQuestion> questions;
	public List<JsonQuestion> getQuestions() {if(questions==null){questions = new ArrayList<JsonQuestion>();} return questions;}
	public void setQuestions(List<JsonQuestion> questions) {this.questions = questions;}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		
		return sb.toString();
	}
}