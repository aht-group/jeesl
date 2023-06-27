package org.jeesl.factory.ejb.module.cl;

import java.time.LocalDate;
import java.util.List;

import org.jeesl.factory.builder.module.ChecklistFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbPositionFactory;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.module.cl.JeeslClChecklist;
import org.jeesl.interfaces.model.module.cl.JeeslClCheckItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbChecklistItemFactory<CL extends JeeslClChecklist<?,?,?>,
									CI extends JeeslClCheckItem<?,CL,M>,
									M extends JeeslIoMarkup<MT>,
									MT extends JeeslIoMarkupType<?,?,MT,?>
							>
{
	final static Logger logger = LoggerFactory.getLogger(EjbChecklistItemFactory.class);
	
	private final ChecklistFactoryBuilder<?,?,?,?,CL,CI,?,M,MT> fbCl;
	
    public EjbChecklistItemFactory(ChecklistFactoryBuilder<?,?,?,?,CL,CI,?,M,MT> fbCl)
    {
        this.fbCl = fbCl;
    } 
	
    public CI build(CL list, List<CI> items)
    {
		try
		{
			CI ejb = fbCl.getClassChecklistItem().newInstance();
			ejb.setChecklist(list);
			EjbPositionFactory.next(ejb,items);
			
			ejb.setValidFrom(LocalDate.now());
			
		    return ejb;
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		throw new RuntimeException("x");
    }
    
    public void converter(JeeslFacade facade, CI ejb)
    {
//    	if(task.getStatus()!=null) {task.setStatus(facade.find(fbTafu.getClassStatus(),task.getStatus()));}
//    	if(task.getScope()!=null) {task.setScope(facade.find(fbTafu.getClassScope(),task.getScope()));}
    }
}