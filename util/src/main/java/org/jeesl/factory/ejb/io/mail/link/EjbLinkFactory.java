package org.jeesl.factory.ejb.io.mail.link;

import java.time.LocalDateTime;

import org.jeesl.interfaces.model.io.mail.link.JeeslLink;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbLinkFactory<LI extends JeeslLink<L,D,T>, T extends JeeslStatus<L,D,T>,L extends JeeslLang,D extends JeeslDescription>
{
	final static Logger logger = LoggerFactory.getLogger(EjbLinkFactory.class);
	
    final Class<LI> clLink;
	
    public static <LI extends JeeslLink<L,D,T>,T extends JeeslStatus<L,D,T>,L extends JeeslLang, D extends JeeslDescription>
    	EjbLinkFactory<LI,T,L,D> createFactory(final Class<LI> clLink)
  {
      return new EjbLinkFactory<LI,T,L,D>(clLink);
  }
    
    public EjbLinkFactory(final Class<LI> clLink)
    {
        this.clLink = clLink;
    } 
    
    public LI create(T type, long refId, String code, LocalDateTime validUntil)
    {
    	LI ejb = null;
    	
    	try
    	{
			ejb = clLink.newInstance();
			ejb.setType(type);
			ejb.setRefId(refId);
			ejb.setCode(code);
			ejb.setStartDate(validUntil);
		}
    	catch (InstantiationException e) {e.printStackTrace();}
    	catch (IllegalAccessException e) {e.printStackTrace();}
    	
    	return ejb;
    }
}