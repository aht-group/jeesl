package org.jeesl.model.json.system.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="pages")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonSecurityPages implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("list")
	private List<JsonSecurityPage> list;
	public List<JsonSecurityPage> getList() {if(Objects.isNull(list)) {list = new ArrayList<>();}return list;}
	public void setList(List<JsonSecurityPage> pages) {this.list = pages;}
}