package org.jeesl.doc.ofx.cms.system.status;

import org.jeesl.interfaces.model.io.cms.JeeslIoCmsContent;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsElement;
import org.openfuxml.factory.xml.table.XmlTableFactory;
import org.openfuxml.model.xml.core.table.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslCmsStatusTableFactory<E extends JeeslIoCmsElement<?,?,?,?,C,?>,
										C extends JeeslIoCmsContent<?,E,?>>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslCmsStatusTableFactory.class);
	
	public JeeslCmsStatusTableFactory()
	{

	}
	
	public Table build(String localeCode, E element)
	{
		logger.info("Building Paragraph ");
		Table table = XmlTableFactory.build();
		
		return table;
	}
}