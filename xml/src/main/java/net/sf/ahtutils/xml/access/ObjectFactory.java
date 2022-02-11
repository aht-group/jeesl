
package net.sf.ahtutils.xml.access;

import javax.xml.bind.annotation.XmlRegistry;


/*
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.sf.ahtutils.xml.access package. 
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


    public RoleAutoAssign createRoleAutoAssign() {
        return new RoleAutoAssign();
    }


    public Views createViews() {
        return new Views();
    }


    public View createView() {
        return new View();
    }


    public Actions createActions() {
        return new Actions();
    }


    public Action createAction() {
        return new Action();
    }


    public Role createRole() {
        return new Role();
    }


    public Usecases createUsecases() {
        return new Usecases();
    }


    public Usecase createUsecase() {
        return new Usecase();
    }


    public AclContainer createAclContainer() {
        return new AclContainer();
    }


    public RoleAutoAssign.Add createRoleAutoAssignAdd() {
        return new RoleAutoAssign.Add();
    }


    public RoleAutoAssign.Rm createRoleAutoAssignRm() {
        return new RoleAutoAssign.Rm();
    }


    public Access createAccess() {
        return new Access();
    }


    public Category createCategory() {
        return new Category();
    }


    public Groups createGroups() {
        return new Groups();
    }


    public Group createGroup() {
        return new Group();
    }


    public Roles createRoles() {
        return new Roles();
    }


    public AclQuery createAclQuery() {
        return new AclQuery();
    }

}
