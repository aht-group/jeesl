package org.jeesl.web.mbean.prototype.system.security;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import org.jeesl.api.bean.JeeslSecurityBean;
import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.controller.util.comparator.ejb.system.security.SecurityActionComparator;
import org.jeesl.controller.util.comparator.ejb.system.security.SecurityRoleComparator;
import org.jeesl.controller.util.comparator.ejb.system.security.SecurityUsecaseComparator;
import org.jeesl.controller.util.comparator.ejb.system.security.SecurityViewComparator;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.factory.ejb.system.security.EjbSecurityActionFactory;
import org.jeesl.factory.ejb.system.security.EjbSecurityActionTemplateFactory;
import org.jeesl.factory.ejb.system.security.EjbSecurityCategoryFactory;
import org.jeesl.factory.ejb.system.security.EjbSecurityUsecaseFactory;
import org.jeesl.factory.ejb.system.security.EjbSecurityViewFactory;
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
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class AbstractAdminSecurityBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
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
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminSecurityBean.class);
	
	private enum Fm {category}
	
	protected JeeslSecurityFacade<C,R,V,U,A,M,USER> fSecurity;
	protected JeeslSecurityCategory.Type categoryType;
	
	protected JeeslSecurityBean<R,V,U,A,AR,CTX,M,USER> bSecurity;
	
	protected final SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,AR,OT,OH,?,?,USER> fbSecurity;
	
	protected final EjbSecurityCategoryFactory<C> efCategory;
	protected final EjbSecurityViewFactory<C,V> efView;
	protected final EjbSecurityUsecaseFactory<C,U> efUsecase;
	protected final EjbSecurityActionFactory<V,A> efAction;
	protected final EjbSecurityActionTemplateFactory<C,AT> efTemplate;

	protected final Comparator<R> comparatorRole;
	protected final Comparator<V> comparatorView;
	protected final Comparator<U> comparatorUsecase;
	protected final Comparator<A> comparatorAction;
		
	//Category
	protected List<C> categories; public List<C> getCategories() {return categories;}
	protected List<V> opViews; public List<V> getOpViews(){return opViews;}
	protected List<A> opActions; public List<A> getOpActions(){return opActions;}
	protected List<U> opUsecases; public List<U> getOpUsecases(){return opUsecases;}
	protected List<AT> templates; public List<AT> getTemplates(){return templates;}
	
	private List<V> opFvActions; public List<V> getOpFvActions(){return opFvActions;} public void setOpFvActions(List<V> opFvActions){this.opFvActions = opFvActions;}
	private List<V> opFvViews;public List<V> getOpFvViews(){return opFvViews;}public void setOpFvViews(List<V> opFvViews){this.opFvViews = opFvViews;}
	private List<U> opFvUsecases;public List<U> getOpFvUsecases(){return opFvUsecases;}public void setOpFvUsecases(List<U> opFvUsecases){this.opFvUsecases = opFvUsecases;}
	
	protected C category;public void setCategory(C category) {this.category = category;}public C getCategory() {return category;}
	
	protected V opView; public V getOpView(){return opView;}public void setOpView(V opView){this.opView = opView;}
	protected V tblView; public V getTblView(){return tblView;}public void setTblView(V tblView){this.tblView = tblView;}
	protected A opAction; public A getOpAction(){return opAction;}public void setOpAction(A opAction){this.opAction = opAction;}
	protected A tblAction; public A getTblAction(){return tblAction;}public void setTblAction(A tblAction){this.tblAction = tblAction;}
	protected U opUsecase; public U getOpUsecase(){return opUsecase;}public void setOpUsecase(U opUsecase){this.opUsecase = opUsecase;}
	protected U tblUsecase; public U getTblUsecase(){return tblUsecase;}public void setTblUsecase(U tblUsecase){this.tblUsecase = tblUsecase;}
	
	public AbstractAdminSecurityBean(SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,AR,OT,OH,?,?,USER> fbSecurity)
	{
		super(fbSecurity.getClassL(),fbSecurity.getClassD());
		this.fbSecurity=fbSecurity;

		efCategory = fbSecurity.ejbCategory();
		efView = fbSecurity.ejbView();
		efUsecase = fbSecurity.ejbUsecase();
		efAction = fbSecurity.ejbAction();
		efTemplate = fbSecurity.ejbTemplate();
		
		comparatorRole = (new SecurityRoleComparator<C,R>()).factory(SecurityRoleComparator.Type.position);
		comparatorView = (new SecurityViewComparator<V>()).factory(SecurityViewComparator.Type.position);
		comparatorUsecase = (new SecurityUsecaseComparator<L,D,C,R,V,U,A,AT,USER>()).factory(SecurityUsecaseComparator.Type.position);
		comparatorAction = fbSecurity.comparatorAction(SecurityActionComparator.Type.position);
	}
	
	public void postConstructSecurity(JeeslSecurityFacade<C,R,V,U,A,M,USER> fSecurity,
									JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
									JeeslSecurityBean<R,V,U,A,AR,CTX,M,USER> bSecurity)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fSecurity=fSecurity;
		this.bSecurity=bSecurity;
		reloadCategories();
	}
	
	public void selectTblAction() {logger.info(AbstractLogMessage.selectEntity(tblAction));}
	public void selectTblView() {logger.info(AbstractLogMessage.selectEntity(tblView));}
	public void selectTblUsecase() {logger.info(AbstractLogMessage.selectEntity(tblUsecase));}
	
	public void selectCategory() throws JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.selectEntity(category));
		category = efLang.persistMissingLangs(fSecurity,localeCodes,category);
		category = efDescription.persistMissingLangs(fSecurity,localeCodes,category);
		categorySelected();
	}
	protected void categorySelected() throws JeeslNotFoundException {}
	
	public void saveCategory() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(category));}
		category = fSecurity.save(category);
		reloadCategories();
		categorySaved();
		bMessage.growlSaved(category);
	}
	protected void categorySaved()  throws JeeslNotFoundException {}
	
	public void rmCategory() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.deleteEntity(category));}
		if(categoryRemoveable())
		{
			fSecurity.rm(category);
			bMessage.growlDeleted(category);
			category = null;
			reloadCategories();
			
		}
		else
		{
			
			logger.warn(fbSecurity.getClassCategory().getSimpleName()+" not removeable. It may be in use or feature not activted (@Override)");
			bMessage.errorConstraintViolationInUse(Fm.category);
		}
	}
	protected boolean categoryRemoveable() throws JeeslNotFoundException {return false;}
	
	protected void reloadCategories()
	{
		
		if(categoryType!=null)
		{
			if(uiShowInvisible){categories = fSecurity.allOrderedPosition(fbSecurity.getClassCategory(),categoryType);}
			else{categories = fSecurity.allOrderedPositionVisible(fbSecurity.getClassCategory(),categoryType);}
			if(debugOnInfo){logger.info(AbstractLogMessage.reloaded(fbSecurity.getClassCategory(),categories));}
		}
	}

	public void reorderCategories() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info("updateOrder "+categories.size());
		int i=1;
		for(C c : categories)
		{
			c.setPosition(i);
			fSecurity.update(c);
			i++;
		}
	}
}