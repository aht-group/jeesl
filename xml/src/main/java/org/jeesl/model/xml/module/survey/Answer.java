
package org.jeesl.model.xml.module.survey;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import org.jeesl.model.xml.text.Remark;


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
 *         &lt;element ref="{http://www.jeesl.org/survey}data"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/survey}question"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/text}answer"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/text}remark"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/survey}option"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/survey}matrix"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}long" /&gt;
 *       &lt;attribute name="valueBoolean" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="valueNumber" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="valueDouble" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
 *       &lt;attribute name="valueDate" type="{http://www.w3.org/2001/XMLSchema}date" /&gt;
 *       &lt;attribute name="score" type="{http://www.w3.org/2001/XMLSchema}double" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "data",
    "question",
    "answer",
    "remark",
    "option",
    "matrix"
})
@XmlRootElement(name = "answer")
public class Answer
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected Data data;
    @XmlElement(required = true)
    protected Question question;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/text", required = true)
    protected net.sf.ahtutils.xml.text.Answer answer;
    @XmlElement(namespace = "http://www.jeesl.org/text", required = true)
    protected Remark remark;
    @XmlElement(required = true)
    protected Option option;
    @XmlElement(required = true)
    protected Matrix matrix;
    @XmlAttribute(name = "id")
    protected Long id;
    @XmlAttribute(name = "valueBoolean")
    protected Boolean valueBoolean;
    @XmlAttribute(name = "valueNumber")
    protected Integer valueNumber;
    @XmlAttribute(name = "valueDouble")
    protected Double valueDouble;
    @XmlAttribute(name = "valueDate")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar valueDate;
    @XmlAttribute(name = "score")
    protected Double score;

    /**
     * Gets the value of the data property.
     * 
     * @return
     *     possible object is
     *     {@link Data }
     *     
     */
    public Data getData() {
        return data;
    }

    /**
     * Sets the value of the data property.
     * 
     * @param value
     *     allowed object is
     *     {@link Data }
     *     
     */
    public void setData(Data value) {
        this.data = value;
    }

    public boolean isSetData() {
        return (this.data!= null);
    }

    /**
     * Gets the value of the question property.
     * 
     * @return
     *     possible object is
     *     {@link Question }
     *     
     */
    public Question getQuestion() {
        return question;
    }

    /**
     * Sets the value of the question property.
     * 
     * @param value
     *     allowed object is
     *     {@link Question }
     *     
     */
    public void setQuestion(Question value) {
        this.question = value;
    }

    public boolean isSetQuestion() {
        return (this.question!= null);
    }

    /**
     * Gets the value of the answer property.
     * 
     * @return
     *     possible object is
     *     {@link net.sf.ahtutils.xml.text.Answer }
     *     
     */
    public net.sf.ahtutils.xml.text.Answer getAnswer() {
        return answer;
    }

    /**
     * Sets the value of the answer property.
     * 
     * @param value
     *     allowed object is
     *     {@link net.sf.ahtutils.xml.text.Answer }
     *     
     */
    public void setAnswer(net.sf.ahtutils.xml.text.Answer value) {
        this.answer = value;
    }

    public boolean isSetAnswer() {
        return (this.answer!= null);
    }

    /**
     * Gets the value of the remark property.
     * 
     * @return
     *     possible object is
     *     {@link Remark }
     *     
     */
    public Remark getRemark() {
        return remark;
    }

    /**
     * Sets the value of the remark property.
     * 
     * @param value
     *     allowed object is
     *     {@link Remark }
     *     
     */
    public void setRemark(Remark value) {
        this.remark = value;
    }

    public boolean isSetRemark() {
        return (this.remark!= null);
    }

    /**
     * Gets the value of the option property.
     * 
     * @return
     *     possible object is
     *     {@link Option }
     *     
     */
    public Option getOption() {
        return option;
    }

    /**
     * Sets the value of the option property.
     * 
     * @param value
     *     allowed object is
     *     {@link Option }
     *     
     */
    public void setOption(Option value) {
        this.option = value;
    }

    public boolean isSetOption() {
        return (this.option!= null);
    }

    /**
     * Gets the value of the matrix property.
     * 
     * @return
     *     possible object is
     *     {@link Matrix }
     *     
     */
    public Matrix getMatrix() {
        return matrix;
    }

    /**
     * Sets the value of the matrix property.
     * 
     * @param value
     *     allowed object is
     *     {@link Matrix }
     *     
     */
    public void setMatrix(Matrix value) {
        this.matrix = value;
    }

    public boolean isSetMatrix() {
        return (this.matrix!= null);
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
     * Gets the value of the valueBoolean property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isValueBoolean() {
        return valueBoolean;
    }

    /**
     * Sets the value of the valueBoolean property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setValueBoolean(boolean value) {
        this.valueBoolean = value;
    }

    public boolean isSetValueBoolean() {
        return (this.valueBoolean!= null);
    }

    public void unsetValueBoolean() {
        this.valueBoolean = null;
    }

    /**
     * Gets the value of the valueNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getValueNumber() {
        return valueNumber;
    }

    /**
     * Sets the value of the valueNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setValueNumber(int value) {
        this.valueNumber = value;
    }

    public boolean isSetValueNumber() {
        return (this.valueNumber!= null);
    }

    public void unsetValueNumber() {
        this.valueNumber = null;
    }

    /**
     * Gets the value of the valueDouble property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public double getValueDouble() {
        return valueDouble;
    }

    /**
     * Sets the value of the valueDouble property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setValueDouble(double value) {
        this.valueDouble = value;
    }

    public boolean isSetValueDouble() {
        return (this.valueDouble!= null);
    }

    public void unsetValueDouble() {
        this.valueDouble = null;
    }

    /**
     * Gets the value of the valueDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getValueDate() {
        return valueDate;
    }

    /**
     * Sets the value of the valueDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setValueDate(XMLGregorianCalendar value) {
        this.valueDate = value;
    }

    public boolean isSetValueDate() {
        return (this.valueDate!= null);
    }

    /**
     * Gets the value of the score property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public double getScore() {
        return score;
    }

    /**
     * Sets the value of the score property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setScore(double value) {
        this.score = value;
    }

    public boolean isSetScore() {
        return (this.score!= null);
    }

    public void unsetScore() {
        this.score = null;
    }

}
