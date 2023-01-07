
package org.jeesl.model.json.system.status;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="source")
public class JsonSource implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("id")
	private Long id;
	public Long getId() {return id;}
	public void setId(Long id) {this.id=id;}
	@JsonIgnore public boolean isSetId() {return id!=null;}

	@JsonProperty("code")
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}
	@JsonIgnore public boolean isSetCode() {return code!=null;}
	
	@JsonProperty("label")
	private String label;
	public String getLabel() {return label;}
	public void setLabel(String label) {this.label = label;}
	@JsonIgnore public boolean isSetLabel() {return label!=null;}
	
	@JsonProperty("description")
	private String description;
	public String getDescription() {return description;}
	public void setDescription(String description) {this.description = description;}
	@JsonIgnore public boolean isSetDescription() {return description!=null;}
}
