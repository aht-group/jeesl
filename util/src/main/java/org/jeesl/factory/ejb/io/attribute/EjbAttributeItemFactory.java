package org.jeesl.factory.ejb.io.attribute;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jeesl.factory.ejb.util.EjbPositionFactory;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCriteria;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeItem;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbAttributeItemFactory<CRITERIA extends JeeslAttributeCriteria<?,?,?,?,?,?>,
									SET extends JeeslAttributeSet<?,?,?,?,ITEM>,
									ITEM extends JeeslAttributeItem<CRITERIA,SET>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbAttributeItemFactory.class);
	
	private final Class<ITEM> cItem;
    
	public EjbAttributeItemFactory(final Class<ITEM> cItem)
	{       
        this.cItem = cItem;
	}
    
	public ITEM build(CRITERIA criteria, SET set, List<ITEM> list)
	{
		ITEM ejb = null;
		try
		{
			ejb = cItem.newInstance();
			ejb.setCriteria(criteria);
			ejb.setItemSet(set);
			EjbPositionFactory.next(ejb, list);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public Set<CRITERIA> toSetCriteria(List<ITEM> list)
	{
		Set<CRITERIA> set = new HashSet<CRITERIA>();
		for(ITEM i : list) {set.add(i.getCriteria());}
		return set;
	}
	
	public CRITERIA toCriteria(List<ITEM> list, String code)
	{
		for(ITEM i : list)
		{
			if(i.getCriteria().getCode().equals(code)) {return i.getCriteria();}
		}
		return null;
	}
	
	public ITEM clone(ITEM item, SET dstSet)
	{
		ITEM ejb = build(item.getCriteria(),dstSet,null);
		ejb.setPosition(item.getPosition());
		ejb.setTableHeader(item.getTableHeader());
		ejb.setVisible(item.isVisible());
		return ejb;
	}
}