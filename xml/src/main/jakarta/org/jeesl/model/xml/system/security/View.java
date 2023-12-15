
package org.jeesl.model.xml.system.security;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import net.sf.ahtutils.xml.status.Descriptions;
import net.sf.ahtutils.xml.status.Langs;
import org.jeesl.model.xml.system.navigation.Navigation;


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
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}descriptions"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}langs"/&gt;
 *         &lt;element ref="{https://www.jeesl.org/jeesl/xsd/system/security}access"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/navigation}navigation"/&gt;
 *         &lt;element ref="{https://www.jeesl.org/jeesl/xsd/system/security}actions"/&gt;
 *         &lt;element ref="{https://www.jeesl.org/jeesl/xsd/system/security}roles"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}long" /&gt;
 *       &lt;attribute name="code" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="label" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="position" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="visible" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="documentation" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="public" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="onlyLoginRequired" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="urlParameter" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
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
    "access",
    "navigation",
    "actions",
    "roles"
})
@XmlRootElement(name = "view")
public class View
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/status", required = true)
    protected Descriptions descriptions;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/status", required = true)
    protected Langs langs;
    @XmlElement(required = true)
    protected Access access;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/navigation", required = true)
    protected Navigation navigation;
    @XmlElement(required = true)
    protected Actions actions;
    @XmlElement(required = true)
    protected Roles roles;
    @XmlAttribute(name = "id")
    protected Long id;
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
    @XmlAttribute(name = "public")
    protected Boolean _public;
    @XmlAttribute(name = "onlyLoginRequired")
    protected Boolean onlyLoginRequired;
    @XmlAttribute(name = "urlParameter")
    protected String urlParameter;

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
     * Ruft den Wert der access-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Access }
     *     
     */
    public Access getAccess() {
        return access;
    }

    /**
     * Legt den Wert der access-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Access }
     *     
     */
    public void setAccess(Access value) {
        this.access = value;
    }

    /**
     * Ruft den Wert der navigation-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Navigation }
     *     
     */
    public Navigation getNavigation() {
        return navigation;
    }

    /**
     * Legt den Wert der navigation-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Navigation }
     *     
     */
    public void setNavigation(Navigation value) {
        this.navigation = value;
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
     * Ruft den Wert der id-Eigenschaft ab.
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
     * Legt den Wert der id-Eigenschaft fest.
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

    /**
     * Ruft den Wert der public-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPublic() {
        return _public;
    }

    /**
     * Legt den Wert der public-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPublic(Boolean value) {
        this._public = value;
    }

    /**
     * Ruft den Wert der onlyLoginRequired-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isOnlyLoginRequired() {
        return onlyLoginRequired;
    }

    /**
     * Legt den Wert der onlyLoginRequired-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOnlyLoginRequired(Boolean value) {
        this.onlyLoginRequired = value;
    }

    /**
     * Ruft den Wert der urlParameter-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrlParameter() {
        return urlParameter;
    }

    /**
     * Legt den Wert der urlParameter-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrlParameter(String value) {
        this.urlParameter = value;
    }

}
