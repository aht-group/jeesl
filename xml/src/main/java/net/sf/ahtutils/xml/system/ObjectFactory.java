
package net.sf.ahtutils.xml.system;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.sf.ahtutils.xml.system package. 
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


    public Info createInfo() {
        return new Info();
    }


    public Uptime createUptime() {
        return new Uptime();
    }

    
    public Request createRequest() {
        return new Request();
    }


    public Constraints createConstraints() {
        return new Constraints();
    }


    public ConstraintScope createConstraintScope() {
        return new ConstraintScope();
    }


    public Constraint createConstraint() {
        return new Constraint();
    }


    public ConstraintAttribute createConstraintAttribute() {
        return new ConstraintAttribute();
    }


    public ConstraintSolution createConstraintSolution() {
        return new ConstraintSolution();
    }


    public Release createRelease() {
        return new Release();
    }

}
