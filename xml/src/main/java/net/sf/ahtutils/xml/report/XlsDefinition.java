
package net.sf.ahtutils.xml.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element ref="{http://ahtutils.aht-group.com/report}xlsWorkbook" maxOccurs="unbounded"/&gt;
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
    "xlsWorkbook"
})
@XmlRootElement(name = "xlsDefinition")
public class XlsDefinition
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected List<XlsWorkbook> xlsWorkbook;


    public List<XlsWorkbook> getXlsWorkbook() {
        if (xlsWorkbook == null) {
            xlsWorkbook = new ArrayList<XlsWorkbook>();
        }
        return this.xlsWorkbook;
    }

    public boolean isSetXlsWorkbook() {
        return ((this.xlsWorkbook!= null)&&(!this.xlsWorkbook.isEmpty()));
    }

    public void unsetXlsWorkbook() {
        this.xlsWorkbook = null;
    }

}
