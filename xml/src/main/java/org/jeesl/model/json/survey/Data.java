package org.jeesl.model.json.survey;

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
public class Data implements Serializable,JeeslSimpleSurveyData
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
	private Correlation correlation;
	public Correlation getCorrelation() {return correlation;}
	public void setCorrelation(Correlation correlation) {this.correlation = correlation;}

	@JsonProperty("answers")
	private List<Answer> answers;
	public List<Answer> getAnswers() {if(answers==null){answers = new ArrayList<Answer>();} return answers;}
	public void setAnswers(List<Answer> answers) {this.answers = answers;}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();	
		return sb.toString();
	}
}