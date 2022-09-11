
package net.sf.ahtutils.xml.report;

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
     * Gets the value of the xlsSheets property.
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
     * Sets the value of the xlsSheets property.
     * 
     * @param value
     *     allowed object is
     *     {@link XlsSheets }
     *     
     */
    public void setXlsSheets(XlsSheets value) {
        this.xlsSheets = value;
    }

    public boolean isSetXlsSheets() {
        return (this.xlsSheets!= null);
    }

    /**
     * Gets the value of the xlsSheet property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
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

    public boolean isSetXlsSheet() {
        return ((this.xlsSheet!= null)&&(!this.xlsSheet.isEmpty()));
    }

    public void unsetXlsSheet() {
        this.xlsSheet = null;
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

}
