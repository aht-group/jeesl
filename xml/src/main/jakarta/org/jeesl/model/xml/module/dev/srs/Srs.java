
package org.jeesl.model.xml.module.dev.srs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


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
 *         &lt;element ref="{http://xsd.jeesl.org/module/dev/srs}version"/&gt;
 *         &lt;element ref="{http://xsd.jeesl.org/module/dev/srs}meta"/&gt;
 *         &lt;element ref="{http://xsd.jeesl.org/module/dev/srs}actors"/&gt;
 *         &lt;element ref="{http://xsd.jeesl.org/module/dev/srs}releases"/&gt;
 *         &lt;element ref="{http://xsd.jeesl.org/module/dev/srs}frs"/&gt;
 *         &lt;element ref="{http://xsd.jeesl.org/module/dev/srs}chapter" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="code" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "version",
    "meta",
    "actors",
    "releases",
    "frs",
    "chapter"
})
@XmlRootElement(name = "srs")
public class Srs
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected Version version;
    @XmlElement(required = true)
    protected Meta meta;
    @XmlElement(required = true)
    protected Actors actors;
    @XmlElement(required = true)
    protected Releases releases;
    @XmlElement(required = true)
    protected Frs frs;
    @XmlElement(required = true)
    protected List<Chapter> chapter;
    @XmlAttribute(name = "code")
    protected String code;

    /**
     * Ruft den Wert der version-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Version }
     *     
     */
    public Version getVersion() {
        return version;
    }

    /**
     * Legt den Wert der version-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Version }
     *     
     */
    public void setVersion(Version value) {
        this.version = value;
    }

    /**
     * Ruft den Wert der meta-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Meta }
     *     
     */
    public Meta getMeta() {
        return meta;
    }

    /**
     * Legt den Wert der meta-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Meta }
     *     
     */
    public void setMeta(Meta value) {
        this.meta = value;
    }

    /**
     * Ruft den Wert der actors-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Actors }
     *     
     */
    public Actors getActors() {
        return actors;
    }

    /**
     * Legt den Wert der actors-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Actors }
     *     
     */
    public void setActors(Actors value) {
        this.actors = value;
    }

    /**
     * Ruft den Wert der releases-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Releases }
     *     
     */
    public Releases getReleases() {
        return releases;
    }

    /**
     * Legt den Wert der releases-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Releases }
     *     
     */
    public void setReleases(Releases value) {
        this.releases = value;
    }

    /**
     * Ruft den Wert der frs-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Frs }
     *     
     */
    public Frs getFrs() {
        return frs;
    }

    /**
     * Legt den Wert der frs-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Frs }
     *     
     */
    public void setFrs(Frs value) {
        this.frs = value;
    }

    /**
     * Gets the value of the chapter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the chapter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getChapter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Chapter }
     * 
     * 
     */
    public List<Chapter> getChapter() {
        if (chapter == null) {
            chapter = new ArrayList<Chapter>();
        }
        return this.chapter;
    }

    /**
     * Ruft den Wert der code-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCode() {
        return code;
    }

    /**
     * Legt den Wert der code-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCode(String value) {
        this.code = value;
    }

}
