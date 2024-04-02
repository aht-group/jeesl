package org.jeesl.model.json.module.survey.data;

import java.io.Serializable;

import org.jeesl.interfaces.model.survey.JeeslSimpleSurveyAnswer;
import org.jeesl.model.json.module.survey.question.JsonOption;
import org.jeesl.model.json.module.survey.question.JsonQuestion;
import org.jeesl.model.json.survey.Matrix;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="answer")
public class JsonAnswer implements Serializable,JeeslSimpleSurveyAnswer
{
	public static final long serialVersionUID=1;

	@JsonProperty("id")
	private Long id;
	@Override public Long getId() {return id;}
	@Override public void setId(Long id) {this.id = id;}

	@JsonProperty("question")
	private JsonQuestion question;
	public JsonQuestion getQuestion() {return question;}
	public void setQuestion(JsonQuestion question) {this.question = question;}

	@JsonProperty("valueBoolean")
	private Boolean valueBoolean;
	@Override public Boolean getValueBoolean() {return valueBoolean;}
	@Override public void setValueBoolean(Boolean valueBoolean) {this.valueBoolean = valueBoolean;}
	
	@JsonProperty("valueDouble")
	private Double valueDouble;
	@Override public Double getValueDouble() {return valueDouble;}
	@Override public void setValueDouble(Double valueDouble) {this.valueDouble = valueDouble;}
	
	@JsonProperty("valueNumber")
	private Integer valueNumber;
	@Override public Integer getValueNumber() {return valueNumber;}
	@Override public void setValueNumber(Integer valueNumber) {this.valueNumber = valueNumber;}
	
	@JsonProperty("valueText")
	private String valueText;
	@Override public String getValueText() {return valueText;}
	@Override public void setValueText(String valueText) {this.valueText = valueText;}
	
	@JsonProperty("score")
	private Double score;
	@Override public Double getScore() {return score;}
	@Override public void setScore(Double score){this.score = score;}
	
	@JsonProperty("remark")
	private String remark;
	@Override public String getRemark() {return remark;}
	@Override public void setRemark(String remark) {this.remark = remark;}
	
	@JsonProperty("option")
	private JsonOption option;
	public JsonOption getOption() {return option;}
	public void setOption(JsonOption option) {this.option = option;}
	
	@JsonProperty("matrix")
	private Matrix matrix;
	public Matrix getMatrix() {return matrix;}
	public void setMatrix(Matrix matrix) {this.matrix = matrix;}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();	
		return sb.toString();
	}
}