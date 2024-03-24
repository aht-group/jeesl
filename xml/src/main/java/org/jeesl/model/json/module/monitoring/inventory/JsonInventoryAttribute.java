
package org.jeesl.model.json.module.monitoring.inventory;

import java.io.Serializable;

import org.jeesl.model.json.system.status.JsonCategory;
import org.jeesl.model.json.system.status.JsonType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="item")
public class JsonInventoryAttribute implements Serializable
{
    private final static long serialVersionUID = 1L;
 
	@JsonProperty("id")
	private Long id;
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}
	
	@JsonProperty("position")
	private Integer position;

	@JsonProperty("category")
	private JsonCategory category;
	public JsonCategory getCategory() {return category;}
	public void setCategory(JsonCategory category) {this.category = category;}
	
	@JsonProperty("type")
	private JsonType type;
	public JsonType getType() {return type;}
	public void setType(JsonType type) {this.type = type;}
}