package org.jeesl.controller.cache.module.aom;

import java.util.List;

import org.jeesl.api.facade.module.JeeslAomFacade;
import org.jeesl.factory.builder.module.AomFactoryBuilder;
import org.jeesl.interfaces.cache.module.aom.JeeslAomEventCache;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventType;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslAomEventLoadingCache <REALM extends JeeslTenantRealm<?,?,REALM,?>,
										ETYPE extends JeeslAomEventType<?,?,ETYPE,?>>
						implements JeeslAomEventCache<REALM,ETYPE>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslAomEventLoadingCache.class);
	public static final long serialVersionUID=1;
	
	private final AomFactoryBuilder<?,?,REALM,?,?,?,?,?,?,?,ETYPE,?,?,?,?,?,?> fbAom;
	private JeeslAomFacade<?,?,REALM,?,?,?,?,?,?,?,?> fAom;
	
	public JeeslAomEventLoadingCache(AomFactoryBuilder<?,?,REALM,?,?,?,?,?,?,?,ETYPE,?,?,?,?,?,?> fbAom,
										JeeslAomFacade<?,?,REALM,?,?,?,?,?,?,?,?> fAom)
	{
		this.fbAom=fbAom;
		this.fAom=fAom;	
	}
	
	@Override public List<ETYPE> getEventType() {return fAom.allOrderedPositionVisible(fbAom.getClassEventType());}
}