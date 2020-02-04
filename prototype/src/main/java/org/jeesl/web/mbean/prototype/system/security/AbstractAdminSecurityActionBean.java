package org.jeesl.web.mbean.prototype.system.security;

import java.io.Serializable;
import java.util.List;

import org.jeesl.api.bean.JeeslSecurityBean;
import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.security.doc.JeeslSecurityHelp;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityArea;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.jsf.util.PositionListReorderer;
import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class AbstractAdminSecurityActionBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslStatus<LOC,L,D>,
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
		extends AbstractAdminSecurityBean<L,D,LOC,C,R,V,U,A,AT,M,AR,H,USER>
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminSecurityActionBean.class);

	private List<AT> templates;public List<AT> getTemplates(){return templates;}
	
	private AT template;public AT getTemplate(){return template;}public void setTemplate(AT template) {this.template = template;}
	
	public AbstractAdminSecurityActionBean(SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,M,AR,H,USER> fbSecurity)
	{
		super(fbSecurity);
	}
	
	public void initSuper(JeeslSecurityFacade<L,D,C,R,V,U,A,AT,M,USER> fSecurity, JeeslTranslationBean bTranslation, JeeslFacesMessageBean bMessage, JeeslSecurityBean<L,D,C,R,V,U,A,AT,M,USER> bSecurity)
	{
		categoryType = JeeslSecurityCategory.Type.action;
		super.postConstructSecurity(fSecurity,bTranslation,bMessage,bSecurity);
	}
	
	@Override public void categorySelected() throws JeeslNotFoundException
	{
		reloadTemplates();
		template=null;
	}
	@Override protected void categorySaved() throws JeeslNotFoundException
	{
		reloadTemplates();
	}
	
	private void reloadTemplates() throws JeeslNotFoundException
	{
		templates = fSecurity.allForCategory(fbSecurity.getClassTemplate(),fbSecurity.getClassCategory(),category.getCode());
		if(debugOnInfo){logger.info(AbstractLogMessage.reloaded(fbSecurity.getClassTemplate(), templates));}
	}

	public void selectTemplate()
	{
		logger.info(AbstractLogMessage.selectEntity(template));
		template = fSecurity.find(fbSecurity.getClassTemplate(), template);
		template = efLang.persistMissingLangs(fSecurity,localeCodes,template);
		template = efDescription.persistMissingLangs(fSecurity,localeCodes,template);
	}
	
	public void addTemplate() throws JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.addEntity(fbSecurity.getClassTemplate()));
		template = efTemplate.build(category,"",templates);
		template.setName(efLang.createEmpty(localeCodes));
		template.setDescription(efDescription.createEmpty(localeCodes));
	}
	
	public void saveTemplate() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.saveEntity(template));
		template.setCategory(fSecurity.find(fbSecurity.getClassCategory(), template.getCategory()));
		template = fSecurity.save(template);
		reloadTemplates();
	}
	
	
	protected void reorderTemplates() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fSecurity, templates);}
}