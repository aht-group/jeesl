
package org.jeesl.model.xml.io.cms.symbol;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import net.sf.ahtutils.xml.status.Styles;


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
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}styles"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/symbol}colors"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/symbol}sizes"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "styles",
    "colors",
    "sizes"
})
@XmlRootElement(name = "symbol")
public class Symbol
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/status", required = true)
    protected Styles styles;
    @XmlElement(required = true)
    protected Colors colors;
    @XmlElement(required = true)
    protected Sizes sizes;

    /**
     * Ruft den Wert der styles-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Styles }
     *     
     */
    public Styles getStyles() {
        return styles;
    }

    /**
     * Legt den Wert der styles-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Styles }
     *     
     */
    public void setStyles(Styles value) {
        this.styles = value;
    }

    /**
     * Ruft den Wert der colors-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Colors }
     *     
     */
    public Colors getColors() {
        return colors;
    }

    /**
     * Legt den Wert der colors-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Colors }
     *     
     */
    public void setColors(Colors value) {
        this.colors = value;
    }

    /**
     * Ruft den Wert der sizes-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Sizes }
     *     
     */
    public Sizes getSizes() {
        return sizes;
    }

    /**
     * Legt den Wert der sizes-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Sizes }
     *     
     */
    public void setSizes(Sizes value) {
        this.sizes = value;
    }

}
