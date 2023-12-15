
package org.jeesl.model.xml.system.navigation;

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
 *         &lt;element ref="{http://ahtutils.aht-group.com/navigation}viewPattern"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/navigation}urlMapping"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="code" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="package" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "viewPattern",
    "urlMapping"
})
@XmlRootElement(name = "navigation")
public class Navigation
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected ViewPattern viewPattern;
    @XmlElement(required = true)
    protected UrlMapping urlMapping;
    @XmlAttribute(name = "code")
    protected String code;
    @XmlAttribute(name = "package")
    protected String _package;

    /**
     * Ruft den Wert der viewPattern-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ViewPattern }
     *     
     */
    public ViewPattern getViewPattern() {
        return viewPattern;
    }

    /**
     * Legt den Wert der viewPattern-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ViewPattern }
     *     
     */
    public void setViewPattern(ViewPattern value) {
        this.viewPattern = value;
    }

    /**
     * Ruft den Wert der urlMapping-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link UrlMapping }
     *     
     */
    public UrlMapping getUrlMapping() {
        return urlMapping;
    }

    /**
     * Legt den Wert der urlMapping-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link UrlMapping }
     *     
     */
    public void setUrlMapping(UrlMapping value) {
        this.urlMapping = value;
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
     * Ruft den Wert der package-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPackage() {
        return _package;
    }

    /**
     * Legt den Wert der package-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPackage(String value) {
        this._package = value;
    }

}
