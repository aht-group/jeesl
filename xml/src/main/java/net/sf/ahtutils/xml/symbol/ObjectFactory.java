
package net.sf.ahtutils.xml.symbol;

import javax.xml.bind.annotation.XmlRegistry;


/*
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.sf.ahtutils.xml.symbol package. 
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


    public Graphic createGraphic() {
        return new Graphic();
    }


    public Symbol createSymbol() {
        return new Symbol();
    }


    public Colors createColors() {
        return new Colors();
    }


    public Color createColor() {
        return new Color();
    }


    public Sizes createSizes() {
        return new Sizes();
    }


    public Size createSize() {
        return new Size();
    }


    public Figures createFigures() {
        return new Figures();
    }

    public Figure createFigure() {
        return new Figure();
    }

}
