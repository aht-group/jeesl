
package org.jeesl.model.xml.module.survey;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import org.jeesl.model.xml.io.cms.text.Remark;
import org.jeesl.model.xml.io.locale.status.Unit;
import org.jeesl.model.xml.module.finance.Figures;


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
    protected org.jeesl.model.xml.io.cms.chars.Question question;
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
     * Ruft den Wert der question-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link org.jeesl.model.xml.io.cms.chars.Question }
     *     
     */
    public org.jeesl.model.xml.io.cms.chars.Question getQuestion() {
        return question;
    }

    /**
     * Legt den Wert der question-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link org.jeesl.model.xml.io.cms.chars.Question }
     *     
     */
    public void setQuestion(org.jeesl.model.xml.io.cms.chars.Question value) {
        this.question = value;
    }

    /**
     * Ruft den Wert der remark-Eigenschaft ab.
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
     * Legt den Wert der remark-Eigenschaft fest.
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
     * Ruft den Wert der unit-Eigenschaft ab.
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
     * Legt den Wert der unit-Eigenschaft fest.
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
     * Ruft den Wert der score-Eigenschaft ab.
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
     * Legt den Wert der score-Eigenschaft fest.
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
     * Ruft den Wert der answer-Eigenschaft ab.
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
     * Legt den Wert der answer-Eigenschaft fest.
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
     * Ruft den Wert der options-Eigenschaft ab.
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
     * Legt den Wert der options-Eigenschaft fest.
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
     * Ruft den Wert der figures-Eigenschaft ab.
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
     * Legt den Wert der figures-Eigenschaft fest.
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
     * Ruft den Wert der id-Eigenschaft ab.
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
     * Legt den Wert der id-Eigenschaft fest.
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
     * Ruft den Wert der position-Eigenschaft ab.
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
     * Legt den Wert der position-Eigenschaft fest.
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
     * Ruft den Wert der visible-Eigenschaft ab.
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
     * Legt den Wert der visible-Eigenschaft fest.
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

    /**
     * Ruft den Wert der topic-Eigenschaft ab.
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
     * Legt den Wert der topic-Eigenschaft fest.
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
     * Ruft den Wert der showBoolean-Eigenschaft ab.
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
     * Legt den Wert der showBoolean-Eigenschaft fest.
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
     * Ruft den Wert der showInteger-Eigenschaft ab.
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
     * Legt den Wert der showInteger-Eigenschaft fest.
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
     * Ruft den Wert der showDouble-Eigenschaft ab.
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
     * Legt den Wert der showDouble-Eigenschaft fest.
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
     * Ruft den Wert der showText-Eigenschaft ab.
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
     * Legt den Wert der showText-Eigenschaft fest.
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
     * Ruft den Wert der showScore-Eigenschaft ab.
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
     * Legt den Wert der showScore-Eigenschaft fest.
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
     * Ruft den Wert der showRemark-Eigenschaft ab.
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
     * Legt den Wert der showRemark-Eigenschaft fest.
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
     * Ruft den Wert der showSelectOne-Eigenschaft ab.
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
     * Legt den Wert der showSelectOne-Eigenschaft fest.
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
