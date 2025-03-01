
package org.jeesl.model.xml.io.db.query;

import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.jeesl.model.xml.io.db.query package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.jeesl.model.xml.io.db.query
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link QuerySurvey }
     * 
     */
    public QuerySurvey createQuerySurvey() {
        return new QuerySurvey();
    }

    /**
     * Create an instance of {@link QueryAttribute }
     * 
     */
    public QueryAttribute createQueryAttribute() {
        return new QueryAttribute();
    }

    /**
     * Create an instance of {@link QueryFinance }
     * 
     */
    public QueryFinance createQueryFinance() {
        return new QueryFinance();
    }

    /**
     * Create an instance of {@link QueryTs }
     * 
     */
    public QueryTs createQueryTs() {
        return new QueryTs();
    }

    /**
     * Create an instance of {@link QueryWf }
     * 
     */
    public QueryWf createQueryWf() {
        return new QueryWf();
    }

    /**
     * Create an instance of {@link QueryCalendar }
     * 
     */
    public QueryCalendar createQueryCalendar() {
        return new QueryCalendar();
    }

    /**
     * Create an instance of {@link QueryRevision }
     * 
     */
    public QueryRevision createQueryRevision() {
        return new QueryRevision();
    }

    /**
     * Create an instance of {@link QuerySecurity }
     * 
     */
    public QuerySecurity createQuerySecurity() {
        return new QuerySecurity();
    }

}
