package org.jeesl.factory.txt.system.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtSecurityRoleFactory <L extends JeeslLang, D extends JeeslDescription,
										 R extends JeeslSecurityRole<L,D,?,?,?,?>
										 >
{
	final static Logger logger = LoggerFactory.getLogger(TxtSecurityRoleFactory.class);
    
	private final String localeCode;
	
	
	public static <L extends JeeslLang, D extends JeeslDescription, R extends JeeslSecurityRole<L,D,?,?,?,?>, E extends Enum<E>>
		TxtSecurityRoleFactory<L,D,R> instance(E localeCode) {return new TxtSecurityRoleFactory<>(localeCode.toString());}
	private TxtSecurityRoleFactory(String localeCode)
	{
		this.localeCode=localeCode;
	}
	
	public String label(R role)
	{
		return role.getName().get(localeCode).getLang();
	}
	
	public static <L extends JeeslLang, R extends JeeslSecurityRole<L,?,?,?,?,?>, E extends Enum<E>> String labels(E locale, List<R> roles) {return TxtSecurityRoleFactory.labels(locale.toString(),roles);}
	public static <L extends JeeslLang, R extends JeeslSecurityRole<L,?,?,?,?,?>> String labels(String localeCode, List<R> roles)
	{
		List<String> list = new ArrayList<>();
		for(R r : roles) {list.add(r.getName().get(localeCode).getLang());}
		return StringUtils.join(list,", ");
	}
}