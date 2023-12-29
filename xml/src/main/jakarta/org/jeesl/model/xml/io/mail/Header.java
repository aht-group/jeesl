
package org.jeesl.model.xml.io.mail;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
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
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.jeesl.org/io/mail}from"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/io/mail}to"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/io/mail}cc"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/io/mail}bcc"/&gt;
 *         &lt;element name="subject"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
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
    "from",
    "to",
    "cc",
    "bcc",
    "subject"
})
@XmlRootElement(name = "header")
public class Header
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected From from;
    @XmlElement(required = true)
    protected To to;
    @XmlElement(required = true)
    protected Cc cc;
    @XmlElement(required = true)
    protected Bcc bcc;
    @XmlElement(namespace = "", required = true)
    protected String subject;

    /**
     * Ruft den Wert der from-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link From }
     *     
     */
    public From getFrom() {
        return from;
    }

    /**
     * Legt den Wert der from-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link From }
     *     
     */
    public void setFrom(From value) {
        this.from = value;
    }

    /**
     * Ruft den Wert der to-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link To }
     *     
     */
    public To getTo() {
        return to;
    }

    /**
     * Legt den Wert der to-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link To }
     *     
     */
    public void setTo(To value) {
        this.to = value;
    }

    /**
     * Ruft den Wert der cc-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Cc }
     *     
     */
    public Cc getCc() {
        return cc;
    }

    /**
     * Legt den Wert der cc-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Cc }
     *     
     */
    public void setCc(Cc value) {
        this.cc = value;
    }

    /**
     * Ruft den Wert der bcc-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Bcc }
     *     
     */
    public Bcc getBcc() {
        return bcc;
    }

    /**
     * Legt den Wert der bcc-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Bcc }
     *     
     */
    public void setBcc(Bcc value) {
        this.bcc = value;
    }

    /**
     * Ruft den Wert der subject-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Legt den Wert der subject-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubject(String value) {
        this.subject = value;
    }

}
