
package org.jeesl.model.json.module.monitoring.inventory;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="snapshot")
public class JsonInventorySnapshot implements Serializable
{
    private final static long serialVersionUID = 1L;
 

	@JsonProperty("attributes")
	private List<JsonInventoryAttribute> attributes;
	public List<JsonInventoryAttribute> getAttributes() {return attributes;}
	public void setAttributes(List<JsonInventoryAttribute> attributes) {this.attributes = attributes;}
}