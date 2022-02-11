
package net.sf.ahtutils.xml.cloud.facebook;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.sf.ahtutils.xml.cloud.facebook package. 
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


    public App createApp() {
        return new App();
    }


    public SignedRequest createSignedRequest() {
        return new SignedRequest();
    }


    public User createUser() {
        return new User();
    }


    public Oauth createOauth() {
        return new Oauth();
    }


    public App.Redirect createAppRedirect() {
        return new App.Redirect();
    }


    public Token createToken() {
        return new Token();
    }

}
