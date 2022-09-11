
package net.sf.ahtutils.xml.cloud.facebook;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/cloud/facebook}user"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/cloud/facebook}oauth"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="issuedAt" type="{http://www.w3.org/2001/XMLSchema}dateTime" /&gt;
 *       &lt;attribute name="expires" type="{http://www.w3.org/2001/XMLSchema}dateTime" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "user",
    "oauth"
})
@XmlRootElement(name = "signedRequest")
public class SignedRequest
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected User user;
    @XmlElement(required = true)
    protected Oauth oauth;
    @XmlAttribute(name = "issuedAt")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar issuedAt;
    @XmlAttribute(name = "expires")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar expires;

    /**
     * Gets the value of the user property.
     * 
     * @return
     *     possible object is
     *     {@link User }
     *     
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the value of the user property.
     * 
     * @param value
     *     allowed object is
     *     {@link User }
     *     
     */
    public void setUser(User value) {
        this.user = value;
    }

    public boolean isSetUser() {
        return (this.user!= null);
    }

    /**
     * Gets the value of the oauth property.
     * 
     * @return
     *     possible object is
     *     {@link Oauth }
     *     
     */
    public Oauth getOauth() {
        return oauth;
    }

    /**
     * Sets the value of the oauth property.
     * 
     * @param value
     *     allowed object is
     *     {@link Oauth }
     *     
     */
    public void setOauth(Oauth value) {
        this.oauth = value;
    }

    public boolean isSetOauth() {
        return (this.oauth!= null);
    }

    /**
     * Gets the value of the issuedAt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getIssuedAt() {
        return issuedAt;
    }

    /**
     * Sets the value of the issuedAt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setIssuedAt(XMLGregorianCalendar value) {
        this.issuedAt = value;
    }

    public boolean isSetIssuedAt() {
        return (this.issuedAt!= null);
    }

    /**
     * Gets the value of the expires property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getExpires() {
        return expires;
    }

    /**
     * Sets the value of the expires property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setExpires(XMLGregorianCalendar value) {
        this.expires = value;
    }

    public boolean isSetExpires() {
        return (this.expires!= null);
    }

}
