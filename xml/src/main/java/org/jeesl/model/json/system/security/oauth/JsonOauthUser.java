package org.jeesl.model.json.system.security.oauth;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="user")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonOauthUser implements Serializable
{
	public static final long serialVersionUID=1;

	public JsonOauthUser() {}
	
	@JsonProperty("sub")
	private String subject;
	public String getSubject() {return subject;}
	public void setSubject(String subject) {this.subject = subject;}

	@JsonProperty("name")
	private String name;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	@JsonProperty("given_name")
	private String firstName;

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@JsonProperty("family_name")
	private String lastName;
	public String getLastName() {return lastName;}
	public void setLastName(String lastName) {this.lastName = lastName;}
	
	@JsonProperty("email")
	private String email;
	public String getEmail() {return email;}
	public void setEmail(String email) {this.email = email;}
	
	@JsonProperty("email_verified")
	private Boolean emailVerified;
	public Boolean getEmailVerified() {return emailVerified;}
	public void setEmailVerified(Boolean emailVerified) {this.emailVerified = emailVerified;}

	@JsonProperty("locale")
	private String locale;

	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	@JsonProperty("picture")
	private String pictureUrl;

	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	
	
}