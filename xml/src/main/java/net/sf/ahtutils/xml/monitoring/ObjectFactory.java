
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


    public ObjectFactory() {
    }


    public Monitoring createMonitoring() {
        return new Monitoring();
    }


    public Transmission createTransmission() {
        return new Transmission();
    }


    public DataSet createDataSet() {
        return new DataSet();
    }


    public Indicator createIndicator() {
        return new Indicator();
    }


    public Observer createObserver() {
        return new Observer();
    }


    public Data createData() {
        return new Data();
    }


    public Value createValue() {
        return new Value();
    }

 
    public ProcessingResult createProcessingResult() {
        return new ProcessingResult();
    }

 
    public Component createComponent() {
        return new Component();
    }


    public Actors createActors() {
        return new Actors();
    }


    public Actor createActor() {
        return new Actor();
    }

}
