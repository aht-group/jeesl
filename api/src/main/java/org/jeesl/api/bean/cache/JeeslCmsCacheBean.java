package org.jeesl.api.bean.cache;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.cms.JeeslIoCmsElement;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsSection;
import org.openfuxml.model.xml.core.ofx.Section;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface JeeslCmsCacheBean <S extends JeeslIoCmsSection<?,S>,
									E extends JeeslIoCmsElement<?,S,?,?,?,?>> extends Serializable
{
	final static Logger logger = LoggerFactory.getLogger(JeeslCmsCacheBean.class);
	
	void clearCache(S section);
	Section buildByElement(String localeCode, E element);
}