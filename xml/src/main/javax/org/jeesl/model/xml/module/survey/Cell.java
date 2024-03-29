
package org.jeesl.model.xml.module.survey;

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
 *       &lt;sequence&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="label" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="valueOption" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="valueBoolean" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="valueNumber" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="valueDouble" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
 *       &lt;attribute name="valueText" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "cell")
public class Cell
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlAttribute(name = "label")
    protected String label;
    @XmlAttribute(name = "valueOption")
    protected String valueOption;
    @XmlAttribute(name = "valueBoolean")
    protected Boolean valueBoolean;
    @XmlAttribute(name = "valueNumber")
    protected Integer valueNumber;
    @XmlAttribute(name = "valueDouble")
    protected Double valueDouble;
    @XmlAttribute(name = "valueText")
    protected String valueText;

    /**
     * Gets the value of the label property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the value of the label property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabel(String value) {
        this.label = value;
    }

    /**
     * Gets the value of the valueOption property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValueOption() {
        return valueOption;
    }

    /**
     * Sets the value of the valueOption property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValueOption(String value) {
        this.valueOption = value;
    }

    /**
     * Gets the value of the valueBoolean property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isValueBoolean() {
        return valueBoolean;
    }

    /**
     * Sets the value of the valueBoolean property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setValueBoolean(Boolean value) {
        this.valueBoolean = value;
    }

    /**
     * Gets the value of the valueNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getValueNumber() {
        return valueNumber;
    }

    /**
     * Sets the value of the valueNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setValueNumber(Integer value) {
        this.valueNumber = value;
    }

    /**
     * Gets the value of the valueDouble property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getValueDouble() {
        return valueDouble;
    }

    /**
     * Sets the value of the valueDouble property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setValueDouble(Double value) {
        this.valueDouble = value;
    }

    /**
     * Gets the value of the valueText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValueText() {
        return valueText;
    }

    /**
     * Sets the value of the valueText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValueText(String value) {
        this.valueText = value;
    }

}
