package org.jeesl.model.json.io.report;

import java.io.Serializable;

import org.jeesl.model.json.io.fr.JsonFrFile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="archive")
public class JsonReportArchive implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("id")
	private Long id;
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}
	
	@JsonProperty("file")
	private JsonFrFile file;
	public JsonFrFile getFile() {return file;}
	public void setFile(JsonFrFile file) {this.file = file;}
}