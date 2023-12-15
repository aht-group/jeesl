
package org.jeesl.model.xml.system.security;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
     * Ruft den Wert der publicUser-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPublicUser() {
        return publicUser;
    }

    /**
     * Legt den Wert der publicUser-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPublicUser(Boolean value) {
        this.publicUser = value;
    }

    /**
     * Ruft den Wert der authenticatedUser-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAuthenticatedUser() {
        return authenticatedUser;
    }

    /**
     * Legt den Wert der authenticatedUser-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAuthenticatedUser(Boolean value) {
        this.authenticatedUser = value;
    }

}
