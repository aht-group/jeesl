package org.jeesl.model.json.module.survey.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jeesl.interfaces.model.survey.JeeslSimpleSurveyData;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="data")
public class JsonData implements Serializable,JeeslSimpleSurveyData
{
	public static final long serialVersionUID=1;

	@JsonProperty("id")
	private Long id;
	@Override public Long getId() {return id;}
	@Override public void setId(Long id) {this.id = id;}

	@JsonProperty("survey")
	private JsonSurvey survey;
	public JsonSurvey getSurvey() {return survey;}
	public void setSurvey(JsonSurvey survey) {this.survey = survey;}

	@JsonProperty("record")
	private Date record;
	@Override public Date getRecord() {return record;}
	@Override public void setRecord(Date record) {this.record = record;}
	
	@JsonProperty("correlation")
	private JsonCorrelation correlation;
	public JsonCorrelation getCorrelation() {return correlation;}
	public void setCorrelation(JsonCorrelation correlation) {this.correlation = correlation;}

	@JsonProperty("answers")
	private List<JsonAnswer> answers;
	public List<JsonAnswer> getAnswers() {if(answers==null){answers = new ArrayList<JsonAnswer>();} return answers;}
	public void setAnswers(List<JsonAnswer> answers) {this.answers = answers;}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();	
		return sb.toString();
	}
}