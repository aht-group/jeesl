
package org.jeesl.model.xml.io.report;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.jeesl.model.xml.io.locale.status.DataType;
import org.jeesl.model.xml.io.locale.status.Descriptions;
import org.jeesl.model.xml.io.locale.status.Langs;


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
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}langs"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}descriptions"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}dataType"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/report}queries"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/report}layout"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/report}styles"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/report}xlsTransformation"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/report}dataAssociation"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="code" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="position" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="visible" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="showLabel" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="showWeb" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="column" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="label" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="example" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="autoWidth" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="width" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
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
    "dataType",
    "queries",
    "layout",
    "styles",
    "xlsTransformation",
    "dataAssociation"
})
@XmlRootElement(name = "xlsColumn")
public class XlsColumn implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/status", required = true)
    protected Langs langs;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/status", required = true)
    protected Descriptions descriptions;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/status", required = true)
    protected DataType dataType;
    @XmlElement(required = true)
    protected Queries queries;
    @XmlElement(required = true)
    protected Layout layout;
    @XmlElement(required = true)
    protected Styles styles;
    @XmlElement(required = true)
    protected XlsTransformation xlsTransformation;
    @XmlElement(required = true)
    protected DataAssociation dataAssociation;
    @XmlAttribute(name = "code")
    protected String code;
    @XmlAttribute(name = "position")
    protected Integer position;
    @XmlAttribute(name = "visible")
    protected Boolean visible;
    @XmlAttribute(name = "showLabel")
    protected Boolean showLabel;
    @XmlAttribute(name = "showWeb")
    protected Boolean showWeb;
    @XmlAttribute(name = "column")
    protected String column;
    @XmlAttribute(name = "required")
    protected Boolean required;
    @XmlAttribute(name = "label")
    protected String label;
    @XmlAttribute(name = "example")
    protected String example;
    @XmlAttribute(name = "autoWidth")
    protected Boolean autoWidth;
    @XmlAttribute(name = "width")
    protected Integer width;

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
     * Gets the value of the dataType property.
     * 
     * @return
     *     possible object is
     *     {@link DataType }
     *     
     */
    public DataType getDataType() {
        return dataType;
    }

    /**
     * Sets the value of the dataType property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataType }
     *     
     */
    public void setDataType(DataType value) {
        this.dataType = value;
    }

    /**
     * Gets the value of the queries property.
     * 
     * @return
     *     possible object is
     *     {@link Queries }
     *     
     */
    public Queries getQueries() {
        return queries;
    }

    /**
     * Sets the value of the queries property.
     * 
     * @param value
     *     allowed object is
     *     {@link Queries }
     *     
     */
    public void setQueries(Queries value) {
        this.queries = value;
    }

    /**
     * Gets the value of the layout property.
     * 
     * @return
     *     possible object is
     *     {@link Layout }
     *     
     */
    public Layout getLayout() {
        return layout;
    }

    /**
     * Sets the value of the layout property.
     * 
     * @param value
     *     allowed object is
     *     {@link Layout }
     *     
     */
    public void setLayout(Layout value) {
        this.layout = value;
    }

    /**
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

    /**
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

    /**
     * Gets the value of the xlsTransformation property.
     * 
     * @return
     *     possible object is
     *     {@link XlsTransformation }
     *     
     */
    public XlsTransformation getXlsTransformation() {
        return xlsTransformation;
    }

    /**
     * Sets the value of the xlsTransformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link XlsTransformation }
     *     
     */
    public void setXlsTransformation(XlsTransformation value) {
        this.xlsTransformation = value;
    }

    /**
     * Gets the value of the dataAssociation property.
     * 
     * @return
     *     possible object is
     *     {@link DataAssociation }
     *     
     */
    public DataAssociation getDataAssociation() {
        return dataAssociation;
    }

    /**
     * Sets the value of the dataAssociation property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataAssociation }
     *     
     */
    public void setDataAssociation(DataAssociation value) {
        this.dataAssociation = value;
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
     * Gets the value of the showLabel property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isShowLabel() {
        return showLabel;
    }

    /**
     * Sets the value of the showLabel property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setShowLabel(Boolean value) {
        this.showLabel = value;
    }

    /**
     * Gets the value of the showWeb property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isShowWeb() {
        return showWeb;
    }

    /**
     * Sets the value of the showWeb property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setShowWeb(Boolean value) {
        this.showWeb = value;
    }

    /**
     * Gets the value of the column property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColumn() {
        return column;
    }

    /**
     * Sets the value of the column property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColumn(String value) {
        this.column = value;
    }

    /**
     * Gets the value of the required property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRequired() {
        return required;
    }

    /**
     * Sets the value of the required property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRequired(Boolean value) {
        this.required = value;
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
     * Gets the value of the example property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExample() {
        return example;
    }

    /**
     * Sets the value of the example property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExample(String value) {
        this.example = value;
    }

    /**
     * Gets the value of the autoWidth property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAutoWidth() {
        return autoWidth;
    }

    /**
     * Sets the value of the autoWidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAutoWidth(Boolean value) {
        this.autoWidth = value;
    }

    /**
     * Gets the value of the width property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * Sets the value of the width property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setWidth(Integer value) {
        this.width = value;
    }

}
