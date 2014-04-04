
package net.sf.ahtutils.xml.sync;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://ahtutils.aht-group.com/sync}mapper" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "mapper"
})
@XmlRootElement(name = "dataUpdate")
public class DataUpdate
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected List<Mapper> mapper;

    /**
     * Gets the value of the mapper property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mapper property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMapper().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Mapper }
     * 
     * 
     */
    public List<Mapper> getMapper() {
        if (mapper == null) {
            mapper = new ArrayList<Mapper>();
        }
        return this.mapper;
    }

    public boolean isSetMapper() {
        return ((this.mapper!= null)&&(!this.mapper.isEmpty()));
    }

    public void unsetMapper() {
        this.mapper = null;
    }

}
