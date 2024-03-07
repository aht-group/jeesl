package org.jeesl.factory.ejb.c.system.filter;

import org.jeesl.model.ejb.system.filter.SystemFilter;
import org.jeesl.model.ejb.system.filter.SystemFilterScope;
import org.jeesl.model.ejb.system.filter.SystemFilterType;
import org.jeesl.model.ejb.system.security.user.SecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbFilterFactory extends org.jeesl.factory.ejb.g.system.filter.EjbFilterFactory<SystemFilter,SystemFilterType,SystemFilterScope,SecurityUser>
{
	final static Logger logger = LoggerFactory.getLogger(EjbFilterFactory.class);
	
	public static EjbFilterFactory instance() {return new EjbFilterFactory();} 
	private EjbFilterFactory()
	{
		super(SystemFilter.class);
	}
}