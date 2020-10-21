package org.jeesl.web.mbean.prototype.system.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jeesl.api.bean.JeeslSecurityBean;
import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.security.doc.JeeslSecurityOnlineHelp;
import org.jeesl.interfaces.model.system.security.doc.JeeslSecurityOnlineTutorial;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityArea;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class AbstractAdminSecurityUsecasesBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											C extends JeeslSecurityCategory<L,D>,
											R extends JeeslSecurityRole<L,D,C,V,U,A,USER>,
											V extends JeeslSecurityView<L,D,C,R,U,A>,
											U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
											A extends JeeslSecurityAction<L,D,R,V,U,AT>,
											AT extends JeeslSecurityTemplate<L,D,C>,
											M extends JeeslSecurityMenu<V,M>,
											AR extends JeeslSecurityArea<L,D,V>,
											OT extends JeeslSecurityOnlineTutorial<L,D,V>,
											OH extends JeeslSecurityOnlineHelp<V,?,?>,
											USER extends JeeslUser<R>>
		extends AbstractAdminSecurityBean<L,D,LOC,C,R,V,U,A,AT,M,AR,OT,OH,USER>
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminSecurityUsecasesBean.class);
	
	private List<U> usecases; public List<U> getUsecases() {return usecases;}
	private List<R> roles; public List<R> getRoles(){return roles;}
	private List<V> views; public List<V> getViews(){return views;}
	private List<A> actions; public List<A> getActions(){return actions;}
	
	private U usecase; public U getUsecase(){return usecase;} public void setUsecase(U usecase){this.usecase = usecase;}
	
	public AbstractAdminSecurityUsecasesBean(SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,M,AR,OT,OH,?,?,USER> fbSecurity)
	{
		super(fbSecurity);
		categoryType = JeeslSecurityCategory.Type.usecase;
	}
	
	public void postConstructUsecase(JeeslSecurityFacade<L,D,C,R,V,U,A,AT,M,USER> fSecurity, JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage, JeeslSecurityBean<L,D,C,R,V,U,A,AT,M,USER> bSecurity)
	{
		super.postConstructSecurity(fSecurity,bTranslation,bMessage,bSecurity);		
		opViews = fSecurity.all(fbSecurity.getClassView());
		Collections.sort(opViews,comparatorView);
		
		opActions = new ArrayList<A>();
	}
	
	public void categorySelected() throws JeeslNotFoundException
	{
		reloadUsecases();
		usecase=null;
	}
	public void saveCategory() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(category));
		category = fSecurity.save(category);
		reloadCategories();
		reloadUsecases();
		categorySaved();
	}
	
	public void selectUsecase()
	{
		logger.info(AbstractLogMessage.selectEntity(usecase));
		reloadUsecase();
		usecase = efLang.persistMissingLangs(fSecurity,localeCodes,usecase);
		usecase = efDescription.persistMissingLangs(fSecurity,localeCodes,usecase);
		
		reloadActions();
	}
	
	//Reload
	private void reloadUsecase()
	{
		usecase = fSecurity.load(fbSecurity.getClassUsecase(),usecase);
		roles = usecase.getRoles();
		views = usecase.getViews();
		actions = usecase.getActions();
		
		Collections.sort(views,comparatorView);
		Collections.sort(actions,comparatorAction);
		Collections.sort(roles,comparatorRole);
	}
	
	private void reloadUsecases() throws JeeslNotFoundException
	{
		logger.info("reloadUsecases");
		usecases = fSecurity.allForCategory(fbSecurity.getClassUsecase(),fbSecurity.getClassCategory(),category.getCode());
	}
	private void reloadActions()
	{
		opActions.clear();
		for(V v : usecase.getViews())
		{
			v = fSecurity.load(fbSecurity.getClassView(),v);
			opActions.addAll(v.getActions());
		}
		Collections.sort(opActions, comparatorAction);
	}
	
	//Add
	public void addCategory() throws JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.addEntity(fbSecurity.getClassCategory()));
		category = efCategory.create(null,JeeslSecurityCategory.Type.usecase.toString());
		category.setName(efLang.createEmpty(localeCodes));
		category.setDescription(efDescription.createEmpty(localeCodes));
	}
	public void addUsecase() throws JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.addEntity(fbSecurity.getClassUsecase()));
		usecase = efUsecase.build(category,"");
		usecase.setName(efLang.createEmpty(localeCodes));
		usecase.setDescription(efDescription.createEmpty(localeCodes));
	}

	//Save

	public void saveUsecase() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.saveEntity(usecase));
		usecase.setCategory(fSecurity.find(fbSecurity.getClassCategory(), usecase.getCategory()));
		usecase = fSecurity.save(usecase);
		reloadUsecase();
		reloadUsecases();
		bSecurity.update(usecase);
	}
	
	//OverlayPanel
	public void opAddView() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(!usecase.getViews().contains(opView))
		{
			usecase.getViews().add(opView);
			usecase = fSecurity.save(usecase);
			opView = null;
			selectUsecase();
			bSecurity.update(usecase);
		}
	}
	public void opAddAction() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(!usecase.getActions().contains(opAction))
		{
			usecase.getActions().add(opAction);
			usecase = fSecurity.save(usecase);
			opAction = null;
			selectUsecase();
			bSecurity.update(usecase);
		}
	}
	
	//Overlay-Rm
	public void opRmView() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(tblView!=null && usecase.getViews().contains(tblView))
		{
			usecase.getViews().remove(tblView);
			usecase = fSecurity.save(usecase);
			tblView = null;
			selectUsecase();
			bSecurity.update(usecase);
		}
	}
	public void opRmAction() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(tblAction!=null && usecase.getActions().contains(tblAction))
		{
			usecase.getActions().remove(tblAction);
			usecase = fSecurity.save(usecase);
			tblAction = null;
			selectUsecase();
			bSecurity.update(usecase);
		}
	}
	
	public void reorderUsecases() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fSecurity, usecases);}
}