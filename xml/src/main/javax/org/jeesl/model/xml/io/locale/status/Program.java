
package org.jeesl.model.xml.io.locale.status;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}transistions"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}langs"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}descriptions"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}lang" maxOccurs="unbounded"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}parent"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}long" /&gt;
 *       &lt;attribute name="key" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="code" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="group" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="label" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="visible" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="image" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="position" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "transistions",
    "langs",
    "descriptions",
    "lang",
    "parent"
})
@XmlRootElement(name = "program")
public class Program
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected Transistions transistions;
    @XmlElement(required = true)
    protected Langs langs;
    @XmlElement(required = true)
    protected Descriptions descriptions;
    @XmlElement(required = true)
    protected List<Lang> lang;
    @XmlElement(required = true)
    protected Parent parent;
    @XmlAttribute(name = "id")
    protected Long id;
    @XmlAttribute(name = "key")
    protected String key;
    @XmlAttribute(name = "code")
    protected String code;
    @XmlAttribute(name = "group")
    protected String group;
    @XmlAttribute(name = "label")
    protected String label;
    @XmlAttribute(name = "visible")
    protected Boolean visible;
    @XmlAttribute(name = "image")
    protected String image;
    @XmlAttribute(name = "position")
    protected Integer position;

    /**
     * Gets the value of the transistions property.
     * 
     * @return
     *     possible object is
     *     {@link Transistions }
     *     
     */
    public Transistions getTransistions() {
        return transistions;
    }

    /**
     * Sets the value of the transistions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Transistions }
     *     
     */
    public void setTransistions(Transistions value) {
        this.transistions = value;
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

    /**
     * Gets the value of the lang property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lang property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLang().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Lang }
     * 
     * 
     */
    public List<Lang> getLang() {
        if (lang == null) {
            lang = new ArrayList<Lang>();
        }
        return this.lang;
    }

    /**
     * Gets the value of the parent property.
     * 
     * @return
     *     possible object is
     *     {@link Parent }
     *     
     */
    public Parent getParent() {
        return parent;
    }

    /**
     * Sets the value of the parent property.
     * 
     * @param value
     *     allowed object is
     *     {@link Parent }
     *     
     */
    public void setParent(Parent value) {
        this.parent = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getId() {
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
    public void setId(Long value) {
        this.id = value;
    }

    /**
     * Gets the value of the key property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the value of the key property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKey(String value) {
        this.key = value;
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

    /**
     * Gets the value of the group property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroup() {
        return group;
    }

    /**
     * Sets the value of the group property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroup(String value) {
        this.group = value;
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

    /**
     * Gets the value of the visible property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isVisible() {
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
    public void setVisible(Boolean value) {
        this.visible = value;
    }

    /**
     * Gets the value of the image property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImage() {
        return image;
    }

    /**
     * Sets the value of the image property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImage(String value) {
        this.image = value;
    }

    /**
     * Gets the value of the position property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPosition() {
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
    public void setPosition(Integer value) {
        this.position = value;
    }

}
