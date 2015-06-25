package net.sf.ahtutils.prototype.web.mbean.admin.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.ahtutils.exception.ejb.UtilsConstraintViolationException;
import net.sf.ahtutils.exception.ejb.UtilsLockingException;
import net.sf.ahtutils.exception.ejb.UtilsNotFoundException;
import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatusFixedCode;
import net.sf.ahtutils.model.interfaces.idm.UtilsUser;
import net.sf.ahtutils.model.interfaces.security.UtilsSecurityAction;
import net.sf.ahtutils.model.interfaces.security.UtilsSecurityCategory;
import net.sf.ahtutils.model.interfaces.security.UtilsSecurityRole;
import net.sf.ahtutils.model.interfaces.security.UtilsSecurityUsecase;
import net.sf.ahtutils.model.interfaces.security.UtilsSecurityView;
import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractAdminSecurityRoleBean <L extends UtilsLang,
											D extends UtilsDescription,
											C extends UtilsSecurityCategory<L,D,C,R,V,U,A,USER>,
											R extends UtilsSecurityRole<L,D,C,R,V,U,A,USER>,
											V extends UtilsSecurityView<L,D,C,R,V,U,A,USER>,
											U extends UtilsSecurityUsecase<L,D,C,R,V,U,A,USER>,
											A extends UtilsSecurityAction<L,D,C,R,V,U,A,USER>,
											USER extends UtilsUser<L,D,C,R,V,U,A,USER>>
			extends AbstractAdminSecurityBean<L,D,C,R,V,U,A,USER>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminSecurityRoleBean.class);
		
	private List<C> categories;
	public List<C> getCategories() {return categories;}
	
	private List<R> roles;
	public List<R> getRoles(){return roles;}

	private C category;
	public void setCategory(C category) {this.category = category;}
	public C getCategory() {return category;}
	
	private R role;
	public R getRole(){return role;}
	public void setRole(R role) {this.role = role;}
	
	private boolean denyRemove; public boolean isDenyRemove(){return denyRemove;}
	
	public void initSuper(final Class<L> cLang, final Class<D> cDescription, final Class<C> cCategory, final Class<R> cRole, final Class<V> cView, final Class<U> cUsecase, final Class<A> cAction, final Class<USER> cUser, String[] langs)
	{
		initSecuritySuper(cLang,cDescription,cCategory,cRole,cView,cUsecase,cAction,cUser,langs);
		opViews = fSecurity.all(cView);
		opActions = new ArrayList<A>();
		opUsecases = fSecurity.all(cUsecase);
		reloadCategories();
	}
	
	// SELECT
	public void selectCategory() throws UtilsNotFoundException
	{
		logger.trace(AbstractLogMessage.selectEntity(category));
		category = efLang.persistMissingLangs(fSecurity,langs,category);
		category = efDescription.persistMissingLangs(fSecurity,langs,category);
		reloadRoles();
		role=null;
	}
	
	public void selectRole()
	{
		logger.trace(AbstractLogMessage.selectEntity(role));
		role = efLang.persistMissingLangs(fSecurity,langs,role);
		role = efDescription.persistMissingLangs(fSecurity,langs,role);		
		role = fSecurity.load(cRole,role);
		reloadActions();
		
		tblView=null;
		tblAction=null;
		tblUsecase=null;
		
		
		denyRemove = false;
		if(role instanceof UtilsStatusFixedCode)
		{
			for(String fixed : ((UtilsStatusFixedCode)role).getFixedCodes())
			{
				if(fixed.equals(role.getCode())){denyRemove=true;}
			}
		}
	}
	
	//RELOAD
	private void reloadCategories()
	{
		categories = fSecurity.allForType(cCategory,UtilsSecurityCategory.Type.role.toString());
	}
	private void reloadRoles() throws UtilsNotFoundException
	{
		roles = fSecurity.allForCategory(cRole,cCategory,category.getCode());
		logger.info("Reloaded "+roles.size());
	}
	private void reloadActions()
	{
		opActions.clear();
		for(V v : role.getViews())
		{
			v = fSecurity.load(cView,v);
			opActions.addAll(v.getActions());
		}
	}
	
	//SAVE
	public void saveCategory() throws UtilsConstraintViolationException, UtilsLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(category));
		category = fSecurity.save(category);
		reloadCategories();
	}


	//Role
	public void addRole() throws UtilsConstraintViolationException
	{
		logger.info(AbstractLogMessage.addEntity(cRole));
		role = efRole.create(category,"");
		role.setName(efLang.createEmpty(langs));
		role.setDescription(efDescription.createEmpty(langs));
	}
	public void rmRole() throws UtilsConstraintViolationException, UtilsNotFoundException
	{
		logger.info(AbstractLogMessage.rmEntity(role));
		fSecurity.rm(role);
		role=null;
		reloadRoles();
		roleUpdatePerformed();
	}
	public void saveRole() throws UtilsConstraintViolationException, UtilsLockingException, UtilsNotFoundException
	{
		logger.info(AbstractLogMessage.saveEntity(role));
		role = fSecurity.save(role);
		reloadRoles();
		roleUpdatePerformed();
	}
	
	protected void roleUpdatePerformed(){}
	
	//OverlayPanel
	public void opAddView() throws UtilsConstraintViolationException, UtilsLockingException
	{
		if(!role.getViews().contains(opView))
		{
			role.getViews().add(opView);
			role = fSecurity.save(role);
			opView = null;
			selectRole();
		}
	}
	public void opAddAction() throws UtilsConstraintViolationException, UtilsLockingException
	{
		if(!role.getActions().contains(opAction))
		{
			role.getActions().add(opAction);
			role = fSecurity.save(role);
			opAction = null;
			selectRole();
		}
	}
	public void opAddUsecase() throws UtilsConstraintViolationException, UtilsLockingException
	{
		if(!role.getUsecases().contains(opUsecase))
		{
			role.getUsecases().add(opUsecase);
			role = fSecurity.save(role);
			opUsecase = null;
			selectRole();
		}
	}
	
	//Overlay-Rm
	public void opRmView() throws UtilsConstraintViolationException, UtilsLockingException
	{
		if(tblView!=null && role.getViews().contains(tblView))
		{
			role.getViews().remove(tblView);
			role = fSecurity.save(role);
			tblView = null;
			selectRole();
		}
	}
	public void opRmAction() throws UtilsConstraintViolationException, UtilsLockingException
	{
		if(tblAction!=null && role.getActions().contains(tblAction))
		{
			role.getActions().remove(tblAction);
			role = fSecurity.save(role);
			tblAction = null;
			selectRole();
		}
	}
	public void opRmUsecase() throws UtilsConstraintViolationException, UtilsLockingException
	{
		if(tblUsecase!=null && role.getUsecases().contains(tblUsecase))
		{
			role.getUsecases().remove(tblUsecase);
			role = fSecurity.save(role);
			tblUsecase = null;
			selectRole();
		}
	}
}