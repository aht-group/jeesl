package org.jeesl.web.mbean.prototype.system.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.exlp.util.io.StringUtil;
import org.jeesl.api.bean.JeeslSecurityBean;
import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusFixedCode;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.doc.JeeslSecurityOnlineHelp;
import org.jeesl.interfaces.model.system.security.doc.JeeslSecurityOnlineTutorial;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityArea;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated //Use JeeslSecurityRoleController
public class AbstractAdminSecurityRoleBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											C extends JeeslSecurityCategory<L,D>,
											R extends JeeslSecurityRole<L,D,C,V,U,A>,
											V extends JeeslSecurityView<L,D,C,R,U,A>,
											U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
											A extends JeeslSecurityAction<L,D,R,V,U,AT>,
											AT extends JeeslSecurityTemplate<L,D,C>,
											CTX extends JeeslSecurityContext<L,D>,
											M extends JeeslSecurityMenu<L,V,CTX,M>,
											AR extends JeeslSecurityArea<L,D,V>,
											OT extends JeeslSecurityOnlineTutorial<L,D,V>,
											OH extends JeeslSecurityOnlineHelp<V,?,?>,
											USER extends JeeslUser<R>>
			extends AbstractAdminSecurityBean<L,D,LOC,C,R,V,U,A,AT,CTX,M,AR,OT,OH,USER>
			implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminSecurityRoleBean.class);
			
	protected List<R> roles; public List<R> getRoles(){return roles;}
	private List<V> views; public List<V> getViews(){return views;}
	private List<A> actions; public List<A> getActions(){return actions;}
	private List<U> usecases; public List<U> getUsecases(){return usecases;}
	
	private R role; public R getRole(){return role;} public void setRole(R role) {this.role = role;}
	
	private boolean denyRemove; public boolean isDenyRemove(){return denyRemove;}
	private boolean userIsDeveloper; public boolean isUserIsDeveloper() {return userIsDeveloper;}
	
	public AbstractAdminSecurityRoleBean(SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,AR,OT,OH,?,?,?,USER> fbSecurity)
	{
		super(fbSecurity);
	}
	
	public void initSuper(JeeslSecurityFacade<C,R,V,U,A,CTX,M,USER> fSecurity,
						JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
						JeeslSecurityBean<R,V,U,A,AR,CTX,M,USER> bSecurity)
	{
		categoryType = JeeslSecurityCategory.Type.role;
		super.postConstructSecurity(fSecurity,bTranslation,bMessage,bSecurity);
		
		opViews = fSecurity.all(fbSecurity.getClassView());
		Collections.sort(opViews, comparatorView);
		
		opActions = new ArrayList<A>();
		opUsecases = fSecurity.all(fbSecurity.getClassUsecase());
		Collections.sort(opUsecases,comparatorUsecase);
		
		roles = new ArrayList<R>();
		
		userIsDeveloper = false;
	}
	
	public void addCategory() 
	{
		category = efCategory.create("", JeeslSecurityCategory.Type.role.toString());
		category.setName(efLang.createEmpty(localeCodes));
		category.setDescription(efDescription.createEmpty(localeCodes));
	}
	
	@Override public void categorySelected() throws JeeslNotFoundException
	{
		reloadRoles();
		role=null;
	}
	@Override protected void categorySaved() throws JeeslNotFoundException
	{
		reloadRoles();
	}
	
	private void reloadRoles() throws JeeslNotFoundException
	{
		roles.clear();
		logger.trace(StringUtil.stars());
		for(R r : fSecurity.allForCategory(fbSecurity.getClassRole(),fbSecurity.getClassCategory(),category.getCode()))
		{
			logger.trace("Role "+r.toString());
			if(r.isVisible() | uiShowInvisible){roles.add(r);}
		}
		Collections.sort(roles, comparatorRole);
		
		logger.info("Reloaded "+roles.size()+" (uiShowInvisible:"+uiShowInvisible+")");
	}
	
	private void reloadActions()
	{
		for(V v : role.getViews())
		{
			// TODO Use cahce!
			opActions.addAll(fSecurity.allForParent(fbSecurity.getClassAction(), v));
//			opActions.addAll(bSecurity.fActions(v));
		}
	}

	//Role
	public void addRole() throws JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.createEntity(fbSecurity.getClassRole()));
		role = fbSecurity.ejbRole().build(category,"");
		role.setName(efLang.createEmpty(localeCodes));
		role.setDescription(efDescription.createEmpty(localeCodes));
	}
	public void rmRole() throws JeeslConstraintViolationException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.deleteEntity(role));
		fSecurity.rm(role);
		role=null;
		reloadRoles();
		roleUpdatePerformed();
	}
	public void saveRole() throws JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.saveEntity(role));
		
		try
		{
			role.setCategory(fSecurity.find(fbSecurity.getClassCategory(), role.getCategory()));
			role = fSecurity.save(role);
			selectRole();
			reloadRoles();
			roleUpdatePerformed();
		}
		catch (JeeslConstraintViolationException e) {bMessage.constraintDuplicate(null);}
	}
	
	public void selectRole()
	{
		logger.trace(AbstractLogMessage.selectEntity(role));
		role = fSecurity.find(fbSecurity.getClassRole(),role);
		role = efLang.persistMissingLangs(fSecurity,localeCodes,role);
		role = efDescription.persistMissingLangs(fSecurity,localeCodes,role);		
		role = fSecurity.load(role);
		reloadActions();
		
		views = role.getViews();
		actions = role.getActions();
		usecases = role.getUsecases();
		
		Collections.sort(views,comparatorView);
		Collections.sort(actions,comparatorAction);
		Collections.sort(usecases,comparatorUsecase);
		
		tblView=null;
		tblAction=null;
		tblUsecase=null;
		
		denyRemove = false;
		if(role instanceof JeeslStatusFixedCode)
		{
			for(String fixed : ((JeeslStatusFixedCode)role).getFixedCodes())
			{
				if(fixed.equals(role.getCode())){denyRemove=true;}
			}
		}
	}
	
	protected void roleUpdatePerformed(){}
	
	//OverlayPanel
	public void opAddView() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(!role.getViews().contains(opView))
		{
			role.getViews().add(opView);
			role = fSecurity.save(role);
			opView = null;
			selectRole();
			bSecurity.update(role);
		}
	}
	public void opAddAction() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(!role.getActions().contains(opAction))
		{
			role.getActions().add(opAction);
			role = fSecurity.save(role);
			opAction = null;
			selectRole();
			bSecurity.update(role);
		}
	}
	public void opAddUsecase() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(!role.getUsecases().contains(opUsecase))
		{
			role.getUsecases().add(opUsecase);
			role = fSecurity.save(role);
			opUsecase = null;
			selectRole();
			bSecurity.update(role);
		}
	}
	
	//Overlay-Rm
	public void opRmView() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(tblView!=null && role.getViews().contains(tblView))
		{
			role.getViews().remove(tblView);
			role = fSecurity.save(role);
			tblView = null;
			selectRole();
			bSecurity.update(role);
		}
	}
	public void opRmAction() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(tblAction!=null && role.getActions().contains(tblAction))
		{
			role.getActions().remove(tblAction);
			role = fSecurity.save(role);
			tblAction = null;
			selectRole();
			bSecurity.update(role);
		}
	}
	public void opRmUsecase() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(tblUsecase!=null && role.getUsecases().contains(tblUsecase))
		{
			role.getUsecases().remove(tblUsecase);
			role = fSecurity.save(role);
			tblUsecase = null;
			selectRole();
			bSecurity.update(role);
		}
	}
	
	public void reorderRoles() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fSecurity, roles);}
}