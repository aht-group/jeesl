package net.sf.ahtutils.jsf.filerepo;

import java.io.File;
import java.io.IOException;

import net.sf.exlp.util.io.HashUtil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilsFileRepository
{
	final static Logger logger = LoggerFactory.getLogger(UtilsFileRepository.class);
	
	private File baseRepo;
	private String fs;
	
	public UtilsFileRepository(File baseRepo)
	{
		this.baseRepo=baseRepo;

	}
	
	protected void setFs(String fs)
	{
		this.fs=fs;
	}
	
	public void save(String filename, byte[] b)
	{
		String hash = HashUtil.hash(b);
		File dir = getDir(hash);
		createDir(dir);
		try
		{
			
			FileUtils.writeByteArrayToFile(new File(dir,getName(filename, hash)), b);
		}
		catch (IOException e) {e.printStackTrace();}
	}
	
	protected void createDir(File dir)
	{
		if(!dir.exists())
		{
			dir.mkdirs();
		}
	}
	
	protected File getDir(String hash)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(hash.substring(0,2)).append(fs);
		sb.append(hash.substring(2,4)).append(fs);
		sb.append(hash.substring(4,6));
		
		return new File(baseRepo,sb.toString());
	}
	
	protected String getName(String filename, String hash)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(hash).append(".");
		sb.append(FilenameUtils.getExtension(filename));
		return sb.toString();
	}
	
}
