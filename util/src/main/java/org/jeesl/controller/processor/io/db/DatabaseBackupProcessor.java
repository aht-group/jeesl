package org.jeesl.controller.processor.io.db;

import java.io.File;

import org.exlp.model.xml.io.Dir;
import org.jeesl.api.rest.rs.jx.io.db.JeeslIoDbRest;
import org.jeesl.util.query.xml.FileQuery;
import org.jeesl.util.query.xml.IoQuery;

import net.sf.exlp.util.io.dir.DirTreeScanner;
import net.sf.exlp.util.xml.JaxbUtil;

public class DatabaseBackupProcessor
{
	private final JeeslIoDbRest rest;
	private final File fDirectory;
	private final String host;
	private final String system;
	
	public DatabaseBackupProcessor(JeeslIoDbRest rest, File fDirectory, String host, String system)
	{		
		this.rest=rest;
		this.fDirectory=fDirectory;
		this.host=host;
		this.system=system;
	}
	
	public void upload()
	{		
		DirTreeScanner dts = new DirTreeScanner(IoQuery.dumpDir());
		Dir dir = dts.getDirTree(fDirectory,false,FileQuery.sql());
		dir.setCode(host);
		dir.setClassifier(system);
		dir.setName(fDirectory.getAbsolutePath());
		JaxbUtil.info(dir);
		rest.uploadDumps(dir);
	}
}