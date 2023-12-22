package org.jeesl.doc.ofx.cms.system.status;

import org.jeesl.interfaces.model.io.cms.JeeslIoCmsContent;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsElement;
import org.openfuxml.factory.xml.list.XmlListFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslCmsStatusListFactory<E extends JeeslIoCmsElement<?,?,?,?,C,?>,
										C extends JeeslIoCmsContent<?,E,?>>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslCmsStatusListFactory.class);
	
	public JeeslCmsStatusListFactory()
	{

	}
	
	public org.openfuxml.model.xml.core.list.List build(String localeCode, E element)
	{
		logger.info("Building Paragraph ");
		org.openfuxml.model.xml.core.list.List list = XmlListFactory.unordered();
		
		
		return list;
	}
}