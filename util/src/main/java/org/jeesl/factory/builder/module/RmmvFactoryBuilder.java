package org.jeesl.factory.builder.module;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.module.rmmv.EjbRmmvConfigFactory;
import org.jeesl.factory.ejb.module.rmmv.EjbRmmvTreeElementFactory;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvModule;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvModuleConfig;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvElement;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RmmvFactoryBuilder<L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
								R extends JeeslTenantRealm<L,D,R,?>, 
								TE extends JeeslRmmvElement<L,R,TE>,
								MOD extends JeeslRmmvModule<?,?,MOD,?>,
								MC extends JeeslRmmvModuleConfig<TE,MOD>>
	extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(RmmvFactoryBuilder.class);
	
	private final Class<LOC> cLocale; public Class<LOC> getClassLocale() {return cLocale;}
	private final Class<TE> cElement; public Class<TE> getClassTreeElement() {return cElement;}
	private final Class<MOD> cModule; public Class<MOD> getClassModule() {return cModule;}
	private final Class<MC> cConfig; public Class<MC> getClassConfig() {return cConfig;}

	public RmmvFactoryBuilder(final Class<L> cL, final Class<D> cD, final Class<LOC> cLocale, final Class<TE> cElement, final Class<MOD> cModule, final Class<MC> cConfig)
	{       
		super(cL,cD);
		this.cLocale = cLocale;
		this.cElement = cElement;
		this.cModule = cModule;
		this.cConfig = cConfig;
	}
	
//	public EjbRmmvClassificationFactory<L,R,TE> ejbClassification(){return new EjbRmmvClassificationFactory<>(cElement);}
	public EjbRmmvTreeElementFactory<L,R,TE> ejbElement(){return new EjbRmmvTreeElementFactory<>(cElement);}
	public EjbRmmvConfigFactory<TE,MOD,MC> ejbConfig(){return new EjbRmmvConfigFactory<>(cConfig);}
}