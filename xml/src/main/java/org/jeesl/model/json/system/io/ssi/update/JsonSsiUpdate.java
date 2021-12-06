package org.jeesl.model.json.system.io.ssi.update;

import java.io.Serializable;

import org.jeesl.model.json.system.job.JsonJob;
import org.jeesl.model.json.system.security.JsonSecurityUser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="dataUpdate")
public class JsonSsiUpdate implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("success")
	private boolean success;
	public boolean isSuccess() {return success;}
	public void setSuccess(boolean success) {this.success = success;}
	
	@JsonProperty("job")
	private JsonJob job;
	public JsonJob getJob() {return job;}
	public void setJob(JsonJob job) {this.job = job;}
	
	@JsonProperty("statistic")
	private JsonSsiStatistic statistic;
	public JsonSsiStatistic getStatistic() {return statistic;}
	public void setStatistic(JsonSsiStatistic statistic) {this.statistic = statistic;}
	
	@JsonProperty("user")
	private JsonSecurityUser user;
	public JsonSecurityUser getUser() {return user;}
	public void setUser(JsonSecurityUser user) {this.user = user;}
}