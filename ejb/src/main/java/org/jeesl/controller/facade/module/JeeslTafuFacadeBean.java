package org.jeesl.controller.facade.module;

import javax.persistence.EntityManager;

import org.jeesl.api.facade.module.JeeslTafuFacade;
import org.jeesl.controller.facade.JeeslFacadeBean;
import org.jeesl.factory.builder.module.TafuFactoryBuilder;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuStatus;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuTask;
import org.jeesl.interfaces.model.module.tafu.JeeslTafuViewport;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.system.time.JeeslTimeDayOfWeek;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslTafuFacadeBean<L extends JeeslLang, D extends JeeslDescription,
								R extends JeeslTenantRealm<L,D,R,?>,
								T extends JeeslTafuTask<R,TS>,
								TS extends JeeslTafuStatus<L,D,TS,?>,
								VP extends JeeslTafuViewport<L,D,VP,?>,
								DOW extends JeeslTimeDayOfWeek<L,D,DOW,?>>
					extends JeeslFacadeBean
					implements JeeslTafuFacade<L,D,R,T,TS,VP,DOW>
{	
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(JeeslAssetFacadeBean.class);
	
	private final TafuFactoryBuilder<L,D,R,T,TS,VP,DOW> fbTafu;
	
	public JeeslTafuFacadeBean(EntityManager em, final TafuFactoryBuilder<L,D,R,T,TS,VP,DOW> fbTafu)
	{
		super(em);
		this.fbTafu=fbTafu;
	}

	
}