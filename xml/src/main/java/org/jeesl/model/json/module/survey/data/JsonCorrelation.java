package org.jeesl.model.json.module.survey.data;

import java.io.Serializable;

import org.jeesl.interfaces.model.survey.JeeslSimpleSurveyCorrelation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="correlation")
public class JsonCorrelation implements Serializable,JeeslSimpleSurveyCorrelation
{
	public static final long serialVersionUID=1;

	@JsonProperty("id")
	private long id;
	public long getId() {return id;}
	public void setId(long id) {this.id = id;}

	@JsonProperty("type")
	private String type;
	public String getType() {return type;}
	public void setType(String type) {this.type = type;}
	
	@JsonProperty("object")
	private Object object;
	public Object getObject() {return object;}
	public void setObject(Object object) {this.object = object;}
	
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();	
		return sb.toString();
	}
}