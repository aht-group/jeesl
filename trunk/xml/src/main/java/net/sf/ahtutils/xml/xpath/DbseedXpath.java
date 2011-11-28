package net.sf.ahtutils.xml.xpath;

import java.util.List;

import net.sf.ahtutils.xml.dbseed.Db;
import net.sf.ahtutils.xml.dbseed.Seed;
import net.sf.exlp.util.exception.ExlpXpathNotFoundException;
import net.sf.exlp.util.exception.ExlpXpathNotUniqueException;

import org.apache.commons.jxpath.JXPathContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbseedXpath
{
	final static Logger logger = LoggerFactory.getLogger(DbseedXpath.class);
	
	public static synchronized Seed getSeed(Db dbSeed, String code) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		JXPathContext context = JXPathContext.newContext(dbSeed);
		
		StringBuffer sb = new StringBuffer();
		sb.append("seed[@code='"+code+"']");
		
		@SuppressWarnings("unchecked")
		List<Seed> listResult = (List<Seed>)context.selectNodes(sb.toString());
		if(listResult.size()==0){throw new ExlpXpathNotFoundException("No "+Seed.class.getSimpleName()+" for code="+code);}
		else if(listResult.size()>1){throw new ExlpXpathNotUniqueException("Multiple "+Seed.class.getSimpleName()+" for code="+code);}
		return listResult.get(0);
	}
}