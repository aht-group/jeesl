
package net.sf.ahtutils.xml.system;

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
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/system}uptime" maxOccurs="unbounded"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/system}request" maxOccurs="unbounded"/&gt;
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
    "uptime",
    "request"
})
@XmlRootElement(name = "info")
public class Info
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected List<Uptime> uptime;
    @XmlElement(required = true)
    protected List<Request> request;

    /**
     * Gets the value of the uptime property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the uptime property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUptime().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Uptime }
     * 
     * 
     */
    public List<Uptime> getUptime() {
        if (uptime == null) {
            uptime = new ArrayList<Uptime>();
        }
        return this.uptime;
    }

    public boolean isSetUptime() {
        return ((this.uptime!= null)&&(!this.uptime.isEmpty()));
    }

    public void unsetUptime() {
        this.uptime = null;
    }

    /**
     * Gets the value of the request property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the request property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRequest().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Request }
     * 
     * 
     */
    public List<Request> getRequest() {
        if (request == null) {
            request = new ArrayList<Request>();
        }
        return this.request;
    }

    public boolean isSetRequest() {
        return ((this.request!= null)&&(!this.request.isEmpty()));
    }

    public void unsetRequest() {
        this.request = null;
    }

}
