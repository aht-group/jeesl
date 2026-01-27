package org.jeesl.model.json.system.security;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="user")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonSecurityUser implements Serializable
{
	public static final long serialVersionUID=1;

	public JsonSecurityUser() {}
	
	@JsonProperty("id")
	private Long id;
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}
	
	@JsonProperty("code")
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}

	@JsonProperty("email")
	private String email;
	public String getEmail() {return email;}
	public void setEmail(String email) {this.email = email;}
	
	@JsonProperty("password")
	private String password;
	public String getPassword() {return password;}
	public void setPassword(String password) {this.password = password;}
	
    @JsonProperty("firstName")
    protected String firstName;
    public String getFirstName() {return firstName;}
    public void setFirstName(String value) {this.firstName = value;}
    
    @JsonProperty("lastName")
    protected String lastName;
    public String getLastName() {return lastName;}
    public void setLastName(String value) {this.lastName = value;}
}