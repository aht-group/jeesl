package org.jeesl.factory.xml.system.io.fr;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.controller.handler.system.io.JeeslFileRepositoryStore;
import org.jeesl.interfaces.model.io.fr.JeeslFileMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.factory.xml.io.XmlDataFactory;

public class XmlFileFactory<META extends JeeslFileMeta<?,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlFileFactory.class);
	
	private JeeslFileRepositoryStore<META> frRepository;
	
	public XmlFileFactory(JeeslFileRepositoryStore<META> frRepository)
	{
		this.frRepository=frRepository;
	}
	
	public org.exlp.model.xml.io.File build(META meta) throws JeeslNotFoundException
	{
		org.exlp.model.xml.io.File xml = net.sf.exlp.factory.xml.io.XmlFileFactory.build();
		xml.setName(meta.getFileName());
		
		if(frRepository!=null)
		{
			xml.setData(XmlDataFactory.build(frRepository.loadFromFileRepository(meta)));
		}
		
		return xml;
	}
}