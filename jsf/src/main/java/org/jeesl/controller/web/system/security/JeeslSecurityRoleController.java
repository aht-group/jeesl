package org.jeesl.controller.web.system.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jeesl.api.bean.JeeslSecurityBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.controller.web.AbstractJeeslWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.factory.ejb.system.security.EjbSecurityCategoryFactory;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusFixedCode;
import org.jeesl.interfaces.model.system.security.doc.JeeslSecurityOnlineHelp;
import org.jeesl.interfaces.model.system.security.doc.JeeslSecurityOnlineTutorial;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityArea;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.web.JeeslJsfSecurityHandler;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.util.comparator.ejb.system.security.SecurityActionComparator;
import org.jeesl.util.comparator.ejb.system.security.SecurityUsecaseComparator;
import org.jeesl.util.comparator.ejb.system.security.SecurityViewComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class JeeslSecurityRoleController  <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										C extends JeeslSecurityCategory<L,D>,
										R extends JeeslSecurityRole<L,D,C,V,U,A,USER>,
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
									extends AbstractJeeslWebController<L,D,LOC>
									implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslSecurityRoleController.class);
	
	private final SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,AR,OT,OH,?,?,USER> fbSecurity;
	private JeeslSecurityFacade<L,D,C,R,V,U,A,AT,CTX,M,USER> fSecurity;
	private JeeslSecurityBean<L,D,C,R,V,U,A,AT,AR,CTX,M,USER> bSecurity;
	
	protected final EjbSecurityCategoryFactory<C> efCategory;
	
	protected final Comparator<V> comparatorView;
	protected final Comparator<U> comparatorUsecase;
	protected final Comparator<A> comparatorAction;
	
	private List<V> opViews; public List<V> getOpViews(){return opViews;}
	private List<A> opActions; public List<A> getOpActions(){return opActions;}
	protected List<U> opUsecases; public List<U> getOpUsecases(){return opUsecases;}
	
	private List<C> categories; public List<C> getCategories() {return categories;}
	private final List<R> roles; public List<R> getRoles(){return roles;}
	private List<V> views; public List<V> getViews(){return views;}
	private List<A> actions; public List<A> getActions(){return actions;}
	private List<U> usecases; public List<U> getUsecases(){return usecases;}
	
	protected C category;public void setCategory(C category) {this.category = category;} public C getCategory() {return category;}
	private R role; public R getRole(){return role;} public void setRole(R role) {this.role = role;}
	
	protected V tblView; public V getTblView() {return tblView;} public void setTblView(V tblView) {this.tblView = tblView;}
	protected A tblAction; public A getTblAction() {return tblAction;} public void setTblAction(A tblAction) {this.tblAction = tblAction;}
	protected U tblUsecase; public U getTblUsecase() {return tblUsecase;} public void setTblUsecase(U tblUsecase) {this.tblUsecase = tblUsecase;}
	
	protected V opView; public V getOpView() {return opView;} public void setOpView(V opView) {this.opView = opView;}
	protected A opAction; public A getOpAction() {return opAction;} public void setOpAction(A opAction) {this.opAction = opAction;}
	protected U opUsecase; public U getOpUsecase() {return opUsecase;} public void setOpUsecase(U opUsecase) {this.opUsecase = opUsecase;}
	
	private boolean uiShowDocumentation; public boolean isUiShowDocumentation() {return uiShowDocumentation;}
	private boolean hasDeveloperAction; public boolean isHasDeveloperAction() {return hasDeveloperAction;}
	private boolean denyRemove; public boolean isDenyRemove(){return denyRemove;}
	private boolean uiShowInvisible; public boolean isUiShowInvisible() {return uiShowInvisible;}
	private boolean uiAllowCode; public boolean isUiAllowCode() {return uiAllowCode;}
	
	public JeeslSecurityRoleController(SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,AR,OT,OH,?,?,USER> fbSecurity)
	{
		super(fbSecurity.getClassL(),fbSecurity.getClassD());
		this.fbSecurity=fbSecurity;
		
		efCategory = fbSecurity.ejbCategory();
		
		comparatorView = (new SecurityViewComparator<V>()).factory(SecurityViewComparator.Type.position);
		comparatorAction = fbSecurity.comparatorAction(SecurityActionComparator.Type.position);
		comparatorUsecase = (new SecurityUsecaseComparator<L,D,C,R,V,U,A,AT,USER>()).factory(SecurityUsecaseComparator.Type.position);
		
		roles = new ArrayList<>();
		
		uiShowDocumentation = false;
		hasDeveloperAction = false;
		uiShowInvisible = true;
		uiAllowCode = true;
	}
	
	public void postConstructRole(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
			JeeslSecurityFacade<L,D,C,R,V,U,A,AT,CTX,M,USER> fSecurity,
			JeeslSecurityBean<L,D,C,R,V,U,A,AT,AR,CTX,M,USER> bSecurity,
			JeeslJsfSecurityHandler<R,V,U,A,AR,USER> security)
	{
		super.postConstructWebController(lp,bMessage);
		this.fSecurity=fSecurity;
		this.bSecurity=bSecurity;
		
		opViews = fSecurity.all(fbSecurity.getClassView());
		Collections.sort(opViews, comparatorView);
		
		opActions = new ArrayList<A>();
		opUsecases = fSecurity.all(fbSecurity.getClassUsecase());
		Collections.sort(opUsecases,comparatorUsecase);
		
		this.reloadCategories();
	}

	protected void reloadCategories()
	{
		categories = fSecurity.allOrderedPosition(fbSecurity.getClassCategory(),JeeslSecurityCategory.Type.role);
		
		logger.info(AbstractLogMessage.reloaded(fbSecurity.getClassCategory(),categories));
	}
	
	public void addCategory() 
	{
		logger.info(AbstractLogMessage.createEntity(fbSecurity.getClassCategory()));
		category = efCategory.create(null,JeeslSecurityCategory.Type.role.toString());
		category.setName(efLang.buildEmpty(lp.getLocales()));
		category.setDescription(efDescription.buildEmpty(lp.getLocales()));
	}

	public void selectCategory() throws JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.selectEntity(category));
		category = efLang.persistMissingLangs(fSecurity,lp.getLocales(),category);
		category = efDescription.persistMissingLangs(fSecurity,lp.getLocales(),category);
		reloadRoles();
		role=null;
	}
	
	private void reloadRoles() throws JeeslNotFoundException
	{
		roles.clear();
		roles.addAll(fSecurity.allForCategory(fbSecurity.getClassRole(),fbSecurity.getClassCategory(),category.getCode()));
		logger.info(AbstractLogMessage.reloaded(fbSecurity.getClassRole(), roles));
	}

	public void selectRole()
	{
		logger.trace(AbstractLogMessage.selectEntity(role));
		role = fSecurity.find(fbSecurity.getClassRole(),role);
		role = efLang.persistMissingLangs(fSecurity,lp.getLocales(),role);
		role = efDescription.persistMissingLangs(fSecurity,lp.getLocales(),role);		
		role = fSecurity.load(role,false);
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
	
	private void reloadActions()
	{
		for(V v : role.getViews())
		{
			opActions.addAll(fSecurity.allForParent(fbSecurity.getClassAction(), v));
			// TODO use Cache
//			opActions.addAll(bSecurity.fActions(v));
		}
	}

	//Role
	public void addRole() throws JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.createEntity(fbSecurity.getClassRole()));
		role = fbSecurity.ejbRole().build(category,"");
		role.setName(efLang.buildEmpty(lp.getLocales()));
		role.setDescription(efDescription.buildEmpty(lp.getLocales()));
	}
	public void rmRole() throws JeeslConstraintViolationException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.rmEntity(role));
		fSecurity.rm(role);
		role=null;
		reloadRoles();
//		roleUpdatePerformed();
	}
	public void saveRole() throws JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.saveEntity(role));
		
		try
		{
			role.setCategory(fSecurity.find(fbSecurity.getClassCategory(), role.getCategory()));
			role = fSecurity.save(role);
			this.selectRole();
			this.reloadRoles();
//			roleUpdatePerformed();
		}
		catch (JeeslConstraintViolationException e) {bMessage.errorConstraintViolationDuplicateObject();}
	}

//	protected void roleUpdatePerformed(){}
//	
//	//OverlayPanel
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

//	//Overlay-Rm
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
	
	public void selectTblAction() {logger.info(AbstractLogMessage.selectEntity(tblAction));}
	public void selectTblView() {logger.info(AbstractLogMessage.selectEntity(tblView));}
	public void selectTblUsecase() {logger.info(AbstractLogMessage.selectEntity(tblUsecase));}
	
	public void reorderRoles() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fSecurity, roles);}
}