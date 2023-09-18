package org.jeesl.factory.builder.module;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.module.rmmv.EjbRmmvClassificationFactory;
import org.jeesl.factory.ejb.module.rmmv.EjbRmmvConfigFactory;
import org.jeesl.factory.ejb.module.rmmv.EjbRmmvSubscriptionFactory;
import org.jeesl.factory.ejb.module.rmmv.EjbRmmvSubscriptionItemFactory;
import org.jeesl.factory.ejb.module.rmmv.EjbRmmvTreeElementFactory;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvClassification;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvElement;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvModule;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvModuleConfig;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvSubscription;
import org.jeesl.interfaces.model.module.rmmv.JeeslRmmvSubscriptionItem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RmmvFactoryBuilder<L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
								R extends JeeslTenantRealm<L,D,R,?>,
								TE extends JeeslRmmvElement<L,R,TE,EC>,
								EC extends JeeslRmmvClassification<L,R,EC,?>,
								MOD extends JeeslRmmvModule<?,?,MOD,?>,
								MC extends JeeslRmmvModuleConfig<TE,MOD>,
								SUB extends JeeslRmmvSubscription<R,MOD,USER>,
								SI extends JeeslRmmvSubscriptionItem<SUB,MC>,
								USER extends EjbWithId>
	extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(RmmvFactoryBuilder.class);
	
	private final Class<LOC> cLocale; public Class<LOC> getClassLocale() {return cLocale;}
	private final Class<TE> cElement; public Class<TE> getClassTreeElement() {return cElement;}
	private final Class<EC> cClassification; public Class<EC> getClassClasification() {return cClassification;}
	private final Class<MOD> cModule; public Class<MOD> getClassModule() {return cModule;}
	private final Class<MC> cConfig; public Class<MC> getClassConfig() {return cConfig;}
	private final Class<SUB> cSubscription; public Class<SUB> getClassSubscription() {return cSubscription;}
	private final Class<SI> cSubscriptionItem; public Class<SI> getClassSubscriptionItem() {return cSubscriptionItem;}

	public RmmvFactoryBuilder(final Class<L> cL, final Class<D> cD, final Class<LOC> cLocale,
								final Class<TE> cElement, final Class<EC> cClassification,
								final Class<MOD> cModule, final Class<MC> cConfig,
								final Class<SUB> cSubscription,
								final Class<SI> cSubscriptionItem)
	{       
		super(cL,cD);
		this.cLocale = cLocale;
		this.cElement = cElement;
		this.cClassification = cClassification;
		this.cModule = cModule;
		this.cConfig = cConfig;
		this.cSubscription = cSubscription;
		this.cSubscriptionItem = cSubscriptionItem;
	}
	
	public EjbRmmvClassificationFactory<L,R,EC> ejbClassification() {return new EjbRmmvClassificationFactory<>(cClassification);}
	public EjbRmmvTreeElementFactory<L,R,TE> ejbElement() {return new EjbRmmvTreeElementFactory<>(cElement);}
	public EjbRmmvConfigFactory<TE,MOD,MC> ejbConfig() {return new EjbRmmvConfigFactory<>(cModule,cConfig);}
	public EjbRmmvSubscriptionFactory<R,MOD,MC,SUB,USER> ejbSubscription() {return new EjbRmmvSubscriptionFactory<>(cSubscription);}
	public EjbRmmvSubscriptionItemFactory<MC,SUB,SI> ejbSubscriptionItem() {return new EjbRmmvSubscriptionItemFactory<>(cSubscriptionItem);}
}