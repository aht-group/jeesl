
package org.jeesl.model.xml.module.ts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import org.jeesl.model.xml.io.locale.status.Interval;
import org.jeesl.model.xml.io.locale.status.Scope;


/**
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
     * Ruft den Wert der scope-Eigenschaft ab.
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
     * Legt den Wert der scope-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Scope }
     *     
     */
    public void setScope(Scope value) {
        this.scope = value;
    }

    /**
     * Ruft den Wert der interval-Eigenschaft ab.
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
     * Legt den Wert der interval-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Interval }
     *     
     */
    public void setInterval(Interval value) {
        this.interval = value;
    }

    /**
     * Ruft den Wert der entity-Eigenschaft ab.
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
     * Legt den Wert der entity-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Entity }
     *     
     */
    public void setEntity(Entity value) {
        this.entity = value;
    }

    /**
     * Ruft den Wert der bridge-Eigenschaft ab.
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
     * Legt den Wert der bridge-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Bridge }
     *     
     */
    public void setBridge(Bridge value) {
        this.bridge = value;
    }

    /**
     * Ruft den Wert der statistic-Eigenschaft ab.
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
     * Legt den Wert der statistic-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Statistic }
     *     
     */
    public void setStatistic(Statistic value) {
        this.statistic = value;
    }

    /**
     * Gets the value of the transaction property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
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

    /**
     * Gets the value of the data property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
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

    /**
     * Gets the value of the ds property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
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

}
