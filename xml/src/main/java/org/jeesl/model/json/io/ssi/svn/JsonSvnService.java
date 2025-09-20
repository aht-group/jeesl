package org.jeesl.model.json.io.ssi.svn;

import java.io.Serializable;
import java.util.List;

import org.jeesl.model.json.io.ssi.core.JsonSsiCredential;
import org.jeesl.model.json.system.status.JsonStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="service")
public class JsonSvnService implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("repository")
	public JsonSvnRepository repository;
	public JsonSvnRepository getRepository() {return repository;}
	public void setRepository(JsonSvnRepository repository) {this.repository = repository;}

	@JsonProperty("code")
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}

	@JsonProperty("connection")
	public JsonSsiCredential connection;
	public JsonSsiCredential getConnection() {return connection;}
	public void setConnection(JsonSsiCredential connection) {this.connection = connection;}
	
	@JsonProperty("status")
	private JsonStatus status;
	public JsonStatus getStatus() {return status;}
	public void setStatus(JsonStatus status) {this.status = status;}
	
	@JsonProperty("revision")
	private Long revision;
	public Long getRevision() {return revision;}
	public void setRevision(Long revision) {this.revision = revision;}

	@JsonProperty("revisions")
	private List<JsonSvnRevision> revisions;
	public List<JsonSvnRevision> getRevisions() {return revisions;}
	public void setRevisions(List<JsonSvnRevision> revisions) {this.revisions = revisions;}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();	
		return sb.toString();
	}
}