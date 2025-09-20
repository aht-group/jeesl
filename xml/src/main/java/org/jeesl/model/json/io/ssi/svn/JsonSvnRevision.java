package org.jeesl.model.json.io.ssi.svn;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="revision")
public class JsonSvnRevision implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("version")
	public Long version;
	public Long getVersion() {return version;}
	public void setVersion(Long version) {this.version = version;}


	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();	
		return sb.toString();
	}
}