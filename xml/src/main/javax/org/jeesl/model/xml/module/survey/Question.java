
package org.jeesl.model.xml.module.survey;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.jeesl.model.xml.io.cms.text.Remark;
import org.jeesl.model.xml.io.locale.status.Unit;
import org.jeesl.model.xml.module.finance.Figures;


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
 *         &lt;element ref="{http://ahtutils.aht-group.com/text}question"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/text}remark"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}unit"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/survey}score"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/survey}answer"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/survey}options"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/finance}figures"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}long" /&gt;
 *       &lt;attribute name="position" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="visible" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="code" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="topic" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="showBoolean" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="showInteger" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="showDouble" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="showText" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="showScore" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="showRemark" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="showSelectOne" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "question",
    "remark",
    "unit",
    "score",
    "answer",
    "options",
    "figures"
})
@XmlRootElement(name = "question")
public class Question
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/text", required = true)
    protected net.sf.ahtutils.xml.text.Question question;
    @XmlElement(namespace = "http://www.jeesl.org/text", required = true)
    protected Remark remark;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/status", required = true)
    protected Unit unit;
    @XmlElement(required = true)
    protected Score score;
    @XmlElement(required = true)
    protected Answer answer;
    @XmlElement(required = true)
    protected Options options;
    @XmlElement(namespace = "http://www.jeesl.org/finance", required = true)
    protected Figures figures;
    @XmlAttribute(name = "id")
    protected Long id;
    @XmlAttribute(name = "position")
    protected Integer position;
    @XmlAttribute(name = "visible")
    protected Boolean visible;
    @XmlAttribute(name = "code")
    protected String code;
    @XmlAttribute(name = "topic")
    protected String topic;
    @XmlAttribute(name = "showBoolean")
    protected Boolean showBoolean;
    @XmlAttribute(name = "showInteger")
    protected Boolean showInteger;
    @XmlAttribute(name = "showDouble")
    protected Boolean showDouble;
    @XmlAttribute(name = "showText")
    protected Boolean showText;
    @XmlAttribute(name = "showScore")
    protected Boolean showScore;
    @XmlAttribute(name = "showRemark")
    protected Boolean showRemark;
    @XmlAttribute(name = "showSelectOne")
    protected Boolean showSelectOne;

    /**
     * Gets the value of the question property.
     * 
     * @return
     *     possible object is
     *     {@link net.sf.ahtutils.xml.text.Question }
     *     
     */
    public net.sf.ahtutils.xml.text.Question getQuestion() {
        return question;
    }

    /**
     * Sets the value of the question property.
     * 
     * @param value
     *     allowed object is
     *     {@link net.sf.ahtutils.xml.text.Question }
     *     
     */
    public void setQuestion(net.sf.ahtutils.xml.text.Question value) {
        this.question = value;
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

    /**
     * Gets the value of the unit property.
     * 
     * @return
     *     possible object is
     *     {@link Unit }
     *     
     */
    public Unit getUnit() {
        return unit;
    }

    /**
     * Sets the value of the unit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Unit }
     *     
     */
    public void setUnit(Unit value) {
        this.unit = value;
    }

    /**
     * Gets the value of the score property.
     * 
     * @return
     *     possible object is
     *     {@link Score }
     *     
     */
    public Score getScore() {
        return score;
    }

    /**
     * Sets the value of the score property.
     * 
     * @param value
     *     allowed object is
     *     {@link Score }
     *     
     */
    public void setScore(Score value) {
        this.score = value;
    }

    /**
     * Gets the value of the answer property.
     * 
     * @return
     *     possible object is
     *     {@link Answer }
     *     
     */
    public Answer getAnswer() {
        return answer;
    }

    /**
     * Sets the value of the answer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Answer }
     *     
     */
    public void setAnswer(Answer value) {
        this.answer = value;
    }

    /**
     * Gets the value of the options property.
     * 
     * @return
     *     possible object is
     *     {@link Options }
     *     
     */
    public Options getOptions() {
        return options;
    }

    /**
     * Sets the value of the options property.
     * 
     * @param value
     *     allowed object is
     *     {@link Options }
     *     
     */
    public void setOptions(Options value) {
        this.options = value;
    }

    /**
     * Gets the value of the figures property.
     * 
     * @return
     *     possible object is
     *     {@link Figures }
     *     
     */
    public Figures getFigures() {
        return figures;
    }

    /**
     * Sets the value of the figures property.
     * 
     * @param value
     *     allowed object is
     *     {@link Figures }
     *     
     */
    public void setFigures(Figures value) {
        this.figures = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getId() {
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
    public void setId(Long value) {
        this.id = value;
    }

    /**
     * Gets the value of the position property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * Sets the value of the position property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPosition(Integer value) {
        this.position = value;
    }

    /**
     * Gets the value of the visible property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isVisible() {
        return visible;
    }

    /**
     * Sets the value of the visible property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setVisible(Boolean value) {
        this.visible = value;
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

    /**
     * Gets the value of the topic property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Sets the value of the topic property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTopic(String value) {
        this.topic = value;
    }

    /**
     * Gets the value of the showBoolean property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isShowBoolean() {
        return showBoolean;
    }

    /**
     * Sets the value of the showBoolean property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setShowBoolean(Boolean value) {
        this.showBoolean = value;
    }

    /**
     * Gets the value of the showInteger property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isShowInteger() {
        return showInteger;
    }

    /**
     * Sets the value of the showInteger property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setShowInteger(Boolean value) {
        this.showInteger = value;
    }

    /**
     * Gets the value of the showDouble property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isShowDouble() {
        return showDouble;
    }

    /**
     * Sets the value of the showDouble property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setShowDouble(Boolean value) {
        this.showDouble = value;
    }

    /**
     * Gets the value of the showText property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isShowText() {
        return showText;
    }

    /**
     * Sets the value of the showText property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setShowText(Boolean value) {
        this.showText = value;
    }

    /**
     * Gets the value of the showScore property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isShowScore() {
        return showScore;
    }

    /**
     * Sets the value of the showScore property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setShowScore(Boolean value) {
        this.showScore = value;
    }

    /**
     * Gets the value of the showRemark property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isShowRemark() {
        return showRemark;
    }

    /**
     * Sets the value of the showRemark property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setShowRemark(Boolean value) {
        this.showRemark = value;
    }

    /**
     * Gets the value of the showSelectOne property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isShowSelectOne() {
        return showSelectOne;
    }

    /**
     * Sets the value of the showSelectOne property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setShowSelectOne(Boolean value) {
        this.showSelectOne = value;
    }

}
