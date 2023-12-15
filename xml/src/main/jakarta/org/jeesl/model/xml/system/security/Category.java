
package org.jeesl.model.xml.system.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import org.jeesl.model.xml.io.locale.status.Descriptions;
import org.jeesl.model.xml.io.locale.status.Langs;


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
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}langs"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}descriptions"/&gt;
 *         &lt;element ref="{https://www.jeesl.org/jeesl/xsd/system/security}roles"/&gt;
 *         &lt;element ref="{https://www.jeesl.org/jeesl/xsd/system/security}actions"/&gt;
 *         &lt;element ref="{https://www.jeesl.org/jeesl/xsd/system/security}templates"/&gt;
 *         &lt;element ref="{https://www.jeesl.org/jeesl/xsd/system/security}usecases"/&gt;
 *         &lt;element ref="{https://www.jeesl.org/jeesl/xsd/system/security}tmp"/&gt;
 *         &lt;element ref="{https://www.jeesl.org/jeesl/xsd/system/security}views"/&gt;
 *         &lt;element ref="{https://www.jeesl.org/jeesl/xsd/system/security}staffs" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="code" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="label" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="position" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="visible" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="documentation" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "langs",
    "descriptions",
    "roles",
    "actions",
    "templates",
    "usecases",
    "tmp",
    "views",
    "staffs"
})
@XmlRootElement(name = "category")
public class Category
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/status", required = true)
    protected Langs langs;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/status", required = true)
    protected Descriptions descriptions;
    @XmlElement(required = true)
    protected Roles roles;
    @XmlElement(required = true)
    protected Actions actions;
    @XmlElement(required = true)
    protected Templates templates;
    @XmlElement(required = true)
    protected Usecases usecases;
    @XmlElement(required = true)
    protected Tmp tmp;
    @XmlElement(required = true)
    protected Views views;
    @XmlElement(required = true)
    protected List<Staffs> staffs;
    @XmlAttribute(name = "code")
    protected String code;
    @XmlAttribute(name = "label")
    protected String label;
    @XmlAttribute(name = "position")
    protected Integer position;
    @XmlAttribute(name = "visible")
    protected Boolean visible;
    @XmlAttribute(name = "documentation")
    protected Boolean documentation;

    /**
     * Ruft den Wert der langs-Eigenschaft ab.
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
     * Legt den Wert der langs-Eigenschaft fest.
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
     * Ruft den Wert der descriptions-Eigenschaft ab.
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
     * Legt den Wert der descriptions-Eigenschaft fest.
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
     * Ruft den Wert der roles-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Roles }
     *     
     */
    public Roles getRoles() {
        return roles;
    }

    /**
     * Legt den Wert der roles-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Roles }
     *     
     */
    public void setRoles(Roles value) {
        this.roles = value;
    }

    /**
     * Ruft den Wert der actions-Eigenschaft ab.
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
     * Legt den Wert der actions-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Actions }
     *     
     */
    public void setActions(Actions value) {
        this.actions = value;
    }

    /**
     * Ruft den Wert der templates-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Templates }
     *     
     */
    public Templates getTemplates() {
        return templates;
    }

    /**
     * Legt den Wert der templates-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Templates }
     *     
     */
    public void setTemplates(Templates value) {
        this.templates = value;
    }

    /**
     * Ruft den Wert der usecases-Eigenschaft ab.
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
     * Legt den Wert der usecases-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Usecases }
     *     
     */
    public void setUsecases(Usecases value) {
        this.usecases = value;
    }

    /**
     * Ruft den Wert der tmp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Tmp }
     *     
     */
    public Tmp getTmp() {
        return tmp;
    }

    /**
     * Legt den Wert der tmp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Tmp }
     *     
     */
    public void setTmp(Tmp value) {
        this.tmp = value;
    }

    /**
     * Ruft den Wert der views-Eigenschaft ab.
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
     * Legt den Wert der views-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Views }
     *     
     */
    public void setViews(Views value) {
        this.views = value;
    }

    /**
     * Gets the value of the staffs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the staffs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStaffs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Staffs }
     * 
     * 
     */
    public List<Staffs> getStaffs() {
        if (staffs == null) {
            staffs = new ArrayList<Staffs>();
        }
        return this.staffs;
    }

    /**
     * Ruft den Wert der code-Eigenschaft ab.
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
     * Legt den Wert der code-Eigenschaft fest.
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
     * Ruft den Wert der label-Eigenschaft ab.
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
     * Legt den Wert der label-Eigenschaft fest.
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
     * Ruft den Wert der position-Eigenschaft ab.
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
     * Legt den Wert der position-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPosition(Integer value) {
        this.position = value;
    }

    /**
     * Ruft den Wert der visible-Eigenschaft ab.
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
     * Legt den Wert der visible-Eigenschaft fest.
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
     * Ruft den Wert der documentation-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDocumentation() {
        return documentation;
    }

    /**
     * Legt den Wert der documentation-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDocumentation(Boolean value) {
        this.documentation = value;
    }

}
