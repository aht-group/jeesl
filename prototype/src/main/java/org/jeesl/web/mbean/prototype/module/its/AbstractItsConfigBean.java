package org.jeesl.web.mbean.prototype.module.its;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslItsFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.ItsFactoryBuilder;
import org.jeesl.interfaces.model.module.its.config.JeeslItsConfig;
import org.jeesl.interfaces.model.module.its.config.JeeslItsConfigOption;
import org.jeesl.interfaces.model.module.its.issue.JeeslItsIssue;
import org.jeesl.interfaces.model.module.its.issue.JeeslItsIssueStatus;
import org.jeesl.interfaces.model.module.its.task.JeeslItsTask;
import org.jeesl.interfaces.model.module.its.task.JeeslItsTaskType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public abstract class AbstractItsConfigBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
										C extends JeeslItsConfig<L,D,R,O>,
										O extends JeeslItsConfigOption<L,D,O,?>,
										I extends JeeslItsIssue<R,I,IS>,
										IS extends JeeslItsIssueStatus<L,D,R,IS,?>,
										T extends JeeslItsTask<I,TT,?>,
										TT extends JeeslItsTaskType<L,D,TT,?>,
										U extends JeeslSimpleUser>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractItsConfigBean.class);
	
	protected final ItsFactoryBuilder<L,D,R,C,O,I,IS,T,TT> fbIts;
	
	protected JeeslItsFacade<L,D,R,C,O,I,IS,T,TT> fIts;
	
	private List<C> configs; public List<C> getConfigs() {return configs;}

	private R realm;
    private C config; public C getConfig() {return config;} public void setConfig(C config) {this.config = config;}

	public AbstractItsConfigBean(ItsFactoryBuilder<L,D,R,C,O,I,IS,T,TT> fbIts)
	{
		super(fbIts.getClassL(),fbIts.getClassD());
		this.fbIts=fbIts;
	}
	
	protected void postConstructItsOption(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
									JeeslItsFacade<L,D,R,C,O,I,IS,T,TT> fIts,
									R realm)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fIts=fIts;
		this.realm=realm;
	}
	
	protected void updateRealmReference(RREF rref)
	{
		configs = fIts.all(fbIts.getClassConfig(),realm,rref);
		Map<O,C> map = fbIts.ejbConfig().toMapOption(configs);
		for(O o : fIts.all(fbIts.getClassOption()))
		{
			if(!map.containsKey(o))
			{
				try
				{
					C c = fbIts.ejbConfig().build(realm,rref,o);
					c.setName(efLang.createEmpty(bTranslation.getLocales()));
					c.setDescription(efDescription.createEmpty(bTranslation.getLocales()));
					configs.add(fIts.save(c));
				}
				catch (JeeslConstraintViolationException | JeeslLockingException e) {e.printStackTrace();}
			}
			logger.info(o.toString());
		}
	}
	
	public void selectConfig()
	{
		logger.info(AbstractLogMessage.selectEntity(config));
		config = efLang.persistMissingLangs(fIts,bTranslation.getLocales(),config);
		config = efDescription.persistMissingLangs(fIts,bTranslation.getLocales(),config);
	}
	
	public void saveConfig() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(config));
		config = fIts.save(config);
	}
}