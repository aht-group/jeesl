package org.jeesl.web.mbean.prototype.system.security;

import java.io.Serializable;
import java.util.List;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated //Use JeeslSecurityActionController
public class AbstractAdminSecurityActionBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
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
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminSecurityActionBean.class);

	private List<AT> templates;public List<AT> getTemplates(){return templates;}
	
	private AT template;public AT getTemplate(){return template;}public void setTemplate(AT template) {this.template = template;}
	
	protected boolean userIsDeveloper; public boolean isUserIsDeveloper() {return userIsDeveloper;} public void setUserIsDeveloper(boolean userIsDeveloper) {this.userIsDeveloper = userIsDeveloper;}
	
	private AbstractAdminSecurityActionBean(SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,AR,OT,OH,?,?,USER> fbSecurity)
	{
		super(fbSecurity);
		userIsDeveloper = true;
	}
	
	public void initSuper(JeeslSecurityFacade<C,R,V,U,A,M,USER> fSecurity, JeeslTranslationBean<L,D,LOC> bTranslation,
											JeeslFacesMessageBean bMessage,
											JeeslSecurityBean<R,V,U,A,AR,CTX,M,USER> bSecurity)
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
		logger.info(AbstractLogMessage.createEntity(fbSecurity.getClassTemplate()));
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