package org.jeesl.web.rest.system.security.updater;

import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.controller.db.updater.JeeslDbCodeEjbUpdater;
import org.jeesl.controller.monitor.DataUpdateTracker;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.factory.xml.system.io.sync.XmlDataUpdateFactory;
import org.jeesl.factory.xml.system.io.sync.XmlResultFactory;
import org.jeesl.factory.xml.system.status.XmlTypeFactory;
import org.jeesl.interfaces.model.system.security.doc.JeeslSecurityHelp;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityArea;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.exception.ejb.UtilsConstraintViolationException;
import net.sf.ahtutils.exception.ejb.UtilsLockingException;
import net.sf.ahtutils.exception.ejb.UtilsNotFoundException;
import net.sf.ahtutils.exception.processing.UtilsConfigurationException;
import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.xml.access.Access;
import net.sf.ahtutils.xml.access.Action;
import net.sf.ahtutils.xml.access.Category;
import net.sf.ahtutils.xml.security.Security;
import net.sf.ahtutils.xml.sync.DataUpdate;

public class SecurityViewUpdater <L extends UtilsLang,
 								D extends UtilsDescription, 
 								C extends JeeslSecurityCategory<L,D>,
 								R extends JeeslSecurityRole<L,D,C,V,U,A,USER>,
 								V extends JeeslSecurityView<L,D,C,R,U,A>,
 								U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
 								A extends JeeslSecurityAction<L,D,R,V,U,AT>,
 								AT extends JeeslSecurityTemplate<L,D,C>,
 								M extends JeeslSecurityMenu<V,M>,
 								AR extends JeeslSecurityArea<L,D,V>,
 								H extends JeeslSecurityHelp<L,D,V>,
 								USER extends JeeslUser<R>>
		extends AbstractSecurityUpdater<L,D,C,R,V,U,A,AT,M,AR,H,USER>
{
	final static Logger logger = LoggerFactory.getLogger(SecurityViewUpdater.class);
	
	private final JeeslDbCodeEjbUpdater<V> dbCleanerView;
	private final JeeslDbCodeEjbUpdater<A> dbCleanerAction;
	
	public SecurityViewUpdater(SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,M,AR,H,USER> fbSecurity, JeeslSecurityFacade<L,D,C,R,V,U,A,AT,M,USER> fSecurity)
	{       
        super(fbSecurity,fSecurity);
		dbCleanerView = JeeslDbCodeEjbUpdater.createFactory(fbSecurity.getClassView());
		dbCleanerAction = JeeslDbCodeEjbUpdater.createFactory(fbSecurity.getClassAction());
	}
	
	@Deprecated public DataUpdate iuViewsAccess(Access access)
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
	
	@Deprecated @Override protected void iuChilds(C aclCategory, Category category) throws UtilsConfigurationException
	{
		logger.info("iuChilds (access.views) "+category.getViews().getView().size());
		if(category.isSetViews() && category.getViews().isSetView())
		{
			for(net.sf.ahtutils.xml.access.View view : category.getViews().getView())
			{
				logger.trace("View: "+view.getCode());
				dbCleanerView.handled(view.getCode());
				iuView(aclCategory, view);
			}
		}
	}
	@Override protected void iuChilds(C eCategory, net.sf.ahtutils.xml.security.Category xCategory) throws UtilsConfigurationException
	{
		logger.info("iuChilds (security.views) "+xCategory.getTmp().getView().size());
		if(xCategory.isSetTmp() && xCategory.getTmp().isSetView())
		{
			for(net.sf.ahtutils.xml.security.View view : xCategory.getTmp().getView())
			{
				logger.trace("View: "+view.getCode());
				dbCleanerView.handled(view.getCode());
				iuView(eCategory, view);
			}
		}
	}
	
	@Deprecated private void iuView(C category, net.sf.ahtutils.xml.access.View view) throws UtilsConfigurationException
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
		catch (UtilsNotFoundException e)
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
			catch (UtilsConstraintViolationException e2) {throw new UtilsConfigurationException(e2.getMessage());}	
		}
		
		try
		{
			if(view.isSetPublic()){ejb.setAccessPublic(view.isPublic());}else{ejb.setAccessPublic(false);}
			if(view.isSetOnlyLoginRequired()){ejb.setAccessLogin(view.isOnlyLoginRequired());}else{ejb.setAccessLogin(false);}
			if(view.isSetDocumentation()){ejb.setDocumentation(view.isDocumentation());}else{ejb.setDocumentation(false);}
			if(view.isSetVisible()){ejb.setVisible(view.isVisible());}else{ejb.setVisible(true);}
			if(view.isSetPosition()){ejb.setPosition(view.getPosition());}else{ejb.setPosition(0);}
			
			ejb.setName(efLang.getLangMap(view.getLangs()));
			ejb.setDescription(efDescription.create(view.getDescriptions()));
			ejb.setCategory(category);
			
			ejb.setPackageName(null);
			ejb.setViewPattern(null);
			ejb.setUrlBase(null);
			ejb.setUrlMapping(null);	
			
			if(view.isSetNavigation())
			{
				if(view.getNavigation().isSetPackage()){ejb.setPackageName(view.getNavigation().getPackage());}
				if(view.getNavigation().isSetViewPattern()){ejb.setViewPattern(view.getNavigation().getViewPattern().getValue());}
				if(view.getNavigation().isSetUrlMapping())
				{
					ejb.setUrlMapping(view.getNavigation().getUrlMapping().getValue());
					if(view.getNavigation().getUrlMapping().isSetUrl()){ejb.setUrlBase(view.getNavigation().getUrlMapping().getUrl());}
				}
			}
			
			ejb=fSecurity.update(ejb);

			logger.trace("Actions: "+view.getCode()+" "+view.isSetActions());
			if(view.isSetActions() && view.getActions().isSetAction())
			{
				for(Action action : view.getActions().getAction())
				{
					dbCleanerAction.handled(action.getCode());
					iuAction(ejb, action);
				}
			}
			dut.success();
		}
		catch (UtilsConstraintViolationException e) {dut.fail(e,false); }
		catch (UtilsLockingException e) {dut.fail(e,false); }
	}
	private void iuView(C category, net.sf.ahtutils.xml.security.View view) throws UtilsConfigurationException
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
		catch (UtilsNotFoundException e)
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
			catch (UtilsConstraintViolationException e2) {throw new UtilsConfigurationException(e2.getMessage());}	
		}
		
		try
		{
			if(view.isSetAccess())
			{
				if(view.getAccess().isSetPublicUser()){ejb.setAccessPublic(view.getAccess().isPublicUser());}else{ejb.setAccessPublic(false);}
				if(view.getAccess().isSetAuthenticatedUser()){ejb.setAccessLogin(view.getAccess().isAuthenticatedUser());}else{ejb.setAccessPublic(false);}
			}
			else
			{
				ejb.setAccessPublic(false);
				ejb.setAccessLogin(false);
			}
			if(view.isSetDocumentation()){ejb.setDocumentation(view.isDocumentation());}else{ejb.setDocumentation(false);}
			if(view.isSetVisible()){ejb.setVisible(view.isVisible());}else{ejb.setVisible(true);}
			if(view.isSetPosition()){ejb.setPosition(view.getPosition());}else{ejb.setPosition(0);}
			
			ejb.setName(efLang.getLangMap(view.getLangs()));
			ejb.setDescription(efDescription.create(view.getDescriptions()));
			ejb.setCategory(category);
			
			ejb.setPackageName(null);
			ejb.setViewPattern(null);
			ejb.setUrlBase(null);
			ejb.setUrlMapping(null);	
			
			if(view.isSetNavigation())
			{
				if(view.getNavigation().isSetPackage()){ejb.setPackageName(view.getNavigation().getPackage());}
				if(view.getNavigation().isSetViewPattern()){ejb.setViewPattern(view.getNavigation().getViewPattern().getValue());}
				if(view.getNavigation().isSetUrlMapping())
				{
					ejb.setUrlMapping(view.getNavigation().getUrlMapping().getValue());
					if(view.getNavigation().getUrlMapping().isSetUrl()){ejb.setUrlBase(view.getNavigation().getUrlMapping().getUrl());}
				}
			}
			
			ejb=fSecurity.update(ejb);

			logger.trace("Actions: "+view.getCode()+" "+view.isSetActions());
			if(view.isSetActions() && view.getActions().isSetAction())
			{
				for(net.sf.ahtutils.xml.security.Action action : view.getActions().getAction())
				{
					dbCleanerAction.handled(action.getCode());
					iuAction(ejb, action);
				}
			}
			dut.success();
		}
		catch (UtilsConstraintViolationException e) {dut.fail(e,false); }
		catch (UtilsLockingException e) {dut.fail(e,false); }
	}
	
	@Deprecated private void iuAction(V ejbView, Action action) throws UtilsConfigurationException
	{
		A ebj;
		try
		{
			ebj = fSecurity.fByCode(fbSecurity.getClassAction(),action.getCode());
			efLang.rmLang(fSecurity,ebj);
			efDescription.rmDescription(fSecurity,ebj);
		}
		catch (UtilsNotFoundException e)
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
			catch (UtilsConstraintViolationException e2) {throw new UtilsConfigurationException(e2.getMessage());}	
		}
		
		try
		{
			ebj.setVisible(true);
			ebj.setPosition(0);
			
			ebj.setName(efLang.getLangMap(action.getLangs()));
			ebj.setDescription(efDescription.create(action.getDescriptions()));
			ebj.setView(ejbView);
			ebj=fSecurity.update(ebj);
		}
		catch (UtilsConstraintViolationException e) {logger.error("Action.Code:"+action.getCode(),e);}
		catch (UtilsLockingException e) {logger.error("",e);}
	}
	private void iuAction(V ejbView, net.sf.ahtutils.xml.security.Action action) throws UtilsConfigurationException
	{
		A ebj;
		try
		{
			ebj = fSecurity.fByCode(fbSecurity.getClassAction(),action.getCode());
			efLang.rmLang(fSecurity,ebj);
			efDescription.rmDescription(fSecurity,ebj);
		}
		catch (UtilsNotFoundException e)
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
			catch (UtilsConstraintViolationException e2) {throw new UtilsConfigurationException(e2.getMessage());}	
		}
		
		try
		{
			ebj.setVisible(true);
			ebj.setPosition(0);
			
			ebj.setName(efLang.getLangMap(action.getLangs()));
			ebj.setDescription(efDescription.create(action.getDescriptions()));
			ebj.setView(ejbView);
			ebj=fSecurity.update(ebj);
		}
		catch (UtilsConstraintViolationException e) {logger.error("Action.Code:"+action.getCode(),e);}
		catch (UtilsLockingException e) {logger.error("",e);}
	}
}