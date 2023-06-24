package org.jeesl.web.mbean.prototype.system.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jeesl.api.facade.core.JeeslUserFacade;
import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.controller.util.comparator.ejb.system.security.SecurityStaffComparator;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.factory.ejb.system.security.EjbStaffFactory;
import org.jeesl.interfaces.bean.op.OpUserBean;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslStaff;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.prototype.controller.handler.op.user.OverlayUserSelectionHandler;
import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class AbstractAdminSecurityDomainBean <C extends JeeslSecurityCategory<?,?>,
												R extends JeeslSecurityRole<?,?,C,?,?,?,USER>,
												USER extends JeeslUser<R>,
												STAFF extends JeeslStaff<R,USER,D1,D2>,
												D1 extends EjbWithId, D2 extends EjbWithId>
		implements Serializable,OpUserBean<USER>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminSecurityDomainBean.class);

	protected JeeslSecurityFacade<?,?,C,R,?,?,?,?,?,?,USER> fSecurity;
	private final SecurityFactoryBuilder<?,?,C,R,?,?,?,?,?,?,?,?,?,?,?,USER> fbSecurity;
	protected JeeslUserFacade<USER> fUser;

	protected final Comparator<STAFF> cpStaff;
	
	protected final Class<STAFF> cStaff;
	
	protected List<R> roles; public List<R> getRoles(){return roles;}
	protected List<D1> domains; public List<D1> getDomains(){return domains;}
	protected List<STAFF> staffs; public List<STAFF> getStaffs(){return staffs;}
	
	protected D1 domain; public D1 getDomain(){return domain;} public void setDomain(D1 domain){this.domain = domain;}
	protected STAFF staff; public STAFF getStaff(){return staff;} public void setStaff(STAFF staff) {this.staff = staff;}
	
	protected EjbStaffFactory<R,USER,STAFF,D1,D2> efStaff;
	
	private OverlayUserSelectionHandler<USER> opContactHandler; @Override public OverlayUserSelectionHandler<USER> getOpUserHandler() {return opContactHandler;}
	
	public AbstractAdminSecurityDomainBean(final SecurityFactoryBuilder<?,?,C,R,?,?,?,?,?,?,?,?,?,?,?,USER> fbSecurity, Class<STAFF> cStaff)
	{
		this.fbSecurity=fbSecurity;
		this.cStaff=cStaff;
		efStaff = fbSecurity.ejbStaff(cStaff);
		cpStaff = (new SecurityStaffComparator<C,R,USER,STAFF>()).factory(SecurityStaffComparator.Type.position);
	}
	
	protected void initSuper(JeeslSecurityFacade<?,?,C,R,?,?,?,?,?,?,USER> fSecurity, JeeslUserFacade<USER> fUser)
	{
		this.fSecurity=fSecurity;
		this.fUser=fUser;
	
		opContactHandler = new OverlayUserSelectionHandler<USER>(this);
	}
	
	protected void loadRoles(String category)
	{
		roles = new ArrayList<R>();
		try
		{
			for(R r : fSecurity.allForCategory(fbSecurity.getClassRole(), fbSecurity.getClassCategory(), category))
			{
				if(r.isVisible())
				{
					roles.add(r);
				}
			}
		}
		catch (JeeslNotFoundException e){logger.warn(e.getMessage());}
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
		Collections.sort(staffs,cpStaff);
	}
	
	public void cancel()
	{
		staff = null;
	}
	
	public void addStaff()
	{
		logger.info(AbstractLogMessage.createEntity(cStaff));
		staff = efStaff.build(null,null,domain);
	}
	
	public void selectStaff()
	{
		logger.info(AbstractLogMessage.selectEntity(staff));
	}
	
	public void save() throws JeeslLockingException
	{
		try
		{
			staff.setUser(fSecurity.find(fbSecurity.getClassUser(),staff.getUser()));
			staff.setRole(fSecurity.find(fbSecurity.getClassRole(),staff.getRole()));
			logger.info(AbstractLogMessage.saveEntity(staff));
			staff = fSecurity.save(staff);
			reloadStaffs();
		}
		catch (JeeslConstraintViolationException e) {saveThrowsConstraintViolation();}
	}
	
	public void rmStaff() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.rmEntity(staff));
		fSecurity.rm(staff);
		staff=null;
		reloadStaffs();
	}
	
	protected void saveThrowsConstraintViolation()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("A "+JeeslConstraintViolationException.class.getSimpleName()+" was detected");
		sb.append(" Most probably by a duplicate object.");
		sb.append(" This should be handled in the implementation class");
		logger.warn(sb.toString());
	}
	
	//AutoComplete User
	public List<USER> autoComplete(String query)
	{
		List<USER> users = fUser.likeNameFirstLast(query);
		logger.info(AbstractLogMessage.autoComplete(fbSecurity.getClassUser(),query,users.size()));
		return users;
	}

	public void autoCompleteSelect()
	{
		staff.setUser(fUser.find(fbSecurity.getClassUser(),staff.getUser()));
		logger.info(AbstractLogMessage.autoCompleteSelect(staff.getUser()));
	}
	
	@Override
	public void selectOpUser(USER user) throws JeeslLockingException, JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.selectEntity(user));
		staff.setUser(fUser.find(fbSecurity.getClassUser(),user));
	}
}