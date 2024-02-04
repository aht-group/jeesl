package org.jeesl.util.query.xml;

import java.util.Date;

import org.exlp.model.xml.io.Dir;
import org.exlp.util.system.DateUtil;

public class IoQuery
{	
	public static Dir dumpDir()
	{
		org.exlp.model.xml.io.File qFile = new org.exlp.model.xml.io.File();
		qFile.setName("");
		qFile.setSize(0l);
		qFile.setLastModifed(DateUtil.toXmlGc(new Date()));
		
		Dir qDir  = new Dir();
		qDir.setName("");
		qDir.getFile().add(qFile);
		
		return qDir;
	}
}