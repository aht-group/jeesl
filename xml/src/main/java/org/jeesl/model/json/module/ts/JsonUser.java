
package org.jeesl.model.json.module.ts;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="user")
public class JsonUser
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @JsonProperty("id")
    protected Long id;
    @JsonProperty( "firstName")
    protected String firstName;
    @JsonProperty( "lastName")
    protected String lastName;
    @JsonProperty( "name")
    protected String name;
    @JsonProperty( "email")
    protected String email;


    /*
     * Gets the value of the id property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getId() {
        return id;
    }

    /*
     * Sets the value of the id property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     */
    public void setId(Long value) {
        this.id = value;
    }

    @JsonIgnore public boolean isSetId() {
        return (this.id!= null);
    }

    public void unsetId() {
        this.id = null;
    }

    /*
     * Gets the value of the firstName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getFirstName() {
        return firstName;
    }

    /*
     * Sets the value of the firstName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    @JsonIgnore public boolean isSetFirstName() {
        return (this.firstName!= null);
    }

    /*
     * Gets the value of the lastName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLastName() {
        return lastName;
    }

    /*
     * Sets the value of the lastName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLastName(String value) {
        this.lastName = value;
    }

    @JsonIgnore public boolean isSetLastName() {
        return (this.lastName!= null);
    }

    /*
     * Gets the value of the name property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getName() {
        return name;
    }

    /*
     * Sets the value of the name property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setName(String value) {
        this.name = value;
    }

    @JsonIgnore public boolean isSetName() {
        return (this.name!= null);
    }

    /*
     * Gets the value of the email property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getEmail() {
        return email;
    }

    /*
     * Sets the value of the email property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setEmail(String value) {
        this.email = value;
    }

    @JsonIgnore public boolean isSetEmail() {
        return (this.email!= null);
    }

}
