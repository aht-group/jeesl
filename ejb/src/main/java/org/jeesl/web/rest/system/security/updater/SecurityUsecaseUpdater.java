package org.jeesl.web.rest.system.security.updater;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.api.rest.rs.system.security.JeeslSecurityRestUsecaseImport;
import org.jeesl.controller.io.db.updater.JeeslDbCodeEjbUpdater;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.factory.xml.system.io.sync.XmlDataUpdateFactory;
import org.jeesl.factory.xml.system.io.sync.XmlResultFactory;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.model.xml.system.security.Security;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jeesl.model.xml.io.ssi.sync.DataUpdate;

public class SecurityUsecaseUpdater <L extends JeeslLang,
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
		implements JeeslSecurityRestUsecaseImport
{
	final static Logger logger = LoggerFactory.getLogger(SecurityUsecaseUpdater.class);
	
	private JeeslDbCodeEjbUpdater<U> updateUsecases;
	
	public SecurityUsecaseUpdater(SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,AR,?,?,OT,OH,?,?,?,USER> fbSecurity,
									JeeslSecurityFacade<C,R,V,U,A,CTX,M,USER> fSecurity)
	{       
        super(fbSecurity,fSecurity);
	}
	
	@Override public DataUpdate iuSecurityUsecases(Security usecases)
	{
		logger.trace("iuSecurityUsecases starting ..."+fSecurity.allForType(fbSecurity.getClassCategory(),JeeslSecurityCategory.Type.usecase.toString()).size());
		updateUsecases = JeeslDbCodeEjbUpdater.instance(fbSecurity.getClassUsecase());
		updateUsecases.dbEjbs(fSecurity.all(fbSecurity.getClassUsecase()));

		DataUpdate du = XmlDataUpdateFactory.build();
		try
		{
			iuCategory(usecases, JeeslSecurityCategory.Type.usecase);
			du.setResult(XmlResultFactory.buildOk());
		}
		catch (UtilsConfigurationException e)
		{
			e.printStackTrace();
			du.setResult(XmlResultFactory.buildFail());
		}
		
		logger.trace("Before: UC "+fSecurity.all(fbSecurity.getClassUsecase()).size());
		updateUsecases.remove(fSecurity);
		logger.trace("After: UC "+fSecurity.all(fbSecurity.getClassUsecase()).size());
		logger.trace("iuSecurityUsecases finished "+fSecurity.allForType(fbSecurity.getClassCategory(),JeeslSecurityCategory.Type.usecase.toString()).size());

		return du;
	}
	
	@Override protected void iuChilds(C aclCategory, org.jeesl.model.xml.system.security.Category category) throws UtilsConfigurationException
	{
		logger.trace("iuChilds "+category.getCode());
		if(Objects.nonNull(category.getUsecases()) && ObjectUtils.isNotEmpty(category.getUsecases().getUsecase()))
		{
			logger.trace("iuChilds "+category.getCode()+ " "+category.getUsecases().getUsecase().size());
			for(org.jeesl.model.xml.system.security.Usecase usecase : category.getUsecases().getUsecase())
			{
				updateUsecases.handled(usecase.getCode());
				iuUsecase(aclCategory, usecase);
			}
		}
	}
	
	private void iuUsecase(C category, org.jeesl.model.xml.system.security.Usecase usecase) throws UtilsConfigurationException
	{
		U ebj;
		try
		{
			ebj = fSecurity.fByCode(fbSecurity.getClassUsecase(),usecase.getCode());
			efLang.rmLang(fSecurity,ebj);
			efDescription.rmDescription(fSecurity,ebj);
		}
		catch (JeeslNotFoundException e)
		{
			try
			{
				ebj = fbSecurity.getClassUsecase().newInstance();
				ebj.setCategory(category);
				ebj.setCode(usecase.getCode());
				ebj = fSecurity.persist(ebj);
			}
			catch (InstantiationException e2) {throw new UtilsConfigurationException(e2.getMessage());}
			catch (IllegalAccessException e2) {throw new UtilsConfigurationException(e2.getMessage());}
			catch (JeeslConstraintViolationException e2) {throw new UtilsConfigurationException(e2.getMessage());}	
		}
		
		try
		{
			if(Objects.nonNull(usecase.isVisible())) {ebj.setVisible(usecase.isVisible());}else{ebj.setVisible(true);}
			if(Objects.nonNull(usecase.getPosition())) {ebj.setPosition(usecase.getPosition());}else{ebj.setPosition(0);}
			
			ebj.setName(efLang.getLangMap(usecase.getLangs()));
			ebj.setDescription(efDescription.create(usecase.getDescriptions()));
			ebj.setCategory(category);
			ebj=fSecurity.update(ebj);
			
			ebj = fSecurity.load(fbSecurity.getClassUsecase(), ebj);
			ebj = iuListViewsSecurity(ebj,usecase.getViews());
			ebj = iuListActions(ebj, usecase.getActions());
		}
		catch (JeeslConstraintViolationException e) {logger.error("",e);}
		catch (JeeslNotFoundException e) {throw new UtilsConfigurationException(e.getMessage());}
		catch (JeeslLockingException e) {logger.error("",e);}
	}
}