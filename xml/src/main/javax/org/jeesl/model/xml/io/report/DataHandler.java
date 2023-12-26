
package org.jeesl.model.xml.io.report;

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
 *       &lt;attribute name="class" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="handledBy" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="validatedBy" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "dataHandler")
public class DataHandler
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlAttribute(name = "class")
    protected String clazz;
    @XmlAttribute(name = "handledBy")
    protected String handledBy;
    @XmlAttribute(name = "validatedBy")
    protected String validatedBy;

    /*
     * Gets the value of the clazz property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClazz() {
        return clazz;
    }

    /*
     * Sets the value of the clazz property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClazz(String value) {
        this.clazz = value;
    }

    public boolean isSetClazz() {
        return (this.clazz!= null);
    }

    /*
     * Gets the value of the handledBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHandledBy() {
        return handledBy;
    }

    /*
     * Sets the value of the handledBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHandledBy(String value) {
        this.handledBy = value;
    }

    public boolean isSetHandledBy() {
        return (this.handledBy!= null);
    }

    /*
     * Gets the value of the validatedBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValidatedBy() {
        return validatedBy;
    }

    /*
     * Sets the value of the validatedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValidatedBy(String value) {
        this.validatedBy = value;
    }

    public boolean isSetValidatedBy() {
        return (this.validatedBy!= null);
    }

}
