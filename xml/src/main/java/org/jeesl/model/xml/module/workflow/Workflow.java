
package org.jeesl.model.xml.module.workflow;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import net.sf.ahtutils.xml.status.Contexts;


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
 *         &lt;element ref="{http://ahtutils.aht-group.com/status}contexts"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/workflow}processes"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/workflow}activities"/&gt;
 *         &lt;element ref="{http://www.jeesl.org/workflow}stage"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "contexts",
    "processes",
    "activities",
    "stage"
})
@XmlRootElement(name = "workflow")
public class Workflow
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(namespace = "http://ahtutils.aht-group.com/status", required = true)
    protected Contexts contexts;
    @XmlElement(required = true)
    protected Processes processes;
    @XmlElement(required = true)
    protected Activities activities;
    @XmlElement(required = true)
    protected Stage stage;

    /**
     * Gets the value of the contexts property.
     * 
     * @return
     *     possible object is
     *     {@link Contexts }
     *     
     */
    public Contexts getContexts() {
        return contexts;
    }

    /**
     * Sets the value of the contexts property.
     * 
     * @param value
     *     allowed object is
     *     {@link Contexts }
     *     
     */
    public void setContexts(Contexts value) {
        this.contexts = value;
    }

    public boolean isSetContexts() {
        return (this.contexts!= null);
    }

    /**
     * Gets the value of the processes property.
     * 
     * @return
     *     possible object is
     *     {@link Processes }
     *     
     */
    public Processes getProcesses() {
        return processes;
    }

    /**
     * Sets the value of the processes property.
     * 
     * @param value
     *     allowed object is
     *     {@link Processes }
     *     
     */
    public void setProcesses(Processes value) {
        this.processes = value;
    }

    public boolean isSetProcesses() {
        return (this.processes!= null);
    }

    /**
     * Gets the value of the activities property.
     * 
     * @return
     *     possible object is
     *     {@link Activities }
     *     
     */
    public Activities getActivities() {
        return activities;
    }

    /**
     * Sets the value of the activities property.
     * 
     * @param value
     *     allowed object is
     *     {@link Activities }
     *     
     */
    public void setActivities(Activities value) {
        this.activities = value;
    }

    public boolean isSetActivities() {
        return (this.activities!= null);
    }

    /**
     * Gets the value of the stage property.
     * 
     * @return
     *     possible object is
     *     {@link Stage }
     *     
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Sets the value of the stage property.
     * 
     * @param value
     *     allowed object is
     *     {@link Stage }
     *     
     */
    public void setStage(Stage value) {
        this.stage = value;
    }

    public boolean isSetStage() {
        return (this.stage!= null);
    }

}
