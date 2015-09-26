package net.sf.ahtutils.prototype.web.mbean.admin.security;

import java.io.Serializable;
import java.util.List;

import net.sf.ahtutils.controller.factory.ejb.security.EjbSecurityActionFactory;
import net.sf.ahtutils.controller.factory.ejb.security.EjbSecurityCategoryFactory;
import net.sf.ahtutils.controller.factory.ejb.security.EjbSecurityRoleFactory;
import net.sf.ahtutils.controller.factory.ejb.security.EjbSecurityUsecaseFactory;
import net.sf.ahtutils.exception.ejb.UtilsConstraintViolationException;
import net.sf.ahtutils.exception.ejb.UtilsLockingException;
import net.sf.ahtutils.exception.ejb.UtilsNotFoundException;
import net.sf.ahtutils.factory.ejb.status.EjbDescriptionFactory;
import net.sf.ahtutils.factory.ejb.status.EjbLangFactory;
import net.sf.ahtutils.interfaces.facade.UtilsSecurityFacade;
import net.sf.ahtutils.interfaces.model.security.UtilsSecurityCategory;
import net.sf.ahtutils.interfaces.model.security.UtilsSecurityRole;
import net.sf.ahtutils.interfaces.model.security.UtilsSecurityUsecase;
import net.sf.ahtutils.interfaces.model.security.UtilsSecurityView;
import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.rest.security.UtilsSecurityAction;
import net.sf.ahtutils.model.interfaces.idm.UtilsUser;
import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractAdminSecurityBean <L extends UtilsLang,
											D extends UtilsDescription,
											C extends UtilsSecurityCategory<L,D,C,R,V,U,A,USER>,
											R extends UtilsSecurityRole<L,D,C,R,V,U,A,USER>,
											V extends UtilsSecurityView<L,D,C,R,V,U,A,USER>,
											U extends UtilsSecurityUsecase<L,D,C,R,V,U,A,USER>,
											A extends UtilsSecurityAction<L,D,C,R,V,U,A,USER>,
											USER extends UtilsUser<L,D,C,R,V,U,A,USER>>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminSecurityBean.class);
	
	protected UtilsSecurityFacade fSecurity;
	protected UtilsSecurityCategory.Type categoryType;
	
	protected EjbLangFactory<L> efLang;
	protected EjbDescriptionFactory<D> efDescription;
	protected EjbSecurityCategoryFactory<L,D,C,R,V,U,A,USER> efCategory;
	protected EjbSecurityRoleFactory<L,D,C,R,V,U,A,USER> efRole;
	protected EjbSecurityUsecaseFactory<L,D,C,R,V,U,A,USER> efUsecase;
	protected EjbSecurityActionFactory<L,D,C,R,V,U,A,USER> efAction;
	
	protected Class<C> cCategory;
	protected Class<R> cRole;
	protected Class<V> cView;
	protected Class<U> cUsecase;
	protected Class<A> cAction;
	
	//Category
	protected List<C> categories;
	public List<C> getCategories() {return categories;}
	
	protected C category;
	public void setCategory(C category) {this.category = category;}
	public C getCategory() {return category;}
	
	//OP Views
	protected List<V> opViews;
	public List<V> getOpViews(){return opViews;}
	
	private List<V> opFvViews;
	public List<V> getOpFvViews(){return opFvViews;}
	public void setOpFvViews(List<V> opFvViews){this.opFvViews = opFvViews;}

	protected V opView;
	public V getOpView(){return opView;}
	public void setOpView(V opView){this.opView = opView;}
	
	protected V tblView;
	public V getTblView(){return tblView;}
	public void setTblView(V tblView){this.tblView = tblView;}
	
	//OP Actions
	protected List<A> opActions;
	public List<A> getOpActions(){return opActions;}
	
	private List<V> opFvActions;
	public List<V> getOpFvActions(){return opFvActions;}
	public void setOpFvActions(List<V> opFvActions){this.opFvActions = opFvActions;}

	protected A opAction;
	public A getOpAction(){return opAction;}
	public void setOpAction(A opAction){this.opAction = opAction;}
	
	protected A tblAction;
	public A getTblAction(){return tblAction;}
	public void setTblAction(A tblAction){this.tblAction = tblAction;}
	
	//OP Usecases
	protected List<U> opUsecases;
	public List<U> getOpUsecases(){return opUsecases;}
	
	private List<U> opFvUsecases;
	public List<U> getOpFvUsecases(){return opFvUsecases;}
	public void setOpFvUsecases(List<U> opFvUsecases){this.opFvUsecases = opFvUsecases;}

	protected U opUsecase;
	public U getOpUsecase(){return opUsecase;}
	public void setOpUsecase(U opUsecase){this.opUsecase = opUsecase;}
	
	protected U tblUsecase;
	public U getTblUsecase(){return tblUsecase;}
	public void setTblUsecase(U tblUsecase){this.tblUsecase = tblUsecase;}
	
	protected String[] langs;
	
	public void initSecuritySuper(final Class<L> cLang, final Class<D> cDescription, final Class<C> cCategory, final Class<R> cRole, final Class<V> cView, final Class<U> cUsecase, final Class<A> cAction, final Class<USER> cUser, String[] langs)
	{
		this.cCategory=cCategory;
		this.cRole=cRole;
		this.cUsecase=cUsecase;
		this.cView=cView;
		this.cAction=cAction;
		this.langs=langs;
		
		efLang = new EjbLangFactory<L>(cLang);
		efDescription = new EjbDescriptionFactory<D>(cDescription);
		efCategory = EjbSecurityCategoryFactory.factory(cLang,cDescription,cCategory,cRole,cView,cUsecase,cAction,cUser);
		efRole = EjbSecurityRoleFactory.factory(cLang,cDescription,cCategory,cRole,cView,cUsecase,cAction,cUser);
		efUsecase = EjbSecurityUsecaseFactory.factory(cLang,cDescription,cCategory,cRole,cView,cUsecase,cAction,cUser);
		efAction = EjbSecurityActionFactory.factory(cLang,cDescription,cCategory,cRole,cView,cUsecase,cAction,cUser);
		
		reloadCategories();
	}
	
	public void selectTblAction() {logger.info(AbstractLogMessage.selectEntity(tblAction));}
	public void selectTblView() {logger.info(AbstractLogMessage.selectEntity(tblView));}
	public void selectTblUsecase() {logger.info(AbstractLogMessage.selectEntity(tblUsecase));}
	
	protected void selectCategory() throws UtilsNotFoundException
	{
		logger.info(AbstractLogMessage.selectEntity(category));
		category = efLang.persistMissingLangs(fSecurity,langs,category);
		category = efDescription.persistMissingLangs(fSecurity,langs,category);
	}
	
	protected void reloadCategories()
	{
		logger.info("reloadCategories");
		categories = fSecurity.allOrderedPosition(cCategory,categoryType);
	}
	
	protected void reorderCategories() throws UtilsConstraintViolationException, UtilsLockingException
	{
		logger.info("updateOrder "+categories.size());
		int i=1;
		for(C c : categories)
		{
			c.setPosition(i);
			fSecurity.update(c);
			i++;
		}
	}
}