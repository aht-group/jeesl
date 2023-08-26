package org.jeesl.util.query.xml;

import java.util.Date;

import net.sf.exlp.util.DateUtil;
import net.sf.exlp.xml.io.Dir;

public class IoQuery
{	
	public static Dir dumpDir()
	{
		net.sf.exlp.xml.io.File qFile = new net.sf.exlp.xml.io.File();
		qFile.setName("");
		qFile.setSize(0l);
		qFile.setLastModifed(DateUtil.toXmlGc(new Date()));
		
		Dir qDir  = new Dir();
		qDir.setName("");
		qDir.getFile().add(qFile);
		
		return qDir;
	}
}