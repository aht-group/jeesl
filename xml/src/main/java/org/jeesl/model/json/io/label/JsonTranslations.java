package org.jeesl.model.json.io.label;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="entity")
public class JsonTranslations implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("entities")
	private List<JsonEntity> entities;

	public List<JsonEntity> getEntities() {
		return entities;
	}

	public void setEntities(List<JsonEntity> entities) {
		this.entities = entities;
	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();

		return sb.toString();
	}
}