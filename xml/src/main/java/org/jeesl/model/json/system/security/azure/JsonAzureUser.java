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
	
	@JsonProperty("PrimarySmtpAddress")
	private String email;
	public String getEmail() {return email;}
	public void setEmail(String email) {this.email = email;}
	
	@JsonProperty("Guid")
	private String guid;
	public String getGuid() {return guid;}
	public void setGuid(String guid) {this.guid = guid;}
	
//	@JsonProperty("DatabaseGuid")
//	private String databaseGuid;
//    public String getDatabaseGuid() {return databaseGuid;}
//	public void setDatabaseGuid(String databaseGuid) {this.databaseGuid = databaseGuid;}
//	
//	@JsonProperty("ExchangeGuid")
//	private String exchangeGuid;
//	public String getExchangeGuid() {return exchangeGuid;}
//	public void setExchangeGuid(String exchangeGuid) {this.exchangeGuid = exchangeGuid;}

	@JsonProperty("DisplayName")
    protected String displayName;
	public String getDisplayName() {return displayName;}
	public void setDisplayName(String displayName) {this.displayName = displayName;}
	
	@JsonProperty("IsShared")
	private Boolean  shared;
	public Boolean getShared() {return shared;}
	public void setShared(Boolean shared) {this.shared = shared;}

	@JsonProperty("RecipientType")
    protected String type;
	public String getType() {return type;}
	public void setType(String type) {this.type = type;}

	@JsonProperty("SurName")
    protected String firstName;
    public String getFirstName() {return firstName;}
    public void setFirstName(String value) {this.firstName = value;}
    
    @JsonProperty("GivenName")
    protected String lastName;
    public String getLastName() {return lastName;}
    public void setLastName(String value) {this.lastName = value;}
}