
package net.sf.ahtutils.xml.sync;

import javax.xml.bind.annotation.XmlRegistry;


/**
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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.sf.ahtutils.xml.sync
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DataUpdate }
     * 
     */
    public DataUpdate createDataUpdate() {
        return new DataUpdate();
    }

    /**
     * Create an instance of {@link Result }
     * 
     */
    public Result createResult() {
        return new Result();
    }

    /**
     * Create an instance of {@link Mapper }
     * 
     */
    public Mapper createMapper() {
        return new Mapper();
    }

    /**
     * Create an instance of {@link Sync }
     * 
     */
    public Sync createSync() {
        return new Sync();
    }

    /**
     * Create an instance of {@link Exceptions }
     * 
     */
    public Exceptions createExceptions() {
        return new Exceptions();
    }

    /**
     * Create an instance of {@link Exception }
     * 
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link Mappings }
     * 
     */
    public Mappings createMappings() {
        return new Mappings();
    }

    /**
     * Create an instance of {@link Entity }
     * 
     */
    public Entity createEntity() {
        return new Entity();
    }

}
