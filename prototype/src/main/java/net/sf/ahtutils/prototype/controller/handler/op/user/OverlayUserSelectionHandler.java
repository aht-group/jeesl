package net.sf.ahtutils.prototype.controller.handler.op.user;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.interfaces.bean.op.OpUserBean;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;

import net.sf.ahtutils.interfaces.controller.handler.op.user.OpUserSelectionHandler;

public class OverlayUserSelectionHandler <USER extends JeeslUser<?>> implements OpUserSelectionHandler<USER>
{
	public static final long serialVersionUID=1;

    private OpUserBean<USER> bean;

    private USER user;
    public USER getUser() {return user;}
    public void setUser(USER user) {this.user = user;}

    public OverlayUserSelectionHandler(OpUserBean<USER> bean)
    {
        this.bean=bean;
        uiUsers = new ArrayList<USER>();
    }

    public void selectListener() throws JeeslLockingException, JeeslConstraintViolationException
    {
        bean.selectOpUser(user);
        user = null;
    }

    //UI
    private List<USER> uiUsers;
    public List<USER> getUiUsers() {return uiUsers;}
    public void setUiUsers(List<USER> uiUsers) {this.uiUsers = uiUsers;}

    private USER uiUser;
    public USER getUiUser(){return uiUser;}
    public void setUiUser(USER uiUser){this.uiUser = uiUser;}

    public void clearUi()
    {
        uiUsers.clear();
        uiUser = null;
    }

    public void addUiUser(USER item)
    {
        if(!uiUsers.contains(item)) {uiUsers.add(item);}
        uiUser=null;
    }

    public void removeUiUser()
    {
        if(uiUsers.contains(uiUser)) {uiUsers.remove(uiUser);}
        uiUser=null;
    }

    public void selectUiUser()
    {

    }
}