
package org.jeesl.model.xml.system.revision;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import net.sf.ahtutils.xml.status.Type;


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
 *         &lt;element ref="{http://www.jeesl.org/revision}entity"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}type"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="owner" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="docOptionsTable" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="docOptionsInline" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "entity",
    "type"
})
@XmlRootElement(name = "relation")
public class Relation
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected Entity entity;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/status", required = true)
    protected Type type;
    @XmlAttribute(name = "owner")
    protected Boolean owner;
    @XmlAttribute(name = "docOptionsTable")
    protected Boolean docOptionsTable;
    @XmlAttribute(name = "docOptionsInline")
    protected Boolean docOptionsInline;

    /**
     * Gets the value of the entity property.
     * 
     * @return
     *     possible object is
     *     {@link Entity }
     *     
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Sets the value of the entity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Entity }
     *     
     */
    public void setEntity(Entity value) {
        this.entity = value;
    }

    public boolean isSetEntity() {
        return (this.entity!= null);
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link Type }
     *     
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link Type }
     *     
     */
    public void setType(Type value) {
        this.type = value;
    }

    public boolean isSetType() {
        return (this.type!= null);
    }

    /**
     * Gets the value of the owner property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isOwner() {
        return owner;
    }

    /**
     * Sets the value of the owner property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOwner(boolean value) {
        this.owner = value;
    }

    public boolean isSetOwner() {
        return (this.owner!= null);
    }

    public void unsetOwner() {
        this.owner = null;
    }

    /**
     * Gets the value of the docOptionsTable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isDocOptionsTable() {
        return docOptionsTable;
    }

    /**
     * Sets the value of the docOptionsTable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDocOptionsTable(boolean value) {
        this.docOptionsTable = value;
    }

    public boolean isSetDocOptionsTable() {
        return (this.docOptionsTable!= null);
    }

    public void unsetDocOptionsTable() {
        this.docOptionsTable = null;
    }

    /**
     * Gets the value of the docOptionsInline property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isDocOptionsInline() {
        return docOptionsInline;
    }

    /**
     * Sets the value of the docOptionsInline property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDocOptionsInline(boolean value) {
        this.docOptionsInline = value;
    }

    public boolean isSetDocOptionsInline() {
        return (this.docOptionsInline!= null);
    }

    public void unsetDocOptionsInline() {
        this.docOptionsInline = null;
    }

}
