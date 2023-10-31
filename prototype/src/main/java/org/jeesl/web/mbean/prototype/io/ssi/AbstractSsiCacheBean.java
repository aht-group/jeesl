package org.jeesl.web.mbean.prototype.io.ssi;

import java.io.Serializable;
import java.util.List;

import org.jeesl.api.facade.io.JeeslIoSsiFacade;
import org.jeesl.factory.builder.io.ssi.IoSsiDataFactoryBuilder;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiCleaning;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractSsiCacheBean <L extends JeeslLang,D extends JeeslDescription,
										LINK extends JeeslIoSsiStatus<L,D,LINK,?>,
										CLEANING extends JeeslIoSsiCleaning<L,D,CLEANING,?>>
						implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractSsiCacheBean.class);
	
	private final IoSsiDataFactoryBuilder<L,D,?,?,?,?,LINK,?,CLEANING,?> fbSsi;
	
	public AbstractSsiCacheBean(final IoSsiDataFactoryBuilder<L,D,?,?,?,?,LINK,?,CLEANING,?> fbSsi)
	{
		this.fbSsi=fbSsi;

	}

	public void postConstructSsiCache(JeeslIoSsiFacade<?,?,?,?,?,LINK,?,CLEANING,?,?> fSsi)
	{
		reloadLinks(fSsi);
		reloadCleanings(fSsi);
	}

	private List<LINK> links;
	public List<LINK> getLinks() {return links;}
	public void reloadLinks(JeeslFacade facade) {links = facade.allOrderedPositionVisible(fbSsi.getClassStatus());}
	
	private List<CLEANING> cleanings;
	public List<CLEANING> getCleanings() {return cleanings;}
	public void reloadCleanings(JeeslFacade facade) {cleanings = facade.allOrderedPositionVisible(fbSsi.getClassCleaning());}
}