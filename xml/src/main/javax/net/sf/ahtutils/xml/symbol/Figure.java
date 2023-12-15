
package net.sf.ahtutils.xml.symbol;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import net.sf.ahtutils.xml.status.Style;


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
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}style"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}long" /&gt;
 *       &lt;attribute name="css" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="color" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="size" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="rotation" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="offsetX" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="offsetY" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "style"
})
@XmlRootElement(name = "figure")
public class Figure
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/status", required = true)
    protected Style style;
    @XmlAttribute(name = "id")
    protected Long id;
    @XmlAttribute(name = "css")
    protected Boolean css;
    @XmlAttribute(name = "color")
    protected String color;
    @XmlAttribute(name = "size")
    protected Integer size;
    @XmlAttribute(name = "rotation")
    protected Integer rotation;
    @XmlAttribute(name = "offsetX")
    protected Integer offsetX;
    @XmlAttribute(name = "offsetY")
    protected Integer offsetY;

    /**
     * Gets the value of the style property.
     * 
     * @return
     *     possible object is
     *     {@link Style }
     *     
     */
    public Style getStyle() {
        return style;
    }

    /**
     * Sets the value of the style property.
     * 
     * @param value
     *     allowed object is
     *     {@link Style }
     *     
     */
    public void setStyle(Style value) {
        this.style = value;
    }

    public boolean isSetStyle() {
        return (this.style!= null);
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
     * Gets the value of the css property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isCss() {
        return css;
    }

    /**
     * Sets the value of the css property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCss(boolean value) {
        this.css = value;
    }

    public boolean isSetCss() {
        return (this.css!= null);
    }

    public void unsetCss() {
        this.css = null;
    }

    /**
     * Gets the value of the color property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the value of the color property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColor(String value) {
        this.color = value;
    }

    public boolean isSetColor() {
        return (this.color!= null);
    }

    /**
     * Gets the value of the size property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets the value of the size property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSize(int value) {
        this.size = value;
    }

    public boolean isSetSize() {
        return (this.size!= null);
    }

    public void unsetSize() {
        this.size = null;
    }

    /**
     * Gets the value of the rotation property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getRotation() {
        return rotation;
    }

    /**
     * Sets the value of the rotation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRotation(int value) {
        this.rotation = value;
    }

    public boolean isSetRotation() {
        return (this.rotation!= null);
    }

    public void unsetRotation() {
        this.rotation = null;
    }

    /**
     * Gets the value of the offsetX property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getOffsetX() {
        return offsetX;
    }

    /**
     * Sets the value of the offsetX property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOffsetX(int value) {
        this.offsetX = value;
    }

    public boolean isSetOffsetX() {
        return (this.offsetX!= null);
    }

    public void unsetOffsetX() {
        this.offsetX = null;
    }

    /**
     * Gets the value of the offsetY property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getOffsetY() {
        return offsetY;
    }

    /**
     * Sets the value of the offsetY property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOffsetY(int value) {
        this.offsetY = value;
    }

    public boolean isSetOffsetY() {
        return (this.offsetY!= null);
    }

    public void unsetOffsetY() {
        this.offsetY = null;
    }

}
