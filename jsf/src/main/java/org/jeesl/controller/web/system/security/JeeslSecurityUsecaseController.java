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
import org.jeesl.factory.ejb.system.security.EjbSecurityUsecaseFactory;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
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
import org.jeesl.util.comparator.ejb.system.security.SecurityRoleComparator;
import org.jeesl.util.comparator.ejb.system.security.SecurityUsecaseComparator;
import org.jeesl.util.comparator.ejb.system.security.SecurityViewComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class JeeslSecurityUsecaseController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
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
	final static Logger logger = LoggerFactory.getLogger(JeeslSecurityUsecaseController.class);
	
	public enum Action{Developer}
	
	private JeeslSecurityFacade<L,D,C,R,V,U,A,AT,CTX,M,USER> fSecurity;
	
	private JeeslSecurityBean<L,D,C,R,V,U,A,AT,AR,CTX,M,USER> bSecurity;
	
	private final SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,AR,OT,OH,?,?,USER> fbSecurity;
	
//	private JeeslJsfSecurityHandler<R,V,U,A,AR,USER> security;
	
	protected final EjbSecurityCategoryFactory<C> efCategory;
	protected final EjbSecurityUsecaseFactory<C,U> efUsecase;
	
	protected final Comparator<R> comparatorRole;
	protected final Comparator<V> comparatorView;
	protected final Comparator<U> comparatorUsecase;
	protected final Comparator<A> comparatorAction;
	
	protected List<V> opViews; public List<V> getOpViews(){return opViews;}
	protected List<A> opActions; public List<A> getOpActions(){return opActions;}
	
	protected List<C> categories; public List<C> getCategories() {return categories;}
	private List<U> usecases; public List<U> getUsecases() {return usecases;}
	private List<R> roles; public List<R> getRoles(){return roles;}
	private List<V> views; public List<V> getViews(){return views;}
	private List<A> actions; public List<A> getActions(){return actions;}
	
	protected C category;public void setCategory(C category) {this.category = category;}public C getCategory() {return category;}
	private U usecase; public U getUsecase(){return usecase;} public void setUsecase(U usecase){this.usecase = usecase;}
	
	protected V opView;public V getOpView(){return opView;}public void setOpView(V opView){this.opView = opView;}
	protected V tblView;public V getTblView(){return tblView;}public void setTblView(V tblView){this.tblView = tblView;}
	
	protected A opAction;public A getOpAction(){return opAction;}public void setOpAction(A opAction){this.opAction = opAction;}
	protected A tblAction;public A getTblAction(){return tblAction;}public void setTblAction(A tblAction){this.tblAction = tblAction;}
	
	protected boolean uiShowDocumentation; public boolean isUiShowDocumentation() {return uiShowDocumentation;}
	protected boolean uiShowInvisible; public boolean isUiShowInvisible() {return uiShowInvisible;}
	protected boolean hasDeveloperAction; public boolean isHasDeveloperAction() {return hasDeveloperAction;}
	
	public JeeslSecurityUsecaseController(SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,AR,OT,OH,?,?,USER> fbSecurity)
	{
		super(fbSecurity.getClassL(),fbSecurity.getClassD());
		this.fbSecurity=fbSecurity;
		
		efCategory = fbSecurity.ejbCategory();
		efUsecase = fbSecurity.ejbUsecase();
		
		comparatorRole = (new SecurityRoleComparator<C,R>()).factory(SecurityRoleComparator.Type.position);
		comparatorView = (new SecurityViewComparator<V>()).factory(SecurityViewComparator.Type.position);
		comparatorUsecase = (new SecurityUsecaseComparator<L,D,C,R,V,U,A,AT,USER>()).factory(SecurityUsecaseComparator.Type.position);
		comparatorAction = fbSecurity.comparatorAction(SecurityActionComparator.Type.position);
	}
	
	public void postConstructUsecase(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
									JeeslSecurityFacade<L,D,C,R,V,U,A,AT,CTX,M,USER> fSecurity,
									JeeslSecurityBean<L,D,C,R,V,U,A,AT,AR,CTX,M,USER> bSecurity,
									JeeslJsfSecurityHandler<R,V,U,A,AR,USER> security)
	{
		super.postConstructWebController(lp,bMessage);
		this.fSecurity=fSecurity;
		this.bSecurity=bSecurity;
//		this.security=security;
		
		opViews = fSecurity.all(fbSecurity.getClassView());
		Collections.sort(opViews,comparatorView);
		
		opActions = new ArrayList<A>();
		
		// Page Security Settings
		hasDeveloperAction = security.allow(security.getPageCode()+".Developer");
		uiShowDocumentation = hasDeveloperAction;
		uiShowInvisible = hasDeveloperAction;
		if(debugOnInfo) {logger.info("Page security for "+security.getPageCode()+" hasDeveloperAction:"+hasDeveloperAction+" uiShowDocumentation:"+uiShowInvisible);}
		
		reloadCategories();
	}
	
	protected void reloadCategories()
	{
		if(hasDeveloperAction){categories = fSecurity.allOrderedPosition(fbSecurity.getClassCategory(),JeeslSecurityCategory.Type.usecase);}
		else{categories = fSecurity.allOrderedPositionVisible(fbSecurity.getClassCategory(),JeeslSecurityCategory.Type.usecase);}
		if(debugOnInfo){logger.info(AbstractLogMessage.reloaded(fbSecurity.getClassCategory(),categories));}
	}
	
	public void selectCategory() throws JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.selectEntity(category));
		category = efLang.persistMissingLangs(fSecurity,lp.getLocales(),category);
		category = efDescription.persistMissingLangs(fSecurity,lp.getLocales(),category);
		reloadUsecases();
		usecase=null;
	}
	public void saveCategory() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(category));
		category = fSecurity.save(category);
		reloadCategories();
		reloadUsecases();
	}
	
	public void selectUsecase()
	{
		logger.info(AbstractLogMessage.selectEntity(usecase));
		reloadUsecase();
		usecase = efLang.persistMissingLangs(fSecurity,lp.getLocales(),usecase);
		usecase = efDescription.persistMissingLangs(fSecurity,lp.getLocales(),usecase);
		
		reloadActions();
	}
	
	//Reload
	private void reloadUsecase()
	{
		usecase = fSecurity.load(fbSecurity.getClassUsecase(),usecase);
		roles = usecase.getRoles();
		views = usecase.getViews();
		actions = usecase.getActions();
		
		Collections.sort(views,comparatorView);
		Collections.sort(actions,comparatorAction);
		Collections.sort(roles,comparatorRole);
	}
	
	private void reloadUsecases() throws JeeslNotFoundException
	{
		logger.info("reloadUsecases");
		usecases = fSecurity.allForCategory(fbSecurity.getClassUsecase(),fbSecurity.getClassCategory(),category.getCode());
	}
	private void reloadActions()
	{
		opActions.clear();
		for(V v : usecase.getViews())
		{
			// TODO Use cahce!
			opActions.addAll(fSecurity.allForParent(fbSecurity.getClassAction(), v));
//			opActions.addAll(bSecurity.fActions(v));
		}
		Collections.sort(opActions, comparatorAction);
	}
	
	//Add
	public void addCategory() throws JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.createEntity(fbSecurity.getClassCategory()));
		category = efCategory.create(null,JeeslSecurityCategory.Type.usecase.toString());
		category.setName(efLang.buildEmpty(lp.getLocales()));
		category.setDescription(efDescription.buildEmpty(lp.getLocales()));
	}
	public void addUsecase() throws JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.createEntity(fbSecurity.getClassUsecase()));
		usecase = efUsecase.build(category,"");
		usecase.setName(efLang.buildEmpty(lp.getLocales()));
		usecase.setDescription(efDescription.buildEmpty(lp.getLocales()));
	}

	//Save
	public void saveUsecase() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.saveEntity(usecase));
		usecase.setCategory(fSecurity.find(fbSecurity.getClassCategory(), usecase.getCategory()));
		usecase = fSecurity.save(usecase);
		reloadUsecase();
		reloadUsecases();
		bSecurity.update(usecase);
	}
	
	//OP View
	public void selectTblView() {logger.info(AbstractLogMessage.selectEntity(tblView));}
	public void opAddView() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(!usecase.getViews().contains(opView))
		{
			usecase.getViews().add(opView);
			usecase = fSecurity.save(usecase);
			opView = null;
			selectUsecase();
			bSecurity.update(usecase);
		}
	}
	public void opRmView() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(tblView!=null && usecase.getViews().contains(tblView))
		{
			usecase.getViews().remove(tblView);
			usecase = fSecurity.save(usecase);
			tblView = null;
			selectUsecase();
			bSecurity.update(usecase);
		}
	}
	
	//OP-Action
	public void selectTblAction() {logger.info(AbstractLogMessage.selectEntity(tblAction));}
	public void opAddAction() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(!usecase.getActions().contains(opAction))
		{
			usecase.getActions().add(opAction);
			usecase = fSecurity.save(usecase);
			opAction = null;
			selectUsecase();
			bSecurity.update(usecase);
		}
	}
	public void opRmAction() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(tblAction!=null && usecase.getActions().contains(tblAction))
		{
			usecase.getActions().remove(tblAction);
			usecase = fSecurity.save(usecase);
			tblAction = null;
			selectUsecase();
			bSecurity.update(usecase);
		}
	}
	
	public void reorderCategories() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fSecurity, categories);}
	public void reorderUsecases() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fSecurity, usecases);}
}