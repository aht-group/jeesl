package org.jeesl.web.mbean.prototype.system.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.core.JeeslUserFacade;
import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.controller.monitoring.counter.ProcessingTimeTracker;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.factory.ejb.system.security.EjbSecurityUserFactory;
import org.jeesl.factory.txt.system.security.TxtUserFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.user.pwd.JeeslWithPwd;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.controller.audit.UtilsRevisionPageFlow;
import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public abstract class AbstractAdminSecurityUserBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											C extends JeeslSecurityCategory<L,D>,
											R extends JeeslSecurityRole<L,D,C,V,U,A,USER>,
											V extends JeeslSecurityView<L,D,C,R,U,A>,
											U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
											A extends JeeslSecurityAction<L,D,R,V,U,AT>,
											AT extends JeeslSecurityTemplate<L,D,C>,
											CTX extends JeeslSecurityContext<L,D>,
											M extends JeeslSecurityMenu<L,V,CTX,M>,
											USER extends JeeslUser<R>>
		extends AbstractAdminBean<L,D,LOC>
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminSecurityUserBean.class);

	protected JeeslUserFacade<USER> fUtilsUser;
	protected JeeslSecurityFacade<C,R,V,U,A,USER> fUtilsSecurity;
	private final SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,?,?,?,?,?,USER> fbSecurity;
	
	protected final List<CTX> contexts; public List<CTX> getContexts() {return contexts;}
	private final List<Boolean> booleans; public List<Boolean> getBooleans() {return booleans;}
	protected List<USER> users;public List<USER> getUsers() {return users;}
	protected List<R> roles; public List<R> getRoles() {return roles;}
	
	protected USER user; public USER getUser(){return user;} public void setUser(USER user){this.user = user;}
	
	protected Map<Long,Boolean> mapRoles; public Map<Long, Boolean> getMapRoles() {return mapRoles;}
	protected final EjbSecurityUserFactory<USER> efUser;
	
	protected boolean performPasswordCheck;
	protected String pwd1; public String getPwd1() {return pwd1;} public void setPwd1(String pwd1) {this.pwd1 = pwd1;}
	protected String pwd2;public String getPwd2() {return pwd2;}public void setPwd2(String pwd2){this.pwd2 = pwd2;}
	
	protected UtilsRevisionPageFlow<USER,USER> revision; public UtilsRevisionPageFlow<USER, USER> getRevision() {return revision;}
	private boolean useSaltedHash; public boolean isUseSaltedHash() {return useSaltedHash;} public void setUseSaltedHash(boolean useSaltedHash) {this.useSaltedHash = useSaltedHash;}
	

	public AbstractAdminSecurityUserBean(SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,?,?,?,?,?,USER> fbSecurity)
	{
		super(fbSecurity.getClassL(),fbSecurity.getClassD());
		this.fbSecurity=fbSecurity;
		efUser = fbSecurity.ejbUser();
		useSaltedHash = false;
		booleans = new ArrayList<>();
		booleans.add(Boolean.TRUE);
		booleans.add(Boolean.FALSE);
		
		contexts = new ArrayList<>();
	}
	
	public void postConstructSecurityUser(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
							JeeslUserFacade<USER> fUtilsUser, JeeslSecurityFacade<C,R,V,U,A,USER> fUtilsSecurity)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fUtilsUser=fUtilsUser;
		this.fUtilsSecurity=fUtilsSecurity;
		
		mapRoles = new Hashtable<Long,Boolean>();
		roles = new ArrayList<R>();
		
		performPasswordCheck = true;
		reloadContexts();
	}
	
	protected void reloadContexts() {}
	
	public void cancelUser() {reset(true);}
	protected void reset(boolean rUser)
	{
		if(rUser) {user=null;}
	}
	
	protected void reloadUsers()
	{
		ProcessingTimeTracker ptt = ProcessingTimeTracker.instance().start();
		logger.info(ptt.debugEvent("Reloading ..."));
		users = fUtilsUser.all(fbSecurity.getClassUser());
		logger.info(AbstractLogMessage.reloaded(fbSecurity.getClassUser(),users)+" "+ptt.debugEvent(""));
	}

	public void addUser() throws JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbSecurity.getClassUser()));}
		user = efUser.build();
		if(revision!=null){revision.pageFlowPrimaryAdd();}
		postAdd();
	}
	protected abstract void postAdd() throws JeeslNotFoundException;
	
	public void selectUser() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(user));}
		if(revision!=null){revision.pageFlowPrimarySelect(user);}
		if(user.getSalt()==null || user.getSalt().trim().length()==0)
		{
			user.setSalt(TxtUserFactory.buildSalt());
			user = fUtilsUser.save(user);
		}
		reloadUser();
	}
	
	protected void reloadUser()
	{		
		user = fUtilsUser.load(user);
		mapRoles.clear();
		if(debugOnInfo){logger.info("Settings roles: "+user.getRoles().size());}
		for(R r : user.getRoles()){mapRoles.put(r.getId(), true);}
		postReload();
	}
	protected abstract void postReload();
	
	public void saveUser() throws JeeslLockingException
	{
		try
		{
			if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(user));}
			boolean changedPassword = checkPwd();
			if(debugOnInfo){logger.info("Changed Password: "+changedPassword);}
			preSave();
			user = fUtilsUser.saveTransaction(user);
			postSave(changedPassword);
			reloadUser();
			bMessage.growlSuccessSaved();
			if(revision!=null){revision.pageFlowPrimarySave(user);}
			userChangePerformed();
		}
		catch (JeeslConstraintViolationException e) {constraintViolationOnSave();}
	}
	protected void preSave() {}
	protected void postSave(boolean changedPassword) {}
	
	public void rm(USER myUser){this.user=myUser;deleteUser();}
	public void deleteUser()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.deleteEntity(user));}
		try
		{
			fUtilsUser.rm(user);
			user = null;
			bMessage.growlSuccessRemoved();
			if(revision!=null){revision.pageFlowPrimaryCancel();}
			userChangePerformed();
		}
		catch (JeeslConstraintViolationException e){constraintViolationOnRemove();}
	}
	
	protected void userChangePerformed() {}
	
	protected boolean checkPwd()
	{
		if(performPasswordCheck && JeeslWithPwd.class.isAssignableFrom(fbSecurity.getClassUser()))
		{
			if(debugOnInfo) {logger.info("Checking PWD");}
			if(pwd1.length()!=pwd2.length())
			{
				bMessage.growlError("fmPwdDidNotMatch");
				return false;
			}
	
			if(pwd1.length()>0 && pwd2.length()>0)
			{
				if(pwd1.equals(pwd2))
				{
					bMessage.growlSuccess("fmPwdChanged");
					
					JeeslWithPwd ejb = (JeeslWithPwd)user;
					if(useSaltedHash) {ejb.setPwd(TxtUserFactory.toHash(pwd1,user.getSalt()));}
					else {ejb.setPwd(pwd1);}
					return true;
				}
				else
				{
					bMessage.growlError("fmPwdDidNotMatch");
					return false;
				}
			}
		}
		else {logger.warn("Password Checking and updating deactivated");}
		return false;
	}
	
	protected void constraintViolationOnSave() {logger.warn("constraintViolationOnSave, this should be @Overriden");}
	protected void constraintViolationOnRemove() {}
	protected void passwordsDoNotMatch() {}
	
	public void grantRole(R role, boolean grant) throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info("Grant ("+grant+") "+role.toString());}
		fUtilsSecurity.grantRole(fbSecurity.getClassUser(),fbSecurity.getClassRole(),user,role,grant);
		reloadUser();
	}
}