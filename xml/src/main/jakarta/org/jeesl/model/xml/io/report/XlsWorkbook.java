
package org.jeesl.model.xml.io.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element ref="{http://ahtutils.aht-group.com/report}xlsSheets"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/report}xlsSheet" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="code" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "xlsSheets",
    "xlsSheet"
})
@XmlRootElement(name = "xlsWorkbook")
public class XlsWorkbook
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected XlsSheets xlsSheets;
    @XmlElement(required = true)
    protected List<XlsSheet> xlsSheet;
    @XmlAttribute(name = "code")
    protected String code;

    /**
     * Ruft den Wert der xlsSheets-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XlsSheets }
     *     
     */
    public XlsSheets getXlsSheets() {
        return xlsSheets;
    }

    /**
     * Legt den Wert der xlsSheets-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XlsSheets }
     *     
     */
    public void setXlsSheets(XlsSheets value) {
        this.xlsSheets = value;
    }

    /**
     * Gets the value of the xlsSheet property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the xlsSheet property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getXlsSheet().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XlsSheet }
     * 
     * 
     */
    public List<XlsSheet> getXlsSheet() {
        if (xlsSheet == null) {
            xlsSheet = new ArrayList<XlsSheet>();
        }
        return this.xlsSheet;
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

}
