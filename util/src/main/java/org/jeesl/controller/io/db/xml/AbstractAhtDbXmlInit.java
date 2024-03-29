package org.jeesl.controller.io.db.xml;

import org.jeesl.controller.handler.system.property.ConfigBootstrap;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.xml.io.db.Db;
import org.jeesl.util.db.updater.JeeslDbStatusUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAhtDbXmlInit <L extends JeeslLang, D extends JeeslDescription> extends AbstractDbRestInit
{
	final static Logger logger = LoggerFactory.getLogger(AbstractAhtDbXmlInit.class);

	protected UtilsIdMapper idMapper;
	protected JeeslDbStatusUpdater<L,D,?,?> asdi;
	
	public AbstractAhtDbXmlInit(Db dbSeed, DataSource datasource, UtilsIdMapper idMapper, JeeslDbStatusUpdater<L,D,?,?> asdi)
	{
		super(dbSeed, datasource,ConfigBootstrap.instance().combine(),idMapper);
		logger.warn("The Configuration2-hack above may fail ... untested");
		this.idMapper=idMapper;
		this.asdi=asdi;
	}
}