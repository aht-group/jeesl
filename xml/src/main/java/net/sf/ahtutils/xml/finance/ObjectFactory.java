
package net.sf.ahtutils.xml.finance;

import javax.xml.bind.annotation.XmlRegistry;


/*
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.sf.ahtutils.xml.finance package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.sf.ahtutils.xml.finance
     * 
     */
    public ObjectFactory() {
    }

 
    public Currency createCurrency() {
        return new Currency();
    }


    public Figures createFigures() {
        return new Figures();
    }


    public Finance createFinance() {
        return new Finance();
    }


    public Time createTime() {
        return new Time();
    }


    public Counter createCounter() {
        return new Counter();
    }


    public Signatures createSignatures() {
        return new Signatures();
    }


    public Signature createSignature() {
        return new Signature();
    }

}
