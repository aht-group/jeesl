package org.jeesl.web.mbean.prototype.module.its;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.bean.module.its.JeeslItsCacheBean;
import org.jeesl.api.facade.module.JeeslItsFacade;
import org.jeesl.factory.builder.module.ItsFactoryBuilder;
import org.jeesl.interfaces.model.module.its.config.JeeslItsConfig;
import org.jeesl.interfaces.model.module.its.config.JeeslItsConfigOption;
import org.jeesl.interfaces.model.module.its.issue.JeeslItsIssue;
import org.jeesl.interfaces.model.module.its.issue.JeeslItsIssueStatus;
import org.jeesl.interfaces.model.module.its.task.JeeslItsTask;
import org.jeesl.interfaces.model.module.its.task.JeeslItsTaskType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractItsCacheBean <L extends JeeslLang, D extends JeeslDescription,
										R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
										C extends JeeslItsConfig<L,D,R,O>,
										O extends JeeslItsConfigOption<L,D,O,?>,
										I extends JeeslItsIssue<R,I,IS>,
										IS extends JeeslItsIssueStatus<L,D,R,IS,?>,
										T extends JeeslItsTask<I,TT,?>,
										TT extends JeeslItsTaskType<L,D,TT,?>>
								implements JeeslItsCacheBean<L,D,R,RREF,IS>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractItsCacheBean.class);
	
	private JeeslItsFacade<L,D,R,C,O,I,IS,T,TT> fIssue;
	
	private final ItsFactoryBuilder<L,D,R,C,O,I,IS,T,TT> fbIssue;
	
	private final Map<RREF,List<IS>> mapStatus; @Override public Map<RREF,List<IS>> getMapStatus() {return mapStatus;}
	    
	public AbstractItsCacheBean(final ItsFactoryBuilder<L,D,R,C,O,I,IS,T,TT> fbIssue)
	{
		this.fbIssue=fbIssue;
		
		mapStatus = new HashMap<>();
		
	}
	
	public void postConstruct(JeeslItsFacade<L,D,R,C,O,I,IS,T,TT> fIssue)
	{
		this.fIssue=fIssue;
	}
	
	public void reloadRealm(R realm, RREF rref)
	{
		if(!mapStatus.containsKey(rref)) {mapStatus.put(rref,fIssue.all(fbIssue.getClassStatus(),realm,rref));}
	}

}