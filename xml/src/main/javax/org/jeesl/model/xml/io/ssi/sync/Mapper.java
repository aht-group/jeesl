
package org.jeesl.model.xml.io.ssi.sync;

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
 *       &lt;attribute name="oldId" type="{http://www.w3.org/2001/XMLSchema}long" /&gt;
 *       &lt;attribute name="newId" type="{http://www.w3.org/2001/XMLSchema}long" /&gt;
 *       &lt;attribute name="class" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="oldCode" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="newCode" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="code" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "mapper")
public class Mapper
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlAttribute(name = "oldId")
    protected Long oldId;
    @XmlAttribute(name = "newId")
    protected Long newId;
    @XmlAttribute(name = "class")
    protected String clazz;
    @XmlAttribute(name = "oldCode")
    protected String oldCode;
    @XmlAttribute(name = "newCode")
    protected String newCode;
    @XmlAttribute(name = "code")
    protected String code;

    /**
     * Gets the value of the oldId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getOldId() {
        return oldId;
    }

    /**
     * Sets the value of the oldId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setOldId(Long value) {
        this.oldId = value;
    }

    /**
     * Gets the value of the newId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getNewId() {
        return newId;
    }

    /**
     * Sets the value of the newId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setNewId(Long value) {
        this.newId = value;
    }

    /**
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

    /**
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

    /**
     * Gets the value of the oldCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOldCode() {
        return oldCode;
    }

    /**
     * Sets the value of the oldCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOldCode(String value) {
        this.oldCode = value;
    }

    /**
     * Gets the value of the newCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewCode() {
        return newCode;
    }

    /**
     * Sets the value of the newCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewCode(String value) {
        this.newCode = value;
    }

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCode(String value) {
        this.code = value;
    }

}
