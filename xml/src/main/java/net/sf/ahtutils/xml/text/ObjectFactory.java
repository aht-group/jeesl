
package net.sf.ahtutils.xml.text;

import javax.xml.bind.annotation.XmlRegistry;


/*
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



    public ObjectFactory() {
    }


    public Question createQuestion() {
        return new Question();
    }


    public Answer createAnswer() {
        return new Answer();
    }


    public Objective createObjective() {
        return new Objective();
    }


    public Solution createSolution() {
        return new Solution();
    }


    public Impact createImpact() {
        return new Impact();
    }


    public Hint createHint() {
        return new Hint();
    }


    public Title createTitle() {
        return new Title();
    }

}
