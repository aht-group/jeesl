package net.sf.ahtutils.factory.xml.audit;

import org.jeesl.model.xml.io.db.revision.Revisions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlRevisionsFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlRevisionsFactory.class);
	
	public static Revisions build()
	{
		Revisions xml = new Revisions();

		return xml;
	}
}