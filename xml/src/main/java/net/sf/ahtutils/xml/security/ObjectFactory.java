
package net.sf.ahtutils.xml.security;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.sf.ahtutils.xml.security package. 
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


    public User createUser() {
        return new User();
    }

    public Staffs createStaffs() {
        return new Staffs();
    }


    public Role createRole() {
        return new Role();
    }


    public Category createCategory() {
        return new Category();
    }


    public Roles createRoles() {
        return new Roles();
    }


    public Actions createActions() {
        return new Actions();
    }


    public Action createAction() {
        return new Action();
    }

    public View createView() {
        return new View();
    }


    public Access createAccess() {
        return new Access();
    }


    public Template createTemplate() {
        return new Template();
    }


    public Templates createTemplates() {
        return new Templates();
    }


    public Usecases createUsecases() {
        return new Usecases();
    }


    public Usecase createUsecase() {
        return new Usecase();
    }


    public Views createViews() {
        return new Views();
    }

    public Tmp createTmp() {
        return new Tmp();
    }

    public Staff createStaff() {
        return new Staff();
    }


    public Security createSecurity() {
        return new Security();
    }

    public Password createPassword() {
        return new Password();
    }


    public Rule createRule() {
        return new Rule();
    }

}
