package org.jeesl.controller.provider;

import org.jeesl.interfaces.bean.system.io.ssi.MobileBrowserInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MobileBrowserProvider implements MobileBrowserInterface
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(MobileBrowserProvider.class);

	boolean isDesktop = true;
	boolean isPhone = true;
	boolean isTablet = true;
	
	public MobileBrowserProvider()
	{
		
	}
	
	@Override public boolean isDesktop() {return isDesktop;}
	@Override public boolean isPhone() {return isPhone;}
	@Override public boolean isTablet() {return isTablet;}
	
	
	
}