
package org.jeesl.model.xml.jeesl;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.jeesl.model.xml.jeesl package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.jeesl.model.xml.jeesl
     * 
     */
    public ObjectFactory() {
    }


    public Container createContainer() {
        return new Container();
    }


    public QuerySurvey createQuerySurvey() {
        return new QuerySurvey();
    }


    public QueryAttribute createQueryAttribute() {
        return new QueryAttribute();
    }


    public QueryFinance createQueryFinance() {
        return new QueryFinance();
    }


    public QueryTs createQueryTs() {
        return new QueryTs();
    }


    public QueryWf createQueryWf() {
        return new QueryWf();
    }


    public QueryCalendar createQueryCalendar() {
        return new QueryCalendar();
    }

  
    public QueryRevision createQueryRevision() {
        return new QueryRevision();
    }

    public QuerySecurity createQuerySecurity() {
        return new QuerySecurity();
    }

}
