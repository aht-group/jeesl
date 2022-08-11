package org.jeesl.factory.ejb.module.workflow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWithWorkflow;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflow;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflowLink;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbWorkflowLinkFactory<RE extends JeeslRevisionEntity<?,?,?,?,?,?>,
									WL extends JeeslWorkflowLink<WF,RE>,
									WF extends JeeslWorkflow<?,?,?,?>
>
{
	final static Logger logger = LoggerFactory.getLogger(EjbWorkflowLinkFactory.class);
	
	final Class<WL> cLink;
    
	public EjbWorkflowLinkFactory(final Class<WL> cLink)
	{       
        this.cLink = cLink;
	}
	    
	public WL build(RE entity, WF workflow, JeeslWithWorkflow<WF> object)
	{
		WL ejb = null;
		try
		{
			ejb = cLink.newInstance();
			ejb.setEntity(entity);
			ejb.setWorkflow(workflow);
			ejb.setRefId(object.getId());
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public static <WL extends JeeslWorkflowLink<?,?>> Map<Long,WL> toMapRefId(List<WL> links)
	{
		Map<Long,WL> map = new HashMap<>();
		for(WL link : links)
		{
			map.put(link.getRefId(),link);
		}
		return map;
	}
	
	public <T extends EjbWithId> Map<T,WL> toMapOwner(List<T> owners, List<WL> links)
	{
		Map<T,WL> map = new HashMap<>();
		Map<Long,WL> ids = toMapRefId(links);
		for(T o : owners)
		{
			if(ids.containsKey(o.getId())) {map.put(o,ids.get(o.getId()));}
		}
		return map;
	}
}