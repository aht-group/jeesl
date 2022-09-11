
package net.sf.ahtutils.xml.sync;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import net.sf.ahtutils.xml.status.Type;


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
 *         &lt;element ref="{http://ahtutils.aht-group.com/sync}result"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/sync}mapper" maxOccurs="unbounded"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}type"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/sync}sync" maxOccurs="unbounded"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/sync}exceptions"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="begin" type="{http://www.w3.org/2001/XMLSchema}dateTime" /&gt;
 *       &lt;attribute name="finished" type="{http://www.w3.org/2001/XMLSchema}dateTime" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "result",
    "mapper",
    "type",
    "sync",
    "exceptions"
})
@XmlRootElement(name = "dataUpdate")
public class DataUpdate
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected Result result;
    @XmlElement(required = true)
    protected List<Mapper> mapper;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/status", required = true)
    protected Type type;
    @XmlElement(required = true)
    protected List<Sync> sync;
    @XmlElement(required = true)
    protected Exceptions exceptions;
    @XmlAttribute(name = "begin")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar begin;
    @XmlAttribute(name = "finished")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar finished;

    /**
     * Gets the value of the result property.
     * 
     * @return
     *     possible object is
     *     {@link Result }
     *     
     */
    public Result getResult() {
        return result;
    }

    /**
     * Sets the value of the result property.
     * 
     * @param value
     *     allowed object is
     *     {@link Result }
     *     
     */
    public void setResult(Result value) {
        this.result = value;
    }

    public boolean isSetResult() {
        return (this.result!= null);
    }

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

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link Type }
     *     
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link Type }
     *     
     */
    public void setType(Type value) {
        this.type = value;
    }

    public boolean isSetType() {
        return (this.type!= null);
    }

    /**
     * Gets the value of the sync property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sync property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSync().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Sync }
     * 
     * 
     */
    public List<Sync> getSync() {
        if (sync == null) {
            sync = new ArrayList<Sync>();
        }
        return this.sync;
    }

    public boolean isSetSync() {
        return ((this.sync!= null)&&(!this.sync.isEmpty()));
    }

    public void unsetSync() {
        this.sync = null;
    }

    /**
     * Gets the value of the exceptions property.
     * 
     * @return
     *     possible object is
     *     {@link Exceptions }
     *     
     */
    public Exceptions getExceptions() {
        return exceptions;
    }

    /**
     * Sets the value of the exceptions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Exceptions }
     *     
     */
    public void setExceptions(Exceptions value) {
        this.exceptions = value;
    }

    public boolean isSetExceptions() {
        return (this.exceptions!= null);
    }

    /**
     * Gets the value of the begin property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getBegin() {
        return begin;
    }

    /**
     * Sets the value of the begin property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setBegin(XMLGregorianCalendar value) {
        this.begin = value;
    }

    public boolean isSetBegin() {
        return (this.begin!= null);
    }

    /**
     * Gets the value of the finished property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFinished() {
        return finished;
    }

    /**
     * Sets the value of the finished property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFinished(XMLGregorianCalendar value) {
        this.finished = value;
    }

    public boolean isSetFinished() {
        return (this.finished!= null);
    }

}
