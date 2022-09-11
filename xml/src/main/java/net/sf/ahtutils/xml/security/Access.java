
package net.sf.ahtutils.xml.security;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="publicUser" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="authenticatedUser" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "access")
public class Access
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlAttribute(name = "publicUser")
    protected Boolean publicUser;
    @XmlAttribute(name = "authenticatedUser")
    protected Boolean authenticatedUser;

    /**
     * Gets the value of the publicUser property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isPublicUser() {
        return publicUser;
    }

    /**
     * Sets the value of the publicUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPublicUser(boolean value) {
        this.publicUser = value;
    }

    public boolean isSetPublicUser() {
        return (this.publicUser!= null);
    }

    public void unsetPublicUser() {
        this.publicUser = null;
    }

    /**
     * Gets the value of the authenticatedUser property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isAuthenticatedUser() {
        return authenticatedUser;
    }

    /**
     * Sets the value of the authenticatedUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAuthenticatedUser(boolean value) {
        this.authenticatedUser = value;
    }

    public boolean isSetAuthenticatedUser() {
        return (this.authenticatedUser!= null);
    }

    public void unsetAuthenticatedUser() {
        this.authenticatedUser = null;
    }

}
