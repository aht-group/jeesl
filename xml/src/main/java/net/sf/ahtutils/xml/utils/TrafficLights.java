
package net.sf.ahtutils.xml.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element ref="{http://ahtutils.aht-group.com/utils}trafficLight" maxOccurs="unbounded"/&gt;
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
    "trafficLight"
})
@XmlRootElement(name = "trafficLights")
public class TrafficLights
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected List<TrafficLight> trafficLight;


    public List<TrafficLight> getTrafficLight() {
        if (trafficLight == null) {
            trafficLight = new ArrayList<TrafficLight>();
        }
        return this.trafficLight;
    }

    public boolean isSetTrafficLight() {
        return ((this.trafficLight!= null)&&(!this.trafficLight.isEmpty()));
    }

    public void unsetTrafficLight() {
        this.trafficLight = null;
    }

}
