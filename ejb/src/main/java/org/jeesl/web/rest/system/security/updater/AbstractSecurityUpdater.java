package org.jeesl.web.rest.system.security.updater;

import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.controller.db.updater.JeeslDbCodeEjbUpdater;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.factory.ejb.system.status.EjbDescriptionFactory;
import org.jeesl.factory.ejb.system.status.EjbLangFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
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
import org.jeesl.interfaces.model.system.security.with.JeeslSecurityWithActions;
import org.jeesl.interfaces.model.system.security.with.JeeslSecurityWithViews;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.access.Access;
import net.sf.ahtutils.xml.access.Category;
import net.sf.ahtutils.xml.access.View;
import net.sf.ahtutils.xml.access.Views;
import net.sf.ahtutils.xml.security.Action;
import net.sf.ahtutils.xml.security.Actions;
import net.sf.ahtutils.xml.security.Security;

public class AbstractSecurityUpdater <L extends JeeslLang,
 								D extends JeeslDescription, 
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
{
	final static Logger logger = LoggerFactory.getLogger(AbstractSecurityUpdater.class);
	
	protected final SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,AR,OT,OH,?,?,USER> fbSecurity;
	
	protected JeeslSecurityFacade<L,D,C,R,V,U,A,AT,CTX,M,USER> fSecurity;
	protected EjbLangFactory<L> efLang;
	protected EjbDescriptionFactory<D> efDescription;
	
	private final JeeslDbCodeEjbUpdater<C> dbCleanerCategory;
				
	public AbstractSecurityUpdater(SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,AR,OT,OH,?,?,USER> fbSecurity,
			JeeslSecurityFacade<L,D,C,R,V,U,A,AT,CTX,M,USER> fAcl)
	{
		this.fbSecurity=fbSecurity;

        this.fSecurity=fAcl;
		
		efLang = EjbLangFactory.instance(fbSecurity.getClassL());
		efDescription = EjbDescriptionFactory.factory(fbSecurity.getClassD());
		
		dbCleanerCategory = JeeslDbCodeEjbUpdater.createFactory(fbSecurity.getClassCategory());
	}
	
	@Deprecated protected void iuCategoryAccess(Access access, JeeslSecurityCategory.Type type) throws UtilsConfigurationException
	{
		logger.info("i/u "+type+" with "+access.getCategory().size()+" categories");
		
		JeeslDbCodeEjbUpdater<C> updateCategory = JeeslDbCodeEjbUpdater.createFactory(fbSecurity.getClassCategory());
		updateCategory.dbEjbs(fSecurity.allForType(fbSecurity.getClassCategory(),type.toString()));

		for(Category category : access.getCategory())
		{
			updateCategory.handled(category.getCode());
			C ejbCategory;
			try
			{
				ejbCategory = fSecurity.fByTypeCode(fbSecurity.getClassCategory(),type.toString(),category.getCode());
				efLang.rmLang(fSecurity,ejbCategory);
				efDescription.rmDescription(fSecurity,ejbCategory);
			}
			catch (JeeslNotFoundException e)
			{
				try
				{
					ejbCategory = fbSecurity.getClassCategory().newInstance();
					ejbCategory.setType(type.toString());
					ejbCategory.setCode(category.getCode());
					logger.trace("Persisting "+ejbCategory.toString());
					ejbCategory = (C)fSecurity.persist(ejbCategory);
				}
				catch (InstantiationException e2) {throw new UtilsConfigurationException(e2.getMessage());}
				catch (IllegalAccessException e2) {throw new UtilsConfigurationException(e2.getMessage());}
				catch (JeeslConstraintViolationException e2) {throw new UtilsConfigurationException(e2.getMessage());}	
			}
			
			try
			{
				ejbCategory.setName(efLang.getLangMap(category.getLangs()));
				ejbCategory.setDescription(efDescription.create(category.getDescriptions()));
				
				if(category.isSetVisible()){ejbCategory.setVisible(category.isVisible());}else{ejbCategory.setVisible(true);}
				if(category.isSetPosition()){ejbCategory.setPosition(category.getPosition());}else{ejbCategory.setPosition(0);}
				
				ejbCategory=fSecurity.update(ejbCategory);
				logger.trace("Proceeding with childs");
				iuChilds(ejbCategory,category);
			}
			catch (JeeslConstraintViolationException e) {logger.error("",e);}
			catch (JeeslLockingException e) {logger.error("",e);}
		}
		
		updateCategory.remove(fSecurity);
		logger.trace("initUpdateUsecaseCategories finished");
	}
	
	protected void iuCategory(Security security, JeeslSecurityCategory.Type type) throws UtilsConfigurationException
	{
		logger.info("i/u "+Security.class.getSimpleName()+"."+type+" with "+security.getCategory().size()+" categories");
		
		dbCleanerCategory.clear();dbCleanerCategory.dbEjbs(fSecurity.allForType(fbSecurity.getClassCategory(),type.toString()));

		for(net.sf.ahtutils.xml.security.Category xCategory : security.getCategory())
		{
			dbCleanerCategory.handled(xCategory.getCode());
			
			C eCategory;
			try
			{
				eCategory = fSecurity.fByTypeCode(fbSecurity.getClassCategory(),type.toString(),xCategory.getCode());
				efLang.rmLang(fSecurity,eCategory);
				efDescription.rmDescription(fSecurity,eCategory);
			}
			catch (JeeslNotFoundException e)
			{
				try
				{
					eCategory = fbSecurity.getClassCategory().newInstance();
					eCategory.setType(type.toString());
					eCategory.setCode(xCategory.getCode());
					eCategory = (C)fSecurity.persist(eCategory);
				}
				catch (InstantiationException e2) {throw new UtilsConfigurationException(e2.getMessage());}
				catch (IllegalAccessException e2) {throw new UtilsConfigurationException(e2.getMessage());}
				catch (JeeslConstraintViolationException e2) {throw new UtilsConfigurationException(e2.getMessage());}	
			}
			
			try
			{
				eCategory.setName(efLang.getLangMap(xCategory.getLangs()));
				eCategory.setDescription(efDescription.create(xCategory.getDescriptions()));
				
				if(xCategory.isSetVisible()){eCategory.setVisible(xCategory.isVisible());}else{eCategory.setVisible(true);}
				if(xCategory.isSetPosition()){eCategory.setPosition(xCategory.getPosition());}else{eCategory.setPosition(0);}
				
				eCategory=(C)fSecurity.update(eCategory);
				iuChilds(eCategory,xCategory);
			}
			catch (JeeslConstraintViolationException e) {logger.error("",e);}
			catch (JeeslLockingException e) {logger.error("",e);}
		}
		
		dbCleanerCategory.remove(fSecurity);
		logger.trace("initUpdateUsecaseCategories finished");
	}
	
	@Deprecated protected void iuChilds(C aclCategory, Category category) throws UtilsConfigurationException
	{
		logger.error("This method *must* be overridden!");
	}
	protected void iuChilds(C eCategory, net.sf.ahtutils.xml.security.Category category) throws UtilsConfigurationException
	{
		logger.error("This method *must* be overridden!");
	}
	
	@Deprecated protected <T extends JeeslSecurityWithViews<V>> T iuListViews(T ejb, Views views) throws JeeslConstraintViolationException, JeeslNotFoundException, JeeslLockingException
	{
		ejb.getViews().clear();
		ejb = fSecurity.update(ejb);
		if(views!=null)
		{
			for(View view : views.getView())
			{
				V ejbView = fSecurity.fByCode(fbSecurity.getClassView(), view.getCode());
				ejb.getViews().add(ejbView);
			}
			ejb = fSecurity.update(ejb);
		}
		return ejb;
	}
	protected <T extends JeeslSecurityWithViews<V>> T iuListViewsSecurity(T ejb, net.sf.ahtutils.xml.security.Views views) throws JeeslConstraintViolationException, JeeslNotFoundException, JeeslLockingException
	{
//		ejb = fSecurity.load(cView, view);
		ejb.getViews().clear();
		ejb = fSecurity.update(ejb);
		if(views!=null)
		{
			for(net.sf.ahtutils.xml.security.View view : views.getView())
			{
				logger.trace("Adding view "+view.getCode()+" to "+ejb.toString());
				V ejbView = fSecurity.fByCode(fbSecurity.getClassView(), view.getCode());
				ejb.getViews().add(ejbView);
			}
			ejb = fSecurity.update(ejb);
		}
		return ejb;
	}
	
	protected <T extends JeeslSecurityWithActions<A>> T iuListActions(T ejb, Actions actions) throws JeeslConstraintViolationException, JeeslNotFoundException, JeeslLockingException
	{
		ejb.getActions().clear();
		ejb = fSecurity.update(ejb);
		if(actions!=null)
		{
			for(net.sf.ahtutils.xml.security.Action action : actions.getAction())
			{
				A ejbAction = fSecurity.fByCode(fbSecurity.getClassAction(), action.getCode());
				ejb.getActions().add(ejbAction);
			}
			ejb = fSecurity.update(ejb);
		}
		return ejb;
	}
}