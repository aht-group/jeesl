package org.jeesl.factory.xml.system.io.fr;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslFileMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.factory.xml.io.XmlFileFactory;
import net.sf.exlp.xml.io.Files;

public class XmlFilesFactory<CONTAINER extends JeeslFileContainer<?,META>,
							META extends JeeslFileMeta<?,CONTAINER,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlFilesFactory.class);
	
	public Files build(List<META> metas)
	{
		Files xml = net.sf.exlp.factory.xml.io.XmlFilesFactory.build();
		
		List<String> names = new ArrayList<String>();
		for(META meta : metas)
		{
			xml.getFile().add(XmlFileFactory.build(meta.getFileName()));
			names.add(meta.getFileName());
		}
		if(!names.isEmpty()) {xml.setName(StringUtils.join(names, ", "));}
		xml.setElements(metas.size());
		
		
		return xml;
	}
}