
package net.sf.ahtutils.xml.aht;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import net.sf.ahtutils.xml.access.Role;
import net.sf.ahtutils.xml.qa.Test;
import net.sf.ahtutils.xml.report.Report;
import net.sf.ahtutils.xml.security.Category;
import net.sf.ahtutils.xml.security.Staff;
import net.sf.ahtutils.xml.status.Langs;
import net.sf.ahtutils.xml.status.Model;
import net.sf.ahtutils.xml.status.Status;
import net.sf.ahtutils.xml.status.Type;
import net.sf.ahtutils.xml.symbol.Graphic;
import net.sf.ahtutils.xml.utils.TrafficLight;
import net.sf.ahtutils.xml.utils.TrafficLights;
import org.jeesl.model.xml.module.survey.Answer;
import org.jeesl.model.xml.module.survey.Survey;
import org.jeesl.model.xml.module.survey.Surveys;
import org.jeesl.model.xml.module.survey.Template;
import org.jeesl.model.xml.module.survey.Templates;
import org.jeesl.model.xml.system.revision.Entity;


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
 *         &lt;element ref="{http://ahtutils.aht-group.com/access}role"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}langs"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}status"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}type"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}model"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/qa}test"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/security}category"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/security}staff"/&gt;
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
    @XmlElement(namespace = "http://ahtutils.aht-group.com/access", required = true)
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
    @XmlElement(namespace = "http://ahtutils.aht-group.com/security", required = true)
    protected Category category;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/security", required = true)
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
     * Gets the value of the role property.
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
     * Sets the value of the role property.
     * 
     * @param value
     *     allowed object is
     *     {@link Role }
     *     
     */
    public void setRole(Role value) {
        this.role = value;
    }

    public boolean isSetRole() {
        return (this.role!= null);
    }

    /**
     * Gets the value of the langs property.
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
     * Sets the value of the langs property.
     * 
     * @param value
     *     allowed object is
     *     {@link Langs }
     *     
     */
    public void setLangs(Langs value) {
        this.langs = value;
    }

    public boolean isSetLangs() {
        return (this.langs!= null);
    }

    /**
     * Gets the value of the status property.
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
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link Status }
     *     
     */
    public void setStatus(Status value) {
        this.status = value;
    }

    public boolean isSetStatus() {
        return (this.status!= null);
    }

    /**
     * Gets the value of the type property.
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
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link Type }
     *     
     */
    public void setType(Type value) {
        this.type = value;
    }

    public boolean isSetType() {
        return (this.type!= null);
    }

    /**
     * Gets the value of the model property.
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
     * Sets the value of the model property.
     * 
     * @param value
     *     allowed object is
     *     {@link Model }
     *     
     */
    public void setModel(Model value) {
        this.model = value;
    }

    public boolean isSetModel() {
        return (this.model!= null);
    }

    /**
     * Gets the value of the test property.
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
     * Sets the value of the test property.
     * 
     * @param value
     *     allowed object is
     *     {@link Test }
     *     
     */
    public void setTest(Test value) {
        this.test = value;
    }

    public boolean isSetTest() {
        return (this.test!= null);
    }

    /**
     * Gets the value of the category property.
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
     * Sets the value of the category property.
     * 
     * @param value
     *     allowed object is
     *     {@link Category }
     *     
     */
    public void setCategory(Category value) {
        this.category = value;
    }

    public boolean isSetCategory() {
        return (this.category!= null);
    }

    /**
     * Gets the value of the staff property.
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
     * Sets the value of the staff property.
     * 
     * @param value
     *     allowed object is
     *     {@link Staff }
     *     
     */
    public void setStaff(Staff value) {
        this.staff = value;
    }

    public boolean isSetStaff() {
        return (this.staff!= null);
    }

    /**
     * Gets the value of the report property.
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
     * Sets the value of the report property.
     * 
     * @param value
     *     allowed object is
     *     {@link Report }
     *     
     */
    public void setReport(Report value) {
        this.report = value;
    }

    public boolean isSetReport() {
        return (this.report!= null);
    }

    /**
     * Gets the value of the entity property.
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
     * Sets the value of the entity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Entity }
     *     
     */
    public void setEntity(Entity value) {
        this.entity = value;
    }

    public boolean isSetEntity() {
        return (this.entity!= null);
    }

    /**
     * Gets the value of the templates property.
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
     * Sets the value of the templates property.
     * 
     * @param value
     *     allowed object is
     *     {@link Templates }
     *     
     */
    public void setTemplates(Templates value) {
        this.templates = value;
    }

    public boolean isSetTemplates() {
        return (this.templates!= null);
    }

    /**
     * Gets the value of the template property.
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
     * Sets the value of the template property.
     * 
     * @param value
     *     allowed object is
     *     {@link Template }
     *     
     */
    public void setTemplate(Template value) {
        this.template = value;
    }

    public boolean isSetTemplate() {
        return (this.template!= null);
    }

    /**
     * Gets the value of the surveys property.
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
     * Sets the value of the surveys property.
     * 
     * @param value
     *     allowed object is
     *     {@link Surveys }
     *     
     */
    public void setSurveys(Surveys value) {
        this.surveys = value;
    }

    public boolean isSetSurveys() {
        return (this.surveys!= null);
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
     * Gets the value of the graphic property.
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
     * Sets the value of the graphic property.
     * 
     * @param value
     *     allowed object is
     *     {@link Graphic }
     *     
     */
    public void setGraphic(Graphic value) {
        this.graphic = value;
    }

    public boolean isSetGraphic() {
        return (this.graphic!= null);
    }

    /**
     * Gets the value of the trafficLight property.
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
     * Sets the value of the trafficLight property.
     * 
     * @param value
     *     allowed object is
     *     {@link TrafficLight }
     *     
     */
    public void setTrafficLight(TrafficLight value) {
        this.trafficLight = value;
    }

    public boolean isSetTrafficLight() {
        return (this.trafficLight!= null);
    }

    /**
     * Gets the value of the trafficLights property.
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
     * Sets the value of the trafficLights property.
     * 
     * @param value
     *     allowed object is
     *     {@link TrafficLights }
     *     
     */
    public void setTrafficLights(TrafficLights value) {
        this.trafficLights = value;
    }

    public boolean isSetTrafficLights() {
        return (this.trafficLights!= null);
    }

    /**
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

    /**
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

}
