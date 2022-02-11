
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
 *         &lt;element ref="{http://ahtutils.aht-group.com/report}xlsSheet" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="query" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "xlsSheet"
})
@XmlRootElement(name = "xlsSheets")
public class XlsSheets
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected List<XlsSheet> xlsSheet;
    @XmlAttribute(name = "query")
    protected String query;


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

    /*
     * Gets the value of the query property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQuery() {
        return query;
    }

    /*
     * Sets the value of the query property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQuery(String value) {
        this.query = value;
    }

    public boolean isSetQuery() {
        return (this.query!= null);
    }

}
