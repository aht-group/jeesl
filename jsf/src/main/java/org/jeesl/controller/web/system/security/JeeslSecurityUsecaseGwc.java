package org.jeesl.controller.web.system.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jeesl.api.bean.JeeslSecurityBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.controller.util.comparator.ejb.system.security.SecurityActionComparator;
import org.jeesl.controller.util.comparator.ejb.system.security.SecurityRoleComparator;
import org.jeesl.controller.util.comparator.ejb.system.security.SecurityUsecaseComparator;
import org.jeesl.controller.util.comparator.ejb.system.security.SecurityViewComparator;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.controller.web.util.AbstractLogMessage;
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
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityArea;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.interfaces.web.JeeslJsfSecurityHandler;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslSecurityUsecaseGwc <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											C extends JeeslSecurityCategory<L,D>,
											R extends JeeslSecurityRole<L,D,C,V,U,A>,
											V extends JeeslSecurityView<L,D,C,R,U,A>,
											U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
											A extends JeeslSecurityAction<L,D,R,V,U,AT>,
											AT extends JeeslSecurityTemplate<L,D,C>,
											CTX extends JeeslSecurityContext<L,D>,
											M extends JeeslSecurityMenu<L,V,CTX,M>,
											AR extends JeeslSecurityArea<L,D,V>,

											USER extends JeeslUser<R>>
									extends AbstractJeeslLocaleWebController<L,D,LOC>
									implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslSecurityUsecaseGwc.class);
	
	public enum Action{Developer}
	
	private JeeslSecurityFacade<C,R,V,U,A,CTX,M,USER> fSecurity;
	
	private JeeslSecurityBean<R,V,U,A,AR,CTX,M,USER> bSecurity;
	
	private final SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,AR,?,?,?,?,?,?,?,USER> fbSecurity;
	
	protected final EjbSecurityCategoryFactory<C> efCategory;
	protected final EjbSecurityUsecaseFactory<C,U> efUsecase;
	
	protected final Comparator<R> comparatorRole;
	protected final Comparator<V> comparatorView;
	protected final Comparator<U> comparatorUsecase;
	protected final Comparator<A> comparatorAction;
	
	protected List<V> opViews; public List<V> getOpViews(){return opViews;}
	protected List<A> opActions; public List<A> getOpActions(){return opActions;}
	
	private List<C> categories; public List<C> getCategories() {return categories;}
	private List<U> usecases; public List<U> getUsecases() {return usecases;}
	private List<R> roles; public List<R> getRoles(){return roles;}
	private List<V> views; public List<V> getViews(){return views;}
	private List<A> actions; public List<A> getActions(){return actions;}
	
	protected C category;public void setCategory(C category) {this.category = category;} public C getCategory() {return category;}
	private U usecase; public U getUsecase(){return usecase;} public void setUsecase(U usecase){this.usecase = usecase;}
	
	protected V opView;public V getOpView(){return opView;}public void setOpView(V opView){this.opView = opView;}
	protected V tblView;public V getTblView(){return tblView;}public void setTblView(V tblView){this.tblView = tblView;}
	
	protected A opAction;public A getOpAction(){return opAction;}public void setOpAction(A opAction){this.opAction = opAction;}
	protected A tblAction;public A getTblAction(){return tblAction;}public void setTblAction(A tblAction){this.tblAction = tblAction;}
	
	protected boolean uiShowDocumentation; public boolean isUiShowDocumentation() {return uiShowDocumentation;}
	protected boolean uiShowInvisible; public boolean isUiShowInvisible() {return uiShowInvisible;}
	
	
	private boolean userIsDeveloper; public boolean isUserIsDeveloper() {return userIsDeveloper;} public void setUserIsDeveloper(boolean userIsDeveloper) {this.userIsDeveloper = userIsDeveloper;}
	public boolean isHasDeveloperAction() {return userIsDeveloper;}
	
	public JeeslSecurityUsecaseGwc(SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,AR,?,?,?,?,?,?,?,USER> fbSecurity)
	{
		super(fbSecurity.getClassL(),fbSecurity.getClassD());
		this.fbSecurity=fbSecurity;
		
		efCategory = fbSecurity.ejbCategory();
		efUsecase = fbSecurity.ejbUsecase();
		
		comparatorRole = (new SecurityRoleComparator<C,R>()).factory(SecurityRoleComparator.Type.position);
		comparatorView = (new SecurityViewComparator<V>()).factory(SecurityViewComparator.Type.position);
		comparatorUsecase = (new SecurityUsecaseComparator<C,U>()).factory(SecurityUsecaseComparator.Type.position);
		comparatorAction = fbSecurity.comparatorAction(SecurityActionComparator.Type.position);
	}
	
	public void postConstruct(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
									JeeslSecurityFacade<C,R,V,U,A,CTX,M,USER> fSecurity,
									JeeslSecurityBean<R,V,U,A,AR,CTX,M,USER> bSecurity,
									JeeslJsfSecurityHandler<R,V,U,A,AR,USER> security)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fSecurity=fSecurity;
		this.bSecurity=bSecurity;
		
		opViews = fSecurity.all(fbSecurity.getClassView());
		Collections.sort(opViews,comparatorView);
		
		opActions = new ArrayList<A>();
		
		// Page Security Settings
		userIsDeveloper = security.allow(security.getPageCode()+".Developer") || security.allow(security.getPageCode()+".developer");
		uiShowDocumentation = userIsDeveloper;
		uiShowInvisible = userIsDeveloper;
		
		if(debugOnInfo) {logger.info("Page security for "+security.getPageCode()+" hasDeveloperAction:"+userIsDeveloper+" uiShowDocumentation:"+uiShowInvisible);}
		
		this.reloadCategories();
	}
	
	private void reset(boolean rCategory, boolean rUsecase)
	{
		if(rCategory) {category = null;}
		if(rUsecase) {usecase = null;}
	}
	
	protected void reloadCategories()
	{
		if(userIsDeveloper){categories = fSecurity.allOrderedPosition(fbSecurity.getClassCategory(),JeeslSecurityCategory.Type.usecase);}
		else{categories = fSecurity.allOrderedPositionVisible(fbSecurity.getClassCategory(),JeeslSecurityCategory.Type.usecase);}
		if(debugOnInfo){logger.info(AbstractLogMessage.reloaded(fbSecurity.getClassCategory(),categories)+" (developer:"+userIsDeveloper+")");}
	}
	
	public void selectCategory() throws JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.selectEntity(category));
		category = efLang.persistMissingLangs(fSecurity,lp,category);
		category = efDescription.persistMissingLangs(fSecurity,lp.getLocales(),category);
		reloadUsecases();
		usecase=null;
	}
	
	public void addCategory() throws JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.createEntity(fbSecurity.getClassCategory()));
		category = efCategory.create(null,JeeslSecurityCategory.Type.usecase.toString());
		category.setName(efLang.buildEmpty(lp.getLocales()));
		category.setDescription(efDescription.buildEmpty(lp.getLocales()));
	}
	
	public void saveCategory() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(category));
		category = fSecurity.save(category);
		reloadCategories();
		reloadUsecases();
	}
	
	public void deleteCategory() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.deleteEntity(category));
		fSecurity.rm(category);
		this.reset(true, true);
		this.reloadCategories();
	}
	
	private void reloadUsecases() throws JeeslNotFoundException
	{
		usecases = fSecurity.allForCategory(fbSecurity.getClassUsecase(),fbSecurity.getClassCategory(),category.getCode());
		logger.info(AbstractLogMessage.reloaded(fbSecurity.getClassUsecase(), usecases));
	}
	
	public void selectUsecase()
	{
		logger.info(AbstractLogMessage.selectEntity(usecase));
		reloadUsecase();
		usecase = efLang.persistMissingLangs(fSecurity,lp,usecase);
		usecase = efDescription.persistMissingLangs(fSecurity,lp.getLocales(),usecase);
		
		reloadActions();
	}
	
	public void addUsecase() throws JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.createEntity(fbSecurity.getClassUsecase()));
		usecase = efUsecase.build(category,"");
		usecase.setName(efLang.buildEmpty(lp.getLocales()));
		usecase.setDescription(efDescription.buildEmpty(lp.getLocales()));
	}
	
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
	
	public void saveUsecase() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.saveEntity(usecase));
		usecase.setCategory(fSecurity.find(fbSecurity.getClassCategory(), usecase.getCategory()));
		usecase = fSecurity.save(usecase);
		this.reloadUsecases();
		this.reloadUsecase();
		bSecurity.update(usecase);
	}
	
	public void deleteUsecase() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.deleteEntity(usecase));
		fSecurity.rm(usecase);
		this.reset(false,true);
		this.reloadUsecases();
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


	//Save

	
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