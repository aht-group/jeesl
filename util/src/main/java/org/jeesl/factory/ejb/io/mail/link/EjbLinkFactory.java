package org.jeesl.factory.ejb.io.mail.link;

import java.util.Date;

import org.jeesl.interfaces.model.io.mail.JeeslMailLink;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbLinkFactory<LI extends JeeslMailLink<L,D,T>, T extends JeeslStatus<L,D,T>,L extends JeeslLang,D extends JeeslDescription>
{
	final static Logger logger = LoggerFactory.getLogger(EjbLinkFactory.class);
	
    final Class<LI> clLink;
	
    public static <LI extends JeeslMailLink<L,D,T>,T extends JeeslStatus<L,D,T>,L extends JeeslLang, D extends JeeslDescription>
    	EjbLinkFactory<LI,T,L,D> createFactory(final Class<LI> clLink)
  {
      return new EjbLinkFactory<LI,T,L,D>(clLink);
  }
    
    public EjbLinkFactory(final Class<LI> clLink)
    {
        this.clLink = clLink;
    } 
    
    public LI create(T type, long refId, String code, Date validUntil)
    {
    	LI ejb = null;
    	
    	try
    	{
			ejb = clLink.newInstance();
			ejb.setType(type);
			ejb.setRefId(refId);
			ejb.setCode(code);
			ejb.setValidUntil(validUntil);
		}
    	catch (InstantiationException e) {e.printStackTrace();}
    	catch (IllegalAccessException e) {e.printStackTrace();}
    	
    	return ejb;
    }
}