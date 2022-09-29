
package org.jeesl.model.json.module.attribute;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="set")
public class JsonAttributeCriteria implements Serializable
{
    private final static long serialVersionUID = 1L;
 
	@JsonProperty("id")
	private Long id;
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}
    
	@JsonProperty("code")
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}
	
	@JsonProperty("visible")
	private Boolean visible;
	public Boolean getVisible() {return visible;}
	public void setVisible(Boolean visible) {this.visible = visible;}
	
	@JsonProperty("allowEmpty")
	private Boolean allowEmpty;
	public Boolean getAllowEmpty() {return allowEmpty;}
	public void setAllowEmpty(Boolean allowEmpty) {this.allowEmpty = allowEmpty;}

	@JsonProperty("label")
	private String label;
	public String getLabel() {return label;}
	public void setLabel(String label) {this.label = label;}
	
	@JsonProperty("description")
	private String description;
	public String getDescription() {return description;}
	public void setDescription(String description) {this.description = description;}
	
	@JsonProperty("type")
	private JsonAttributeType type;
	public JsonAttributeType getType() {return type;}
	public void setType(JsonAttributeType type) {this.type = type;}
	
	@JsonProperty("options")
	private List<JsonAttributeOption> options;
	public List<JsonAttributeOption> getOptions() {return options;}
	public void setOptions(List<JsonAttributeOption> options) {this.options = options;}
}