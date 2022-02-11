
package net.sf.ahtutils.xml.project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import net.sf.ahtutils.xml.security.Roles;
import net.sf.ahtutils.xml.security.User;


/*
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/security}roles"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/security}user" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "roles",
    "user"
})
@XmlRootElement(name = "responsibilities")
public class Responsibilities
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/security", required = true)
    protected Roles roles;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/security", required = true)
    protected List<User> user;

    /*
     * Gets the value of the roles property.
     * 
     * @return
     *     possible object is
     *     {@link Roles }
     *     
     */
    public Roles getRoles() {
        return roles;
    }

    /*
     * Sets the value of the roles property.
     * 
     * @param value
     *     allowed object is
     *     {@link Roles }
     *     
     */
    public void setRoles(Roles value) {
        this.roles = value;
    }

    public boolean isSetRoles() {
        return (this.roles!= null);
    }

  
    public List<User> getUser() {
        if (user == null) {
            user = new ArrayList<User>();
        }
        return this.user;
    }

    public boolean isSetUser() {
        return ((this.user!= null)&&(!this.user.isEmpty()));
    }

    public void unsetUser() {
        this.user = null;
    }

}
