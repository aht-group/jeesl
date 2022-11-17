package org.jeesl.model.json.io.fr;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="FileRepositoryFileSystem")
public class JsonFrFileSystem implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("baseDir")
	private String baseDir;
	public String getBaseDir() {return baseDir;}
	public void setBaseDir(String baseDir) {this.baseDir = baseDir;}
	
	@JsonProperty("level")
	private int level;
	public int getLevel() {return level;}
	public void setLevel(int level) {this.level = level;}
}