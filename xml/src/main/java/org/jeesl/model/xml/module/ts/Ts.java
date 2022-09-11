
package org.jeesl.model.xml.module.ts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import net.sf.ahtutils.xml.status.Interval;
import net.sf.ahtutils.xml.status.Scope;


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
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}scope"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}interval"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/timeseries}entity"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/timeseries}bridge"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/timeseries}statistic"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/timeseries}transaction" maxOccurs="unbounded"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/timeseries}data" maxOccurs="unbounded"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/timeseries}ds" maxOccurs="unbounded"/&gt;
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
    "scope",
    "interval",
    "entity",
    "bridge",
    "statistic",
    "transaction",
    "data",
    "ds"
})
@XmlRootElement(name = "ts")
public class Ts
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/status", required = true)
    protected Scope scope;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/status", required = true)
    protected Interval interval;
    @XmlElement(required = true)
    protected Entity entity;
    @XmlElement(required = true)
    protected Bridge bridge;
    @XmlElement(required = true)
    protected Statistic statistic;
    @XmlElement(required = true)
    protected List<Transaction> transaction;
    @XmlElement(required = true)
    protected List<Data> data;
    @XmlElement(required = true)
    protected List<Ds> ds;

    /**
     * Gets the value of the scope property.
     * 
     * @return
     *     possible object is
     *     {@link Scope }
     *     
     */
    public Scope getScope() {
        return scope;
    }

    /**
     * Sets the value of the scope property.
     * 
     * @param value
     *     allowed object is
     *     {@link Scope }
     *     
     */
    public void setScope(Scope value) {
        this.scope = value;
    }

    public boolean isSetScope() {
        return (this.scope!= null);
    }

    /**
     * Gets the value of the interval property.
     * 
     * @return
     *     possible object is
     *     {@link Interval }
     *     
     */
    public Interval getInterval() {
        return interval;
    }

    /**
     * Sets the value of the interval property.
     * 
     * @param value
     *     allowed object is
     *     {@link Interval }
     *     
     */
    public void setInterval(Interval value) {
        this.interval = value;
    }

    public boolean isSetInterval() {
        return (this.interval!= null);
    }

    /**
     * Gets the value of the entity property.
     * 
     * @return
     *     possible object is
     *     {@link Entity }
     *     
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Sets the value of the entity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Entity }
     *     
     */
    public void setEntity(Entity value) {
        this.entity = value;
    }

    public boolean isSetEntity() {
        return (this.entity!= null);
    }

    /**
     * Gets the value of the bridge property.
     * 
     * @return
     *     possible object is
     *     {@link Bridge }
     *     
     */
    public Bridge getBridge() {
        return bridge;
    }

    /**
     * Sets the value of the bridge property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bridge }
     *     
     */
    public void setBridge(Bridge value) {
        this.bridge = value;
    }

    public boolean isSetBridge() {
        return (this.bridge!= null);
    }

    /**
     * Gets the value of the statistic property.
     * 
     * @return
     *     possible object is
     *     {@link Statistic }
     *     
     */
    public Statistic getStatistic() {
        return statistic;
    }

    /**
     * Sets the value of the statistic property.
     * 
     * @param value
     *     allowed object is
     *     {@link Statistic }
     *     
     */
    public void setStatistic(Statistic value) {
        this.statistic = value;
    }

    public boolean isSetStatistic() {
        return (this.statistic!= null);
    }

    /**
     * Gets the value of the transaction property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the transaction property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTransaction().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Transaction }
     * 
     * 
     */
    public List<Transaction> getTransaction() {
        if (transaction == null) {
            transaction = new ArrayList<Transaction>();
        }
        return this.transaction;
    }

    public boolean isSetTransaction() {
        return ((this.transaction!= null)&&(!this.transaction.isEmpty()));
    }

    public void unsetTransaction() {
        this.transaction = null;
    }

    /**
     * Gets the value of the data property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the data property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Data }
     * 
     * 
     */
    public List<Data> getData() {
        if (data == null) {
            data = new ArrayList<Data>();
        }
        return this.data;
    }

    public boolean isSetData() {
        return ((this.data!= null)&&(!this.data.isEmpty()));
    }

    public void unsetData() {
        this.data = null;
    }

    /**
     * Gets the value of the ds property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ds property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Ds }
     * 
     * 
     */
    public List<Ds> getDs() {
        if (ds == null) {
            ds = new ArrayList<Ds>();
        }
        return this.ds;
    }

    public boolean isSetDs() {
        return ((this.ds!= null)&&(!this.ds.isEmpty()));
    }

    public void unsetDs() {
        this.ds = null;
    }

}
