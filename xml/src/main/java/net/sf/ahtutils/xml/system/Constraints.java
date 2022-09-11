
package net.sf.ahtutils.xml.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element ref="{http://ahtutils.aht-group.com/system}constraintScope" maxOccurs="unbounded"/&gt;
 *         &lt;element ref="{http://ahtutils.aht-group.com/system}constraintAttribute" maxOccurs="unbounded"/&gt;
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
    "constraintScope",
    "constraintAttribute"
})
@XmlRootElement(name = "constraints")
public class Constraints
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected List<ConstraintScope> constraintScope;
    @XmlElement(required = true)
    protected List<ConstraintAttribute> constraintAttribute;

    /**
     * Gets the value of the constraintScope property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the constraintScope property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConstraintScope().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConstraintScope }
     * 
     * 
     */
    public List<ConstraintScope> getConstraintScope() {
        if (constraintScope == null) {
            constraintScope = new ArrayList<ConstraintScope>();
        }
        return this.constraintScope;
    }

    public boolean isSetConstraintScope() {
        return ((this.constraintScope!= null)&&(!this.constraintScope.isEmpty()));
    }

    public void unsetConstraintScope() {
        this.constraintScope = null;
    }

    /**
     * Gets the value of the constraintAttribute property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the constraintAttribute property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConstraintAttribute().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConstraintAttribute }
     * 
     * 
     */
    public List<ConstraintAttribute> getConstraintAttribute() {
        if (constraintAttribute == null) {
            constraintAttribute = new ArrayList<ConstraintAttribute>();
        }
        return this.constraintAttribute;
    }

    public boolean isSetConstraintAttribute() {
        return ((this.constraintAttribute!= null)&&(!this.constraintAttribute.isEmpty()));
    }

    public void unsetConstraintAttribute() {
        this.constraintAttribute = null;
    }

}
