package org.jeesl.factory.builder.system;

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

public class LocaleFactoryBuilder<L extends JeeslLang,
									D extends JeeslDescription,
									LOC extends JeeslLocale<L,D,LOC,?>>
	extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(LocaleFactoryBuilder.class);
	
	private final Class<LOC> cLoc; public Class<LOC> getClassLocale(){return cLoc;}
	private final Class<?> cStatus; public Class<?> getClassStatus(){return cStatus;}
	
	public LocaleFactoryBuilder(final Class<L> cL, final Class<D> cD, final Class<LOC> cLoc, final Class<?> cStatus)
	{       
		super(cL,cD);
		this.cLoc = cLoc;
		this.cStatus=cStatus;
	}
	
	public EjbLangFactory<L> ejbLang(){return new EjbLangFactory<L>(cL);}
	public EjbDescriptionFactory<D> ejbDescription(){return new EjbDescriptionFactory<D>(cD);}
	
	public <S extends JeeslStatus<S,L,D>> EjbStatusFactory<S,L,D> ejbStatus(final Class<S> cS) {return new EjbStatusFactory<S,L,D>(cS,cL,cD);}
}