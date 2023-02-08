package net.sf.ahtutils.db.xml;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.xml.system.io.db.Db;
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
		super(dbSeed, datasource,null,idMapper);
		this.idMapper=idMapper;
		this.asdi=asdi;
	}
}