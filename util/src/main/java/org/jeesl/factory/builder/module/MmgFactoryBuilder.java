package org.jeesl.factory.builder.module;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.module.mmg.EjbMmgClassificationFactory;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgClassification;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgGallery;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgItem;
import org.jeesl.interfaces.model.module.mmg.JeeslMmgQuality;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MmgFactoryBuilder<L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
								R extends JeeslTenantRealm<L,D,R,?>, 
								MG extends JeeslMmgGallery<L>,
							
								MI extends JeeslMmgItem<L,D,R>,
								MC extends JeeslMmgClassification<L,R,MC,?>,
								MQ extends JeeslMmgQuality<L,D,MQ,?>>
	extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(MmgFactoryBuilder.class);
	
	private final Class<LOC> cLocale; public Class<LOC> getClassLocale() {return cLocale;}
	private final Class<MC> cClassification; public Class<MC> getClassClassification() {return cClassification;}

	public MmgFactoryBuilder(final Class<L> cL, final Class<D> cD, final Class<LOC> cLocale,
								final Class<MC> cClassification)
	{       
		super(cL,cD);
		this.cLocale = cLocale;
		this.cClassification = cClassification;
	}
	
	public EjbMmgClassificationFactory<L,R,MC> ejbClassification(){return new EjbMmgClassificationFactory<>(cClassification);}
}