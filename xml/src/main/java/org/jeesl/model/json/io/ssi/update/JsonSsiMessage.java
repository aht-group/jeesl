package org.jeesl.model.json.io.ssi.update;

import java.io.Serializable;

import org.jeesl.model.json.system.job.JsonSystemJob;
import org.jeesl.model.json.system.security.JsonSecurityUser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="message")
public class JsonSsiMessage implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("message")
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}