
package net.sf.ahtutils.xml.access;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *       &lt;/sequence&gt;
 *       &lt;attribute name="lang" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="delQueryAfterProcessing" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "aclQuery")
public class AclQuery
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlAttribute(name = "lang")
    protected String lang;
    @XmlAttribute(name = "delQueryAfterProcessing")
    protected Boolean delQueryAfterProcessing;

    /*
     * Gets the value of the lang property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLang() {
        return lang;
    }

    /*
     * Sets the value of the lang property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLang(String value) {
        this.lang = value;
    }

    public boolean isSetLang() {
        return (this.lang!= null);
    }

    /*
     * Gets the value of the delQueryAfterProcessing property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isDelQueryAfterProcessing() {
        return delQueryAfterProcessing;
    }

    /*
     * Sets the value of the delQueryAfterProcessing property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDelQueryAfterProcessing(boolean value) {
        this.delQueryAfterProcessing = value;
    }

    public boolean isSetDelQueryAfterProcessing() {
        return (this.delQueryAfterProcessing!= null);
    }

    public void unsetDelQueryAfterProcessing() {
        this.delQueryAfterProcessing = null;
    }

}
