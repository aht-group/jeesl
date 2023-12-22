
package org.jeesl.model.xml.dev.srs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *         &lt;element ref="{http://www.jeesl.org/dev/srs}version"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/dev/srs}meta"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/dev/srs}actors"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/dev/srs}releases"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/dev/srs}frs"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/dev/srs}chapter" maxOccurs="unbounded"/&gt;
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
     * Gets the value of the version property.
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
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link Version }
     *     
     */
    public void setVersion(Version value) {
        this.version = value;
    }

    public boolean isSetVersion() {
        return (this.version!= null);
    }

    /**
     * Gets the value of the meta property.
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
     * Sets the value of the meta property.
     * 
     * @param value
     *     allowed object is
     *     {@link Meta }
     *     
     */
    public void setMeta(Meta value) {
        this.meta = value;
    }

    public boolean isSetMeta() {
        return (this.meta!= null);
    }

    /**
     * Gets the value of the actors property.
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
     * Sets the value of the actors property.
     * 
     * @param value
     *     allowed object is
     *     {@link Actors }
     *     
     */
    public void setActors(Actors value) {
        this.actors = value;
    }

    public boolean isSetActors() {
        return (this.actors!= null);
    }

    /**
     * Gets the value of the releases property.
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
     * Sets the value of the releases property.
     * 
     * @param value
     *     allowed object is
     *     {@link Releases }
     *     
     */
    public void setReleases(Releases value) {
        this.releases = value;
    }

    public boolean isSetReleases() {
        return (this.releases!= null);
    }

    /**
     * Gets the value of the frs property.
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
     * Sets the value of the frs property.
     * 
     * @param value
     *     allowed object is
     *     {@link Frs }
     *     
     */
    public void setFrs(Frs value) {
        this.frs = value;
    }

    public boolean isSetFrs() {
        return (this.frs!= null);
    }

    /**
     * Gets the value of the chapter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
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

    public boolean isSetChapter() {
        return ((this.chapter!= null)&&(!this.chapter.isEmpty()));
    }

    public void unsetChapter() {
        this.chapter = null;
    }

    /**
     * Gets the value of the code property.
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
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCode(String value) {
        this.code = value;
    }

    public boolean isSetCode() {
        return (this.code!= null);
    }

}
