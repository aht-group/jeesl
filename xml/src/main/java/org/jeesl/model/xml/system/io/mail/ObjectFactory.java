
package org.jeesl.model.xml.system.io.mail;

import javax.xml.bind.annotation.XmlRegistry;


/*
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.jeesl.model.xml.system.io.mail package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.jeesl.model.xml.system.io.mail
     * 
     */
    public ObjectFactory() {
    }


    public Mails createMails() {
        return new Mails();
    }


    public Mail createMail() {
        return new Mail();
    }


    public Header createHeader() {
        return new Header();
    }


    public From createFrom() {
        return new From();
    }


    public EmailAddress createEmailAddress() {
        return new EmailAddress();
    }


    public To createTo() {
        return new To();
    }


    public Cc createCc() {
        return new Cc();
    }


    public Bcc createBcc() {
        return new Bcc();
    }


    public Template createTemplate() {
        return new Template();
    }


    public Text createText() {
        return new Text();
    }


    public Html createHtml() {
        return new Html();
    }


    public Attachment createAttachment() {
        return new Attachment();
    }

    /*
     * Create an instance of {@link Image }
     * 
     */
    public Image createImage() {
        return new Image();
    }

    /*
     * Create an instance of {@link Tracker }
     * 
     */
    public Tracker createTracker() {
        return new Tracker();
    }

    /*
     * Create an instance of {@link Link }
     * 
     */
    public Link createLink() {
        return new Link();
    }

}
