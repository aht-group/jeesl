
package org.jeesl.model.xml.io.report;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
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
     * Ruft den Wert der dataAssociations-Eigenschaft ab.
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
     * Legt den Wert der dataAssociations-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link DataAssociations }
     *     
     */
    public void setDataAssociations(DataAssociations value) {
        this.dataAssociations = value;
    }

    /**
     * Ruft den Wert der targetClass-Eigenschaft ab.
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
     * Legt den Wert der targetClass-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTargetClass(String value) {
        this.targetClass = value;
    }

}
