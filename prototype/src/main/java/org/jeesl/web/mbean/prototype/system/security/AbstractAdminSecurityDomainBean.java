package org.jeesl.web.mbean.prototype.system.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.facade.core.JeeslUserFacade;
import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.factory.ejb.system.security.EjbStaffFactory;
import org.jeesl.interfaces.bean.op.OpUserBean;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslStaff;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.exception.ejb.UtilsConstraintViolationException;
import net.sf.ahtutils.exception.ejb.UtilsLockingException;
import net.sf.ahtutils.exception.ejb.UtilsNotFoundException;
import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.model.interfaces.with.EjbWithId;
import net.sf.ahtutils.prototype.controller.handler.op.user.OverlayUserSelectionHandler;
import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class AbstractAdminSecurityDomainBean <L extends UtilsLang,
												D extends UtilsDescription,
												C extends JeeslSecurityCategory<L,D>,
												R extends JeeslSecurityRole<L,D,C,V,U,A,USER>,
												V extends JeeslSecurityView<L,D,C,R,U,A>,
												U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
												A extends JeeslSecurityAction<L,D,R,V,U,AT>,
												AT extends JeeslSecurityTemplate<L,D,C>,
												USER extends JeeslUser<R>,
												STAFF extends JeeslStaff<L,D,C,R,V,U,A,AT,USER,D1,D2>,
												D1 extends EjbWithId, D2 extends EjbWithId>
		implements Serializable,OpUserBean<L,D,C,R,V,U,A,AT,USER>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminSecurityDomainBean.class);

	protected JeeslSecurityFacade<L,D,C,R,V,U,A,AT,USER> fSecurity;
	protected JeeslUserFacade<L,D,C,R,V,U,A,AT,USER> fUser;
	
	protected Class<C> cCategory;
	protected Class<R> cRole;
	protected Class<USER> cUser;
	protected Class<STAFF> cStaff;
	
	protected List<R> roles; public List<R> getRoles(){return roles;}
	protected List<D1> domains; public List<D1> getDomains(){return domains;}
	protected List<STAFF> staffs; public List<STAFF> getStaffs(){return staffs;}
	
	protected D1 domain; public D1 getDomain(){return domain;} public void setDomain(D1 domain){this.domain = domain;}
	protected STAFF staff; public STAFF getStaff(){return staff;} public void setStaff(STAFF staff) {this.staff = staff;}
	
	protected EjbStaffFactory<L,D,C,R,V,U,A,AT,USER,STAFF,D1,D2> efStaff;
	
	private OverlayUserSelectionHandler<L,D,C,R,V,U,A,AT,USER> opContactHandler;
	@Override public OverlayUserSelectionHandler<L,D,C,R,V,U,A,AT,USER> getOpUserHandler() {return opContactHandler;}
	
	public AbstractAdminSecurityDomainBean(final SecurityFactoryBuilder<L,D,C,R,V,U,A,AT,USER> fbSecurity)
	{
		
	}
	
	protected void initSuper(JeeslSecurityFacade<L,D,C,R,V,U,A,AT,USER> fSecurity, JeeslUserFacade<L,D,C,R,V,U,A,AT,USER> fUser, Class<C> cCategory, Class<R> cRole, Class<USER> cUser, Class<STAFF> cStaff)
	{
		this.fSecurity=fSecurity;
		this.fUser=fUser;
		
		this.cCategory=cCategory;
		this.cRole=cRole;
		this.cUser=cUser;
		this.cStaff=cStaff;
		
		efStaff = EjbStaffFactory.factory(cStaff);
		
		opContactHandler = new OverlayUserSelectionHandler<L,D,C,R,V,U,A,AT,USER>(this);
	}
	
	protected void loadRoles(String category)
	{
		roles = new ArrayList<R>();
		try
		{
			for(R r : fSecurity.allForCategory(cRole, cCategory, category))
			{
				if(r.isVisible())
				{
					roles.add(r);
				}
			}
		}
		catch (UtilsNotFoundException e){logger.warn(e.getMessage());}
	}
	
	public void selectDomain()
	{
		logger.info(AbstractLogMessage.selectEntity(domain));
		reloadStaffs();
		
		staff = null;
	}

	protected void reloadStaffs()
	{
		staffs = fSecurity.fStaffD(cStaff, domain);
	}
	
	public void cancel()
	{
		staff = null;
	}
	
	public void addStaff()
	{
		logger.info(AbstractLogMessage.addEntity(cStaff));
		staff = efStaff.build(null,null,domain);
	}
	
	public void selectStaff()
	{
		logger.info(AbstractLogMessage.selectEntity(staff));
	}
	
	public void save() throws UtilsLockingException
	{
		try
		{
			staff.setUser(fSecurity.find(cUser,staff.getUser()));
			staff.setRole(fSecurity.find(cRole,staff.getRole()));
			logger.info(AbstractLogMessage.saveEntity(staff));
			staff = fSecurity.save(staff);
			reloadStaffs();
		}
		catch (UtilsConstraintViolationException e) {saveThrowsConstraintViolation();}
	}
	
	public void rmStaff() throws UtilsConstraintViolationException, UtilsLockingException
	{
		logger.info(AbstractLogMessage.rmEntity(staff));
		fSecurity.rm(staff);
		staff=null;
		reloadStaffs();
	}
	
	protected void saveThrowsConstraintViolation()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("A "+UtilsConstraintViolationException.class.getSimpleName()+" was detected");
		sb.append(" Most probably by a duplicate object.");
		sb.append(" This should be handled in the implementation class");
		logger.warn(sb.toString());
	}
	
	//AutoComplete User
	public List<USER> autoComplete(String query)
	{
		List<USER> users = fUser.likeNameFirstLast(cUser,query);
		logger.info(AbstractLogMessage.autoComplete(cUser,query,users.size()));
		return users;
	}

	public void autoCompleteSelect()
	{
		staff.setUser(fUser.find(cUser,staff.getUser()));
		logger.info(AbstractLogMessage.autoCompleteSelect(staff.getUser()));
	}
	
	@Override
	public void selectOpUser(USER user) throws UtilsLockingException, UtilsConstraintViolationException
	{
		logger.info(AbstractLogMessage.selectEntity(user));
		staff.setUser(fUser.find(cUser,user));
	}
}