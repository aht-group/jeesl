
package org.jeesl.model.xml.system.io.mail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.jeesl.model.xml.text.Example;


/*
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.jeesl.org/io/mail}mail"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/io/mail}header"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/io/mail}template" maxOccurs="unbounded"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/io/mail}text"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/io/mail}html"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/io/mail}attachment" maxOccurs="unbounded"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/io/mail}image" maxOccurs="unbounded"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/text}example"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}long" /&gt;
 *       &lt;attribute name="code" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="lang" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="msgId" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="dir" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="test" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "mail",
    "header",
    "template",
    "text",
    "html",
    "attachment",
    "image",
    "example"
})
@XmlRootElement(name = "mail")
public class Mail
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected Mail mail;
    @XmlElement(required = true)
    protected Header header;
    @XmlElement(required = true)
    protected List<Template> template;
    @XmlElement(required = true)
    protected Text text;
    @XmlElement(required = true)
    protected Html html;
    @XmlElement(required = true)
    protected List<Attachment> attachment;
    @XmlElement(required = true)
    protected List<Image> image;
    @XmlElement(namespace = "http://www.jeesl.org/text", required = true)
    protected Example example;
    @XmlAttribute(name = "id")
    protected Long id;
    @XmlAttribute(name = "code")
    protected String code;
    @XmlAttribute(name = "lang")
    protected String lang;
    @XmlAttribute(name = "type")
    protected String type;
    @XmlAttribute(name = "msgId")
    protected String msgId;
    @XmlAttribute(name = "dir")
    protected String dir;
    @XmlAttribute(name = "test")
    protected Boolean test;

    /*
     * Gets the value of the mail property.
     * 
     * @return
     *     possible object is
     *     {@link Mail }
     *     
     */
    public Mail getMail() {
        return mail;
    }

    /*
     * Sets the value of the mail property.
     * 
     * @param value
     *     allowed object is
     *     {@link Mail }
     *     
     */
    public void setMail(Mail value) {
        this.mail = value;
    }

    public boolean isSetMail() {
        return (this.mail!= null);
    }

    /*
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link Header }
     *     
     */
    public Header getHeader() {
        return header;
    }

    /*
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link Header }
     *     
     */
    public void setHeader(Header value) {
        this.header = value;
    }

    public boolean isSetHeader() {
        return (this.header!= null);
    }

    /*
     * Gets the value of the template property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the template property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTemplate().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Template }
     * 
     * 
     */
    public List<Template> getTemplate() {
        if (template == null) {
            template = new ArrayList<Template>();
        }
        return this.template;
    }

    public boolean isSetTemplate() {
        return ((this.template!= null)&&(!this.template.isEmpty()));
    }

    public void unsetTemplate() {
        this.template = null;
    }

    /*
     * Gets the value of the text property.
     * 
     * @return
     *     possible object is
     *     {@link Text }
     *     
     */
    public Text getText() {
        return text;
    }

    /*
     * Sets the value of the text property.
     * 
     * @param value
     *     allowed object is
     *     {@link Text }
     *     
     */
    public void setText(Text value) {
        this.text = value;
    }

    public boolean isSetText() {
        return (this.text!= null);
    }

    /*
     * Gets the value of the html property.
     * 
     * @return
     *     possible object is
     *     {@link Html }
     *     
     */
    public Html getHtml() {
        return html;
    }

    /*
     * Sets the value of the html property.
     * 
     * @param value
     *     allowed object is
     *     {@link Html }
     *     
     */
    public void setHtml(Html value) {
        this.html = value;
    }

    public boolean isSetHtml() {
        return (this.html!= null);
    }

    /*
     * Gets the value of the attachment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attachment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttachment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Attachment }
     * 
     * 
     */
    public List<Attachment> getAttachment() {
        if (attachment == null) {
            attachment = new ArrayList<Attachment>();
        }
        return this.attachment;
    }

    public boolean isSetAttachment() {
        return ((this.attachment!= null)&&(!this.attachment.isEmpty()));
    }

    public void unsetAttachment() {
        this.attachment = null;
    }

    /*
     * Gets the value of the image property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the image property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getImage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Image }
     * 
     * 
     */
    public List<Image> getImage() {
        if (image == null) {
            image = new ArrayList<Image>();
        }
        return this.image;
    }

    public boolean isSetImage() {
        return ((this.image!= null)&&(!this.image.isEmpty()));
    }

    public void unsetImage() {
        this.image = null;
    }

    /*
     * Gets the value of the example property.
     * 
     * @return
     *     possible object is
     *     {@link Example }
     *     
     */
    public Example getExample() {
        return example;
    }

    /*
     * Sets the value of the example property.
     * 
     * @param value
     *     allowed object is
     *     {@link Example }
     *     
     */
    public void setExample(Example value) {
        this.example = value;
    }

    public boolean isSetExample() {
        return (this.example!= null);
    }

    /*
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public long getId() {
        return id;
    }

    /*
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setId(long value) {
        this.id = value;
    }

    public boolean isSetId() {
        return (this.id!= null);
    }

    public void unsetId() {
        this.id = null;
    }

    /*
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

    /*
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

    /*
     * Gets the value of the lang property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLang() {
        return lang;
    }

    /*
     * Sets the value of the lang property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLang(String value) {
        this.lang = value;
    }

    public boolean isSetLang() {
        return (this.lang!= null);
    }

    /*
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /*
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    public boolean isSetType() {
        return (this.type!= null);
    }

    /*
     * Gets the value of the msgId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsgId() {
        return msgId;
    }

    /*
     * Sets the value of the msgId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsgId(String value) {
        this.msgId = value;
    }

    public boolean isSetMsgId() {
        return (this.msgId!= null);
    }

    /*
     * Gets the value of the dir property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDir() {
        return dir;
    }

    /*
     * Sets the value of the dir property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDir(String value) {
        this.dir = value;
    }

    public boolean isSetDir() {
        return (this.dir!= null);
    }

    /*
     * Gets the value of the test property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isTest() {
        return test;
    }

    /*
     * Sets the value of the test property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTest(boolean value) {
        this.test = value;
    }

    public boolean isSetTest() {
        return (this.test!= null);
    }

    public void unsetTest() {
        this.test = null;
    }

}
