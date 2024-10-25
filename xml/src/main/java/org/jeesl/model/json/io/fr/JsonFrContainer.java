package org.jeesl.model.json.io.fr;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="frContainer")
public class JsonFrContainer implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("id")
	private Long id;
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	@JsonProperty("files")
	private List<JsonFrFile> files;
	public List<JsonFrFile> getFiles() {return files;}
	public void setFiles(List<JsonFrFile> files) {this.files = files;}
}