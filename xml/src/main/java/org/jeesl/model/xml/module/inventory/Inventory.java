
package org.jeesl.model.xml.module.inventory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.jeesl.model.xml.module.inventory.pc.Computer;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.jeesl.org/inventory/pc}computer" maxOccurs="unbounded"/&gt;
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
    "computer"
})
@XmlRootElement(name = "inventory")
public class Inventory
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(namespace = "http://www.jeesl.org/inventory/pc", required = true)
    protected List<Computer> computer;

    /**
     * Gets the value of the computer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the computer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComputer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Computer }
     * 
     * 
     */
    public List<Computer> getComputer() {
        if (computer == null) {
            computer = new ArrayList<Computer>();
        }
        return this.computer;
    }

    public boolean isSetComputer() {
        return ((this.computer!= null)&&(!this.computer.isEmpty()));
    }

    public void unsetComputer() {
        this.computer = null;
    }

}
