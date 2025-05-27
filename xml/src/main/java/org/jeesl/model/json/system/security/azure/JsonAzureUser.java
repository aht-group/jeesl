package org.jeesl.model.json.system.security.azure;

import java.io.Serializable;
import java.util.List;

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
	
	@JsonProperty("DatabaseGuid")
	private String databaseGuid;
    public String getDatabaseGuid() {return databaseGuid;}
	public void setDatabaseGuid(String databaseGuid) {this.databaseGuid = databaseGuid;}
	
	@JsonProperty("ExchangeGuid")
	private String exchangeGuid;
	public String getExchangeGuid() {return exchangeGuid;}
	public void setExchangeGuid(String exchangeGuid) {this.exchangeGuid = exchangeGuid;}

	@JsonProperty("DisplayName")
    protected String displayName;
	public String getDisplayName() {return displayName;}
	public void setDisplayName(String displayName) {this.displayName = displayName;}
	
	@JsonProperty("IsShared")
	private Boolean shared;
	public Boolean getShared() {return shared;}
	public void setShared(Boolean shared) {this.shared = shared;}

	@JsonProperty("RecipientType")
    protected String type;
	public String getType() {return type;}
	public void setType(String type) {this.type = type;}
	
	@JsonProperty("ResourceType")
    protected String resourceType;
	public String getResourceType() {return resourceType;}
	public void setResourceType(String resourceType) {this.resourceType = resourceType;}
	
	@JsonProperty("WhenChangedUTC")
    protected String created;
	public String getCreated() {return created;}
	public void setCreated(String created) {this.created = created;}

	@JsonProperty("SurName")
    protected String firstName;
    public String getFirstName() {return firstName;}
    public void setFirstName(String value) {this.firstName = value;}
    
    @JsonProperty("GivenName")
    protected String lastName;
    public String getLastName() {return lastName;}
    public void setLastName(String value) {this.lastName = value;}
    
    @JsonProperty("MailboxPlan")
    private String mailboxPlan;
    public String getMailboxPlan() {return mailboxPlan;}
	public void setMailboxPlan(String mailboxPlan) {this.mailboxPlan = mailboxPlan;}
	
	@JsonProperty("uoLocation") //Only for XLSX
    private String uoLocation;
	public String getUoLocation() {return uoLocation;}
	public void setUoLocation(String uoLocation) {this.uoLocation = uoLocation;}

	@JsonProperty("BoxUsers")
    private List<String> accessedBy;
	public List<String> getAccessedBy() {return accessedBy;}
	public void setAccessedBy(List<String> accessedBy) {this.accessedBy = accessedBy;}
	
	@JsonProperty("department")
	private String department;
	public String getDepartment() {return department;}
	public void setDepartment(String department) {this.department = department;}
	
	@JsonProperty("title")
	private String title;
	public String setTitle() {return title;}
	public void getTitle(String title) {this.title = title;}
	
	@JsonProperty("manager")
	private String manager;
	public String getManager() {return manager;}
	public void setManager(String manager) {this.manager = manager;}
}