package org.jeesl.factory.ejb.module.cl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jeesl.factory.builder.module.ChecklistFactoryBuilder;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.cl.JeeslClCheckItem;
import org.jeesl.interfaces.model.module.cl.JeeslClTrackItem;
import org.jeesl.interfaces.model.module.cl.JeeslClTracklist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbTrackItemFactory<CI extends JeeslClCheckItem<?,?,?>,
								TL extends JeeslClTracklist<?,?,?>,
								TI extends JeeslClTrackItem<CI,TL,?>
							>
{
	final static Logger logger = LoggerFactory.getLogger(EjbTrackItemFactory.class);
	
	private final ChecklistFactoryBuilder<?,?,?,?,?,?,TL,TI,?,?,?> fbCl;
	
    public EjbTrackItemFactory(ChecklistFactoryBuilder<?,?,?,?,?,?,TL,TI,?,?,?> fbCl)
    {
        this.fbCl = fbCl;
    } 
	
    public TI build(TL tracklist, CI checkItem)
    {
		try
		{
			TI ejb = fbCl.getClassTrackItem().newInstance();
			ejb.setTracklist(tracklist);
			ejb.setItem(checkItem);
		    return ejb;
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		throw new RuntimeException("x");
    }
    
    public void converter(JeeslFacade facade, TI ejb)
    {
//    	if(Objects.nonNull(ejb.getTopic())) {ejb.setTopic(facade.find(fbCl.getClassTopic(),ejb.getTopic()));}
//    	if(task.getScope()!=null) {task.setScope(facade.find(fbTafu.getClassScope(),task.getScope()));}
    }
    
    public Set<CI> toLSetCheckItem(List<TI> list)
    {
    	Set<CI> set = new HashSet<>();
    	for(TI ti : list) {set.add(ti.getItem());}
    	return set;
    }
}