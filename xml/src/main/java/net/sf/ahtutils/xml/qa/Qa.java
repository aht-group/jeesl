
package net.sf.ahtutils.xml.qa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import net.sf.ahtutils.xml.security.Staff;
import org.jeesl.model.xml.module.survey.Survey;


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
 *         &lt;element ref="{http://ahtutils.aht-group.com/qa}category" maxOccurs="unbounded"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/security}staff" maxOccurs="unbounded"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/survey}survey"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/qa}groups"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/qa}checklist" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}long" /&gt;
 *       &lt;attribute name="client" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="developer" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "category",
    "staff",
    "survey",
    "groups",
    "checklist"
})
@XmlRootElement(name = "qa")
public class Qa
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected List<Category> category;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/security", required = true)
    protected List<Staff> staff;
    @XmlElement(namespace = "http://www.jeesl.org/survey", required = true)
    protected Survey survey;
    @XmlElement(required = true)
    protected Groups groups;
    @XmlElement(required = true)
    protected List<Checklist> checklist;
    @XmlAttribute(name = "id")
    protected Long id;
    @XmlAttribute(name = "client")
    protected String client;
    @XmlAttribute(name = "developer")
    protected String developer;

    /**
     * Gets the value of the category property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the category property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCategory().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Category }
     * 
     * 
     */
    public List<Category> getCategory() {
        if (category == null) {
            category = new ArrayList<Category>();
        }
        return this.category;
    }

    public boolean isSetCategory() {
        return ((this.category!= null)&&(!this.category.isEmpty()));
    }

    public void unsetCategory() {
        this.category = null;
    }

    /**
     * Gets the value of the staff property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the staff property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStaff().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Staff }
     * 
     * 
     */
    public List<Staff> getStaff() {
        if (staff == null) {
            staff = new ArrayList<Staff>();
        }
        return this.staff;
    }

    public boolean isSetStaff() {
        return ((this.staff!= null)&&(!this.staff.isEmpty()));
    }

    public void unsetStaff() {
        this.staff = null;
    }

    /**
     * Gets the value of the survey property.
     * 
     * @return
     *     possible object is
     *     {@link Survey }
     *     
     */
    public Survey getSurvey() {
        return survey;
    }

    /**
     * Sets the value of the survey property.
     * 
     * @param value
     *     allowed object is
     *     {@link Survey }
     *     
     */
    public void setSurvey(Survey value) {
        this.survey = value;
    }

    public boolean isSetSurvey() {
        return (this.survey!= null);
    }

    /**
     * Gets the value of the groups property.
     * 
     * @return
     *     possible object is
     *     {@link Groups }
     *     
     */
    public Groups getGroups() {
        return groups;
    }

    /**
     * Sets the value of the groups property.
     * 
     * @param value
     *     allowed object is
     *     {@link Groups }
     *     
     */
    public void setGroups(Groups value) {
        this.groups = value;
    }

    public boolean isSetGroups() {
        return (this.groups!= null);
    }

    /**
     * Gets the value of the checklist property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the checklist property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getChecklist().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Checklist }
     * 
     * 
     */
    public List<Checklist> getChecklist() {
        if (checklist == null) {
            checklist = new ArrayList<Checklist>();
        }
        return this.checklist;
    }

    public boolean isSetChecklist() {
        return ((this.checklist!= null)&&(!this.checklist.isEmpty()));
    }

    public void unsetChecklist() {
        this.checklist = null;
    }

    /**
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

    /**
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

    /**
     * Gets the value of the client property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClient() {
        return client;
    }

    /**
     * Sets the value of the client property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClient(String value) {
        this.client = value;
    }

    public boolean isSetClient() {
        return (this.client!= null);
    }

    /**
     * Gets the value of the developer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeveloper() {
        return developer;
    }

    /**
     * Sets the value of the developer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeveloper(String value) {
        this.developer = value;
    }

    public boolean isSetDeveloper() {
        return (this.developer!= null);
    }

}
