
package net.sf.ahtutils.xml.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import net.sf.ahtutils.xml.symbol.Color;


/*
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/report}font"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/report}offset"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/report}size" maxOccurs="unbounded"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/symbol}color" maxOccurs="unbounded"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/report}styles"/&gt;
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
    "font",
    "offset",
    "size",
    "color",
    "styles"
})
@XmlRootElement(name = "layout")
public class Layout
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected Font font;
    @XmlElement(required = true)
    protected Offset offset;
    @XmlElement(required = true)
    protected List<Size> size;
    @XmlElement(namespace = "http://www.jeesl.org/symbol", required = true)
    protected List<Color> color;
    @XmlElement(required = true)
    protected Styles styles;

    /*
     * Gets the value of the font property.
     * 
     * @return
     *     possible object is
     *     {@link Font }
     *     
     */
    public Font getFont() {
        return font;
    }

    /*
     * Sets the value of the font property.
     * 
     * @param value
     *     allowed object is
     *     {@link Font }
     *     
     */
    public void setFont(Font value) {
        this.font = value;
    }

    public boolean isSetFont() {
        return (this.font!= null);
    }

    /*
     * Gets the value of the offset property.
     * 
     * @return
     *     possible object is
     *     {@link Offset }
     *     
     */
    public Offset getOffset() {
        return offset;
    }

    /*
     * Sets the value of the offset property.
     * 
     * @param value
     *     allowed object is
     *     {@link Offset }
     *     
     */
    public void setOffset(Offset value) {
        this.offset = value;
    }

    public boolean isSetOffset() {
        return (this.offset!= null);
    }


    public List<Size> getSize() {
        if (size == null) {
            size = new ArrayList<Size>();
        }
        return this.size;
    }

    public boolean isSetSize() {
        return ((this.size!= null)&&(!this.size.isEmpty()));
    }

    public void unsetSize() {
        this.size = null;
    }


    public List<Color> getColor() {
        if (color == null) {
            color = new ArrayList<Color>();
        }
        return this.color;
    }

    public boolean isSetColor() {
        return ((this.color!= null)&&(!this.color.isEmpty()));
    }

    public void unsetColor() {
        this.color = null;
    }

    /*
     * Gets the value of the styles property.
     * 
     * @return
     *     possible object is
     *     {@link Styles }
     *     
     */
    public Styles getStyles() {
        return styles;
    }

    /*
     * Sets the value of the styles property.
     * 
     * @param value
     *     allowed object is
     *     {@link Styles }
     *     
     */
    public void setStyles(Styles value) {
        this.styles = value;
    }

    public boolean isSetStyles() {
        return (this.styles!= null);
    }

}
