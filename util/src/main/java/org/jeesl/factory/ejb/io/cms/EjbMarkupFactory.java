package org.jeesl.factory.ejb.io.cms;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.io.cms.markup.w.JeeslWithMarkupMulti;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbMarkupFactory<LOC extends JeeslLocale<?,?,LOC,?>,
								M extends JeeslIoMarkup<MT>,
								MT extends JeeslIoMarkupType<?,?,MT,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbMarkupFactory.class);

    final Class<M> cM;

    public static <LOC extends JeeslLocale<?,?,LOC,?>,
					M extends JeeslIoMarkup<MT>,
					MT extends JeeslIoMarkupType<?,?,MT,?>>
    		EjbMarkupFactory<LOC,M,MT> instance(final Class<M> cM)
    {
        return new EjbMarkupFactory<>(cM);
    }
    private EjbMarkupFactory(final Class<M> cM)
    {
        this.cM = cM;
    }

    public Map<String,M> build(LOC locale, MT type) {return this.build(Arrays.asList(locale), type);}
	public Map<String,M> build(List<LOC> locales, MT type)
	{
		Map<String,M> map = new HashMap<>();
		for(LOC loc : locales)
		{
			M markup = this.single(loc, type);
			map.put(loc.getCode(),markup);	
		}
		return map;
	}
	
	private M single(LOC loc, MT type)
	{
		M markup = null;
		try
		{
			markup = cM.newInstance();
			markup.setLkey(loc.getCode());
			markup.setType(type);
			markup.setContent("");
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return markup;
	}
	
	public <T extends JeeslWithMarkupMulti<M>> T persistMissingLangs(JeeslFacade fUtils, List<LOC> locales, T ejb, MT type)
	{
		for(LOC l : locales)
		{
			if(!ejb.getMarkup().containsKey(l.getCode()))
			{
				try
				{
					M m = this.single(l, type);
					m = fUtils.persist(m);
					ejb.getMarkup().put(l.getCode(),m);
					ejb = fUtils.update(ejb);
				}
				catch (JeeslConstraintViolationException e) {e.printStackTrace();}
				catch (JeeslLockingException e) {e.printStackTrace();}
			}
		}
		return ejb;
	}
}