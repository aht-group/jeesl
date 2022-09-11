package org.jeesl.factory.xml.system.security;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslStaff;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.xml.jeesl.QuerySecurity;
import org.jeesl.model.xml.system.security.Staff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Domain;

public class XmlStaffFactory<L extends JeeslLang, D extends JeeslDescription,
							C extends JeeslSecurityCategory<L,D>,
							R extends JeeslSecurityRole<L,D,C,V,U,A,USER>,
							V extends JeeslSecurityView<L,D,C,R,U,A>,
							U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
							A extends JeeslSecurityAction<L,D,R,V,U,AT>,
							AT extends JeeslSecurityTemplate<L,D,C>,
							USER extends JeeslUser<R>,
							STAFF extends JeeslStaff<R,USER,D1,D2>,
							D1 extends EjbWithId, D2 extends EjbWithId>
{
	final static Logger logger = LoggerFactory.getLogger(XmlStaffFactory.class);
		
	private Staff q;
	
	private XmlRoleFactory<L,D,C,R,V,U,A,AT,USER> xfRole;
	private XmlUserFactory<USER> xfUser;
	
	public XmlStaffFactory(QuerySecurity query)
	{
		this(query.getLocaleCode(),query.getStaff());
	}
	public XmlStaffFactory(Staff q){this(null,q);}
	public XmlStaffFactory(String localeCode, Staff q)
	{
		this.q=q;
		
		if(q.isSetRole()){xfRole = new XmlRoleFactory<L,D,C,R,V,U,A,AT,USER>(localeCode,q.getRole());}
		if(q.isSetUser()){xfUser = new XmlUserFactory<USER>(q.getUser());}
	}
	
	public static Staff build()
	{
		Staff xml = new Staff();

		return xml;
	}
	
	public List<Staff> build(List<STAFF> list)
	{
		List<Staff> xList = new ArrayList<Staff>();
		for(STAFF staff : list) {xList.add(build(staff));}
		return xList;
	}
	
	public Staff build(STAFF staff)
	{
		Staff xml = new Staff();
		
		if(q.isSetId()){xml.setId(staff.getId());}
		
		if(q.isSetUser()){xml.setUser(xfUser.build(staff.getUser()));}
		if(q.isSetRole()){xml.setRole(xfRole.build(staff.getRole()));}
		
		if(q.isSetDomain())
		{
			Domain domain = new Domain();
			domain.setId(staff.getDomain().getId());
			xml.setDomain(domain);
		}
		
		return xml;
	}
	
	public static Map<Long,Staff> toMapId(List<Staff> staffs)
	{
		Map<Long,Staff> map = new Hashtable<Long,Staff>();
		for(Staff staff : staffs)
		{
			long id = staff.getId();
			if(!map.containsKey(id)){map.put(id,staff);}
		}
		return map;
	}
}