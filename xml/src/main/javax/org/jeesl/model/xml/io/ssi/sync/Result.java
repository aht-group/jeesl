
package org.jeesl.model.xml.io.ssi.sync;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.jeesl.model.xml.io.locale.status.Status;


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
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}status"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="total" type="{http://www.w3.org/2001/XMLSchema}long" /&gt;
 *       &lt;attribute name="success" type="{http://www.w3.org/2001/XMLSchema}long" /&gt;
 *       &lt;attribute name="skip" type="{http://www.w3.org/2001/XMLSchema}long" /&gt;
 *       &lt;attribute name="fail" type="{http://www.w3.org/2001/XMLSchema}long" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "status"
})
@XmlRootElement(name = "result")
public class Result
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/status", required = true)
    protected Status status;
    @XmlAttribute(name = "total")
    protected Long total;
    @XmlAttribute(name = "success")
    protected Long success;
    @XmlAttribute(name = "skip")
    protected Long skip;
    @XmlAttribute(name = "fail")
    protected Long fail;

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
     * Gets the value of the total property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTotal() {
        return total;
    }

    /**
     * Sets the value of the total property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTotal(Long value) {
        this.total = value;
    }

    /**
     * Gets the value of the success property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSuccess() {
        return success;
    }

    /**
     * Sets the value of the success property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSuccess(Long value) {
        this.success = value;
    }

    /**
     * Gets the value of the skip property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSkip() {
        return skip;
    }

    /**
     * Sets the value of the skip property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSkip(Long value) {
        this.skip = value;
    }

    /**
     * Gets the value of the fail property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getFail() {
        return fail;
    }

    /**
     * Sets the value of the fail property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setFail(Long value) {
        this.fail = value;
    }

}
