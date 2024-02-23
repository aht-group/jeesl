package org.jeesl.factory.txt.system.io.vcs;

import java.io.File;

import org.exlp.util.io.txt.ExlpTxtWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SvnSync 
{
	final static Logger logger = LoggerFactory.getLogger(SvnSync.class);
		
	private String svnBaseDir;
	private ExlpTxtWriter writerInit,writerCron;
	
	public SvnSync(String svnBaseDir)
	{
		this.svnBaseDir=svnBaseDir;
		writerInit = new ExlpTxtWriter();
		writerCron = new ExlpTxtWriter();
		
		writerInit.add("#!/bin/sh");writerInit.add("");
		writerCron.add("#!/bin/sh");writerCron.add("");
	}
	
	public void writeInit(File f) {writerInit.writeFile(f);}
	public void writeCron(File f) {writerCron.writeFile(f);}
	
	public void add(String svnUser, String repoUrl)
	{
		int index = repoUrl.lastIndexOf("/");
		String repoCode = repoUrl.substring(index+1);
		String repoPath = svnBaseDir+"/"+repoCode;
		
		writerInit.add("#########################");
		writerInit.add("svnadmin create "+repoPath);
		writerInit.add("echo '#!/bin/sh\\nexit 0' > "+repoPath+"/hooks/pre-revprop-change");
		writerInit.add("chmod a+x "+repoPath+"/hooks/pre-revprop-change");
		writerInit.add("svnsync init --source-username "+svnUser+" file://"+repoPath+" "+repoUrl);
		writerInit.add(" ");
		
		writerCron.add("svnsync sync --source-username "+svnUser+" file://"+repoPath);	
	}	
}