
package org.jeesl.model.json.module.ts;

import java.io.Serializable;
import java.util.Date;

import org.jeesl.model.json.system.security.JsonSecurityUser;
import org.jeesl.model.json.system.status.JsonSource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value = "transaction")
public class JsonTransaction implements Serializable
{
    private final static long serialVersionUID = 1L;

    @JsonProperty("source")
    protected JsonSource source;
   
    @JsonProperty("id")
    private Long id;
    public Long getId() {return id;}
    public void setId(Long value) {this.id = value;}
    
    
    @JsonProperty("record")
    protected Date record;


    @JsonProperty("user")
    protected JsonSecurityUser user;
    public JsonSecurityUser getUser() {
        return user;
    }

    /*
     * Sets the value of the user property.
     *
     * @param value
     *     allowed object is
     *     {@link User }
     *
     */
    public void setUser(JsonSecurityUser value) {
        this.user = value;
    }

    @JsonIgnore public boolean isSetUser() {
        return (this.user!= null);
    }

    /*
     * Gets the value of the source property.
     *
     * @return
     *     possible object is
     *     {@link JsonSource }
     *
     */
    public JsonSource getSource() {
        return source;
    }

    /*
     * Sets the value of the source property.
     *
     * @param value
     *     allowed object is
     *     {@link JsonSource }
     *
     */
    public void setSource(JsonSource value) {
        this.source = value;
    }

    @JsonIgnore public boolean isSetSource() {
        return (this.source!= null);
    }

    /*
     * Gets the value of the id property.
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
   

    @JsonIgnore public boolean isSetId() {
        return (this.id!= null);
    }

    public void unsetId() {
        this.id = null;
    }


    public Date getRecord() {
        return record;
    }

    public void setRecord(Date value) {
        this.record = value;
    }

    @JsonIgnore public boolean isSetRecord() {
        return (this.record!= null);
    }

}
