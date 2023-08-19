package org.jeesl.factory.txt.system.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslStaff;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtStaffFactory <L extends JeeslLang, D extends JeeslDescription,
								R extends JeeslSecurityRole<L,D,?,?,?,?,USER>,
								USER extends JeeslUser<R>,
								STAFF extends JeeslStaff<R,USER,D1,D2>,
								D1 extends EjbWithId, D2 extends EjbWithId>
{
	final static Logger logger = LoggerFactory.getLogger(TxtStaffFactory.class);
    
	private final String localeCode;
	
    public TxtStaffFactory(final String localeCode)
    {
    	this.localeCode=localeCode;
    } 
    
    public String staff(STAFF staff)
    {
    	StringBuffer sb = new StringBuffer();
    	sb.append("Role:").append(staff.getRole().getName().get(localeCode).getLang());
    	sb.append(" User:").append(staff.getUser().getFirstName()).append(staff.getUser().getLastName());
    	sb.append(" Doamin:").append(staff.getDomain().toString());
    	return sb.toString();
    }
    
    public String names(List<STAFF> staffs)
    {
    	List<String> list = new ArrayList<String>();
    	for(STAFF staff : staffs) {list.add(staff.getUser().getFirstName()+" "+staff.getUser().getLastName());}
    	return StringUtils.join(list, ", ");
    }
    
    public String namesLast(List<STAFF> staffs)
    {
    	List<String> list = new ArrayList<String>();
    	for(STAFF staff : staffs) {list.add(staff.getUser().getLastName());}
    	return StringUtils.join(list, ", ");
    }
}