package org.jeesl.model.json.system.io.ssi.update;

import java.io.Serializable;

import org.jeesl.model.json.system.job.JsonJob;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="total")
public class JsonSsiStatistic implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("total")
	private Integer total;
	public Integer getTotal() {return total;}
	public void setTotal(Integer total) {this.total = total;}
	
	@JsonProperty("success")
	private Integer success;
	public Integer getSuccess() {
		return success;
	}
	public void setSuccess(Integer success) {
		this.success = success;
	}
}