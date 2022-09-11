
package net.sf.ahtutils.xml.report;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
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
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/report}dataAssociations"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="targetClass" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "dataAssociations"
})
@XmlRootElement(name = "importStructure")
public class ImportStructure implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected DataAssociations dataAssociations;
    @XmlAttribute(name = "targetClass")
    protected String targetClass;

    /**
     * Gets the value of the dataAssociations property.
     * 
     * @return
     *     possible object is
     *     {@link DataAssociations }
     *     
     */
    public DataAssociations getDataAssociations() {
        return dataAssociations;
    }

    /**
     * Sets the value of the dataAssociations property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataAssociations }
     *     
     */
    public void setDataAssociations(DataAssociations value) {
        this.dataAssociations = value;
    }

    public boolean isSetDataAssociations() {
        return (this.dataAssociations!= null);
    }

    /**
     * Gets the value of the targetClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTargetClass() {
        return targetClass;
    }

    /**
     * Sets the value of the targetClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTargetClass(String value) {
        this.targetClass = value;
    }

    public boolean isSetTargetClass() {
        return (this.targetClass!= null);
    }

}
