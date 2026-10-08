
package org.jeesl.model.xml.xsd.aht;

import java.io.Serializable;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import org.jeesl.model.xml.io.graphic.Graphic;
import org.jeesl.model.xml.io.label.Entity;
import org.jeesl.model.xml.io.locale.status.Langs;
import org.jeesl.model.xml.io.locale.status.Model;
import org.jeesl.model.xml.io.locale.status.Status;
import org.jeesl.model.xml.io.locale.status.Type;
import org.jeesl.model.xml.io.report.Report;
import org.jeesl.model.xml.module.dev.qa.Test;
import org.jeesl.model.xml.module.survey.Answer;
import org.jeesl.model.xml.module.survey.Survey;
import org.jeesl.model.xml.module.survey.Surveys;
import org.jeesl.model.xml.module.survey.Template;
import org.jeesl.model.xml.module.survey.Templates;
import org.jeesl.model.xml.system.security.Category;
import org.jeesl.model.xml.system.security.Role;
import org.jeesl.model.xml.system.security.Staff;
import org.jeesl.model.xml.system.util.TrafficLight;
import org.jeesl.model.xml.system.util.TrafficLights;


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
 *         &lt;element ref="{https://www.jeesl.org/jeesl/xsd/system/security}role"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}langs"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}status"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}type"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}model"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/qa}test"/&gt;
 *         &lt;element ref="{https://www.jeesl.org/jeesl/xsd/system/security}category"/&gt;
 *         &lt;element ref="{https://www.jeesl.org/jeesl/xsd/system/security}staff"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/report}report"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/revision}entity"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/survey}templates"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/survey}template"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/survey}surveys"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/survey}survey"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/survey}answer"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/symbol}graphic"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/utils}trafficLight"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/utils}trafficLights"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="lang" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "role",
    "langs",
    "status",
    "type",
    "model",
    "test",
    "category",
    "staff",
    "report",
    "entity",
    "templates",
    "template",
    "surveys",
    "survey",
    "answer",
    "graphic",
    "trafficLight",
    "trafficLights"
})
@XmlRootElement(name = "query")
public class Query
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(namespace = "https://www.jeesl.org/jeesl/xsd/system/security", required = true)
    protected Role role;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/status", required = true)
    protected Langs langs;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/status", required = true)
    protected Status status;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/status", required = true)
    protected Type type;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/status", required = true)
    protected Model model;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/qa", required = true)
    protected Test test;
    @XmlElement(namespace = "https://www.jeesl.org/jeesl/xsd/system/security", required = true)
    protected Category category;
    @XmlElement(namespace = "https://www.jeesl.org/jeesl/xsd/system/security", required = true)
    protected Staff staff;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/report", required = true)
    protected Report report;
    @XmlElement(namespace = "http://www.jeesl.org/revision", required = true)
    protected Entity entity;
    @XmlElement(namespace = "http://www.jeesl.org/survey", required = true)
    protected Templates templates;
    @XmlElement(namespace = "http://www.jeesl.org/survey", required = true)
    protected Template template;
    @XmlElement(namespace = "http://www.jeesl.org/survey", required = true)
    protected Surveys surveys;
    @XmlElement(namespace = "http://www.jeesl.org/survey", required = true)
    protected Survey survey;
    @XmlElement(namespace = "http://www.jeesl.org/survey", required = true)
    protected Answer answer;
    @XmlElement(namespace = "http://www.jeesl.org/symbol", required = true)
    protected Graphic graphic;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/utils", required = true)
    protected TrafficLight trafficLight;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/utils", required = true)
    protected TrafficLights trafficLights;
    @XmlAttribute(name = "lang")
    protected String lang;

    /**
     * Ruft den Wert der role-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Role }
     *     
     */
    public Role getRole() {
        return role;
    }

    /**
     * Legt den Wert der role-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Role }
     *     
     */
    public void setRole(Role value) {
        this.role = value;
    }

    /**
     * Ruft den Wert der langs-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Langs }
     *     
     */
    public Langs getLangs() {
        return langs;
    }

    /**
     * Legt den Wert der langs-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Langs }
     *     
     */
    public void setLangs(Langs value) {
        this.langs = value;
    }

    /**
     * Ruft den Wert der status-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Status }
     *     
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Legt den Wert der status-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Status }
     *     
     */
    public void setStatus(Status value) {
        this.status = value;
    }

    /**
     * Ruft den Wert der type-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Type }
     *     
     */
    public Type getType() {
        return type;
    }

    /**
     * Legt den Wert der type-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Type }
     *     
     */
    public void setType(Type value) {
        this.type = value;
    }

    /**
     * Ruft den Wert der model-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Model }
     *     
     */
    public Model getModel() {
        return model;
    }

    /**
     * Legt den Wert der model-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Model }
     *     
     */
    public void setModel(Model value) {
        this.model = value;
    }

    /**
     * Ruft den Wert der test-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Test }
     *     
     */
    public Test getTest() {
        return test;
    }

    /**
     * Legt den Wert der test-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Test }
     *     
     */
    public void setTest(Test value) {
        this.test = value;
    }

    /**
     * Ruft den Wert der category-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Category }
     *     
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Legt den Wert der category-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Category }
     *     
     */
    public void setCategory(Category value) {
        this.category = value;
    }

    /**
     * Ruft den Wert der staff-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Staff }
     *     
     */
    public Staff getStaff() {
        return staff;
    }

    /**
     * Legt den Wert der staff-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Staff }
     *     
     */
    public void setStaff(Staff value) {
        this.staff = value;
    }

    /**
     * Ruft den Wert der report-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Report }
     *     
     */
    public Report getReport() {
        return report;
    }

    /**
     * Legt den Wert der report-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Report }
     *     
     */
    public void setReport(Report value) {
        this.report = value;
    }

    /**
     * Ruft den Wert der entity-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Entity }
     *     
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Legt den Wert der entity-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Entity }
     *     
     */
    public void setEntity(Entity value) {
        this.entity = value;
    }

    /**
     * Ruft den Wert der templates-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Templates }
     *     
     */
    public Templates getTemplates() {
        return templates;
    }

    /**
     * Legt den Wert der templates-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Templates }
     *     
     */
    public void setTemplates(Templates value) {
        this.templates = value;
    }

    /**
     * Ruft den Wert der template-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Template }
     *     
     */
    public Template getTemplate() {
        return template;
    }

    /**
     * Legt den Wert der template-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Template }
     *     
     */
    public void setTemplate(Template value) {
        this.template = value;
    }

    /**
     * Ruft den Wert der surveys-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Surveys }
     *     
     */
    public Surveys getSurveys() {
        return surveys;
    }

    /**
     * Legt den Wert der surveys-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Surveys }
     *     
     */
    public void setSurveys(Surveys value) {
        this.surveys = value;
    }

    /**
     * Ruft den Wert der survey-Eigenschaft ab.
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
     * Legt den Wert der survey-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Survey }
     *     
     */
    public void setSurvey(Survey value) {
        this.survey = value;
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
     * Ruft den Wert der graphic-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Graphic }
     *     
     */
    public Graphic getGraphic() {
        return graphic;
    }

    /**
     * Legt den Wert der graphic-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Graphic }
     *     
     */
    public void setGraphic(Graphic value) {
        this.graphic = value;
    }

    /**
     * Ruft den Wert der trafficLight-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link TrafficLight }
     *     
     */
    public TrafficLight getTrafficLight() {
        return trafficLight;
    }

    /**
     * Legt den Wert der trafficLight-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link TrafficLight }
     *     
     */
    public void setTrafficLight(TrafficLight value) {
        this.trafficLight = value;
    }

    /**
     * Ruft den Wert der trafficLights-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link TrafficLights }
     *     
     */
    public TrafficLights getTrafficLights() {
        return trafficLights;
    }

    /**
     * Legt den Wert der trafficLights-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link TrafficLights }
     *     
     */
    public void setTrafficLights(TrafficLights value) {
        this.trafficLights = value;
    }

    /**
     * Ruft den Wert der lang-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLang() {
        return lang;
    }

    /**
     * Legt den Wert der lang-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLang(String value) {
        this.lang = value;
    }

}
