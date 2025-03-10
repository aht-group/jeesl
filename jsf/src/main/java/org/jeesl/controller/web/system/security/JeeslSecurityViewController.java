package org.jeesl.controller.web.system.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.jeesl.api.bean.JeeslSecurityBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.controller.util.comparator.ejb.system.security.SecurityActionComparator;
import org.jeesl.controller.util.comparator.ejb.system.security.SecurityRoleComparator;
import org.jeesl.controller.util.comparator.ejb.system.security.SecurityUsecaseComparator;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.factory.ejb.system.security.EjbSecurityActionFactory;
import org.jeesl.factory.ejb.system.security.EjbSecurityCategoryFactory;
import org.jeesl.factory.ejb.system.security.EjbSecurityViewFactory;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.controller.web.system.security.JeeslSecurityViewCallback;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
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
import org.jeesl.interfaces.web.JeeslJsfSecurityHandler;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.jsf.util.TriStateBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslSecurityViewController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
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
		extends AbstractJeeslLocaleWebController<L,D,LOC>
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslSecurityViewController.class);

	private enum SecuritySuffix {developer}
	private enum SecuritySuffixDeprecated {Developer}
	
	private final SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,AR,?,?,OT,OH,?,?,?,USER> fbSecurity;
	private JeeslSecurityFacade<C,R,V,U,A,CTX,M,USER> fSecurity;
	
	private final JeeslSecurityViewCallback callback;
	
	private final EjbSecurityCategoryFactory<C> efCategory;
	protected final EjbSecurityViewFactory<C,V> efView;
	protected final EjbSecurityActionFactory<V,A> efAction;
	
	protected final Comparator<R> comparatorRole;
	protected final Comparator<U> comparatorUsecase;
	protected final Comparator<A> comparatorAction;
	
	private List<C> categories; public List<C> getCategories() {return categories;}
	private List<V> views; public List<V> getViews(){return views;}
	private final List<A> actions; public List<A> getActions(){return actions;}
	private List<AR> areas; public List<AR> getAreas(){return areas;}
	private List<R> roles; public List<R> getRoles(){return roles;}
	private List<U> usecases; public List<U> getUsecases(){return usecases;}
	private List<AT> templates; public List<AT> getTemplates(){return templates;}
	
	private C category;public void setCategory(C category) {this.category = category;} public C getCategory() {return category;}
	private V view; public V getView(){return view;} public void setView(V view) {this.view = view;}
	private A action; public A getAction(){return action;} public void setAction(A action) {this.action = action;}
	private AR area; public AR getArea(){return area;} public void setArea(AR area) {this.area = area;}
	
	private JeeslSecurityBean<R,V,U,A,AR,CTX,M,USER> bSecurity;
	private final TriStateBinder tsb; public TriStateBinder getTsb() {return tsb;}
	
	private boolean userIsDeveloper; public boolean isUserIsDeveloper() {return userIsDeveloper;} public void setUserIsDeveloper(boolean userIsDeveloper) {this.userIsDeveloper = userIsDeveloper;}
	
	public JeeslSecurityViewController(JeeslSecurityViewCallback callback, SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,AR,?,?,OT,OH,?,?,?,USER> fbSecurity)
	{
		super(fbSecurity.getClassL(),fbSecurity.getClassD());
		this.callback = callback;
		this.fbSecurity = fbSecurity;
		
		efCategory = fbSecurity.ejbCategory();
		efView = fbSecurity.ejbView();
		efAction = fbSecurity.ejbAction();
		
		comparatorRole = (new SecurityRoleComparator<C,R>()).factory(SecurityRoleComparator.Type.position);
		comparatorUsecase = (new SecurityUsecaseComparator<C,U>()).factory(SecurityUsecaseComparator.Type.position);
		comparatorAction = fbSecurity.comparatorAction(SecurityActionComparator.Type.position);
		
		tsb = new TriStateBinder();
		actions = new ArrayList<>();
		
		userIsDeveloper = false;
	}
	
	public void postConstruct(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
			JeeslSecurityFacade<C,R,V,U,A,CTX,M,USER> fSecurity,
			JeeslSecurityBean<R,V,U,A,AR,CTX,M,USER> bSecurity,
			JeeslJsfSecurityHandler<R,V,U,A,AR,USER> security)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fSecurity=fSecurity;
		this.bSecurity=bSecurity;
		templates = fSecurity.allOrderedPositionVisible(fbSecurity.getClassTemplate());
		
		this.reloadCategories();
		if(Objects.nonNull(security)) {this.postConstruct(security);}	
	}
	
	private void postConstruct(JeeslJsfSecurityHandler<R,V,U,A,AR,USER> security)
	{
		userIsDeveloper = userIsDeveloper || security.allowSuffixCode(AbstractJeeslLocaleWebController.SecurityActionSuffix.developer) || security.allowSuffixCode(AbstractJeeslLocaleWebController.SecurityActionSuffixDeprecated.Developer);
	}
	
	private void reset(boolean rCategory, boolean rView, boolean rAction, boolean rArea)
	{
		if(rCategory) {category=null;}
		if(rView) {view=null;}
		if(rAction) {action=null;}
		if(rArea) {area=null;}
	}
	
	private void reloadCategories()
	{
		categories = fSecurity.allOrderedPosition(fbSecurity.getClassCategory(),JeeslSecurityCategory.Type.view);
		logger.info(AbstractLogMessage.reloaded(fbSecurity.getClassCategory(),categories));
	}
	public void addCategory() throws JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.createEntity(fbSecurity.getClassCategory()));
		category = efCategory.create(null,JeeslSecurityCategory.Type.view.toString());
		category.setName(efLang.buildEmpty(lp.getLocales()));
		category.setDescription(efDescription.buildEmpty(lp.getLocales()));
	}
	public void selectCategory() throws JeeslNotFoundException
	{
		this.reset(false, true, true, true);
		logger.info(AbstractLogMessage.selectEntity(category));
		category = efLang.persistMissingLangs(fSecurity,lp,category);
		category = efDescription.persistMissingLangs(fSecurity,lp.getLocales(),category);
		this.reloadViews();
	}
	public void saveCategory() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(category));
		category = fSecurity.save(category);
		this.reloadCategories();
		this.reloadViews();
	}
	public void deleteCategory() throws JeeslConstraintViolationException
	{
		if(views.isEmpty())
		{
			fSecurity.rm(category);
			this.reset(true, true, true, true);
			this.reloadCategories();
		}
	}
	
	private void reloadViews() throws JeeslNotFoundException
	{
		views = fSecurity.allForParent(fbSecurity.getClassView(),category);
		logger.info(AbstractLogMessage.reloaded(fbSecurity.getClassView(), views));
	}
	
	private void reloadActions()
	{
		actions.clear();
		actions.addAll(fSecurity.allForParent(fbSecurity.getClassAction(), view));
		Collections.sort(actions, comparatorAction);
	}

	//VIEW
	public void addView() throws JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.createEntity(fbSecurity.getClassView()));
		view = efView.build(category,"",views);
		view.setName(efLang.buildEmpty(lp.getLocales()));
		view.setDescription(efDescription.buildEmpty(lp.getLocales()));
		tsb.booleanToA(view.getRedirect());
	}
	
	public void selectView()
	{
		this.reset(false,false,true,true);
		logger.info(AbstractLogMessage.selectEntity(view));
		view = fSecurity.load(fbSecurity.getClassView(), view);
		view = efLang.persistMissingLangs(fSecurity,lp,view);
		view = efDescription.persistMissingLangs(fSecurity,lp,view);
		
		this.reloadView();
		this.reloadActions();
	}
	
	private void reloadView()
	{
		if(debugOnInfo) {logger.trace("reloadView()");}
		view = fSecurity.load(fbSecurity.getClassView(),view);
		tsb.booleanToA(view.getRedirect());
		roles = view.getRoles();
		Collections.sort(roles,comparatorRole);
		
		usecases = view.getUsecases();
		Collections.sort(usecases,comparatorUsecase);
		if(debugOnInfo) {logger.info(AbstractLogMessage.reloaded(fbSecurity.getClassUsecase(),usecases,view));}
		
		this.reloadAreas();
	}
	
	public void saveView() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.saveEntity(view));
		
		if(Objects.isNull(fSecurity)) {logger.warn("fSecurity is null");}
		if(Objects.isNull(view)) {logger.warn("View is null");}
		else if(Objects.isNull(view.getCategory())) {logger.warn("view.category is null");}
		
		if(Objects.isNull(fbSecurity)) {logger.warn("fbSecurity is null");}
		else if(Objects.isNull(fbSecurity.getClassCategory())) {logger.warn("fbSecurity.getClassCategory is null");}
		
		view.setCategory(fSecurity.find(fbSecurity.getClassCategory(), view.getCategory()));
		view.setRedirect(tsb.aToBoolean());
		view = fSecurity.save(view);
		this.reloadView();
		this.reloadViews();
		if(Objects.nonNull(bMessage)) {bMessage.growlSaved(view);}
		bSecurity.update(view);
		callback.propagateChanges();
	}
	
	public void cloneView() throws JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.createEntity(fbSecurity.getClassView()));
		V clone = efView.clone(view);
		clone.setName(efLang.clone(view.getName()));
		clone.setDescription(efDescription.clone(view.getDescription()));
		reset(false,true,true,true);
		view = clone;
	}
	
	protected void propagateChanges()
	{
		logger.warn("NYI: propagateChanges");
	}
	
	public void deleteView() throws JeeslConstraintViolationException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.deleteEntity(view));

		List<M> menus = fSecurity.allForParent(fbSecurity.getClassMenu(), JeeslSecurityMenu.Attributes.view.toString(),view,-1);
		for(M m : menus)
		{
			List<M> childs = fSecurity.allForParent(fbSecurity.getClassMenu(), m);
			if(!childs.isEmpty())
			{
				bMessage.constraintInUse(null);
				return;
			}
			else
			{
				fSecurity.rm(m);
			}
		}
		
		fSecurity.rm(view);
		bMessage.growlDeleted(view);
		reset(false,true,true,true);
		reloadViews();
		propagateChanges();	
	}
	
	//ACTION
	public void selectAction()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(action));}
		action = efLang.persistMissingLangs(fSecurity,lp,action);
		action = efDescription.persistMissingLangs(fSecurity,lp.getLocales(),action);
	}
	public void addAction() throws JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.createEntity(fbSecurity.getClassAction()));
		action = efAction.build(view,"",actions);
		action.setName(efLang.buildEmpty(lp.getLocales()));
		action.setDescription(efDescription.buildEmpty(lp.getLocales()));
	}
	
	public void saveAction() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(action));
		List<L> langs = new ArrayList<L>();
		List<D> descriptions = new ArrayList<D>();
		if(action.getTemplate()!=null)
		{
			action.setTemplate(fSecurity.find(fbSecurity.getClassTemplate(), action.getTemplate()));
			logger.info("Testing ... "+action.toString());
			
			if(action.getName()!=null){langs.addAll(action.getName().values());}
			if(action.getDescription()!=null){descriptions.addAll(action.getDescription().values());}
			
			action.setName(null);
			action.setDescription(null);
		}
		action = fSecurity.save(action);
		if(!langs.isEmpty()){fSecurity.rm(langs);}
		if(!descriptions.isEmpty()){fSecurity.rm(descriptions);}
		reloadView();
		reloadActions();
		propagateChanges();
		bMessage.growlSaved(action);
	}
	
	public void rmAction() throws JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.deleteEntity(action));
		fSecurity.rm(action);
		bMessage.growlDeleted(action);
		action=null;
		reloadView();
		reloadActions();
		propagateChanges();
		
	}
	
	public void changeTemplate()
	{
		logger.info(AbstractLogMessage.selectOneMenuChange(action.getTemplate()));
		if(action.getTemplate()!=null)
		{
			action.setTemplate(fSecurity.find(fbSecurity.getClassTemplate(), action.getTemplate()));
			action.setCode(UUID.randomUUID().toString());
		}
		else
		{
			action = efLang.persistMissingLangs(fSecurity,lp,action);
			action = efDescription.persistMissingLangs(fSecurity,lp.getLocales(),action);
		}
	}
	
	//AREA
	public void reloadAreas()
	{
		if(debugOnInfo) {logger.info("reloadAreas() for "+fbSecurity.getClassArea().getSimpleName()+" and view:"+view.toString());}
		areas = fSecurity.allForParent(fbSecurity.getClassArea(),view);
		if(debugOnInfo) {logger.info(AbstractLogMessage.reloaded(fbSecurity.getClassArea(),areas));}
	}
	
	public void addArea() throws JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.createEntity(fbSecurity.getClassArea()));
		area = fbSecurity.ejbArea().build(view,areas);
		area.setName(efLang.buildEmpty(lp.getLocales()));
		area.setDescription(efDescription.buildEmpty(lp.getLocales()));
	}
	
	public void saveArea() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(area));
		area = fSecurity.save(area);
		reloadAreas();
		propagateChanges();
		bMessage.growlSaved(area);
	}
	
	public void selectArea() throws JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.createEntity(fbSecurity.getClassArea()));
		area = efLang.persistMissingLangs(fSecurity,lp,area);
		area = efDescription.persistMissingLangs(fSecurity,lp.getLocales(),area);
	}
	
	public void deleteArea() throws JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.deleteEntity(area));
		fSecurity.rm(area);
		bMessage.growlDeleted(area);
		reset(false,false,false,true);
		reloadAreas();
		propagateChanges();
		
	}
	
	public void reorderCategories() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fSecurity,categories);}
//	{
//		logger.info("updateOrder "+categories.size());
//		int i=1;
//		for(C c : categories)
//		{
//			c.setPosition(i);
//			fSecurity.update(c);
//			i++;
//		}
//	}
	public void reorderViews() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fSecurity,views);}
	public void reorderActions() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fSecurity,actions);}
	public void reorderAreas() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fSecurity, areas);}
}