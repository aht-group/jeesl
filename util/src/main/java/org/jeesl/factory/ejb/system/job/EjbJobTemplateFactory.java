package org.jeesl.factory.ejb.system.job;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.job.JeeslJobCategory;
import org.jeesl.interfaces.model.system.job.JeeslJobPriority;
import org.jeesl.interfaces.model.system.job.JeeslJobTemplate;
import org.jeesl.interfaces.model.system.job.JeeslJobType;
import org.jeesl.interfaces.model.system.job.cache.JeeslJobExpiration;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbJobTemplateFactory <L extends JeeslLang,D extends JeeslDescription,
									TEMPLATE extends JeeslJobTemplate<L,D,CATEGORY,TYPE,PRIORITY,EXPIRE>,
									CATEGORY extends JeeslJobCategory<L,D,CATEGORY,?>,
									TYPE extends JeeslJobType<L,D,TYPE,?>,
									EXPIRE extends JeeslJobExpiration<L,D,EXPIRE,?>,
									PRIORITY extends JeeslJobPriority<L,D,PRIORITY,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbJobTemplateFactory.class);
	
	private final Class<TEMPLATE> cTemplate;
	private final Class<CATEGORY> cCategory;
	private final Class<TYPE> cType;
	private final Class<EXPIRE> cExpire;
	private final Class<PRIORITY> cPriority;

	public EjbJobTemplateFactory(final Class<TEMPLATE> cTemplate, final Class<CATEGORY> cCategory, final Class<TYPE> cType, final Class<EXPIRE> cExpire, final Class<PRIORITY> cPriority)
	{
		this.cTemplate=cTemplate;
		this.cCategory=cCategory;
		this.cType=cType;
		this.cExpire=cExpire;
		this.cPriority=cPriority;
	}
 
	public TEMPLATE build(CATEGORY category, TYPE type)
	{
		TEMPLATE ejb = null;
		try
		{
			ejb = cTemplate.newInstance();
			ejb.setCategory(category);
			ejb.setType(type);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public void converter(JeeslFacade facade, TEMPLATE template)
	{
		if(template.getCategory()!=null) {template.setCategory(facade.find(cCategory,template.getCategory()));}
		if(template.getType()!=null) {template.setType(facade.find(cType,template.getType()));}
		if(template.getPriority()!=null) {template.setPriority(facade.find(cPriority,template.getPriority()));}
		if(template.getExpiration()!=null) {template.setExpiration(facade.find(cExpire,template.getExpiration()));}
	}
}