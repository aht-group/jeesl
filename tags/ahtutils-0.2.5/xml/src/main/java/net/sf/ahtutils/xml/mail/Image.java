
package net.sf.ahtutils.xml.mail;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import net.sf.exlp.xml.io.File;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="data">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}hexBinary">
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element ref="{http://exlp.sf.net/io}file"/>
 *       &lt;/sequence>
 *       &lt;attribute name="cid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "data",
    "file"
})
@XmlRootElement(name = "image")
public class Image
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(namespace = "", required = true, type = String.class)
    @XmlJavaTypeAdapter(HexBinaryAdapter.class)
    protected byte[] data;
    @XmlElement(namespace = "http://exlp.sf.net/io", required = true)
    protected File file;
    @XmlAttribute(name = "cid")
    protected String cid;

    /**
     * Gets the value of the data property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Sets the value of the data property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setData(byte[] value) {
        this.data = ((byte[]) value);
    }

    public boolean isSetData() {
        return (this.data!= null);
    }

    /**
     * Gets the value of the file property.
     * 
     * @return
     *     possible object is
     *     {@link File }
     *     
     */
    public File getFile() {
        return file;
    }

    /**
     * Sets the value of the file property.
     * 
     * @param value
     *     allowed object is
     *     {@link File }
     *     
     */
    public void setFile(File value) {
        this.file = value;
    }

    public boolean isSetFile() {
        return (this.file!= null);
    }

    /**
     * Gets the value of the cid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCid() {
        return cid;
    }

    /**
     * Sets the value of the cid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCid(String value) {
        this.cid = value;
    }

    public boolean isSetCid() {
        return (this.cid!= null);
    }

}
