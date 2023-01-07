package org.jeesl.model.json.ssi.jira;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="issue")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Issue implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("expand")
	private String expand;
	public String getExpand() {return expand;}
	public void setExpand(String expand) {this.expand = expand;}

	@JsonProperty("fields")
	private Fields fields;
	public Fields getFields() {return fields;}
	public void setFields(Fields fields) {this.fields = fields;}
}