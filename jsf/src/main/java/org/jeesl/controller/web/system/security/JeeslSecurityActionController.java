package org.jeesl.controller.web.system.security;

import java.io.Serializable;
import java.util.List;

import org.jeesl.api.bean.JeeslSecurityBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.factory.ejb.system.security.EjbSecurityActionTemplateFactory;
import org.jeesl.factory.ejb.system.security.EjbSecurityCategoryFactory;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
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
import org.jeesl.interfaces.web.JeeslJsfSecurityHandler;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslSecurityActionController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
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
		extends AbstractJeeslLocaleWebController<L,D,LOC>
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslSecurityActionController.class);

	JeeslSecurityFacade<C,R,V,U,A,CTX,M,USER> fSecurity;
	JeeslSecurityBean<R,V,U,A,AR,CTX,M,USER> bSecurity;
	
	private final SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,AR,OT,OH,?,?,USER> fbSecurity;
	
	private final EjbSecurityCategoryFactory<C> efCategory;
	protected final EjbSecurityActionTemplateFactory<C,AT> efTemplate;
	
	private List<C> categories; public List<C> getCategories() {return categories;}
	private List<AT> templates;public List<AT> getTemplates(){return templates;}
	
	private C category;public void setCategory(C category) {this.category = category;} public C getCategory() {return category;}
	private AT template; public AT getTemplate(){return template;}public void setTemplate(AT template) {this.template = template;}
	
	protected boolean userIsDeveloper; public boolean isUserIsDeveloper() {return userIsDeveloper;} public void setUserIsDeveloper(boolean userIsDeveloper) {this.userIsDeveloper = userIsDeveloper;}
	
	public JeeslSecurityActionController(SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,CTX,M,AR,OT,OH,?,?,USER> fbSecurity)
	{
		super(fbSecurity.getClassL(),fbSecurity.getClassD());
		this.fbSecurity=fbSecurity;
		
		efCategory = fbSecurity.ejbCategory();
		efTemplate = fbSecurity.ejbTemplate();
		
		userIsDeveloper = true;
	}
	
	public void postConstruct(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
			JeeslSecurityFacade<C,R,V,U,A,CTX,M,USER> fSecurity,
			JeeslSecurityBean<R,V,U,A,AR,CTX,M,USER> bSecurity,
			JeeslJsfSecurityHandler<R,V,U,A,AR,USER> security)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fSecurity=fSecurity;
		this.bSecurity=bSecurity;
		this.reloadCategories();
	}
	
	public void reorderTemplates() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fSecurity, templates);}
	
	private void reloadCategories()
	{
		categories = fSecurity.allOrderedPosition(fbSecurity.getClassCategory(),JeeslSecurityCategory.Type.action);
		logger.info(AbstractLogMessage.reloaded(fbSecurity.getClassCategory(),categories));
	}
	public void addCategory() 
	{
		logger.info(AbstractLogMessage.createEntity(fbSecurity.getClassCategory()));
		category = efCategory.create(null,JeeslSecurityCategory.Type.action.toString());
		category.setName(efLang.buildEmpty(lp.getLocales()));
		category.setDescription(efDescription.buildEmpty(lp.getLocales()));
	}
	public void selectCategory() throws JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.selectEntity(category));
		category = efLang.persistMissingLangs(fSecurity,lp,category);
		category = efDescription.persistMissingLangs(fSecurity,lp.getLocales(),category);
		this.reloadTemplates();
		template=null;
	}
	public void saveCategory() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(category));
		category = fSecurity.save(category);
		this.reloadCategories();
		this.reloadTemplates();
	}
	
	private void reloadTemplates() throws JeeslNotFoundException
	{
		templates = fSecurity.allForCategory(fbSecurity.getClassTemplate(),fbSecurity.getClassCategory(),category.getCode());
		if(debugOnInfo){logger.info(AbstractLogMessage.reloaded(fbSecurity.getClassTemplate(), templates));}
	}
	public void addTemplate() throws JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.createEntity(fbSecurity.getClassTemplate()));
		template = efTemplate.build(category,"",templates);
		template.setName(efLang.buildEmpty(lp.getLocales()));
		template.setDescription(efDescription.buildEmpty(lp.getLocales()));
	}
	public void selectTemplate()
	{
		logger.info(AbstractLogMessage.selectEntity(template));
		template = fSecurity.find(fbSecurity.getClassTemplate(), template);
		template = efLang.persistMissingLangs(fSecurity,lp,template);
		template = efDescription.persistMissingLangs(fSecurity,lp.getLocales(),template);
	}	
	public void saveTemplate() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.saveEntity(template));
		template.setCategory(fSecurity.find(fbSecurity.getClassCategory(), template.getCategory()));
		template = fSecurity.save(template);
		this.reloadTemplates();
	}
}