
package org.jeesl.model.xml.jeesl;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.jeesl.model.xml.system.revision.Diagram;
import org.jeesl.model.xml.system.revision.Entity;
import org.jeesl.model.xml.system.revision.Revision;


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
     * Gets the value of the diagram property.
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
     * Sets the value of the diagram property.
     * 
     * @param value
     *     allowed object is
     *     {@link Diagram }
     *     
     */
    public void setDiagram(Diagram value) {
        this.diagram = value;
    }

    public boolean isSetDiagram() {
        return (this.diagram!= null);
    }

    /**
     * Gets the value of the revision property.
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
     * Sets the value of the revision property.
     * 
     * @param value
     *     allowed object is
     *     {@link Revision }
     *     
     */
    public void setRevision(Revision value) {
        this.revision = value;
    }

    public boolean isSetRevision() {
        return (this.revision!= null);
    }

    /**
     * Gets the value of the localeCode property.
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
     * Sets the value of the localeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocaleCode(String value) {
        this.localeCode = value;
    }

    public boolean isSetLocaleCode() {
        return (this.localeCode!= null);
    }

}
