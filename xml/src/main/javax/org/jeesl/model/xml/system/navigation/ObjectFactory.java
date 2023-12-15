
package org.jeesl.model.xml.system.navigation;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.jeesl.model.xml.system.navigation package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.jeesl.model.xml.system.navigation
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Navigation }
     * 
     */
    public Navigation createNavigation() {
        return new Navigation();
    }

    /**
     * Create an instance of {@link ViewPattern }
     * 
     */
    public ViewPattern createViewPattern() {
        return new ViewPattern();
    }

    /**
     * Create an instance of {@link UrlMapping }
     * 
     */
    public UrlMapping createUrlMapping() {
        return new UrlMapping();
    }

    /**
     * Create an instance of {@link Menu }
     * 
     */
    public Menu createMenu() {
        return new Menu();
    }

    /**
     * Create an instance of {@link MenuItem }
     * 
     */
    public MenuItem createMenuItem() {
        return new MenuItem();
    }

    /**
     * Create an instance of {@link Breadcrumb }
     * 
     */
    public Breadcrumb createBreadcrumb() {
        return new Breadcrumb();
    }

}
