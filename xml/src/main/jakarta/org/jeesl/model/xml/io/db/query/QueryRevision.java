
package org.jeesl.model.xml.io.db.query;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import org.jeesl.model.xml.io.label.Diagram;
import org.jeesl.model.xml.io.label.Entity;
import org.jeesl.model.xml.io.label.Revision;


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
 *         &lt;element ref="{http://www.jeesl.org/revision}entity"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/revision}diagram"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/revision}revision"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="localeCode" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "entity",
    "diagram",
    "revision"
})
@XmlRootElement(name = "queryRevision")
public class QueryRevision
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(namespace = "http://www.jeesl.org/revision", required = true)
    protected Entity entity;
    @XmlElement(namespace = "http://www.jeesl.org/revision", required = true)
    protected Diagram diagram;
    @XmlElement(namespace = "http://www.jeesl.org/revision", required = true)
    protected Revision revision;
    @XmlAttribute(name = "localeCode")
    protected String localeCode;

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
     * Ruft den Wert der diagram-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Diagram }
     *     
     */
    public Diagram getDiagram() {
        return diagram;
    }

    /**
     * Legt den Wert der diagram-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Diagram }
     *     
     */
    public void setDiagram(Diagram value) {
        this.diagram = value;
    }

    /**
     * Ruft den Wert der revision-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Revision }
     *     
     */
    public Revision getRevision() {
        return revision;
    }

    /**
     * Legt den Wert der revision-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Revision }
     *     
     */
    public void setRevision(Revision value) {
        this.revision = value;
    }

    /**
     * Ruft den Wert der localeCode-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocaleCode() {
        return localeCode;
    }

    /**
     * Legt den Wert der localeCode-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocaleCode(String value) {
        this.localeCode = value;
    }

}
