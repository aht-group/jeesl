
package net.sf.ahtutils.xml.report;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 					This is for xlsTransformation
 * 				
 * 
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="dataClass" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="formatPattern" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="beanProperty" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="xPath" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "xlsTransformation")
public class XlsTransformation
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlAttribute(name = "dataClass")
    protected String dataClass;
    @XmlAttribute(name = "formatPattern")
    protected String formatPattern;
    @XmlAttribute(name = "beanProperty")
    protected String beanProperty;
    @XmlAttribute(name = "xPath")
    protected String xPath;

    /**
     * Gets the value of the dataClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataClass() {
        return dataClass;
    }

    /**
     * Sets the value of the dataClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataClass(String value) {
        this.dataClass = value;
    }

    public boolean isSetDataClass() {
        return (this.dataClass!= null);
    }

    /**
     * Gets the value of the formatPattern property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormatPattern() {
        return formatPattern;
    }

    /**
     * Sets the value of the formatPattern property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormatPattern(String value) {
        this.formatPattern = value;
    }

    public boolean isSetFormatPattern() {
        return (this.formatPattern!= null);
    }

    /**
     * Gets the value of the beanProperty property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeanProperty() {
        return beanProperty;
    }

    /**
     * Sets the value of the beanProperty property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeanProperty(String value) {
        this.beanProperty = value;
    }

    public boolean isSetBeanProperty() {
        return (this.beanProperty!= null);
    }

    /**
     * Gets the value of the xPath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXPath() {
        return xPath;
    }

    /**
     * Sets the value of the xPath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXPath(String value) {
        this.xPath = value;
    }

    public boolean isSetXPath() {
        return (this.xPath!= null);
    }

}
