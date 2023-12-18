
package net.sf.ahtutils.xml.text;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.sf.ahtutils.xml.text package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.sf.ahtutils.xml.text
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Question }
     * 
     */
    public Question createQuestion() {
        return new Question();
    }

    /**
     * Create an instance of {@link Answer }
     * 
     */
    public Answer createAnswer() {
        return new Answer();
    }

    /**
     * Create an instance of {@link Objective }
     * 
     */
    public Objective createObjective() {
        return new Objective();
    }

    /**
     * Create an instance of {@link Solution }
     * 
     */
    public Solution createSolution() {
        return new Solution();
    }

    /**
     * Create an instance of {@link Impact }
     * 
     */
    public Impact createImpact() {
        return new Impact();
    }

    /**
     * Create an instance of {@link Hint }
     * 
     */
    public Hint createHint() {
        return new Hint();
    }

    /**
     * Create an instance of {@link Title }
     * 
     */
    public Title createTitle() {
        return new Title();
    }

}
