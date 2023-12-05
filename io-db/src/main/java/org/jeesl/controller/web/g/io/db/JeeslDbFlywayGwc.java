package org.jeesl.controller.web.g.io.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoDbFacade;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.factory.builder.io.db.IoDbFlywayFactoryBuilder;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.controller.web.io.db.JeeslIoDbFlywayCallback;
import org.jeesl.interfaces.model.io.db.flyway.JeeslIoDbFlyway;
import org.jeesl.interfaces.model.io.db.flyway.JeeslIoDbFlywayType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.util.db.cache.EjbCodeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslDbFlywayGwc <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
								FLY extends JeeslIoDbFlyway,
								FT extends JeeslIoDbFlywayType<L,D,FT,?>
>
					extends AbstractJeeslLocaleWebController<L,D,LOC>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslDbFlywayGwc.class);
	
	private final IoDbFlywayFactoryBuilder<L,D,FLY,FT> fbDb;
	
	private JeeslIoDbFacade<?,?,?,?,?,?,?,?,?,FLY> fDb;
	
	private final EjbCodeCache<FT> cacheType;
	
	@SuppressWarnings("unused")
	private final JeeslIoDbFlywayCallback callback;
		
	private final List<FLY> flyways; public List<FLY> getFlyways() {return flyways;}

	public JeeslDbFlywayGwc(JeeslIoDbFlywayCallback callback, IoDbFlywayFactoryBuilder<L,D,FLY,FT> fbDb)
	{
		super(fbDb.getClassL(),fbDb.getClassD());
		this.callback = callback;
		this.fbDb = fbDb;
		
		cacheType = EjbCodeCache.instance(fbDb.getClassType());
		
		flyways = new ArrayList<>();
	}

	public void postConstruct(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage, JeeslIoDbFacade<?,?,?,?,?,?,?,?,?,FLY> fDb)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fDb = fDb;
		
		cacheType.facade(fDb);
		
		
		this.reloadFlyways();
	}
	
	private void reloadFlyways()
	{
		flyways.clear();
		flyways.addAll(fDb.all(fbDb.getClassFlyway()));
		Collections.reverse(flyways);
	}
}