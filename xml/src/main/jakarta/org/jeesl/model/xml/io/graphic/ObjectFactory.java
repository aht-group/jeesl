
package org.jeesl.model.xml.io.graphic;

import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.jeesl.model.xml.io.graphic package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.jeesl.model.xml.io.graphic
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Graphic }
     * 
     */
    public Graphic createGraphic() {
        return new Graphic();
    }

    /**
     * Create an instance of {@link Symbol }
     * 
     */
    public Symbol createSymbol() {
        return new Symbol();
    }

    /**
     * Create an instance of {@link Colors }
     * 
     */
    public Colors createColors() {
        return new Colors();
    }

    /**
     * Create an instance of {@link Color }
     * 
     */
    public Color createColor() {
        return new Color();
    }

    /**
     * Create an instance of {@link Sizes }
     * 
     */
    public Sizes createSizes() {
        return new Sizes();
    }

    /**
     * Create an instance of {@link Size }
     * 
     */
    public Size createSize() {
        return new Size();
    }

    /**
     * Create an instance of {@link Figures }
     * 
     */
    public Figures createFigures() {
        return new Figures();
    }

    /**
     * Create an instance of {@link Figure }
     * 
     */
    public Figure createFigure() {
        return new Figure();
    }

}
