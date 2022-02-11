
package net.sf.ahtutils.xml.sync;

import javax.xml.bind.annotation.XmlRegistry;


/*
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.sf.ahtutils.xml.sync package. 
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


    /*
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.sf.ahtutils.xml.sync
     * 
     */
    public ObjectFactory() {
    }


    public DataUpdate createDataUpdate() {
        return new DataUpdate();
    }


    public Result createResult() {
        return new Result();
    }


    public Mapper createMapper() {
        return new Mapper();
    }


    public Sync createSync() {
        return new Sync();
    }


    public Exceptions createExceptions() {
        return new Exceptions();
    }

    public Exception createException() {
        return new Exception();
    }

    public Mappings createMappings() {
        return new Mappings();
    }

    public Entity createEntity() {
        return new Entity();
    }

}
