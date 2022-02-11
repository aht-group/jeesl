
package net.sf.ahtutils.xml.access;

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
 *         &lt;element ref="{http://ahtutils.aht-group.com/access}roleAutoAssign" maxOccurs="unbounded"/&gt;
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
    "roleAutoAssign"
})
@XmlRootElement(name = "aclContainer")
public class AclContainer
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected List<RoleAutoAssign> roleAutoAssign;

  
    public List<RoleAutoAssign> getRoleAutoAssign() {
        if (roleAutoAssign == null) {
            roleAutoAssign = new ArrayList<RoleAutoAssign>();
        }
        return this.roleAutoAssign;
    }

    public boolean isSetRoleAutoAssign() {
        return ((this.roleAutoAssign!= null)&&(!this.roleAutoAssign.isEmpty()));
    }

    public void unsetRoleAutoAssign() {
        this.roleAutoAssign = null;
    }

}
