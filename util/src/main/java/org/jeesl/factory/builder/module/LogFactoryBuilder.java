package org.jeesl.factory.builder.module;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.module.log.EjbLogBookFactory;
import org.jeesl.factory.ejb.module.log.EjbLogItemFactory;
import org.jeesl.interfaces.model.module.journal.JeeslJournalItem;
import org.jeesl.interfaces.model.module.journal.JeeslJournalDomain;
import org.jeesl.interfaces.model.module.journal.JeeslJournalBook;
import org.jeesl.interfaces.model.module.journal.JeeslJournalImpact;
import org.jeesl.interfaces.model.module.journal.JeeslJournalScope;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogFactoryBuilder<L extends JeeslLang, D extends JeeslDescription,
								LOG extends JeeslJournalBook<DOMAIN,ITEM>,
								DOMAIN extends JeeslJournalDomain<L,D,DOMAIN,?>,
								ITEM extends JeeslJournalItem<L,D,?,?,LOG,IMPACT,CONF,USER>,
								IMPACT extends JeeslJournalImpact<L,D,IMPACT,?>,
								CONF extends JeeslJournalScope<L,D,CONF,?>,
								USER extends EjbWithId
								>
	extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(LogFactoryBuilder.class);
	
	private final Class<LOG> cLog;
	private final Class<ITEM> cItem; public Class<ITEM> getClassItem() {return cItem;}
	private final Class<CONF> cScope; public Class<CONF> getClassScope() {return cScope;}
	private final Class<IMPACT> cImpact;
	private final Class<USER> cUser;

	public LogFactoryBuilder(final Class<L> cL,final Class<D> cD,
							 final Class<LOG> cLog,
							 final Class<ITEM> cItem,
							 final Class<IMPACT> cImpact,
							 final Class<CONF> cScope,
							 final Class<USER> cUser)
	{       
		super(cL,cD);
        this.cLog = cLog;
        this.cItem = cItem;
        this.cImpact = cImpact;
        this.cScope = cScope;
        this.cUser = cUser;
	}
	
	public EjbLogBookFactory<LOG,DOMAIN> log(){return new EjbLogBookFactory<>(cLog);}
	public EjbLogItemFactory<LOG,ITEM,IMPACT,CONF,USER> item(){return new EjbLogItemFactory<>(cItem,cImpact,cUser);}
}