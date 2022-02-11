
package net.sf.ahtutils.xml.qa;

import javax.xml.bind.annotation.XmlRegistry;


/*
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.sf.ahtutils.xml.qa package. 
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


    public Test createTest() {
        return new Test();
    }

    public Reference createReference() {
        return new Reference();
    }


    public Description createDescription() {
        return new Description();
    }


    public PreCondition createPreCondition() {
        return new PreCondition();
    }


    public Steps createSteps() {
        return new Steps();
    }


    public Expected createExpected() {
        return new Expected();
    }


    public Results createResults() {
        return new Results();
    }


    public Result createResult() {
        return new Result();
    }


    public Comment createComment() {
        return new Comment();
    }


    public Actual createActual() {
        return new Actual();
    }


    public Info createInfo() {
        return new Info();
    }

 
    public Groups createGroups() {
        return new Groups();
    }


    public Group createGroup() {
        return new Group();
    }


    public Qa createQa() {
        return new Qa();
    }


    public Category createCategory() {
        return new Category();
    }


    public Checklist createChecklist() {
        return new Checklist();
    }

}
