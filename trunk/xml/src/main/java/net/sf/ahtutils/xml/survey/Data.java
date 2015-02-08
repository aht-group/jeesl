
package net.sf.ahtutils.xml.survey;

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
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://ahtutils.aht-group.com/survey}survey"/>
 *         &lt;element ref="{http://ahtutils.aht-group.com/survey}correlation"/>
 *         &lt;element ref="{http://ahtutils.aht-group.com/survey}answer" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}long" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "survey",
    "correlation",
    "answer"
})
@XmlRootElement(name = "data")
public class Data
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected Survey survey;
    @XmlElement(required = true)
    protected Correlation correlation;
    @XmlElement(required = true)
    protected List<Answer> answer;
    @XmlAttribute(name = "id")
    protected Long id;

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
     * Gets the value of the correlation property.
     * 
     * @return
     *     possible object is
     *     {@link Correlation }
     *     
     */
    public Correlation getCorrelation() {
        return correlation;
    }

    /**
     * Sets the value of the correlation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Correlation }
     *     
     */
    public void setCorrelation(Correlation value) {
        this.correlation = value;
    }

    public boolean isSetCorrelation() {
        return (this.correlation!= null);
    }

    /**
     * Gets the value of the answer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the answer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAnswer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Answer }
     * 
     * 
     */
    public List<Answer> getAnswer() {
        if (answer == null) {
            answer = new ArrayList<Answer>();
        }
        return this.answer;
    }

    public boolean isSetAnswer() {
        return ((this.answer!= null)&&(!this.answer.isEmpty()));
    }

    public void unsetAnswer() {
        this.answer = null;
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

}
