package org.jeesl.model.json.io.ssi.core;

import java.io.Serializable;
import java.util.List;

import org.jeesl.model.json.io.ssi.svn.JsonSvnRepository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="ssi")
public class JsonSsiContainer implements Serializable
{
	public static final long serialVersionUID=1;


	@JsonProperty("versions")
	private List<JsonSsiVersion> versions;
	public List<JsonSsiVersion> getVersions() {return versions;}
	public void setVersions(List<JsonSsiVersion> versions) {this.versions = versions;}

	@JsonProperty("credentials")
	private List<JsonSsiCredential> credentials;
	public List<JsonSsiCredential> getCredentials() {return credentials;}
	public void setCredentials(List<JsonSsiCredential> credentials) {this.credentials = credentials;}
	
	@JsonProperty("nats")
	private List<JsonSsiNat> nats;
	public List<JsonSsiNat> getNats() {return nats;}
	public void setNats(List<JsonSsiNat> nats) {this.nats = nats;}

	@JsonProperty("svns")
	private List<JsonSvnRepository> svns;
	public List<JsonSvnRepository> getSvns() {return svns;}
	public void setSvns(List<JsonSvnRepository> svns) {this.svns = svns;}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();	
		return sb.toString();
	}
}