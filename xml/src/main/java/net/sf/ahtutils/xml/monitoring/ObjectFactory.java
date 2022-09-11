
package net.sf.ahtutils.xml.monitoring;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.sf.ahtutils.xml.monitoring package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.sf.ahtutils.xml.monitoring
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Monitoring }
     * 
     */
    public Monitoring createMonitoring() {
        return new Monitoring();
    }

    /**
     * Create an instance of {@link Transmission }
     * 
     */
    public Transmission createTransmission() {
        return new Transmission();
    }

    /**
     * Create an instance of {@link DataSet }
     * 
     */
    public DataSet createDataSet() {
        return new DataSet();
    }

    /**
     * Create an instance of {@link Indicator }
     * 
     */
    public Indicator createIndicator() {
        return new Indicator();
    }

    /**
     * Create an instance of {@link Observer }
     * 
     */
    public Observer createObserver() {
        return new Observer();
    }

    /**
     * Create an instance of {@link Data }
     * 
     */
    public Data createData() {
        return new Data();
    }

    /**
     * Create an instance of {@link Value }
     * 
     */
    public Value createValue() {
        return new Value();
    }

    /**
     * Create an instance of {@link ProcessingResult }
     * 
     */
    public ProcessingResult createProcessingResult() {
        return new ProcessingResult();
    }

    /**
     * Create an instance of {@link Component }
     * 
     */
    public Component createComponent() {
        return new Component();
    }

    /**
     * Create an instance of {@link Actors }
     * 
     */
    public Actors createActors() {
        return new Actors();
    }

    /**
     * Create an instance of {@link Actor }
     * 
     */
    public Actor createActor() {
        return new Actor();
    }

}
