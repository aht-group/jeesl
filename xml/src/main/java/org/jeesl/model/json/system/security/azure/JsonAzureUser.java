package org.jeesl.model.json.system.security.azure;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="user")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonAzureUser implements Serializable
{
	public static final long serialVersionUID=1;

	public JsonAzureUser() {}
	
	@JsonProperty("guid")
	private String guid;
	public String getGuid() {return guid;}
	public void setGuid(String guid) {this.guid = guid;}

	@JsonProperty("email")
	private String email;
	public String getEmail() {return email;}
	public void setEmail(String email) {this.email = email;}
	
    @JsonProperty("SurName")
    protected String firstName;
    public String getFirstName() {return firstName;}
    public void setFirstName(String value) {this.firstName = value;}
    
    @JsonProperty("GivenName")
    protected String lastName;
    public String getLastName() {return lastName;}
    public void setLastName(String value) {this.lastName = value;}
}