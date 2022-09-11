package net.sf.ahtutils.controller.db;

import org.jeesl.util.query.xpath.SecurityXpath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.access.Access;
import net.sf.ahtutils.xml.security.Category;
import net.sf.ahtutils.xml.security.Security;
import net.sf.ahtutils.xml.security.View;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class UtilsSecurityMerger
{
	final static Logger logger = LoggerFactory.getLogger(UtilsSecurityMerger.class);
	
    
    public UtilsSecurityMerger()
	{       

	}
	
	public void mergeViews(Access fileVersion, Security securityRest)
	{	
		for(Category cFile : fileVersion.getCategory())
		{
			try
			{
				net.sf.ahtutils.xml.security.Category cRest = SecurityXpath.getCategory(securityRest,cFile.getCode());
				cFile.setLangs(cRest.getLangs());
				cFile.setDescriptions(cRest.getDescriptions());
				cFile.setPosition(cRest.getPosition());
				cFile.setVisible(cRest.isVisible());
			}
			catch (ExlpXpathNotFoundException e) {e.printStackTrace();}
			catch (ExlpXpathNotUniqueException e) {e.printStackTrace();}

			if(cFile.isSetViews())
			{
				for(View vFile : cFile.getViews().getView())
				{
					try
					{
						View vRest = SecurityXpath.getView(securityRest,vFile.getCode());
						vFile.setLangs(vRest.getLangs());
						vFile.setDescriptions(vRest.getDescriptions());
						vFile.setPosition(vRest.getPosition());
						vFile.setVisible(vRest.isVisible());
						
						vFile.setActions(vRest.getActions());
					}
					catch (ExlpXpathNotFoundException e) {e.printStackTrace();}
					catch (ExlpXpathNotUniqueException e) {e.printStackTrace();}	
				}
			}
		}
	}
}