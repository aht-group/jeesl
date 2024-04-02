package org.jeesl.model.json.survey;

import java.io.Serializable;

import org.jeesl.model.json.module.survey.question.JsonOption;
import org.jeesl.model.json.module.survey.question.JsonQuestion;
import org.jeesl.model.json.system.status.JsonType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="answer")
public class Condition implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("id")
	private Long id;
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}
	
	@JsonProperty("negate")
	private boolean negate;
	public boolean isNegate() {return negate;}
	public void setNegate(boolean negate) {this.negate = negate;}
	
	@JsonProperty("position")
	private int position;
	public int getPosition() {return position;}
	public void setPosition(int position) {this.position = position;}

	@JsonProperty("question")
	private JsonQuestion question;
	public JsonQuestion getQuestion() {return question;}
	public void setQuestion(JsonQuestion question) {this.question = question;}

	@JsonProperty("trigger")
	private JsonQuestion trigger;
	public JsonQuestion getTrigger() {return trigger;}
	public void setTrigger(JsonQuestion trigger) {this.trigger = trigger;}
	
	@JsonProperty("option")
	private JsonOption option;
	public JsonOption getOption() {return option;}
	public void setOption(JsonOption option) {this.option = option;}
	
	@JsonProperty("type")
	private JsonType type;
	public JsonType getType() {return type;}
	public void setType(JsonType type) {this.type = type;}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();	
		return sb.toString();
	}
}