package org.jeesl.web.mbean.prototype.system.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
import org.jeesl.jsf.util.TriStateBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated //Use JeeslSecurityViewController instead
public abstract class AbstractAdminSecurityViewBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
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
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminSecurityViewBean.class);

	private List<V> views; public List<V> getViews(){return views;}
	private final List<A> actions; public List<A> getActions(){return actions;}
	private List<AR> areas; public List<AR> getAreas(){return areas;}
	private List<R> roles; public List<R> getRoles(){return roles;}
	private List<U> usecases; public List<U> getUsecases(){return usecases;}
	
	private V view; public V getView(){return view;} public void setView(V view) {this.view = view;}
	private A action; public A getAction(){return action;} public void setAction(A action) {this.action = action;}
	private AR area; public AR getArea(){return area;} public void setArea(AR area) {this.area = area;}
	
	private JeeslSecurityBean<R,V,U,A,AR,CTX,M,USER> bSecurity;
	private final TriStateBinder tsb; public TriStateBinder getTsb() {return tsb;}
	
	private boolean userIsDeveloper; public boolean isUserIsDeveloper() {return userIsDeveloper;}
	
	public AbstractAdminSecurityViewBean(SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,AR,?,?,OT,OH,?,?,?,USER> fbSecurity)
	{
		super(fbSecurity);
		categoryType = JeeslSecurityCategory.Type.view;
		tsb = new TriStateBinder();
		actions = new ArrayList<>();
		
		userIsDeveloper = false;
	}
	
	public void initSuper(JeeslSecurityFacade<C,R,V,U,A,CTX,M,USER> fSecurity,
							JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
							JeeslSecurityBean<R,V,U,A,AR,CTX,M,USER> bSecurity)
	{
		super.postConstructSecurity(fSecurity,bTranslation,bMessage,bSecurity);
		this.bSecurity=bSecurity;
		templates = fSecurity.allOrderedPositionVisible(fbSecurity.getClassTemplate());
	}
	
	@Override public void categorySelected() throws JeeslNotFoundException
	{
		reloadViews();
		view=null;
		action=null;
	}
	@Override protected void categorySaved() throws JeeslNotFoundException
	{
		reloadViews();
	}
	
	protected boolean categoryRemoveable() throws JeeslNotFoundException
	{
		views = fSecurity.allForCategory(fbSecurity.getClassView(),fbSecurity.getClassCategory(),category.getCode());
		return views.isEmpty();
	}
	
	private void reloadViews() throws JeeslNotFoundException
	{
		views = fSecurity.allForCategory(fbSecurity.getClassView(),fbSecurity.getClassCategory(),category.getCode());
		logger.info(AbstractLogMessage.reloaded(fbSecurity.getClassView(), views));
	}
	
	private void reloadView()
	{
		view = fSecurity.load(fbSecurity.getClassView(),view);
		tsb.booleanToA(view.getRedirect());
		roles = view.getRoles();
		Collections.sort(roles,comparatorRole);
		
		usecases = view.getUsecases();
		if(debugOnInfo) {logger.info(AbstractLogMessage.reloaded(fbSecurity.getClassUsecase(),usecases,view));}
		Collections.sort(usecases,comparatorUsecase);
		
		reloadAreas();
	}
	
	private void reloadActions()
	{
		actions.clear();
		actions.addAll(fSecurity.allForParent(fbSecurity.getClassAction(), view));
		Collections.sort(actions, comparatorAction);
	}
	
	private void reset(boolean rView, boolean rAction, boolean rArea)
	{
		if(rView) {view=null;}
		if(rAction) {action=null;}
		if(rArea) {area=null;}
	}
	
	//Add
	public void addCategory() throws JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.createEntity(fbSecurity.getClassCategory()));
		category = efCategory.create(null,JeeslSecurityCategory.Type.view.toString());
		category.setName(efLang.createEmpty(localeCodes));
		category.setDescription(efDescription.createEmpty(localeCodes));
	}
	
	//VIEW
	public void addView() throws JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.createEntity(fbSecurity.getClassView()));
		view = efView.build(category,"",views);
		view.setName(efLang.createEmpty(localeCodes));
		view.setDescription(efDescription.createEmpty(localeCodes));
		tsb.booleanToA(view.getRedirect());
	}
	
	public void selectView()
	{
		reset(false,true,true);
		logger.info(AbstractLogMessage.selectEntity(view));
		view = fSecurity.load(fbSecurity.getClassView(), view);
		view = efLang.persistMissingLangs(fSecurity,localeCodes,view);
		view = efDescription.persistMissingLangs(fSecurity,localeCodes,view);
		reloadView();
		reloadActions();
	}
	
	public void selectAction()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(action));}
		action = efLang.persistMissingLangs(fSecurity,localeCodes,action);
		action = efDescription.persistMissingLangs(fSecurity,localeCodes,action);
	}
	
	public void saveView() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.saveEntity(view));
		view.setCategory(fSecurity.find(fbSecurity.getClassCategory(), view.getCategory()));
		view.setRedirect(tsb.aToBoolean());
		view = fSecurity.save(view);
		reloadView();
		reloadViews();
		bMessage.growlSaved(view);
		propagateChanges();
		bSecurity.update(view);
	}
	
	public void cloneView() throws JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.createEntity(fbSecurity.getClassView()));
		V clone = efView.clone(view);
		clone.setName(efLang.clone(view.getName()));
		clone.setDescription(efDescription.clone(view.getDescription()));
		reset(true,true,true);
		view = clone;
	}
	
	protected abstract void propagateChanges();
	
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
		reset(true,true,true);
		reloadViews();
		propagateChanges();
		
	}
	
	//ACTION
	public void addAction() throws JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.createEntity(fbSecurity.getClassAction()));
		action = efAction.build(view,"",actions);
		action.setName(efLang.createEmpty(localeCodes));
		action.setDescription(efDescription.createEmpty(localeCodes));
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
			action = efLang.persistMissingLangs(fSecurity,localeCodes,action);
			action = efDescription.persistMissingLangs(fSecurity,localeCodes,action);
		}
	}
	
	//AREA
	public void reloadAreas()
	{
		areas = fSecurity.allForParent(fbSecurity.getClassArea(), view);
	}
	
	public void addArea() throws JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.createEntity(fbSecurity.getClassArea()));
		area = fbSecurity.ejbArea().build(view,areas);
		area.setName(efLang.createEmpty(localeCodes));
		area.setDescription(efDescription.createEmpty(localeCodes));
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
		area = efLang.persistMissingLangs(fSecurity,localeCodes,area);
		area = efDescription.persistMissingLangs(fSecurity,localeCodes,area);
	}
	
	public void deleteArea() throws JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.deleteEntity(area));
		fSecurity.rm(area);
		bMessage.growlDeleted(area);
		reset(false,false,true);
		reloadAreas();
		propagateChanges();
		
	}
	
	public void reorderViews() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fSecurity,views);}
	public void reorderActions() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fSecurity,actions);}
	public void reorderAreas() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fSecurity, areas);}
}