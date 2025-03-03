package org.jeesl.doc.resources;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.exlp.interfaces.system.property.Configuration;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.openfuxml.media.transcode.Svg2PngTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.UtilsDocumentation;
import net.sf.exlp.util.io.FileIO;
import net.sf.exlp.util.io.resourceloader.MultiResourceLoader;

public class JeeslIconBuilder
{	
	final static Logger logger = LoggerFactory.getLogger(JeeslMsgBuilder.class);
	
	private MultiResourceLoader mrl;
	private File baseIcon;
	private File baseSvg;
	
	public JeeslIconBuilder(Configuration config)
	{
		mrl = MultiResourceLoader.instance();
		baseIcon = new File(config.getString(UtilsDocumentation.keyBaseIconDir));
		baseSvg = new File(config.getString(UtilsDocumentation.keyBaseDirSvg));
		logger.info("Using msg.dir ("+UtilsDocumentation.keyBaseIconDir+"): "+baseIcon.getAbsolutePath());
	}
	
	public void svg() throws UtilsConfigurationException 
	{
		build("jeesl/svg/icon/ui","control/arrow/blue",  "left","right","up","down");
		build("jeesl/svg/icon/ui","control",  "add","cancel","edit","editGrey");
		build("jeesl/svg/icon/ui","generic",  "ghost","unsaved");
		build("jeesl/svg/icon/ui","io/attribute",  "empty","criteriaWithDescription","criteriaWithoutDescription");
		
		build("jeesl/svg/icon/ui","system/io/file",  "doc","pdf","xls");
	}
		
	private void build(String resourceDir, String targetDir, String... items) throws UtilsConfigurationException 
	{
		for(String item : items)
		{
			buildIcon(resourceDir,targetDir,item);
		}
	}
	
	private void buildIcon(String resourceDir, String targetDir, String item) throws UtilsConfigurationException 
	{
		try
		{
			InputStream is = mrl.searchIs(resourceDir+"/"+targetDir+"/"+item+".svg");
			File fTarget = new File(baseIcon,targetDir+File.separator+item+".png");
			
			if(!fTarget.getParentFile().exists()) {throw new UtilsConfigurationException("Directory "+fTarget.getParentFile().getAbsolutePath()+" does not exist");}
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			logger.info("Generating to "+fTarget);
			Svg2PngTranscoder.transcode(12, is, baos);
			FileIO.writeFileIfDiffers(baos.toByteArray(), fTarget);
			
		}
		catch (FileNotFoundException e) {throw new UtilsConfigurationException(e.getMessage());}
		catch (IOException e) {throw new UtilsConfigurationException(e.getMessage());}
		catch (TranscoderException e) {throw new UtilsConfigurationException(e.getMessage());}
	}
	
	public void png(String prefix) throws UtilsConfigurationException
	{
		File base = new File(prefix+"/jeesl/prototype/src/main/resources/META-INF/resources/jeeslPrototypeGfx/12/ui");
		copy("png",FilenameUtils.normalize(base.getAbsolutePath()),"control",   "delete","clone","download","filter","move","remove","save","search","upload","refresh","clean");
		copy("png",FilenameUtils.normalize(base.getAbsolutePath()),"control/dm",   "circleMinusGrey","circleMinusRed","circleGrey","circleGreen");
		copy("png",FilenameUtils.normalize(base.getAbsolutePath()),"security",   "check-mark","documentation","x-mark");
	}
	
	public void svg(String prefix) throws UtilsConfigurationException
	{
		File base = new File(prefix+"/jeesl/doc/src/main/resources/jeesl/svg");
		copy("svg",FilenameUtils.normalize(base.getAbsolutePath()),"icon/ui/system", "home");
		copy("svg",FilenameUtils.normalize(base.getAbsolutePath()),"icon/ui/control", "clone");
		copy("svg",FilenameUtils.normalize(base.getAbsolutePath()),"icon/ui/control/toggle", "overview","detail");
	}
	
	private void copy(String suffix, String resourceDir, String targetDir, String... items) throws UtilsConfigurationException 
	{
		for(String item : items)
		{
			copyIcon(suffix,resourceDir,targetDir,item);
		}
	}
	
	private void copyIcon(String suffix, String resourceDir, String targetDir, String item) throws UtilsConfigurationException 
	{
		try
		{
			String src = resourceDir+"/"+targetDir+"/"+item+"."+suffix;
			InputStream is = mrl.searchIs(src);
			File fTarget = null;
			if(suffix.equals("png")){fTarget = new File(baseIcon,targetDir+File.separator+item+"."+suffix);}
			else if(suffix.equals("svg")){fTarget = new File(baseSvg,targetDir+File.separator+item+"."+suffix);}
			
			if(!fTarget.getParentFile().exists()) {throw new UtilsConfigurationException("Directory "+fTarget.getParentFile().getAbsolutePath()+" does not exist");}
			
			logger.info("Copy to "+fTarget);
			FileIO.writeFileIfDiffers(IOUtils.toByteArray(is), fTarget);
			
		}
		catch (FileNotFoundException e) {throw new UtilsConfigurationException(e.getMessage());}
		catch (IOException e) {throw new UtilsConfigurationException(e.getMessage());}
	}
}