package org.jeesl.model.json.system.io.ssi.update;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="total")
public class JsonSsiStatistic implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("expected")
	private Integer expected;
	public Integer getExpected() {return expected;}
	public void setExpected(Integer expected) {this.expected = expected;}

	@JsonProperty("total")
	private Integer total;
	public Integer getTotal() {return total;}
	public void setTotal(Integer total) {this.total = total;}
	
	@JsonProperty("success")
	private Integer success;
	public Integer getSuccess() {return success;}
	public void setSuccess(Integer success) {this.success = success;}
	
	@JsonProperty("obsolete")
	private Integer obsolete;
	public Integer getObsolete() {return obsolete;}
	public void setObsolete(Integer obsolete) {this.obsolete = obsolete;}
	
	@JsonProperty("error")
	private Integer error;
	public Integer getError() {return error;}
	public void setError(Integer error) {this.error = error;}
	
	@JsonProperty("updated")
	private Integer updated;
	public Integer getUpdated() {return updated;}
	public void setUpdated(Integer updated) {this.updated = updated;}
	
	@JsonProperty("created")
	private Integer created;
	public Integer getCreated() {return created;}
	public void setCreated(Integer created) {this.created = created;}
}