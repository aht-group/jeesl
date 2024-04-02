package org.jeesl.model.json.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.model.json.module.survey.data.JsonSurvey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="surveys")
public class Surveys implements Serializable
{
	public static final long serialVersionUID=1;
	
	@JsonProperty("survey")
	private List<JsonSurvey> survey;
	public List<JsonSurvey> getSurvey() {if(survey==null){survey = new ArrayList<JsonSurvey>();} return survey;}
	public void setSurvey(List<JsonSurvey> survey) {this.survey = survey;}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}
}