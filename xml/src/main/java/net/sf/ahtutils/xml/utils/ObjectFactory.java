
package net.sf.ahtutils.xml.utils;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.sf.ahtutils.xml.utils package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.sf.ahtutils.xml.utils
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TrafficLight }
     * 
     */
    public TrafficLight createTrafficLight() {
        return new TrafficLight();
    }

    /**
     * Create an instance of {@link TrafficLights }
     * 
     */
    public TrafficLights createTrafficLights() {
        return new TrafficLights();
    }

    /**
     * Create an instance of {@link Utils }
     * 
     */
    public Utils createUtils() {
        return new Utils();
    }

    /**
     * Create an instance of {@link Property }
     * 
     */
    public Property createProperty() {
        return new Property();
    }

    /**
     * Create an instance of {@link Criteria }
     * 
     */
    public Criteria createCriteria() {
        return new Criteria();
    }

}
