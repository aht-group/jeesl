package org.jeesl.web.rest.system.security.updater;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.api.rest.rs.system.security.JeeslSecurityRestRoleImport;
import org.jeesl.controller.db.updater.JeeslDbCodeEjbUpdater;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.factory.xml.system.io.sync.XmlDataUpdateFactory;
import org.jeesl.factory.xml.system.io.sync.XmlResultFactory;
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
import org.jeesl.model.xml.system.security.Usecase;
import org.jeesl.model.xml.system.security.Usecases;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.sync.DataUpdate;

public class SecurityRoleUpdater <L extends JeeslLang,D extends JeeslDescription, 
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
		implements JeeslSecurityRestRoleImport
{
	final static Logger logger = LoggerFactory.getLogger(SecurityRoleUpdater.class);
	
	private JeeslDbCodeEjbUpdater<R> updateRole;
	
	public SecurityRoleUpdater(SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,AR,OT,OH,?,?,USER> fbSecurity,
								JeeslSecurityFacade<C,R,V,U,A,M,USER> fSecurity)
	{       
        super(fbSecurity,fSecurity);
	}
	
	@Override protected void iuChilds(C aclCategory, org.jeesl.model.xml.system.security.Category category) throws UtilsConfigurationException
	{
		if(Objects.nonNull(category.getRoles()) && ObjectUtils.isNotEmpty(category.getRoles().getRole()))
		{
			for(org.jeesl.model.xml.system.security.Role role : category.getRoles().getRole())
			{
				updateRole.handled(role.getCode());
				iuRole(aclCategory, role);
			}
		}
	}
	
	public DataUpdate iuSecurityRoles(Security security)
	{
		updateRole = JeeslDbCodeEjbUpdater.createFactory(fbSecurity.getClassRole());
		updateRole.dbEjbs(fSecurity.all(fbSecurity.getClassRole()));

		DataUpdate du = XmlDataUpdateFactory.build();
		try
		{
			iuCategory(security, JeeslSecurityCategory.Type.role);
			du.setResult(XmlResultFactory.buildOk());
		}
		catch (UtilsConfigurationException e)
		{
			e.printStackTrace();
			du.setResult(XmlResultFactory.buildFail());
		}
		
		updateRole.remove(fSecurity);
		logger.trace("iuRoles finished");

		return du;
	}
	

	
	private void iuRole(C category, org.jeesl.model.xml.system.security.Role role) throws UtilsConfigurationException
	{
		R ejb;
		try
		{
			ejb = fSecurity.fByCode(fbSecurity.getClassRole(),role.getCode());
			efLang.rmLang(fSecurity,ejb);
			efDescription.rmDescription(fSecurity,ejb);
		}
		catch (JeeslNotFoundException e)
		{
			try
			{
				ejb = fbSecurity.getClassRole().newInstance();
				ejb.setCategory(category);
				ejb.setCode(role.getCode());
				ejb = fSecurity.persist(ejb);				
			}
			catch (InstantiationException e2) {throw new UtilsConfigurationException(e2.getMessage());}
			catch (IllegalAccessException e2) {throw new UtilsConfigurationException(e2.getMessage());}
			catch (JeeslConstraintViolationException e2) {throw new UtilsConfigurationException(e2.getMessage());}
		}
		
		try
		{
			if(role.isSetDocumentation()){ejb.setDocumentation(role.isDocumentation());}else{ejb.setDocumentation(false);}
			if(role.isSetVisible()){ejb.setVisible(role.isVisible());}else{ejb.setVisible(true);}
			if(role.isSetPosition()){ejb.setPosition(role.getPosition());}else{ejb.setPosition(0);}
			
			ejb.setName(efLang.getLangMap(role.getLangs()));
			ejb.setDescription(efDescription.create(role.getDescriptions()));
			ejb.setCategory(category);
			ejb = fSecurity.save(ejb);

			ejb = fSecurity.load(ejb);
			ejb = iuListViewsSecurity(ejb, role.getViews());
			ejb = iuListActions(ejb, role.getActions());
			ejb = iuUsecasesForRole(ejb, role.getUsecases());
		}
		catch (JeeslConstraintViolationException e) {logger.error("",e);}
		catch (JeeslNotFoundException e) {throw new UtilsConfigurationException(e.getMessage());}
		catch (JeeslLockingException e) {logger.error("",e);}
	}
	
	private R iuUsecasesForRole(R ejb, Usecases usecases) throws JeeslConstraintViolationException, JeeslNotFoundException, JeeslLockingException
	{
		ejb.getUsecases().clear();
		ejb = fSecurity.save(ejb);
		if(usecases!=null)
		{
			for(Usecase usecase : usecases.getUsecase())
			{
				U ejbUsecase = fSecurity.fByCode(fbSecurity.getClassUsecase(), usecase.getCode());
				ejb.getUsecases().add(ejbUsecase);
			}
			ejb = fSecurity.save(ejb);
		}
		return ejb;
	}
}