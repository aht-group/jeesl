
package net.sf.ahtutils.xml.security;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import net.sf.ahtutils.xml.status.Descriptions;
import net.sf.ahtutils.xml.status.Langs;


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
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}descriptions"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}langs"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/security}category"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/security}views"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/security}actions"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/security}usecases"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/security}staffs"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}long" /&gt;
 *       &lt;attribute name="code" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="position" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="visible" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="documentation" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="label" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "descriptions",
    "langs",
    "category",
    "views",
    "actions",
    "usecases",
    "staffs"
})
@XmlRootElement(name = "role")
public class Role
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/status", required = true)
    protected Descriptions descriptions;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/status", required = true)
    protected Langs langs;
    @XmlElement(required = true)
    protected Category category;
    @XmlElement(required = true)
    protected Views views;
    @XmlElement(required = true)
    protected Actions actions;
    @XmlElement(required = true)
    protected Usecases usecases;
    @XmlElement(required = true)
    protected Staffs staffs;
    @XmlAttribute(name = "id")
    protected Long id;
    @XmlAttribute(name = "code")
    protected String code;
    @XmlAttribute(name = "position")
    protected Integer position;
    @XmlAttribute(name = "visible")
    protected Boolean visible;
    @XmlAttribute(name = "documentation")
    protected Boolean documentation;
    @XmlAttribute(name = "label")
    protected String label;

    /**
     * Gets the value of the descriptions property.
     * 
     * @return
     *     possible object is
     *     {@link Descriptions }
     *     
     */
    public Descriptions getDescriptions() {
        return descriptions;
    }

    /**
     * Sets the value of the descriptions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Descriptions }
     *     
     */
    public void setDescriptions(Descriptions value) {
        this.descriptions = value;
    }

    public boolean isSetDescriptions() {
        return (this.descriptions!= null);
    }

    /**
     * Gets the value of the langs property.
     * 
     * @return
     *     possible object is
     *     {@link Langs }
     *     
     */
    public Langs getLangs() {
        return langs;
    }

    /**
     * Sets the value of the langs property.
     * 
     * @param value
     *     allowed object is
     *     {@link Langs }
     *     
     */
    public void setLangs(Langs value) {
        this.langs = value;
    }

    public boolean isSetLangs() {
        return (this.langs!= null);
    }

    /**
     * Gets the value of the category property.
     * 
     * @return
     *     possible object is
     *     {@link Category }
     *     
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Sets the value of the category property.
     * 
     * @param value
     *     allowed object is
     *     {@link Category }
     *     
     */
    public void setCategory(Category value) {
        this.category = value;
    }

    public boolean isSetCategory() {
        return (this.category!= null);
    }

    /**
     * Gets the value of the views property.
     * 
     * @return
     *     possible object is
     *     {@link Views }
     *     
     */
    public Views getViews() {
        return views;
    }

    /**
     * Sets the value of the views property.
     * 
     * @param value
     *     allowed object is
     *     {@link Views }
     *     
     */
    public void setViews(Views value) {
        this.views = value;
    }

    public boolean isSetViews() {
        return (this.views!= null);
    }

    /**
     * Gets the value of the actions property.
     * 
     * @return
     *     possible object is
     *     {@link Actions }
     *     
     */
    public Actions getActions() {
        return actions;
    }

    /**
     * Sets the value of the actions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Actions }
     *     
     */
    public void setActions(Actions value) {
        this.actions = value;
    }

    public boolean isSetActions() {
        return (this.actions!= null);
    }

    /**
     * Gets the value of the usecases property.
     * 
     * @return
     *     possible object is
     *     {@link Usecases }
     *     
     */
    public Usecases getUsecases() {
        return usecases;
    }

    /**
     * Sets the value of the usecases property.
     * 
     * @param value
     *     allowed object is
     *     {@link Usecases }
     *     
     */
    public void setUsecases(Usecases value) {
        this.usecases = value;
    }

    public boolean isSetUsecases() {
        return (this.usecases!= null);
    }

    /**
     * Gets the value of the staffs property.
     * 
     * @return
     *     possible object is
     *     {@link Staffs }
     *     
     */
    public Staffs getStaffs() {
        return staffs;
    }

    /**
     * Sets the value of the staffs property.
     * 
     * @param value
     *     allowed object is
     *     {@link Staffs }
     *     
     */
    public void setStaffs(Staffs value) {
        this.staffs = value;
    }

    public boolean isSetStaffs() {
        return (this.staffs!= null);
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setId(long value) {
        this.id = value;
    }

    public boolean isSetId() {
        return (this.id!= null);
    }

    public void unsetId() {
        this.id = null;
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

    public boolean isSetCode() {
        return (this.code!= null);
    }

    /**
     * Gets the value of the position property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets the value of the position property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPosition(int value) {
        this.position = value;
    }

    public boolean isSetPosition() {
        return (this.position!= null);
    }

    public void unsetPosition() {
        this.position = null;
    }

    /**
     * Gets the value of the visible property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets the value of the visible property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setVisible(boolean value) {
        this.visible = value;
    }

    public boolean isSetVisible() {
        return (this.visible!= null);
    }

    public void unsetVisible() {
        this.visible = null;
    }

    /**
     * Gets the value of the documentation property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isDocumentation() {
        return documentation;
    }

    /**
     * Sets the value of the documentation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDocumentation(boolean value) {
        this.documentation = value;
    }

    public boolean isSetDocumentation() {
        return (this.documentation!= null);
    }

    public void unsetDocumentation() {
        this.documentation = null;
    }

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

    public boolean isSetLabel() {
        return (this.label!= null);
    }

}
