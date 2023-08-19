package org.jeesl.api.bean.cache;

import java.io.Serializable;

import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface JeeslHelpCacheBean <VIEW extends JeeslSecurityView<?,?,?,?,?,?>> extends Serializable
{
	final static Logger logger = LoggerFactory.getLogger(JeeslHelpCacheBean.class);
	
	void clearCache(VIEW view);
}