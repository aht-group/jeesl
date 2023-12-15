package net.sf.ahtutils.controller.db;

import java.util.Objects;

import org.jeesl.model.xml.system.security.Category;
import org.jeesl.model.xml.system.security.Security;
import org.jeesl.model.xml.system.security.View;
import org.jeesl.util.query.xpath.SecurityXpath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class UtilsSecurityMerger
{
	final static Logger logger = LoggerFactory.getLogger(UtilsSecurityMerger.class);
	
    
    public UtilsSecurityMerger()
	{       

	}
	
	public void mergeViews(Security fileVersion, Security securityRest)
	{	
		for(Category cFile : fileVersion.getCategory())
		{
			try
			{
				org.jeesl.model.xml.system.security.Category cRest = SecurityXpath.getCategory(securityRest,cFile.getCode());
				cFile.setLangs(cRest.getLangs());
				cFile.setDescriptions(cRest.getDescriptions());
				cFile.setPosition(cRest.getPosition());
				cFile.setVisible(cRest.isVisible());
			}
			catch (ExlpXpathNotFoundException e) {e.printStackTrace();}
			catch (ExlpXpathNotUniqueException e) {e.printStackTrace();}

			if(Objects.nonNull(cFile.getViews()))
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