package org.jeesl.factory.builder.io;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.system.status.EjbDescriptionFactory;
import org.jeesl.factory.ejb.system.status.EjbLangFactory;
import org.jeesl.factory.ejb.system.status.EjbStatusFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoLocaleFactoryBuilder<L extends JeeslLang,
									D extends JeeslDescription,
									LOC extends JeeslLocale<L,D,LOC,?>>
	extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(IoLocaleFactoryBuilder.class);
	
	private final Class<LOC> cLoc; public Class<LOC> getClassLocale(){return cLoc;}
	private final Class<?> cStatusGlobal; public Class<?> getClassStatusGlobal(){return cStatusGlobal;}
	private final Class<?> cStatusTenant; public Class<?> getClassStatusTenant(){return cStatusTenant;}
	
	public IoLocaleFactoryBuilder(final Class<L> cL, final Class<D> cD, final Class<LOC> cLoc,
								final Class<?> cStatusGlobal, final Class<?> cStatusTenant)
	{       
		super(cL,cD);
		this.cLoc = cLoc;
		this.cStatusGlobal=cStatusGlobal;
		this.cStatusTenant=cStatusTenant;
	}
	
	public EjbLangFactory<L> ejbLang(){return EjbLangFactory.instance(cL);}
	public EjbDescriptionFactory<D> ejbDescription(){return EjbDescriptionFactory.instance(cD);}
	
	public <S extends JeeslStatus<L,D,S>> EjbStatusFactory<L,D,S> ejbStatus(final Class<S> cS) {return new EjbStatusFactory<L,D,S>(cS,cL,cD);}
}