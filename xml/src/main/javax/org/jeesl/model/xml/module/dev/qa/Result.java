
package org.jeesl.model.xml.module.dev.qa;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import org.jeesl.model.xml.io.locale.status.Status;
import org.jeesl.model.xml.system.security.Staff;


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
 *         &lt;element ref="{https://www.jeesl.org/jeesl/xsd/system/security}staff"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}status"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/qa}comment"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/qa}actual"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}long" /&gt;
 *       &lt;attribute name="record" type="{http://www.w3.org/2001/XMLSchema}dateTime" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "staff",
    "status",
    "comment",
    "actual"
})
@XmlRootElement(name = "result")
public class Result
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(namespace = "https://www.jeesl.org/jeesl/xsd/system/security", required = true)
    protected Staff staff;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/status", required = true)
    protected Status status;
    @XmlElement(required = true)
    protected Comment comment;
    @XmlElement(required = true)
    protected Actual actual;
    @XmlAttribute(name = "id")
    protected Long id;
    @XmlAttribute(name = "record")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar record;

    /**
     * Gets the value of the staff property.
     * 
     * @return
     *     possible object is
     *     {@link Staff }
     *     
     */
    public Staff getStaff() {
        return staff;
    }

    /**
     * Sets the value of the staff property.
     * 
     * @param value
     *     allowed object is
     *     {@link Staff }
     *     
     */
    public void setStaff(Staff value) {
        this.staff = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link Status }
     *     
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link Status }
     *     
     */
    public void setStatus(Status value) {
        this.status = value;
    }

    /**
     * Gets the value of the comment property.
     * 
     * @return
     *     possible object is
     *     {@link Comment }
     *     
     */
    public Comment getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     * 
     * @param value
     *     allowed object is
     *     {@link Comment }
     *     
     */
    public void setComment(Comment value) {
        this.comment = value;
    }

    /**
     * Gets the value of the actual property.
     * 
     * @return
     *     possible object is
     *     {@link Actual }
     *     
     */
    public Actual getActual() {
        return actual;
    }

    /**
     * Sets the value of the actual property.
     * 
     * @param value
     *     allowed object is
     *     {@link Actual }
     *     
     */
    public void setActual(Actual value) {
        this.actual = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getId() {
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
    public void setId(Long value) {
        this.id = value;
    }

    /**
     * Gets the value of the record property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRecord() {
        return record;
    }

    /**
     * Sets the value of the record property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRecord(XMLGregorianCalendar value) {
        this.record = value;
    }

}
