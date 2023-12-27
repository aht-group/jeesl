package net.sf.ahtutils.factory.xml.audit;

import org.jeesl.model.xml.io.db.revision.Change;
import org.jeesl.model.xml.io.db.revision.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlChangeFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlChangeFactory.class);
	
	public static Change build(int aid, Scope scope)
	{
		Change xml = new Change();
		xml.setAid(aid);
		xml.setScope(scope);
		return xml;
	}
	
	public static Change build(int aid, String text)
	{
		Change xml = new Change();
		xml.setAid(aid);
		xml.setText(text);
		return xml;
	}
}