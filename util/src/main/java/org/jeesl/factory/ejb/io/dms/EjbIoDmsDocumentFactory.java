package org.jeesl.factory.ejb.io.dms;

import java.util.List;

import org.jeesl.interfaces.model.io.dms.JeeslIoDmsDocument;
import org.jeesl.interfaces.model.io.dms.JeeslIoDmsSection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EjbIoDmsDocumentFactory <SECTION extends JeeslIoDmsSection<?,?,SECTION>,FILE extends JeeslIoDmsDocument<?,SECTION,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoDmsDocumentFactory.class);
	
	private final Class<FILE> cFile;

	public EjbIoDmsDocumentFactory(final Class<FILE> cFile)
	{
        this.cFile = cFile;
	}
 
	public FILE build(SECTION section, List<FILE> files)
	{
		FILE ejb = null;
		try
		{
			ejb = cFile.newInstance();
			ejb.setSection(section);
			
			if(files!=null) {ejb.setPosition(files.size()+1);}
			else {ejb.setPosition(1);}
			
			ejb.setVisible(true);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}