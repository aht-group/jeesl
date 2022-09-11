
package org.jeesl.model.xml.module.survey;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import net.sf.ahtutils.xml.finance.Figures;
import net.sf.ahtutils.xml.status.Unit;
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

    public boolean isSetQuestion() {
        return (this.question!= null);
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

    public boolean isSetUnit() {
        return (this.unit!= null);
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

    public boolean isSetScore() {
        return (this.score!= null);
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

    public boolean isSetAnswer() {
        return (this.answer!= null);
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

    public boolean isSetOptions() {
        return (this.options!= null);
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

    public boolean isSetFigures() {
        return (this.figures!= null);
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
     * Gets the value of the position property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getPosition() {
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
    public void setPosition(int value) {
        this.position = value;
    }

    public boolean isSetPosition() {
        return (this.position!= null);
    }

    public void unsetPosition() {
        this.position = null;
    }

    /**
     * Gets the value of the visible property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isVisible() {
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
    public void setVisible(boolean value) {
        this.visible = value;
    }

    public boolean isSetVisible() {
        return (this.visible!= null);
    }

    public void unsetVisible() {
        this.visible = null;
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

    public boolean isSetTopic() {
        return (this.topic!= null);
    }

    /**
     * Gets the value of the showBoolean property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isShowBoolean() {
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
    public void setShowBoolean(boolean value) {
        this.showBoolean = value;
    }

    public boolean isSetShowBoolean() {
        return (this.showBoolean!= null);
    }

    public void unsetShowBoolean() {
        this.showBoolean = null;
    }

    /**
     * Gets the value of the showInteger property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isShowInteger() {
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
    public void setShowInteger(boolean value) {
        this.showInteger = value;
    }

    public boolean isSetShowInteger() {
        return (this.showInteger!= null);
    }

    public void unsetShowInteger() {
        this.showInteger = null;
    }

    /**
     * Gets the value of the showDouble property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isShowDouble() {
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
    public void setShowDouble(boolean value) {
        this.showDouble = value;
    }

    public boolean isSetShowDouble() {
        return (this.showDouble!= null);
    }

    public void unsetShowDouble() {
        this.showDouble = null;
    }

    /**
     * Gets the value of the showText property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isShowText() {
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
    public void setShowText(boolean value) {
        this.showText = value;
    }

    public boolean isSetShowText() {
        return (this.showText!= null);
    }

    public void unsetShowText() {
        this.showText = null;
    }

    /**
     * Gets the value of the showScore property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isShowScore() {
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
    public void setShowScore(boolean value) {
        this.showScore = value;
    }

    public boolean isSetShowScore() {
        return (this.showScore!= null);
    }

    public void unsetShowScore() {
        this.showScore = null;
    }

    /**
     * Gets the value of the showRemark property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isShowRemark() {
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
    public void setShowRemark(boolean value) {
        this.showRemark = value;
    }

    public boolean isSetShowRemark() {
        return (this.showRemark!= null);
    }

    public void unsetShowRemark() {
        this.showRemark = null;
    }

    /**
     * Gets the value of the showSelectOne property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isShowSelectOne() {
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
    public void setShowSelectOne(boolean value) {
        this.showSelectOne = value;
    }

    public boolean isSetShowSelectOne() {
        return (this.showSelectOne!= null);
    }

    public void unsetShowSelectOne() {
        this.showSelectOne = null;
    }

}
