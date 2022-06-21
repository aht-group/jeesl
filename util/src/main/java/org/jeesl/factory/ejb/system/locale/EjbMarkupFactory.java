package org.jeesl.factory.ejb.system.locale;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.interfaces.model.io.cms.JeeslIoCmsMarkupType;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.JeeslMarkup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbMarkupFactory<LOC extends JeeslLocale<?,?,LOC,?>,
								M extends JeeslMarkup<MT>,
								MT extends JeeslIoCmsMarkupType<?,?,MT,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbMarkupFactory.class);

    final Class<M> cM;

    public static <LOC extends JeeslLocale<?,?,LOC,?>,
					M extends JeeslMarkup<MT>,
					MT extends JeeslIoCmsMarkupType<?,?,MT,?>>
    		EjbMarkupFactory<LOC,M,MT> instance(final Class<M> cM)
    {
        return new EjbMarkupFactory<>(cM);
    }
    private EjbMarkupFactory(final Class<M> cM)
    {
        this.cM = cM;
    }

	public Map<String,M> build(List<LOC> locales, MT type)
	{
		Map<String,M> map = new HashMap<>();
		for(LOC loc : locales)
		{
			try
			{
				M markup = cM.newInstance();
				markup.setLkey(loc.getCode());
				markup.setType(type);
				markup.setContent("");
				map.put(loc.getCode(),markup);
			}
			catch (InstantiationException | IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
		return map;
	}
}