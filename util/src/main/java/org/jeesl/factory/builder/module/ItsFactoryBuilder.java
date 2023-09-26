package org.jeesl.factory.builder.module;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.module.its.EjbItsConfigFactory;
import org.jeesl.factory.ejb.module.its.EjbItsFactory;
import org.jeesl.interfaces.model.module.its.config.JeeslItsConfig;
import org.jeesl.interfaces.model.module.its.config.JeeslItsConfigOption;
import org.jeesl.interfaces.model.module.its.issue.JeeslItsIssue;
import org.jeesl.interfaces.model.module.its.issue.JeeslItsIssueStatus;
import org.jeesl.interfaces.model.module.its.task.JeeslItsTask;
import org.jeesl.interfaces.model.module.its.task.JeeslItsTaskType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItsFactoryBuilder<L extends JeeslLang,D extends JeeslDescription,
								R extends JeeslTenantRealm<L,D,R,?>,
								C extends JeeslItsConfig<L,D,R,O>,
								O extends JeeslItsConfigOption<L,D,O,?>,
								I extends JeeslItsIssue<R,I,IS>,
								IS extends JeeslItsIssueStatus<L,D,R,IS,?>,
								T extends JeeslItsTask<I,TT,?>,
								TT extends JeeslItsTaskType<L,D,TT,?>>
		extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(ItsFactoryBuilder.class);
	
	private final Class<R> cRealm; public Class<R> getClassRealm() {return cRealm;}
	
	private final Class<C> cConfig; public Class<C> getClassConfig() {return cConfig;}
	private final Class<O> cOption; public Class<O> getClassOption() {return cOption;}
	
	private final Class<I> cIssue; public Class<I> getClassIssue() {return cIssue;}
	private final Class<IS> cStatus; public Class<IS> getClassStatus() {return cStatus;}
	
	public ItsFactoryBuilder(final Class<L> cL,final Class<D> cD,
								final Class<R> cRealm,
								final Class<C> cConfig,
								final Class<O> cOption,
								final Class<I> cIssue,
								final Class<IS> cStatus)
	{       
		super(cL,cD);
		this.cRealm=cRealm;
		this.cIssue=cIssue;
		this.cConfig=cConfig;
		this.cOption=cOption;
		this.cStatus=cStatus;
	}
	
	public EjbItsConfigFactory<R,C,O> ejbConfig() {return new EjbItsConfigFactory<>(cConfig);}
	public EjbItsFactory<R,I,IS> ejbIssue() {return new EjbItsFactory<>(this);}
}