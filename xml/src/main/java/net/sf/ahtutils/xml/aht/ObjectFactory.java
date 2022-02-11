
package net.sf.ahtutils.xml.aht;

import javax.xml.bind.annotation.XmlRegistry;


/*
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.sf.ahtutils.xml.aht package. 
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


    public Aht createAht() {
        return new Aht();
    }


    public Container createContainer() {
        return new Container();
    }

    public Query createQuery() {
        return new Query();
    }

}
