
package net.sf.ahtutils.xml.access;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *         &lt;element name="add"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element ref="{http://ahtutils.aht-group.com/access}group" maxOccurs="unbounded"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="rm"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element ref="{http://ahtutils.aht-group.com/access}group" maxOccurs="unbounded"/&gt;
 *                 &lt;/sequence&gt;
 *                 &lt;attribute name="immediate" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="code" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "add",
    "rm"
})
@XmlRootElement(name = "roleAutoAssign")
public class RoleAutoAssign
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(namespace = "", required = true)
    protected RoleAutoAssign.Add add;
    @XmlElement(namespace = "", required = true)
    protected RoleAutoAssign.Rm rm;
    @XmlAttribute(name = "code")
    protected String code;


    public RoleAutoAssign.Add getAdd() {
        return add;
    }

    /*
     * Sets the value of the add property.
     * 
     * @param value
     *     allowed object is
     *     {@link RoleAutoAssign.Add }
     *     
     */
    public void setAdd(RoleAutoAssign.Add value) {
        this.add = value;
    }

    public boolean isSetAdd() {
        return (this.add!= null);
    }

    /*
     * Gets the value of the rm property.
     * 
     * @return
     *     possible object is
     *     {@link RoleAutoAssign.Rm }
     *     
     */
    public RoleAutoAssign.Rm getRm() {
        return rm;
    }

    /*
     * Sets the value of the rm property.
     * 
     * @param value
     *     allowed object is
     *     {@link RoleAutoAssign.Rm }
     *     
     */
    public void setRm(RoleAutoAssign.Rm value) {
        this.rm = value;
    }

    public boolean isSetRm() {
        return (this.rm!= null);
    }

    /*
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCode() {
        return code;
    }

    /*
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCode(String value) {
        this.code = value;
    }

    public boolean isSetCode() {
        return (this.code!= null);
    }


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
     *         &lt;element ref="{http://ahtutils.aht-group.com/access}group" maxOccurs="unbounded"/&gt;
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
        "group"
    })
    public static class Add
        implements Serializable
    {

        private final static long serialVersionUID = 1L;
        @XmlElement(required = true)
        protected List<Group> group;

     
        public List<Group> getGroup() {
            if (group == null) {
                group = new ArrayList<Group>();
            }
            return this.group;
        }

        public boolean isSetGroup() {
            return ((this.group!= null)&&(!this.group.isEmpty()));
        }

        public void unsetGroup() {
            this.group = null;
        }

    }


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
     *         &lt;element ref="{http://ahtutils.aht-group.com/access}group" maxOccurs="unbounded"/&gt;
     *       &lt;/sequence&gt;
     *       &lt;attribute name="immediate" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "group"
    })
    public static class Rm
        implements Serializable
    {

        private final static long serialVersionUID = 1L;
        @XmlElement(required = true)
        protected List<Group> group;
        @XmlAttribute(name = "immediate")
        protected Boolean immediate;


        public List<Group> getGroup() {
            if (group == null) {
                group = new ArrayList<Group>();
            }
            return this.group;
        }

        public boolean isSetGroup() {
            return ((this.group!= null)&&(!this.group.isEmpty()));
        }

        public void unsetGroup() {
            this.group = null;
        }

        /*
         * Gets the value of the immediate property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public boolean isImmediate() {
            return immediate;
        }

        /*
         * Sets the value of the immediate property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setImmediate(boolean value) {
            this.immediate = value;
        }

        public boolean isSetImmediate() {
            return (this.immediate!= null);
        }

        public void unsetImmediate() {
            this.immediate = null;
        }

    }

}
