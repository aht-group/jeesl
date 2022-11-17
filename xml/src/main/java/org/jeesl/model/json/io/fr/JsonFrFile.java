package org.jeesl.model.json.io.fr;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="frFile")
public class JsonFrFile implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("name")
	private String name;
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	
	@JsonProperty("content")
	private byte[] content;
	public byte[] getContent() {return content;}
	public void setContent(byte[] content) {this.content = content;}
}