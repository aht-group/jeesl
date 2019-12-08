package  org.jeesl.factory.ejb.system.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslStaff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.model.interfaces.with.EjbWithId;

public class EjbStaffFactory <R extends JeeslSecurityRole<?,?,?,?,?,?,USER>,
						USER extends JeeslUser<R>,
						STAFF extends JeeslStaff<R,USER,D1,D2>,
						D1 extends EjbWithId, D2 extends EjbWithId>
{
	final static Logger logger = LoggerFactory.getLogger(EjbStaffFactory.class);
	
	final Class<STAFF> cStaff;
	  
    public EjbStaffFactory(final Class<STAFF> cStaff)
    {
        this.cStaff = cStaff;
    } 
    
    public STAFF build(USER user, R role, D1 domain)
    {
    	STAFF ejb = null;
    	try
    	{
			ejb = cStaff.newInstance();
			ejb.setUser(user);
			ejb.setRole(role);
			ejb.setDomain(domain);
		}
    	catch (InstantiationException e) {e.printStackTrace();}
    	catch (IllegalAccessException e) {e.printStackTrace();}
    	
    	return ejb;
    }
    
    public Map<D1,List<STAFF>> toMapDomainStaff(List<STAFF> staffs)
	{
		Map<D1,List<STAFF>> map = new HashMap<>();
		for(STAFF staff : staffs)
		{
			if(!map.containsKey(staff.getDomain())){map.put(staff.getDomain(), new ArrayList<STAFF>());}
			if(!map.get(staff.getDomain()).contains(staff)){map.get(staff.getDomain()).add(staff);}
		}
		return map;
	}
    
    public static <R extends JeeslSecurityRole<?,?,?,?,?,?,USER>,
					USER extends JeeslUser<R>,
					STAFF extends JeeslStaff<R,USER,D1,D2>,
					D1 extends EjbWithId, D2 extends EjbWithId>
    	List<USER> toUsers(List<STAFF> staffs)
	{
    	Set<USER> set = new HashSet<USER>();
    	for(STAFF staff : staffs){set.add(staff.getUser());}
    	return new ArrayList<USER>(set);
	}
    
    public static <R extends JeeslSecurityRole<?,?,?,?,?,?,USER>,
			USER extends JeeslUser<R>,
			STAFF extends JeeslStaff<R,USER,D1,D2>,
			D1 extends EjbWithId, D2 extends EjbWithId>
		Map<D1,List<USER>> toMapDomainUsers(List<STAFF> staffs)
	{
    	Map<D1,List<USER>> map = new HashMap<D1,List<USER>>();
		for(STAFF staff : staffs)
		{
			if(!map.containsKey(staff.getDomain())){map.put(staff.getDomain(), new ArrayList<USER>());}
			if(!map.get(staff.getDomain()).contains(staff.getUser())){map.get(staff.getDomain()).add(staff.getUser());}
		}
		return map;
	}
    
    public static <R extends JeeslSecurityRole<?,?,?,?,?,?,USER>,
			USER extends JeeslUser<R>,
			STAFF extends JeeslStaff<R,USER,D1,D2>,
			D1 extends EjbWithId, D2 extends EjbWithId>
		List<D1> toDomains(List<STAFF> staffs)
	{
		Set<D1> set = new HashSet<D1>();
		for(STAFF staff : staffs)
		{
			if(!set.contains(staff.getDomain())){set.add(staff.getDomain());}
		}
		return new ArrayList<D1>(set);
	}
    
    public static <R extends JeeslSecurityRole<?,?,?,?,?,?,USER>,
			USER extends JeeslUser<R>,
			STAFF extends JeeslStaff<R,USER,D1,D2>,
			D1 extends EjbWithId, D2 extends EjbWithId>
		Set<D1> toDomainSet(List<STAFF> staffs)
	{
		Set<D1> set = new HashSet<D1>();
		for(STAFF staff : staffs)
		{
			if(!set.contains(staff.getDomain())){set.add(staff.getDomain());}
		}
		return set;
	}
}