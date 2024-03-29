package org.jeesl.web.rest.system.security.updater;

import java.util.Objects;

import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.controller.io.db.updater.JeeslDbCodeEjbUpdater;
import org.jeesl.controller.monitoring.counter.DataUpdateTracker;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.factory.xml.system.io.sync.XmlDataUpdateFactory;
import org.jeesl.factory.xml.system.io.sync.XmlResultFactory;
import org.jeesl.factory.xml.system.status.XmlTypeFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
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
import org.jeesl.model.xml.system.security.Security;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jeesl.model.xml.io.ssi.sync.DataUpdate;

public class SecurityViewUpdater <L extends JeeslLang,
 								D extends JeeslDescription, 
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
		extends AbstractSecurityUpdater<L,D,C,R,V,U,A,AT,CTX,M,AR,OT,OH,USER>
{
	final static Logger logger = LoggerFactory.getLogger(SecurityViewUpdater.class);
	
	private final JeeslDbCodeEjbUpdater<V> dbCleanerView;
	private final JeeslDbCodeEjbUpdater<A> dbCleanerAction;
	
	public SecurityViewUpdater(SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,AR,OT,OH,?,?,USER> fbSecurity,
								JeeslSecurityFacade<C,R,V,U,A,CTX,M,USER> fSecurity)
	{       
        super(fbSecurity,fSecurity);
		dbCleanerView = JeeslDbCodeEjbUpdater.instance(fbSecurity.getClassView());
		dbCleanerAction = JeeslDbCodeEjbUpdater.instance(fbSecurity.getClassAction());
	}
	
	@Deprecated public DataUpdate iuViewsAccess(Security access)
	{
		logger.trace("iuViews starting ...");
		
		dbCleanerView.clear();dbCleanerView.dbEjbs(fSecurity.all(fbSecurity.getClassView()));
		dbCleanerAction.clear();dbCleanerAction.dbEjbs(fSecurity.all(fbSecurity.getClassAction()));

		DataUpdate du = XmlDataUpdateFactory.build();
		try
		{
			iuCategoryAccess(access, JeeslSecurityCategory.Type.view);
			du.setResult(XmlResultFactory.buildOk());
		}
		catch (UtilsConfigurationException e)
		{
			du.setResult(XmlResultFactory.buildFail());
			e.printStackTrace();
		}
		
		dbCleanerView.remove(fSecurity);
		dbCleanerAction.remove(fSecurity);
		logger.trace("iuViews finished");
		
		return du;
	}
	public DataUpdate iuViews(Security security)
	{
		logger.trace("iuViews starting ...");
		
		dbCleanerView.clear();dbCleanerView.dbEjbs(fSecurity.all(fbSecurity.getClassView()));
		dbCleanerAction.clear();dbCleanerAction.dbEjbs(fSecurity.all(fbSecurity.getClassAction()));

		DataUpdate du = XmlDataUpdateFactory.build();
		try
		{
			iuCategory(security, JeeslSecurityCategory.Type.view);
			du.setResult(XmlResultFactory.buildOk());
		}
		catch (UtilsConfigurationException e)
		{
			du.setResult(XmlResultFactory.buildFail());
			e.printStackTrace();
		}
		
		dbCleanerView.remove(fSecurity);
		dbCleanerAction.remove(fSecurity);
		logger.trace("iuViews finished");
		
		return du;
	}
	
	@Override protected void iuChilds(C eCategory, org.jeesl.model.xml.system.security.Category xCategory) throws UtilsConfigurationException
	{
		logger.info("iuChilds (security.views) "+xCategory.getTmp().getView().size());
		if(Objects.nonNull(xCategory.getTmp()) && Objects.nonNull(xCategory.getTmp().getView()))
		{
			for(org.jeesl.model.xml.system.security.View view : xCategory.getTmp().getView())
			{
				logger.trace("View: "+view.getCode());
				dbCleanerView.handled(view.getCode());
				iuView(eCategory, view);
			}
		}
	}
	
	private void iuView(C category, org.jeesl.model.xml.system.security.View view) throws UtilsConfigurationException
	{
		DataUpdateTracker dut = new DataUpdateTracker(true);
		dut.setType(XmlTypeFactory.build(fbSecurity.getClassView().getName(),"DB Import/Update"));
		
		V ejb;
		try
		{
			ejb = fSecurity.fByCode(fbSecurity.getClassView(),view.getCode());
			efLang.rmLang(fSecurity,ejb);
			efDescription.rmDescription(fSecurity,ejb);
		}
		catch (JeeslNotFoundException e)
		{
			try
			{
				ejb = fbSecurity.getClassView().newInstance();
				ejb.setCategory(category);
				ejb.setCode(view.getCode());
				ejb = fSecurity.persist(ejb);
			}
			catch (InstantiationException e2) {throw new UtilsConfigurationException(e2.getMessage());}
			catch (IllegalAccessException e2) {throw new UtilsConfigurationException(e2.getMessage());}
			catch (JeeslConstraintViolationException e2) {throw new UtilsConfigurationException(e2.getMessage());}	
		}
		
		try
		{
			if(Objects.nonNull(view.getAccess()))
			{
				if(Objects.nonNull(view.getAccess().isPublicUser())) {ejb.setAccessPublic(view.getAccess().isPublicUser());}else{ejb.setAccessPublic(false);}
				if(Objects.nonNull(view.getAccess().isAuthenticatedUser())) {ejb.setAccessLogin(view.getAccess().isAuthenticatedUser());}else{ejb.setAccessPublic(false);}
			}
			else
			{
				ejb.setAccessPublic(false);
				ejb.setAccessLogin(false);
			}
			if(Objects.nonNull(view.isDocumentation())) {ejb.setDocumentation(view.isDocumentation());}else{ejb.setDocumentation(false);}
			if(Objects.nonNull(view.isVisible())) {ejb.setVisible(view.isVisible());}else{ejb.setVisible(true);}
			if(Objects.nonNull(view.getPosition())) {ejb.setPosition(view.getPosition());}else{ejb.setPosition(0);}
			
			ejb.setName(efLang.getLangMap(view.getLangs()));
			ejb.setDescription(efDescription.create(view.getDescriptions()));
			ejb.setCategory(category);
			
			ejb.setPackageName(null);
			ejb.setViewPattern(null);
			ejb.setUrlBase(null);
			ejb.setUrlMapping(null);	
			
			if(Objects.nonNull(view.getNavigation()))
			{
				if(Objects.nonNull(view.getNavigation().getPackage())) {ejb.setPackageName(view.getNavigation().getPackage());}
				if(Objects.nonNull(view.getNavigation().getViewPattern())) {ejb.setViewPattern(view.getNavigation().getViewPattern().getValue());}
				if(Objects.nonNull(view.getNavigation().getUrlMapping()))
				{
					ejb.setUrlMapping(view.getNavigation().getUrlMapping().getValue());
					if(Objects.nonNull(view.getNavigation().getUrlMapping().getUrl())) {ejb.setUrlBase(view.getNavigation().getUrlMapping().getUrl());}
				}
			}
			
			ejb=fSecurity.save(ejb);

			if(Objects.nonNull(view.getActions()) && Objects.nonNull(view.getActions().getAction()))
			{
				for(org.jeesl.model.xml.system.security.Action action : view.getActions().getAction())
				{
					dbCleanerAction.handled(action.getCode());
					iuAction(ejb, action);
				}
			}
			dut.success();
		}
		catch (JeeslConstraintViolationException e) {dut.fail(e,false); }
		catch (JeeslLockingException e) {dut.fail(e,false); }
	}
	
	
	private void iuAction(V ejbView, org.jeesl.model.xml.system.security.Action action) throws UtilsConfigurationException
	{
		A ebj;
		try
		{
			ebj = fSecurity.fByCode(fbSecurity.getClassAction(),action.getCode());
			efLang.rmLang(fSecurity,ebj);
			efDescription.rmDescription(fSecurity,ebj);
		}
		catch (JeeslNotFoundException e)
		{
			try
			{
				ebj = fbSecurity.getClassAction().newInstance();
				ebj.setView(ejbView);
				ebj.setCode(action.getCode());
				ebj = fSecurity.persist(ebj);
			}
			catch (InstantiationException e2) {throw new UtilsConfigurationException(e2.getMessage());}
			catch (IllegalAccessException e2) {throw new UtilsConfigurationException(e2.getMessage());}
			catch (JeeslConstraintViolationException e2) {throw new UtilsConfigurationException(e2.getMessage());}	
		}
		
		try
		{
			ebj.setVisible(true);
			ebj.setPosition(0);
			
			ebj.setName(efLang.getLangMap(action.getLangs()));
			ebj.setDescription(efDescription.create(action.getDescriptions()));
			ebj.setView(ejbView);
			ebj=fSecurity.save(ebj);
		}
		catch (JeeslConstraintViolationException e) {logger.error("Action.Code:"+action.getCode(),e);}
		catch (JeeslLockingException e) {logger.error("",e);}
	}
}