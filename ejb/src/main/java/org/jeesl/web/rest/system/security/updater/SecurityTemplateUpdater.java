package org.jeesl.web.rest.system.security.updater;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.api.rest.rs.system.security.JeeslSecurityRestTemplateImport;
import org.jeesl.controller.db.updater.JeeslDbCodeEjbUpdater;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.factory.xml.system.io.sync.XmlDataUpdateFactory;
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

public class SecurityTemplateUpdater <L extends JeeslLang,
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
		implements JeeslSecurityRestTemplateImport
{
	final static Logger logger = LoggerFactory.getLogger(SecurityTemplateUpdater.class);
	
	private JeeslDbCodeEjbUpdater<R> updateRole;
	
	public SecurityTemplateUpdater(SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,AR,OT,OH,?,?,USER> fbSecurity,
									JeeslSecurityFacade<C,R,V,U,A,M,USER> fSecurity)
	{       
        super(fbSecurity,fSecurity);
	}
	
	public DataUpdate iuSecurityTemplates(Security security)
	{
		updateRole = JeeslDbCodeEjbUpdater.createFactory(fbSecurity.getClassRole());
		updateRole.dbEjbs(fSecurity.all(fbSecurity.getClassRole()));

		DataUpdate du = XmlDataUpdateFactory.build();
		logger.warn("NYI iuSecurityTemplates");
/*		try
		{
			iuCategory(security, UtilsSecurityCategory.Type.role);
			du.setResult(XmlResultFactory.buildOk());
		}
		catch (UtilsConfigurationException e)
		{
			e.printStackTrace();
			du.setResult(XmlResultFactory.buildFail());
		}
		
		updateRole.remove(fSecurity);
		logger.trace("iuRoles finished");
*/
		return du;
	}
	
	@Override protected void iuChilds(C category, org.jeesl.model.xml.system.security.Category templates) throws UtilsConfigurationException
	{
		if(Objects.nonNull(templates.getTemplates()) && ObjectUtils.isNotEmpty(templates.getTemplates().getTemplate()))
		{
			for(org.jeesl.model.xml.system.security.Template template : templates.getTemplates().getTemplate())
			{
				updateRole.handled(template.getCode());
				iuTemplate(category, template);
			}
		}
	}
	
	private void iuTemplate(C category, org.jeesl.model.xml.system.security.Template role) throws UtilsConfigurationException
	{
		AT ejb;
		try
		{
			ejb = fSecurity.fByCode(fbSecurity.getClassTemplate(),role.getCode());
			efLang.rmLang(fSecurity,ejb);
			efDescription.rmDescription(fSecurity,ejb);
		}
		catch (JeeslNotFoundException e)
		{
			try
			{
				ejb = fbSecurity.getClassTemplate().newInstance();
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
			if(Objects.nonNull(role.isVisible())) {ejb.setVisible(role.isVisible());}else{ejb.setVisible(true);}
			if(Objects.nonNull(role.getPosition())) {ejb.setPosition(role.getPosition());}else{ejb.setPosition(0);}
			
			ejb.setName(efLang.getLangMap(role.getLangs()));
			ejb.setDescription(efDescription.create(role.getDescriptions()));
			ejb.setCategory(category);
			ejb=fSecurity.update(ejb);
		}
		catch (JeeslConstraintViolationException e) {logger.error("",e);}
		catch (JeeslLockingException e) {logger.error("",e);}
	}
}