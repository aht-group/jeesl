package org.jeesl.model.json.io.ssi.update;

import java.io.Serializable;
import java.util.List;

import org.jeesl.model.json.system.job.JsonSystemJob;
import org.jeesl.model.json.system.security.JsonSecurityUser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(Include.NON_NULL)
@JsonRootName(value="dataUpdate")
public class JsonSsiUpdate implements Serializable
{
	public static final long serialVersionUID=1;

	public JsonSsiUpdate() {}
	
	@JsonProperty("success")
	private Boolean success;
	public Boolean isSuccess() {return success;}
	public void setSuccess(Boolean success) {this.success = success;}
	
	@JsonProperty("job")
	private JsonSystemJob job;
	public JsonSystemJob getJob() {return job;}
	public void setJob(JsonSystemJob job) {this.job = job;}
	
	@JsonProperty("statistic")
	private JsonSsiStatistic statistic;
	public JsonSsiStatistic getStatistic() {return statistic;}
	public void setStatistic(JsonSsiStatistic statistic) {this.statistic = statistic;}
	
	@JsonProperty("messages")
	private List<JsonSsiMessage> messages;
	public List<JsonSsiMessage> getMessages() {return messages;}
	public void setMessages(List<JsonSsiMessage> messages) {this.messages = messages;}

	@JsonProperty("user")
	private JsonSecurityUser user;
	public JsonSecurityUser getUser() {return user;}
	public void setUser(JsonSecurityUser user) {this.user = user;}
}