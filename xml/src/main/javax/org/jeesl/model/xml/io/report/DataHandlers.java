
package org.jeesl.model.xml.io.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/*
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/report}dataHandler" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "dataHandler"
})
@XmlRootElement(name = "dataHandlers")
public class DataHandlers
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected List<DataHandler> dataHandler;


    public List<DataHandler> getDataHandler() {
        if (dataHandler == null) {
            dataHandler = new ArrayList<DataHandler>();
        }
        return this.dataHandler;
    }

    public boolean isSetDataHandler() {
        return ((this.dataHandler!= null)&&(!this.dataHandler.isEmpty()));
    }

    public void unsetDataHandler() {
        this.dataHandler = null;
    }

}
