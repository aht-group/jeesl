package org.jeesl.factory.ejb.module.log;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.journal.JeeslJournalItem;
import org.jeesl.interfaces.model.module.journal.JeeslJournalBook;
import org.jeesl.interfaces.model.module.journal.JeeslJournalImpact;
import org.jeesl.interfaces.model.module.journal.JeeslJournalScope;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbLogItemFactory<BOOK extends JeeslJournalBook<?,ITEM>,
								ITEM extends JeeslJournalItem<?,?,?,?,BOOK,IMPACT,CONF,USER>,
								IMPACT extends JeeslJournalImpact<?,?,IMPACT,?>,
								CONF extends JeeslJournalScope<?,?,CONF,?>,
								USER extends EjbWithId
								>
{
	final static Logger logger = LoggerFactory.getLogger(EjbLogItemFactory.class);
	
	private final Class<ITEM> cItem;
	private final Class<IMPACT> cImpact;
	private final Class<USER> cUser;
    
	public EjbLogItemFactory(final Class<ITEM> cItem, final Class<IMPACT> cImpact, final Class<USER> cUser)
	{  
        this.cItem = cItem;
        this.cImpact = cImpact;
        this.cUser = cUser;
	}
	    
	public ITEM build(USER user, IMPACT impact)
	{
		ITEM ejb = null;
		try
		{
			ejb = cItem.newInstance();
			ejb.setRecord(LocalDate.now());
			ejb.setAuthor(user);
			ejb.setImpact(impact);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public void lazy(JeeslFacade facade, ITEM item)
	{
		if(item.getAuthor()!=null) {item.setAuthor(facade.find(cUser,item.getAuthor()));}
		if(item.getImpact()!=null) {item.setImpact(facade.find(cImpact,item.getImpact()));}
	}
	
	public List<BOOK> toBooks(List<ITEM> items)
	{
		Set<BOOK> set = new HashSet<>();
		for(ITEM i : items)
		{
			if(!set.contains(i.getLog())) {set.add(i.getLog());}
		}
		return new ArrayList<>(set);
	}
}