
package net.sf.ahtutils.xml.sync;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import net.sf.ahtutils.xml.status.Status;


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

    public boolean isSetStatus() {
        return (this.status!= null);
    }

    /**
     * Gets the value of the total property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public long getTotal() {
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
    public void setTotal(long value) {
        this.total = value;
    }

    public boolean isSetTotal() {
        return (this.total!= null);
    }

    public void unsetTotal() {
        this.total = null;
    }

    /**
     * Gets the value of the success property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public long getSuccess() {
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
    public void setSuccess(long value) {
        this.success = value;
    }

    public boolean isSetSuccess() {
        return (this.success!= null);
    }

    public void unsetSuccess() {
        this.success = null;
    }

    /**
     * Gets the value of the skip property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public long getSkip() {
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
    public void setSkip(long value) {
        this.skip = value;
    }

    public boolean isSetSkip() {
        return (this.skip!= null);
    }

    public void unsetSkip() {
        this.skip = null;
    }

    /**
     * Gets the value of the fail property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public long getFail() {
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
    public void setFail(long value) {
        this.fail = value;
    }

    public boolean isSetFail() {
        return (this.fail!= null);
    }

    public void unsetFail() {
        this.fail = null;
    }

}
