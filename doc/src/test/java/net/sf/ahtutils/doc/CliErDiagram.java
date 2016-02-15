package net.sf.ahtutils.doc;

import java.io.File;
import java.io.IOException;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.AhtUtilsBootstrap;
import net.sf.ahtutils.doc.er.AbstractErDiagram;

public class CliErDiagram extends AbstractErDiagram
{
	final static Logger logger = LoggerFactory.getLogger(CliErDiagram.class);
	
	public CliErDiagram(Configuration config)
	{
		super(config,null);
		logger.warn("MLW is null");
		fDot = new File(fTmp,"er.dot");
		fSrc = new File("../entities/src/main/java");
		fSvg = new File("../doc/src/main/resources/aht-utils/svg/admin/development/er");
		
		packages = "net/sf/ahtutils/model/ejb";
		colorScheme = "aht-utils/listing/development/er/colorScheme.xml";
	}
	
	public void create() throws IOException, ClassNotFoundException, TranscoderException
	{
//		create("security");
//		create("status");
//		create("symbol");
//		create("ts");
		create("revision");
//		create("io");
	}
	
	public static void main(String args[]) throws Exception
	{
		Configuration config = AhtUtilsBootstrap.init();
		CliErDiagram er = new CliErDiagram(config);
		er.create();
	}
}